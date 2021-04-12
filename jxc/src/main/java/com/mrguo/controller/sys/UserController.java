package com.mrguo.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.MessageInfo;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.config.StaticConfig;
import com.mrguo.entity.config.PriceType;
import com.mrguo.dto.sys.UserDto;
import com.mrguo.dto.sys.UserInfoApp;
import com.mrguo.entity.sys.*;
import com.mrguo.service.impl.sys.*;
import com.mrguo.util.business.VerifyCodeNoFell;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName UserController
 * @Description 注册，登录，登出
 * @date 2018/12/277:51 PM
 * @updater 郭成兴
 * @updatedate 2018/12/277:51 PM
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserServiceImpl userService;
    @Autowired
    private SysUserRoleServiceImpl sysUserRoleService;
    @Autowired
    private SysBusinessSetupServiceImpl sysBusinessSetupService;
    @Autowired
    private VerifyCodeNoFell verifyCodeNoFell;

    @Autowired
    private StaticConfig staticConfig;

    @ApiOperation(value = "注册", notes = "")
    @PostMapping(value = "/register")
    public Result register(@RequestBody SysUser user) throws Exception {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", user.getUsername());
        List<SysUser> sysUsers = userService.selectByExample(example);
        if (sysUsers == null) {
            return Result.fail(MessageInfo.REGISTER_ERROR);
        }
        if (sysUsers.size() > 0) {
            return Result.fail(MessageInfo.USER_IS_EXSIT);
        }
        // 注册
        return Result.ok(userService.regist(user));
    }

    @ApiOperation(value = "删除用户", notes = "")
    @PostMapping(value = "/delListData")
    public Result delData(@RequestBody List<Long> userIds) throws Exception {
        userService.deleteListDataByKey(userIds);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户角色", notes = "")
    @PostMapping(value = "/updateRoles")
    public Result updateRoles(@RequestBody UserDto userDto) {
        return userService.updateRoles(userDto);
    }

    @ApiOperation(value = "查询子账号", notes = "")
    @PostMapping(value = "/listSonData")
    public IPage<SysUser> listSonData(@RequestBody PageParams<SysUser> pageParams) {
        return userService.listCustom(pageParams);
    }

    @ApiOperation(value = "查询用户的角色", notes = "")
    @PostMapping(value = "/listUserRoles")
    public Result listRoles(@RequestBody Long userId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        List<SysUserRole> sysUserRoleList = sysUserRoleService.select(sysUserRole);
        return Result.ok(sysUserRoleList);
    }

    @ApiOperation(value = "用户名是否存在", notes = "")
    @PostMapping(value = "/isExistUsername")
    public Result isExistUsername(@RequestBody String username) throws Exception {
        return userService.isExistUsername(username);
    }

    @ApiOperation(value = "用户名(手机号)是否存在", notes = "")
    @PostMapping(value = "/isExistPhone")
    public Result isExistPhone(@RequestBody String phoneNo) throws Exception {
        return userService.isExistPhoneNo(phoneNo);
    }

    @ApiOperation(value = "获取用户信息", notes = "")
    @PostMapping(value = "/getUserInfo")
    public Result getUserInfo() {
        return Result.ok(userService.getUserInfoApp());
    }


    @ApiOperation(value = "初始化App,配置文件，用户信息等", notes = "")
    @PostMapping(value = "/initApp")
    public Result initApp() {
        UserInfoApp userInfoApp = userService.getUserInfoApp();
        List<PriceType> priceTypes = staticConfig.getPriceTypes();
        List<String> permissions = userService.getPermissionsByCurrentUser();
        priceTypes = priceTypes.stream().filter(item -> {
                    return !"0_1".equals(item.getValue());
                }
        ).collect(Collectors.toList());
        Map<String, Object> configs = new HashMap<>();
        configs.put("priceTypes", priceTypes);
        BusinessSetup businessSetup = sysBusinessSetupService.getBusinessSetupObj();
        //
        Map<String, Object> initData = new HashMap<>();
        initData.put("userInfo", userInfoApp);
        initData.put("configs", configs);
        initData.put("businessSetup", businessSetup);
        initData.put("permissions", permissions);
        return Result.ok(initData);
    }

    /**
     * 登录验证
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2018/12/28 9:17 AM
     * @updater 郭成兴
     * @updatedate 2018/12/28 9:17 AM
     */
    @ApiOperation(value = "登录", notes = "")
    @PostMapping(value = "/login")
    public Result login(@RequestBody SysUser user) throws Exception {
        String title = "登录失败";
        try {
            return userService.login(user);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            return Result.fail(401, title, MessageInfo.USER_ISNOT_EXSIT);
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            return Result.fail(401, title, MessageInfo.LOGIN_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(401, title, MessageInfo.LOGIN_SYS_ERROR);
        }
    }

    @ApiOperation(value = "登出", notes = "")
    @PostMapping(value = "/logout")
    public Result logout() {
        return Result.okmsg("退出成功");
    }

}
