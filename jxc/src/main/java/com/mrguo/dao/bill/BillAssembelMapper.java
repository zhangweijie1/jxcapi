package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.vo.bill.BillAssembelVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("billAssembelMapper")
public interface BillAssembelMapper extends MyMapper<BillAssembelVo> {

    /**
     * 列表查询
     *
     * @param page
     * @param params
     * @return
     */
    List<BillAssembelVo> selectListData(Page page, @Param("record") Map<String, Object> params);

    /**
     * getDetail
     *
     * @return
     */
    BillAssembelVo selectOneByBillId(@Param("billId") Long billId);
}