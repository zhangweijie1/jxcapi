package com.mrguo.service.impl.basedata;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bsd.StoreMapper;
import com.mrguo.dao.bsd.StoreOptionsMapper;
import com.mrguo.entity.bsd.Store;
import com.mrguo.dto.OptionStore;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import com.mrguo.util.enums.ElmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/11/149:28 AM
 * @updater 郭成兴
 * @updatedate 2019/11/149:28 AM
 */
@Service
public class StoreOptionsServiceImpl extends BaseServiceImpl<Store> {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private StoreOptionsMapper storeOptionsMapper;

    @Override
    public MyMapper<Store> getMapper() {
        return storeMapper;
    }

    /**
     * 没有级, 单纯仓库下拉
     *
     * @return
     */
    public List<OptionStore> getStoreAllOptions() {
        return storeOptionsMapper.selectStoreAllOptions();
    }
}
