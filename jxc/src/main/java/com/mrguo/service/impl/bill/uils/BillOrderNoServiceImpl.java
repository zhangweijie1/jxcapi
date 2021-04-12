package com.mrguo.service.impl.bill.uils;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.bill.BillMapper;
import com.mrguo.dao.fin.FinBillMapper;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.dao.sys.SysBillNoRuleMapper;
import com.mrguo.dao.bill.BillStockMapper;
import com.mrguo.entity.sys.SysBillNoRule;
import com.mrguo.util.business.OrderNoGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 单据编号
 * @date 2020/4/157:50 PM
 * @updater 郭成兴
 * @updatedate 2020/4/157:50 PM
 */
@Service
public class BillOrderNoServiceImpl {

    @Autowired
    private OrderNoGenerator orderNoGenerator;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private FinBillMapper finBillMapper;
    @Autowired
    private BillStockMapper billStockMapper;
    @Autowired
    private SysBillNoRuleMapper sysBillNoRuleMapper;
    @Autowired
    private GoodsskuMapper goodsskuMapper;

    /**
     * 生成单据编号
     * 无论是否已经存在单据号，都会生成一个新的单据号
     * 如果存在就用新的单据号，不存在，就用前端的单据号
     *
     * @return
     */
    public String genBillCode(String billNo, String billCat) throws CustomsException {
        // 判断前端的单据号是否存在
        SysBillNoRule billNoPre = getOrderNoPre(billCat);
        String newBillNo = orderNoGenerator.genBillNo(
                billNoPre.getPrefix(), billNoPre.getSuffixLen());
        return billMapper.countBybillNo(billNo) > 0
                ? newBillNo
                : billNo;
    }

    public String genFinBillCode(String billNo, String billCat) throws CustomsException {
        // 判断前端的单据号是否存在
        SysBillNoRule billNoPre = getOrderNoPre(billCat);
        String newBillNo = orderNoGenerator.genBillNo(
                billNoPre.getPrefix(), billNoPre.getSuffixLen());
        return finBillMapper.countBybillNo(billNo) > 0
                ? newBillNo
                : billNo;
    }

    public String genStockBillCode(String billNo, String billCat) throws CustomsException {
        // 判断前端的单据号是否存在
        SysBillNoRule billNoPre = getOrderNoPre(billCat);
        String newBillNo = orderNoGenerator.genBillNo(
                billNoPre.getPrefix(), billNoPre.getSuffixLen());
        return billStockMapper.countBybillNo(billNo) > 0
                ? newBillNo
                : billNo;
    }

    /**
     * 获取单据号
     * 只用于在前端显示
     *
     * @param billCat
     * @return
     */
    public String getBillCode(String billCat) throws CustomsException {
        SysBillNoRule billNoPre = getOrderNoPre(billCat);
        return orderNoGenerator.getBillNo(
                billNoPre.getPrefix(), billNoPre.getSuffixLen());
    }

    public Result getSkuCode() throws CustomsException {
        SysBillNoRule noPre = getOrderNoPre("sku");
        return Result.ok(orderNoGenerator.getSkuCode(
                noPre.getPrefix(), noPre.getSuffixLen()));
    }

    public String genSkuCode(String skucode) throws CustomsException {
        // 判断前端的单据号是否存在
        SysBillNoRule billNoPre = getOrderNoPre("sku");
        if (StringUtils.isBlank(skucode)) {
            return genNewSkuCode(billNoPre);
        }
        if (goodsskuMapper.countByCode(skucode) > 0) {
            // 说明存在
            return genNewSkuCode(billNoPre);
        } else {
            genNewSkuCode(billNoPre);
            return skucode;
        }
    }

    private String genNewSkuCode(SysBillNoRule billNoPre) {
        String newBillNo = orderNoGenerator.genSkuCode(
                billNoPre.getPrefix(), billNoPre.getSuffixLen());
        if (goodsskuMapper.countByCode(newBillNo) > 0) {
            genNewSkuCode(billNoPre);
        }
        return newBillNo;
    }

    private SysBillNoRule getOrderNoPre(String orderCat) throws CustomsException {
        List<SysBillNoRule> sysBillNoRules = sysBillNoRuleMapper.listDataByBillCat(orderCat);
        if (CollectionUtils.isEmpty(sysBillNoRules)) {
            throw new CustomsException("请先配置单据编号规则！");
        }
        if (sysBillNoRules.size() > 1) {
            throw new CustomsException("单据编号规则类型重复，请修改！");
        }
        SysBillNoRule sysBillNoRule = sysBillNoRules.get(0);
        String prefix = sysBillNoRule.getPrefix();
        int length = sysBillNoRule.getSuffixLen();
        if (prefix.length() > 4) {
            throw new CustomsException("自定义前缀最长为4位，请修改！");
        }
        if (length > 4) {
            throw new CustomsException("后缀长度最长为4位，请修改！");
        }
        return sysBillNoRule;
    }
}
