package com.mrguo.service.impl.bill.basebill;

import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.dao.bsd.StoreMapper;
import com.mrguo.dao.origin.OriginMasterMapper;
import com.mrguo.entity.bsd.Store;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.enums.ElmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 单据的操作校验
 */
@Service
public class BillValidationServiceImpl {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OriginMasterMapper originMasterMapper;
    @Autowired
    private StoreMapper storeMapper;

    /**
     * 校验单据操作权限
     * 1. 关联仓库后，可以查看仓库库存以及经营仓库业务。
     * 2. 分店员工关联其他门店仓库只能查看其他门店仓库库存，不能经营其他门店业务。
     */
    public void valiOperPermission(Long storeId) throws CustomsException {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        if (ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            // 管理员
            return;
        }
        SysDataPermission dataPermission = userInfo.getDataPermission();
        List<String> relationStores = dataPermission.getRelationStores();
        if (!relationStores.contains(String.valueOf(storeId))) {
            throw new CustomsException("该仓库您无权操作!");
        }
    }

    /**
     * 校验单据作废数据权限
     * 1. 可以作废他人创建的单据。
     * 2. 总部可以作废所有人单据.
     * 3. 分店仅限作废本门店的单据
     */
    public void valiCanclePermission(Long createUserId) throws CustomsException {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        Long userId = (Long) request.getAttribute("userId");
        if (ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            // 管理员
            return;
        }
        if (!userId.equals(createUserId)) {
            throw new CustomsException("该单据不是您创建的，无权限作废！");
        }
    }

    /**
     * 结账日期校验
     */
    public void valiBusinessTime(Date businessTime) throws CustomsException {
        // 不能早于系统开启日期，不得早于等于上次结算日期
        Map<String, Object> map = originMasterMapper.selectBalanceTime();
        if (map == null) {
            throw new BusinessException("结账时间不存在");
        }
        String lastBalanceTimeStr = (String) map.get("lastBalanceTime");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date lastBalanceTime = null;
        try {
            lastBalanceTime = simpleDateFormat.parse(lastBalanceTimeStr);
            businessTime = simpleDateFormat.parse(simpleDateFormat.format(businessTime));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CustomsException("结账时间解析有误！");
        }
        if (businessTime.compareTo(lastBalanceTime) <= 0) {
            throw new CustomsException("单据日期，不得等于或小于系统结账日期：" + lastBalanceTimeStr);
        }
    }


    /**
     * 校验仓库是否锁定
     *
     * @param storeId
     */
    public void valiStoreIsLock(Long storeId) throws CustomsException {
        // 非盘点单，才校验
        Store store = storeMapper.selectByPrimaryKey(storeId);
        if ("1".equals(store.getIsLock())) {
            throw new CustomsException("仓库[" + store.getName() + "]被锁定，无法操作！");
        }
    }
}
