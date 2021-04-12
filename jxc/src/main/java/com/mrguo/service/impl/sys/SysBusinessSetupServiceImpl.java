package com.mrguo.service.impl.sys;

import com.alibaba.fastjson.JSONObject;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.sys.SysBusinessSetupMapper;
import com.mrguo.entity.sys.BusinessSetup;
import com.mrguo.entity.sys.SysBusinessSetup;
import com.mrguo.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.BufferPoolMXBean;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/232:46 PM
 * @updater 郭成兴
 * @updatedate 2020/7/232:46 PM
 */
@Service
public class SysBusinessSetupServiceImpl {

    @Autowired
    private SysBusinessSetupMapper businessSetupMapper;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private HttpServletRequest request;

    /**
     * 业务设置
     */
    public void businessSetup(SysBusinessSetup businessSetup) throws CustomsException {
        if (businessSetupMapper.updateValue(businessSetup.getValue()) == 0) {
            throw new CustomsException("修改失败！");
        } else {
            delBusinessSetupRedis();
        }
    }

    public String getBusinessSetup() {
        String key = getBusinessSetupKey();
        String str = redisClient.get(key);
        if (str == null) {
            str = businessSetupMapper.selectValue();
            redisClient.set(key, str);
        }
        return str != null ? str : "{}";
    }

    public BusinessSetup getBusinessSetupObj() {
        String businessSetup = getBusinessSetup();
        return JSONObject.parseObject(businessSetup, BusinessSetup.class);
    }

    public void delBusinessSetupRedis() {
        String key = getBusinessSetupKey();
        redisClient.del(key);
    }

    private String getBusinessSetupKey() {
        Long tanantId = (Long) request.getAttribute("tenantId");
        return tanantId + ":BUSINESS_SETUP";
    }
}
