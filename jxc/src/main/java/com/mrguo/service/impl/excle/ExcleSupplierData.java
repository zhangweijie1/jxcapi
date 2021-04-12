package com.mrguo.service.impl.excle;

import com.mrguo.entity.bsd.Supplier;
import com.mrguo.interfaces.ExcleName;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import lombok.Data;
import lombok.Value;

import javax.persistence.Transient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author 郭成兴
 * @ClassName ExcleCustomerTempGenerater
 * @Description 供应商列表excle模板生成
 * @date 2020/7/308:11 PM
 * @updater 郭成兴
 * @updatedate 2020/7/308:11 PM
 */
@Data
public class ExcleSupplierData {

    @ExcleName("分类")
    private String catName;

    @ExcleName("供应商编号")
    private String code;

    @ExcleName("供应商名称")
    private String name;

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
