package com.mrguo.service.impl.sys;

import com.mrguo.common.entity.Result;
import com.mrguo.dao.sys.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2018/12/278:01 PM
 * @updater 郭成兴
 * @updatedate 2018/12/278:01 PM
 */
@Service
public class LoginServiceImpl {

    @Autowired
    private SysUserMapper sysUserMapper;

    public static Result logout(){
        return Result.ok("退出成功");
    }
}
