package com.mrguo.service.inter.bill;

import com.mrguo.common.entity.Result;

import java.util.List;

/**
 * 单据审核的接口
 * 1. 审核单据
 * 2. 反审核单据
 */
public interface BillAuditService {

    /**
     * 审核单据
     *
     * @return
     */
    Result passAuditList(List<Long> billIds);

    /**
     * 反审核单据
     * @param billIds
     * @return
     */
    Result antiAuditList(List<Long> billIds);
}
