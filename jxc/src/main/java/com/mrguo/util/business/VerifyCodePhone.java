package com.mrguo.util.business;

import com.mrguo.common.entity.Result;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.entity.sys.SysVerifyCode;
import com.mrguo.service.impl.sys.SysVerifyCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * @author 郭成兴
 * @ClassName VerifyCodePhone
 * @Description 手机验证码
 * @date 2020/8/27:49 AM
 * @updater 郭成兴
 * @updatedate 2020/8/27:49 AM
 */
@Component
public class VerifyCodePhone {

    @Autowired
    private SysVerifyCodeServiceImpl sysVerifyCodeService;

    /**
     * 获取code
     *
     * @return
     * @throws Exception
     */
    public Result getVerifyCode(String phone) throws Exception {
        if (!sysVerifyCodeService.isAllowSendCode(phone)) {
            return Result.fail("一分钟之内不得重复获取验证码！");
        }
        String code = genVerifyCode();
        // 插入表
        SysVerifyCode sysVerifyCode = getSysVerifyCode(phone, code);
        sysVerifyCodeService.saveData(sysVerifyCode);
        // 真实发送
        return Result.ok("发送成功！");
    }


    public Result valiVerifyCode(String phoneNo, String code) throws Exception {
        if (!sysVerifyCodeService.isExistCode(phoneNo, code)) {
            return Result.fail("该验证码不存在！");
        } else {
            return Result.ok();
        }
    }

    /**
     * 生成一个验证码
     *
     * @return
     */
    private String genVerifyCode() {
        String range = "123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            //生成10以内的整数随机数
            builder.append(range.charAt(new Random().nextInt(range.length())));
        }
        return builder.toString();
    }

    private SysVerifyCode getSysVerifyCode(String phoneNo, String code) {
        SysVerifyCode sysVerifyCode = new SysVerifyCode();
        sysVerifyCode.setId(IDUtil.getSnowflakeId());
        sysVerifyCode.setCode(code);
        sysVerifyCode.setPhoneNo(phoneNo);
        sysVerifyCode.setCreateTime(new Date());
        return sysVerifyCode;
    }
}
