package com.mrguo.service.impl.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.sys.SysVerifyCodeMapper;
import com.mrguo.entity.sys.SysVerifyCode;
import com.mrguo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/8/29:43 AM
 * @updater 郭成兴
 * @updatedate 2020/8/29:43 AM
 */
@Service
public class SysVerifyCodeServiceImpl extends BaseServiceImpl<SysVerifyCode> {

    @Autowired
    private SysVerifyCodeMapper sysVerifyCodeMapper;

    @Override
    public MyMapper<SysVerifyCode> getMapper() {
        return sysVerifyCodeMapper;
    }

    public Boolean isExistCode(String phoneNo, String code) {
        String minusMin = DateUtil.getMinusMin(1);
        return sysVerifyCodeMapper.countAfterTimeByPhoneNoAndCode(code, phoneNo, minusMin) > 0;
    }

    public Boolean isAllowSendCode(String phoneNo) {
        String minusMin = DateUtil.getMinusMin(1);
        return sysVerifyCodeMapper.countAfterTimeByPhoneNo(phoneNo, minusMin) == 0;
    }
}
