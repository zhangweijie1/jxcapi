package com.mrguo.dao.goods;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.goods.GoodsSpu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("goodsspuMapper")
public interface GoodsspuMapper extends MyMapper<GoodsSpu> {

    /**
     * 获取最大编码
     *
     * @return
     */
    @Select(value="SELECT MAX(SUBSTRING(code,-12)) as code FROM `bsd_goods_spu` WHERE TO_DAYS(create_time) = TO_DAYS(NOW())")
    Long getTodayMaxGoodsCode();
}