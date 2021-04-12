package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillSale;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("billSaleMapper")
public interface BillSaleMapper extends MyMapper<Bill> {

    /**
     * 根据Id获取销售单数据
     *
     * @param billId
     * @return
     */
    BillSale getDataById(Long billId);

    /**
     * 自定义查询
     *
     * @param page
     * @param data
     * @return
     */
    List<BillSale> listCustom(IPage<BillSale> page,
                            @Param("record") Map<String, Object> data);
}