package com.mrguo.service.impl.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.config.StaticConfig;
import com.mrguo.dao.sys.SysPermissionMapper;
import com.mrguo.entity.sys.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 郭成兴
 * @ClassName PermissionServiceImpl
 * @Description 权限Service
 * @date 2018/12/2910:16 AM
 * @updater 郭成兴
 * @updatedate 2018/12/2910:16 AM
 */
@Service
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission> {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private StaticConfig staticConfig;

    @Override
    public MyMapper<SysPermission> getMapper() {
        return sysPermissionMapper;
    }

    @Override
    public SysPermission saveData(SysPermission permission) {
        if (permission.getPerParent() == null) {
            permission.setPerParent(String.valueOf(0));
        }
        sysPermissionMapper.insert(permission);
        return permission;
    }

    public void deleteAll(){
        sysPermissionMapper.deleteAll();
    }

    public List<SysPermission> listAllData() {
        return sysPermissionMapper.selectAll();
    }

    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        return sysPermissionMapper.selectPermissionsByRoleId(roleId);
    }

    /**
     * reload的添加方法
     * 格式 => 商品:添加
     * 如果:前存在,则以该节点为父节点进行添加
     * 如果不存在,则先添加父节点,再以该节点为父节点进行添加
     *
     * @param permissions
     * @return
     */
    public void saveDataForReload(List<SysPermission> permissions, List<SysPermission> parentPermissions) {
        addListData(permissions);
        addListData(parentPermissions);
        // 顶部permisions
        List<SysPermission> permissionMaxParentList = new ArrayList<>();
        Map<String, String> permissionsParent = staticConfig.getPermissionsParent();
        for(String key : permissionsParent.keySet()){
            SysPermission sysPermission = new SysPermission();
            sysPermission.setPerId(key);
            String name = permissionsParent.get(key);
            String[] nameSplit = name.split(",");
            sysPermission.setPerName(nameSplit[0]);
            sysPermission.setPriority(Integer.valueOf(nameSplit[1]));
            sysPermission.setResource("0");
            sysPermission.setPerParent("0");
            permissionMaxParentList.add(sysPermission);
        }
        addListData(permissionMaxParentList);
    }

    public void addListData(List<SysPermission> permissions) {
        sysPermissionMapper.insertList(permissions);
    }

    /**
     * 根据权限Id获取name
     *
     * @param permisionId
     * @return
     */
    public String getPermissionNameById(String permisionId) {
        String[] split = permisionId.split(":");
        String key = split[0];
        return staticConfig.getPermissions().get(key);
    }
}
