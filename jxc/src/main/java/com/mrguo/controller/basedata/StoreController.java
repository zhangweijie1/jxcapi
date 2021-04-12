package com.mrguo.controller.basedata;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.vo.basedata.StoreVo;
import com.mrguo.entity.bsd.Store;
import com.mrguo.dto.*;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.basedata.StoreOptionsServiceImpl;
import com.mrguo.service.impl.basedata.StoreServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/11/149:32 AM
 * @updater 郭成兴
 * @updatedate 2019/11/149:32 AM
 */
@Api(tags = "仓库管理")
@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreServiceImpl storeService;
    @Autowired
    private StoreOptionsServiceImpl storeOptionsService;

    @ApiOperation(value = "新增或编辑仓库")
    @ApiPermission(value = "store:adedit", pname = "新增编辑")
    @PostMapping("/saveData")
    public Result saveData(@RequestBody @Valid StoreVo storeVo) throws Exception {
        if (storeVo.getId() == null) {
            return storeService.addData(storeVo);
        } else {
            if (storeService.updateData(storeVo) == null) {
                return Result.fail("修改失败");
            } else {
                return Result.ok("修改成功");
            }
        }
    }

    @ApiOperation(value = "删除仓库")
    @ApiPermission(value = "store:del", pname = "删除")
    @PostMapping("/delete/{id}")
    public Result delData(@PathVariable Long id) throws Exception {
        return storeService.delData(id);
    }

    @ApiOperation(value = "获取仓库详情")
    @PostMapping("/getDetail/{id}")
    public Result getById(@PathVariable Long id) throws Exception {
        StoreVo storeVo = storeService.getDataById(id);
        return Result.ok(storeVo);
    }

    @ApiOperation(value = "分页查询仓库")
    @ApiPermission(value = "store:list", pname = "查询")
    @PostMapping("/listPage")
    public Result listPage(@RequestBody PageParams<StoreVo> pageParams) throws Exception {
        return storeService.listPage(pageParams);
    }

    @ApiOperation(value = "查询所有仓库")
    @PostMapping("/listAllData")
    public Result listAllData() throws Exception {
        return storeService.listAllData();
    }

    /**
     * 查询下拉框
     *
     * @return
     */
    @ApiOperation(value = "查询所有仓库下拉")
    @PostMapping("/listAllOptions")
    public Result getStoreAllOptions() {
        List<OptionStore> options = storeOptionsService.getStoreAllOptions();
        return Result.ok(options);
    }

    @ApiOperation(value = "修改仓库锁定状态")
    @PostMapping("/updateLockStatusByIds")
    public Result updateIsLockByIds(@RequestBody List<Store> stores) throws Exception {
        return storeService.updateLockStatusByIds(stores);
    }
}
