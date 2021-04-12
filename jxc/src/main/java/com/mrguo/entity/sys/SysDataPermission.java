package com.mrguo.entity.sys;

import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName SysDataPermission
 * @Description 用户数据权限，用于string转实体，反序列化
 * @date 2020/6/173:06 PM
 * @updater 郭成兴
 * @updatedate 2020/6/173:06 PM
 */
@Data
public class SysDataPermission {

    /**
     * 是否可以查看其他仓库
     */
    private String isCanViewOtherStore;

    /**
     * 是否可以查看他人单据
     */
    private String isCanViewOtherUserBill;

    /**
     * 是否可以作废他人单据
     */
    private String isCanCancleOtherUserBill;

    /**
     * 是否可以查看他人客户
     */
    private String isCanViewOtherUserCustomer;

    /**
     * 关联的仓库
     */
    private List<String> relationStores;

    /**
     * 关联的价格
     */
    private List<String> relationPrices;
}
