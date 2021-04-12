package com.mrguo.dto.sys;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author 郭成兴
 * @ClassName
 * @Description userInfo用于给前端的
 * @date 2019/4/192:34 PM
 * @updater 郭成兴
 * @updatedate 2019/4/192:34 PM
 */
@Data
public class UserInfoApp {

    private String userId;

    private String username;

    @Column(name = "real_name")
    private String realName;

    /**
     * 用户数量
     */
    @Column(name = "user_count")
    private String userCount;

}
