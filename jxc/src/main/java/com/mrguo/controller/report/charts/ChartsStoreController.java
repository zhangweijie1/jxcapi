package com.mrguo.controller.report.charts;


import com.mrguo.common.entity.Result;
import com.mrguo.service.impl.basedata.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("charts/store")
public class ChartsStoreController {

    @Autowired
    private StoreServiceImpl storeService;

    @PostMapping("/listStockDataGroupByStore")
    public Result listStockDataGroupByStore() {
        return storeService.listStockDataGroupByStore();
    }
}
