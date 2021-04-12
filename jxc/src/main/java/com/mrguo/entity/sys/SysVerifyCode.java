package com.mrguo.entity.sys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName SysVerifyCode
 * @Description 验证码
 * @date 2019/11/259:50 AM
 * @updater 郭成兴
 * @updatedate 2019/11/259:50 AM
 */

@Data
@Table(name = "sys_verify_code")
public class SysVerifyCode {

    private Long id;

    private String code;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "create_time")
    private Date createTime;
}
