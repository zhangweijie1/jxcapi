package com.mrguo.controller.basedata;

import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.CustomerCat;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.CustomerCatServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 后台管理商品类别Controller
 *
 * @author mrguo
 */
@Api(tags = "客户分类")
@RestController
@RequestMapping("/customerCat")
public class CustomerCatController {

    @Resource
    private CustomerCatServiceImpl customerCatService;

    /**
     * 加载类别树菜单
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取所有类别", notes = "")
    @PostMapping("/listAllData")
    public Result listAllData() throws Exception {
        return Result.ok(customerCatService.listAllData());
    }

    /**
     * 添加类别
     *
     * @param customerCat
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增或保存", notes = "")
    @ApiPermission(value = "customercat:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody @Valid CustomerCat customerCat) throws Exception {
        return customerCatService.addOrUpdateData(customerCat);
    }

    /**
     * 类别删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除", notes = "")
    @ApiPermission(value = "customercat:del", pname = "删除")
    @PostMapping("/delete")
    public Result delete(@RequestBody String id) throws Exception {
        return customerCatService.delete(id);
    }
}
