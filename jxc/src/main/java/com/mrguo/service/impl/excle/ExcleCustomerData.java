package com.mrguo.service.impl.excle;

import com.mrguo.interfaces.ExcleName;
import lombok.Data;

/**
 * @author 郭成兴
 * @ClassName ExcleCustomerTempGenerater
 * @Description 供应商列表excle模板生成
 * @date 2020/7/308:11 PM
 * @updater 郭成兴
 * @updatedate 2020/7/308:11 PM
 */
@Data
public class ExcleCustomerData {

    @ExcleName("分类")
    private String catName;

    @ExcleName("客户编号")
    private String code;

    @ExcleName("客户名称")
    private String name;

    @ExcleName("关联业务员")
    private String employeeName;

    @ExcleName("单位电话")
    private String comepanyTel;

    @ExcleName("联系人")
    private String contacter;

    @ExcleName("联系电话")
    private String phone;

    @ExcleName("期初欠款")
    private String originDebt;

    @ExcleName("传真")
    private String tax;

    @ExcleName("邮箱")
    private String email;

    @ExcleName("邮编")
    private String postal;

    @ExcleName("QQ")
    private String qq;

    @ExcleName("地址")
    private String address;

    @ExcleName("备注")
    private String remarks;

    @ExcleName("状态")
    private String isEnable;
}
