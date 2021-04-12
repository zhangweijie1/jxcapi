package com.mrguo.controller.sys;

import com.mrguo.common.entity.Result;
import com.mrguo.dao.sys.SysDictMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/8/38:21 PM
 * @updater 郭成兴
 * @updatedate 2020/8/38:21 PM
 */

@Api(tags = "数据字典")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private SysDictMapper sysDictMapper;

    @ApiOperation(value = "根据父节点，获取子节点")
    @PostMapping(value = "/getDataByParent")
    public Result getDataByParent(@RequestBody String parent) {
        return Result.ok(sysDictMapper.selectByParent(parent));
    }
}
