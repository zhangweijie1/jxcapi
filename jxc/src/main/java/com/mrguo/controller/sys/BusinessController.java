
package com.mrguo.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.sys.BusinessSetup;
import com.mrguo.entity.sys.SysBusinessSetup;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.sys.SysBusinessSetupServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: BusinessController
 * @Description: 业务设置控制层
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/12/10 3:05 下午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Api(tags = "业务设置")
@RestController
@RequestMapping("/busetup")
public class BusinessController {

    @Autowired
    private SysBusinessSetupServiceImpl businessSetupService;

    @ApiOperation(value = "保存业务设置")
    @ApiPermission(value = "busetup:oper", pname = "设置")
    @PostMapping(value = "/save_business_setup")
    public Result businessSetup(@RequestBody SysBusinessSetup businessSetup) throws Exception {
        businessSetupService.businessSetup(businessSetup);
        return Result.ok(businessSetup);
    }

    @ApiOperation(value = "获取业务设置", notes = "")
    @ApiPermission(value = "busetup:oper", pname = "设置")
    @PostMapping(value = "/get_business_setup")
    public Result getBusinessSetup() throws Exception {
        return Result.ok(businessSetupService.getBusinessSetup());
    }

    @ApiOperation(value = "设置是否开启等级价", notes = "")
    @ApiPermission(value = "busetup:oper", pname = "设置")
    @PostMapping(value = "/setIsOpenLevel")
    public Result setIsOpenLevel() throws Exception {
        String businessSetupStr = businessSetupService.getBusinessSetup();
        BusinessSetup businessSetup = JSONObject.parseObject(businessSetupStr, BusinessSetup.class);
        String isOpenLevel = businessSetup.getIsOpenLevel();
        if (isOpenLevel == null || "0".equals(isOpenLevel)) {
            businessSetup.setIsOpenLevel("1");
        } else {
            businessSetup.setIsOpenLevel("0");
        }
        SysBusinessSetup sysBusinessSetup = new SysBusinessSetup();
        sysBusinessSetup.setValue(JSONObject.toJSONString(businessSetup));
        businessSetupService.businessSetup(sysBusinessSetup);
        return Result.ok(businessSetup);
    }
}
