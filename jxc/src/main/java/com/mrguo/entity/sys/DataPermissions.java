package com.mrguo.entity.sys;

import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 数据权限
 * @date 2020/6/85:51 PM
 * @updater 郭成兴
 * @updatedate 2020/6/85:51 PM
 */
@Data
public class DataPermissions {

    /**
     * 是否可以看其他店铺数据
     */
    private String isCanViewOtherStore;

    /**
     * 查看其他门店库存
     */
    private List<String> viewOtherStore;

    /**
     * 价格权限（批发，零售等）
     */
    private List<String> pricePermissions;

    /**
     * 查看他人单据
     */
    private String viewOtherUserBill;

    /**
     * 作废他人单据
     */
    private String cancleOtherUserBill;

    /**
     * 查询他人客户
     */
    private String viewOtherUserCustomer;
}
