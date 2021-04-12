package com.mrguo.service.impl.basedata;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.common.utils.MapToEntityUtil;
import com.mrguo.dao.bill.BillMapper;
import com.mrguo.dao.bsd.StoreMapper;
import com.mrguo.vo.basedata.StoreVo;
import com.mrguo.entity.bsd.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/11/149:28 AM
 * @updater 郭成兴
 * @updatedate 2019/11/149:28 AM
 */
@Service
public class StoreServiceImpl extends BaseServiceImpl<Store> {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private BillMapper billMapper;

    @Override
    public MyMapper<Store> getMapper() {
        return storeMapper;
    }

    /**
     * 新增仓库，包含门店信息
     *
     * @param storeVo
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(StoreVo storeVo) throws CustomsException {
        // 保存store
        addStoreData(storeVo);
        return Result.okmsg("新增成功");
    }

    /**
     * 删除仓库
     *
     * @param storeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result delData(Long storeId) {
        ArrayList<String> storeIds = new ArrayList<>();
        storeIds.add(storeId.toString());
        if (billMapper.countByStoreIds(storeIds) > 0) {
            return Result.fail("删除失败", "该仓库已有单据引用，不能删除！");
        }
        if (storeMapper.deleteByPrimaryKey(storeId) > 0) {
            return Result.okmsg("删除成功！");
        } else {
            return Result.fail("删除失败！");
        }
    }

    public Result listPage(PageParams<StoreVo> pageParams) throws IOException {
        Page<StoreVo> page = pageParams.getPage();
        StoreVo data = MapToEntityUtil.map2Entity(pageParams.getData(), StoreVo.class);
        page.setRecords(storeMapper.listPage(page, data));
        return Result.ok(page);
    }

    public Result listAllData() {
        return Result.ok(storeMapper.listAllData());
    }

    public StoreVo getDataById(Long id) {
        return storeMapper.selectById(id);
    }

    public Result listStockDataGroupByStore() {
        HashMap<Object, Object> data = new HashMap<>();
        return Result.ok(storeMapper.listStockDataGroupByStore(data));
    }

    public Result updateLockStatusByIds(List<Store> stores) {
        storeMapper.batchUpdateByPrimaryKeySelective(stores);
        return Result.ok();
    }

    void addStoreData(Store store) throws CustomsException {
        if (store.getStatus() == null) {
            store.setStatus("1");
        }
        if (store.getIsLock() == null) {
            store.setIsLock("0");
        }
        int count = storeMapper.countStore();
        store.setId(IDUtil.getSnowflakeId());
        store.setCreateTime(new Date());
        store.setUpdateTime(store.getCreateTime());
        if (storeMapper.insertSelective(store) <= 0) {
            throw new CustomsException("新增仓库失败");
        }
    }
}
