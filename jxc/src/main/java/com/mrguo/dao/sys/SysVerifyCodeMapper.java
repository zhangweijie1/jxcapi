package com.mrguo.dao.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.sys.SysVerifyCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/8/29:36 AM
 * @updater 郭成兴
 * @updatedate 2020/8/29:36 AM
 */

@Repository("sysVerifyCodeMapper")
public interface SysVerifyCodeMapper extends MyMapper<SysVerifyCode> {

    /**
     * 手机号XXX, time时间内的验证码个数
     *
     * @param phoneNo
     * @param time
     * @return
     */
    @Select(value = "select count(1) from sys_verify_code\n" +
            "WHERE phone_no = #{phoneNo} and create_time > #{time}")
    int countAfterTimeByPhoneNo(@Param("phoneNo") String phoneNo,
                                  @Param("time") String time);


    /**
     * 手机号XXX, 验证码XXX, time时间内是否存在
     *
     * @param code
     * @param phoneNo
     * @param time
     * @return
     */
    @Select(value = "select count(1) from sys_verify_code\n" +
            "WHERE phone_no = #{phoneNo} and code = #{code} and create_time > #{time}")
    int countAfterTimeByPhoneNoAndCode(@Param("code") String code,
                                @Param("phoneNo") String phoneNo,
                                @Param("time") String time);

}
