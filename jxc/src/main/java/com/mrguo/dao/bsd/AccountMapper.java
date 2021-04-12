package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AccountMapper
 * @Description:  账户mapper
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/12 10:13 上午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Repository("accountMapper")
public interface AccountMapper extends MyMapper<Account> {

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<Account> listPage(Page<Account> page,
                             @Param("record") Map<String,Object> params);

    /**
     * 下拉框
     *
     * @return
     */
    List<Account> listOptions();

    /**
     * 获取账户by Ids
     *
     * @return
     */
    List<Account> selectDataByIds(@Param("list") List<String> ids);
}