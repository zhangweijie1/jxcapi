package com.mrguo.service.impl.sys;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.sys.SysCardMapper;
import com.mrguo.entity.sys.SysCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/8/138:01 AM
 * @updater 郭成兴
 * @updatedate 2020/8/138:01 AM
 */
@Service
public class SysCardServiceImpl extends BaseServiceImpl<SysCard> {

    @Autowired
    private SysCardMapper sysCardMapper;

    @Override
    public MyMapper<SysCard> getMapper() {
        return sysCardMapper;
    }

    public SysCard selectById(String id){
        return sysCardMapper.selectById(id);
    }
}
