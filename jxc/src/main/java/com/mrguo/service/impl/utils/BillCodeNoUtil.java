package com.mrguo.service.impl.utils;

import com.mrguo.common.entity.Result;
import com.mrguo.dao.sys.SysBillNoRuleMapper;
import com.mrguo.entity.sys.SysBillNoRule;
import com.mrguo.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/2/139:40 PM
 * @updater 郭成兴
 * @updatedate 2020/2/139:40 PM
 */
@Component
public class BillCodeNoUtil {

    @Autowired
    private SysBillNoRuleMapper sysBillNoRuleMapper;

    /**
     * 编号前缀规则
     *
     * @param billCat
     * @return
     */
    public Result getCodeRule(String billCat) {
        List<SysBillNoRule> sysBillNoRuleList = sysBillNoRuleMapper.listDataByBillCat(billCat);
        if (CollectionUtils.isEmpty(sysBillNoRuleList)) {
            return Result.fail("获取订单号失败，请先配置订单号规则！");
        }
        if (sysBillNoRuleList.size() > 1) {
            return Result.fail("获取订单号失败，订单号规则类型重复，请修改！");
        }
        SysBillNoRule sysBillNoRule = sysBillNoRuleList.get(0);
        String prefix = sysBillNoRule.getPrefix();
        int length = sysBillNoRule.getSuffixLen();
        if (prefix.length() > 4) {
            return Result.fail("自定义前缀最长为4位，请修改");
        }
        if (length > 4) {
            return Result.fail("后缀长度最长为4位，请修改");
        }
        return Result.ok(sysBillNoRule);
    }

    /**
     * 生成code
     *
     * @param billCat
     * @param codeCount
     * @return
     */
    public Result genCode(String billCat, int codeCount) {
        Result codeRule = getCodeRule(billCat);
        if (!codeRule.isFlag()) {
            return codeRule;
        }
        SysBillNoRule sysBillNoRule = (SysBillNoRule) codeRule.getData();
        String prefix = sysBillNoRule.getPrefix();
        int length = sysBillNoRule.getSuffixLen();
        int currentNo = codeCount;
        String date = DateUtil.getCurrentDateStr();
        StringBuffer stringBuffer = new StringBuffer();
        String code = String.format("%0" + length + "d", currentNo + 1);
        String billNo = stringBuffer.append(prefix).append(date).append(code).toString();
        return Result.ok(billNo);
    }
}
