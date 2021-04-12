package com.mrguo.controller.logs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.interfaces.ApiPermission;
import com.mrguo.service.impl.log.LogAmountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 资金流水
 * @date 2020/3/219:29 AM
 * @updater 郭成兴
 * @updatedate 2020/3/219:29 AM
 */

@RestController
@RequestMapping("/log/finAmount")
public class LogFinAmountController {

    @Autowired
    private LogAmountServiceImpl finAmountLogService;

    @ApiPermission(value = "log_amount:list", pname = "查询")
    @PostMapping("/listPage")
    public Result addData(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return finAmountLogService.listData(pageParams);
    }

    @ApiPermission(value = "log_amount:export", pname = "导出")
    @PostMapping("/export")
    public Result export(@RequestBody PageParams<Map<String, Object>> pageParams) throws Exception {
        return null;
    }
}
