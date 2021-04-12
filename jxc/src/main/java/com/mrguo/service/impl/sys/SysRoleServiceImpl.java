package com.mrguo.service.impl.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.sys.SysRoleMapper;
import com.mrguo.entity.sys.SysPermission;
import com.mrguo.entity.sys.SysRole;
import com.mrguo.entity.sys.SysRolePermission;
import com.mrguo.entity.sys.SysUser;
import com.mrguo.service.inter.bill.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName RoleServiceImpl
 * @Description 角色Service
 * @date 2019/1/310:08 AM
 * @updater 郭成兴
 * @updatedate 2019/1/310:08 AM
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermissionServiceImpl sysRolePermissionService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<SysRole> getMapper() {
        return sysRoleMapper;
    }

    @Override
    public List<String> getRoleByUserId(String userId) {
        return sysRoleMapper.selectRoleByUserId(userId);
    }

    /**
     * 添加角色
     *
     * @param sysRole
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveOrUpdateData(SysRole sysRole) throws CustomsException {
        if (sysRole.getStatus() == null) {
            sysRole.setStatus((byte) 1);
        }
        if (sysRole.getId() == null) {
            sysRole.setId(IDUtil.getSnowflakeId());
            return sysRoleMapper.insertSelective(sysRole) < 1
                    ? Result.fail("新增失败！")
                    : Result.ok();
        } else {
            return sysRoleMapper.updateByPrimaryKeySelective(sysRole) < 1
                    ? Result.fail("修改失败！")
                    : Result.ok();
        }
    }

    /**
     * 删除角色和对应的权限关联表
     *
     * @param roleIds
     * @return
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delListData(List<Long> roleIds) throws CustomsException {
        for (Long id : roleIds) {
            // 删除角色
            sysRoleMapper.deleteByPrimaryKey(id);
            // 删除角色权限
            delPermissionsByRoleId(id);
        }
        return Result.ok();
    }

    /**
     * 修改角色的权限:
     * 先清空之前的权限
     * 再新增后面的权限
     *
     * @param sysRole
     * @param permissionList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result updateRolePermissionsData(SysRole sysRole, List<SysPermission> permissionList) throws Exception {
        if (sysRole.getId() == null) {
            return Result.fail("角色ID不能为空！");
        }
//        if (sysRole.getId() == 0) {
//            return Result.fail("管理员权限不可编辑！");
//        }
        if (permissionList != null) {
            delPermissionsByRoleId(sysRole.getId());
            savePermissionsByRoleId(sysRole.getId(), permissionList);
        }
        // 删除角色对应的user的权限缓存
        List<SysUser> userList = sysUserService.getUserListByRoleId(sysRole.getId());
        if (userList.size() > 0) {
            List<Long> userIds = userList.stream().map(SysUser::getUid).collect(Collectors.toList());
            sysUserService.delPermissionsCacheByUserIds(userIds);
        }
        return Result.ok();
    }

    /**
     * 模糊搜索角色
     *
     * @param pageParams
     * @return
     */
    public Result listPage(PageParams<SysRole> pageParams) throws IOException {
        Page<SysRole> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(sysRoleMapper.listPage(page, data));
        return Result.ok(page);
    }

    public Result listOptions() {
        return Result.ok(sysRoleMapper.listOptions());
    }

    private void delPermissionsByRoleId(Long roleId) throws CustomsException {
        Example example = new Example(SysRolePermission.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        sysRolePermissionService.deleteByExample(example);
    }

    private void savePermissionsByRoleId(Long roleId, List<SysPermission> permissionList) throws CustomsException {
        ArrayList<SysRolePermission> objects = new ArrayList<>();
        for (SysPermission sysPermission : permissionList) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(roleId);
            sysRolePermission.setPermissionId(sysPermission.getPerId());
            objects.add(sysRolePermission);
        }
        sysRolePermissionService.insertListData(objects);
    }

}
