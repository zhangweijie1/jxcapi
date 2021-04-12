package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodsspuMapper;
import com.mrguo.entity.goods.GoodsSpu;
import com.mrguo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品Service实现类
 *
 * @author mrguo
 */
@Service("goodsSpuService")
public class GoodsSpuServiceImpl extends BaseServiceImpl<GoodsSpu> {

    @Autowired
    private GoodsspuMapper goodsspuMapper;
    @Autowired
    private HttpServletRequest request;

    @Value("${business.digit}")
    private int digit;

    @Override
    public MyMapper<GoodsSpu> getMapper() {
        return goodsspuMapper;
    }

    public Result genGoodSpuCode() {
        String prefix = "SP";
        String date = DateUtil.getCurrentDateStr();
        Long maxGoodspuCode = goodsspuMapper.getTodayMaxGoodsCode();
        if (maxGoodspuCode != null) {
            return Result.ok(prefix + String.valueOf(maxGoodspuCode + 1));
        }
        String code = String.format("%0" + digit + "d", 0);
        return Result.ok(prefix + date + code);
    }

    public void clearAllData() {
    }
}
