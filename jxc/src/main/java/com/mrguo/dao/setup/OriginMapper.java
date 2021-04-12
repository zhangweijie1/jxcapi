package com.mrguo.dao.setup;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.dto.setup.Origin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/1512:59 PM
 * @updater 郭成兴
 * @updatedate 2020/6/1512:59 PM
 */
@Repository("originMapper")
public interface OriginMapper {

    /**
     * 查询期初库存
     *
     * @return
     */
    List<Origin> listStock(Page page, @Param("record") Map<String, Object> map);

    List<Customer> listDeptRece(Page page, @Param("record") Map<String, Object> map);

    List<Supplier> listDeptPay(Page page, @Param("record") Map<String, Object> map);

    List<Account> listAccountOrigin(Page page, @Param("record") Map<String, Object> map);
}
