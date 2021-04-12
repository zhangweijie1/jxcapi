package com.mrguo.util;

import com.mrguo.entity.sys.SysPermission;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/5/58:34 PM
 * @updater 郭成兴
 * @updatedate 2019/5/58:34 PM
 */
public class ServiceUtil {
    /**
     * str的数组转化成"，"分割的str
     * @param list
     * @return
     */
    public static String list2str(List<Long> list){
        StringBuilder result = new StringBuilder();
        for(Long num : list){
            result.append(String.valueOf(num) + ",");
        }
        return result.toString();
    }

    public static String listSysPermission2str(List<SysPermission> list){
        List<String> listStr = new ArrayList<>();
        for(SysPermission sysPermission: list){
           listStr.add(sysPermission.getPerId());
        }
        return listStr.toString();
    }

    private static Boolean isExsitChildren(List<SysPermission> list, SysPermission target) {
        for (SysPermission sysPermission : list) {
            if(target.getPerId().equals(sysPermission.getPerParent())){
                return true;
            }
        }
        return false;
    }

    private static List<SysPermission> getChildren(List<SysPermission> list, SysPermission target){
        List<SysPermission> result = new ArrayList<>();
        for(SysPermission sysPermission: list){
            if(target.getPerId().equals(sysPermission.getPerParent())){
                result.add(sysPermission);
                if(isExsitChildren(list, sysPermission)){getChildren(list,sysPermission);}
            }
        }
        return result;
    }


    private static List<SysPermission> filterPrmissions(List<SysPermission> permissions, String category){
        List<SysPermission> result = new ArrayList<>();
        List<SysPermission> fatherList = new ArrayList<>();
        for(SysPermission sysPermission : permissions){
            if(category.equals(sysPermission.getPerParent())){
                fatherList.add(sysPermission);
            }
        }
        // 获取指定的父节点（如api的）
        // 把这些节点的所有 儿子都添加进去
        for(SysPermission sysPermission: fatherList){
            List<SysPermission> children = getChildren(permissions, sysPermission);
            result.addAll(children);
        }
        result.addAll(fatherList);
        return result;
    }

    /**
     * 提取api分类的权限
     * @param permissions
     * @return
     */
    public static List<SysPermission> getApiPrmissions(List<SysPermission> permissions){
        return filterPrmissions(permissions,"1");
    }

    /**
     * 提取menu分类的权限
     * @param permissions
     * @return
     */
    public static List<SysPermission> getMenuPermissions(List<SysPermission> permissions){
        return filterPrmissions(permissions,"2");
    }

    /**
     * 提取组件分类的权限
     * @param permissions
     * @return
     */
    public static List<SysPermission> getCompPermissions(List<SysPermission> permissions){
        return filterPrmissions(permissions,"3");
    }
}
