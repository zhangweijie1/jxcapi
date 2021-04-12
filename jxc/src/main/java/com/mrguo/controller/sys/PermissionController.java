package com.mrguo.controller.sys;

import com.mrguo.common.entity.Result;
import com.mrguo.config.StaticConfig;
import com.mrguo.entity.config.PriceType;
import com.mrguo.entity.sys.SysPermission;
import com.mrguo.entity.sys.SysRolePermission;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysPermissionServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2018/12/289:06 PM
 * @updater 郭成兴
 * @updatedate 2018/12/289:06 PM
 */
@Api(tags = "权限管理")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    WebApplicationContext applicationContext;
    @Autowired
    private StaticConfig staticConfig;

    @Autowired
    private SysPermissionServiceImpl sysPermissionService;

    @ApiOperation(value = "获取所有权限", notes = "")
    @PostMapping(value = "/listAllData")
    public Result listAllData() {
        List<SysPermission> sysPermissions = sysPermissionService.listAllData();
        return Result.ok("获取权限成功", sysPermissions);
    }

    @ApiOperation(value = "获取价格权限列表", notes = "")
    @PostMapping(value = "/listPriceTypeOptions")
    public Result getPricePermissions() {
        List<PriceType> priceTypes = staticConfig.getPriceTypes();
        return Result.ok(priceTypes);
    }

    @ApiOperation(value = "通过角色ID获取权限", notes = "")
    @PostMapping(value = "/listData/{roleId}")
    public Result getPermissionsByRoleId(@PathVariable Long roleId) {
        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(roleId);
        List<SysPermission> sysPermissions = sysPermissionService.getPermissionsByRoleId(roleId);
        return Result.ok(sysPermissions);
    }

    /**
     * 重新加载权限：将权限系统所有的权限都加入数据库
     *
     * @param
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2018/12/28 9:08 PM
     * @updater 郭成兴
     * @updatedate 2018/12/28 9:08 PM
     */
    @ApiOperation(value = "初始化权限", notes = "把权限新增资源新增到数据库")
    @RequestMapping(value = "/reload")
    public Result reload(@RequestParam String pas) {
        if (!"512830037".equals(pas)) {
            return Result.fail("密码错误!");
        }
        //1 获取Controller里面，所有带有@RequestMapper标签的方法
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        //2 遍历所有的方法，判断是否有@SecurityInterface
        Collection<HandlerMethod> methodList = handlerMethods.values();
        List<SysPermission> permissionList = new ArrayList<>();
        List<SysPermission> parentPermissionList = new ArrayList<>();
        List<String> parentKeys = new ArrayList<>();
        for (HandlerMethod method : methodList) {
            ApiPermission security = method.getMethodAnnotation(ApiPermission.class);
            if (security == null) {
                continue;
            }
            //存在权限
            PostMapping methodAnnotation = method.getMethodAnnotation(PostMapping.class);
            RequestMapping classAnnotation = method.getMethod().getDeclaringClass().getAnnotation(RequestMapping.class);
            //获取：权限表达式
            String permissionId = security.value();
            String[] split = permissionId.split(":");
            String parentId = split[0];
            String parentName = sysPermissionService.getPermissionNameById(permissionId);
            System.out.println("permissionId: "+ permissionId + parentName);
            String permissionName = security.pname();
            String classResource = classAnnotation.value()[0];
            String resource = classResource + methodAnnotation.value()[0];
            // 组装权限实体
            SysPermission sysPermission = new SysPermission();
            sysPermission.setPerId(permissionId);
            sysPermission.setPerName(permissionName);
            sysPermission.setPerParent(parentId);
            sysPermission.setResource(resource);
            permissionList.add(sysPermission);
            // 组装权限父实体
            if (parentKeys.indexOf(parentId) < 0) {
                SysPermission p = new SysPermission();
                p.setPerId(parentId);
                String[] splitName = parentName.split(",");
                p.setPerName(splitName[0]);
                p.setPerParent(splitName[1]);
                p.setPriority(Integer.valueOf(splitName[2]));
                p.setResource(classResource);
                parentPermissionList.add(p);
                System.out.println(p.toString());
                parentKeys.add(parentId);
            }
        }
        sysPermissionService.deleteAll();
        sysPermissionService.saveDataForReload(permissionList, parentPermissionList);
        return Result.ok("权限初始化成功");
    }
}
