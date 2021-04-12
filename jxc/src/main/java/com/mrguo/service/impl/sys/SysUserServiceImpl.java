package com.mrguo.service.impl.sys;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.*;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.common.utils.MapToEntityUtil;
import com.mrguo.dao.sys.SysEmployeeMapper;
import com.mrguo.dao.sys.SysUserMapper;
import com.mrguo.dao.sys.SysUserRoleMapper;
import com.mrguo.dto.sys.UserDto;
import com.mrguo.dto.sys.UserInfoApp;
import com.mrguo.entity.sys.*;
import com.mrguo.service.inter.bill.sys.UserService;
import com.mrguo.util.business.JwtUtil;
import com.mrguo.util.RedisClient;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/1412:04 PM
 * @updater 郭成兴
 * @updatedate 2019/4/1412:04 PM
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysEmployeeMapper employeeMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisClient redisClient;

    @Value("${business.redis-prefix.permission}")
    private String prefixPermission;
    @Value("${business.redis-prefix.userInfo}")
    private String prefixUserInfo;

    @Override
    public MyMapper<SysUser> getMapper() {
        return sysUserMapper;
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    public SysUser regist(SysUser user) throws CustomsException {
        String username = user.getUsername();
        if (sysUserMapper.countByUserName(username) > 0) {
            throw new CustomsException("用户名: " + username + ",已存在");
        }
        Date date = new Date();
        user.setUid(IDUtil.getSnowflakeId());
        user.setPassword(IDUtil.encryptPassword(user.getPassword(), user.getUsername()));
        user.setIsActivation(Byte.valueOf("1"));
        user.setIsFrozen(Byte.valueOf("0"));
        user.setCreateTime(date);
        user.setUpdateTime(date);
        sysUserMapper.insertSelective(user);
        return user;
    }

    public Result updateRoles(UserDto userDto) {
        deleteUserRoles(userDto.getSysUser().getUid());
        addUserRoles(userDto.getSysUser().getUid(), userDto.getRoleList());
        return Result.ok();
    }

    void deleteUserRoles(Long userId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        userRoleMapper.delete(sysUserRole);
    }

    void addUserRoles(Long userId, List<SysRole> roleList) {
        for (SysRole role : roleList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(role.getId());
            sysUserRole.setUserId(userId);
            userRoleMapper.insert(sysUserRole);
        }
    }

    /**
     * 登录
     * 获取用户id，加密到jwt
     *
     * @param user
     * @param user 用户名
     * @param user 密码（明文）
     * @return
     */
    public Result login(SysUser user) {
        user.setPassword(IDUtil.encryptPassword(user.getPassword(), user.getUsername()));
        List<SysUser> users = sysUserMapper.select(user);
        if (users.size() == 0) {
            return Result.fail(MessageInfo.LOGIN_FAIL);
        }
        if (users.size() > 1) {
            return Result.fail(MessageInfo.LOGIN_SYS_TOO_MANY);
        }
        user = users.get(0);
        String jwtToken = JwtUtil.createJWT(
                String.valueOf(user.getUid()));
        return Result.ok(jwtToken);
    }

    public UserInfoApp getUserInfoApp() {
        UserInfo userInfo = UserInfoThreadLocalUtils.get();
        Long userId = userInfo.getUserId();
        UserInfoApp userInfoApp = sysUserMapper.selectUserInfoByUserId(userId);
        userInfoApp.setUserId(String.valueOf(userId));
        return userInfoApp;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     * @throws Exception
     */
    public SysUser getUserByName(String username) throws Exception {
        if (sysUserMapper.countByUserName(username) == 0) {
            throw new Exception("用户名不存在！");
        }
        return sysUserMapper.getDataByUserName(username);
    }

    public List<SysUser> getUserListByRoleId(Long roleId) {
        return sysUserMapper.selectListDataByRoleId(roleId);
    }

    public Result isExistUsername(String username) throws Exception {
        int length = username.length();
        if (length < 6 || length > 20) {
            return Result.ok(400, "请输入6到20个字符");
        }
        String pattern1 = "[\\da-zA-z]+";
        String pattern2 = ".*[a-zA-Z]+.*";
        boolean isMatch1 = Pattern.matches(pattern1, username);
        boolean isMatch2 = Pattern.matches(pattern2, username);
        if (!isMatch1 || !isMatch2) {
            return Result.ok(400, "用户名必须为字母或字母与数字的组合");
        }
        return sysUserMapper.countByUserName(username) > 0
                ? Result.ok(400, "该用户名已被占用！")
                : Result.ok();
    }

    public Result isExistPhoneNo(String phoneNo) throws Exception {
        return sysUserMapper.countByUserName(phoneNo) > 0
                ? Result.fail("该手机已注册！")
                : Result.ok();
    }

    /**
     * 自定义查询用户
     *
     * @param pageParams
     * @return
     */
    public IPage<SysUser> listCustom(PageParams<SysUser> pageParams) {
        Page<SysUser> page = pageParams.getPage();
        SysUser data = MapToEntityUtil.map2Entity(pageParams.getData(), SysUser.class);
        return page.setRecords(sysUserMapper.listCustom(page, data));
    }

    /**
     * 获取用户功能权限
     * redis缓存，更新的时候，要删除redis对应的key
     *
     * @param userId
     * @return
     */
    public List<String> getPermissionsByUserId(Long userId) {
        String key = getPermissionKey(userId);
        String s = redisClient.get(key);
        if (s == null) {
            List<String> permissions = sysUserMapper.selectPermissionsByUserId(userId);
            String perStr = String.join(",", permissions);
            redisClient.set(key, perStr);
            return permissions;
        } else {
            String[] split = s.split(",");
            return new ArrayList<String>(Arrays.asList(split));
        }
    }

    public List<String> getPermissionsByCurrentUser() {
        Long userId = (Long) request.getAttribute("userId");
        String key = getPermissionKey(userId);
        String s = redisClient.get(key);
        if (StringUtils.isBlank(s)) {
            List<String> permissions = sysUserMapper.selectPermissionsByUserId(userId);
            String perStr = String.join(",", permissions);
            redisClient.set(key, perStr);
            return permissions;
        } else {
            String[] split = s.split(",");
            return new ArrayList<String>(Arrays.asList(split));
        }
    }

    public void delPermissionsCacheByUserIds(List<Long> userIds) {
        List<String> keysList = new ArrayList<>();
        for (Long id : userIds) {
            String key = getPermissionKey(id);
            keysList.add(key);
        }
        String[] keys = keysList.toArray(new String[0]);
        redisClient.del(keys);
    }

    /**
     * 获取UserInfo，保存在redis的数据（dataPermissions等信息）
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoByUserId(Long userId) {
        String key = getUserInfoKey(userId);
        String s = redisClient.get(key);
        if (s == null) {
            UserInfo userInfo = new UserInfo();
            String dataPermissionStr = sysUserMapper.selectDataPermissionsByUserId(userId);
            SysEmployee sysEmployee = sysEmployeeMapper.selectByUserId(userId);
            SysDataPermission dataPermission = JSONObject.parseObject(dataPermissionStr, SysDataPermission.class);
            userInfo.setDataPermission(dataPermission);
            userInfo.setEmpType(sysEmployee.getType());
            //
            String str = JSONObject.toJSONString(userInfo);
            redisClient.set(key, str);
            return userInfo;
        } else {
            return JSONObject.parseObject(s, UserInfo.class);
        }
    }

    public void delUserInfoCacheByUserId(Long userId) {
        String key = getUserInfoKey(userId);
        redisClient.del(key);
    }


    private String getUserInfoKey(Long userId) {
        return prefixUserInfo + ":" + userId;
    }

    private String getPermissionKey(Long userId) {
        return prefixPermission + ":" + userId;
    }
}
