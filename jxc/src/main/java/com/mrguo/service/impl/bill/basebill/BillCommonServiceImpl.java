package com.mrguo.service.impl.bill.basebill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillMapper;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.service.inter.bill.BillCommonService;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.util.enums.ElmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author 郭成兴（wx:512830037）
 * @Description 单据的基础操作： 新增，作废，查询等
 * @Date
 * @Param
 * @return
 **/
@Service("billCommonServiceImpl")
public class BillCommonServiceImpl extends BaseServiceImpl<Bill>
        implements BillCommonService {

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<Bill> getMapper() {
        return billMapper;
    }

    @Override
    public void addData(Bill bill, List<BillDetail> detailList) throws CustomsException {
        // 1. 新增票据主表
        if (billMapper.insert(bill) <= 0) {
            throw new CustomsException("新增单据失败");
        }
        // 2. 新增票据明细
        if (billDetailService.insertListData(detailList) <= 0) {
            throw new CustomsException("新增单据明细失败");
        }
    }

    @Override
    public void cancle(Long billId) throws CustomsException {
        Bill params = new Bill();
        params.setId(billId);
        params.setIsCancle("1");
        if (billMapper.updateByPrimaryKeySelective(params) <= 0) {
            throw new CustomsException("作废失败");
        }
    }

    @Override
    public void close(Long billId) throws CustomsException {
        Bill params = new Bill();
        params.setId(billId);
        params.setIsClose("1");
        if (billMapper.updateByPrimaryKeySelective(params) <= 0) {
            throw new CustomsException("关闭失败");
        }
    }

    @Override
    public void updateBillAndDetails(Bill bill, List<BillDetail> detailList) throws CustomsException {
        // 1. 修改票据主表
        if (billMapper.updateByPrimaryKeySelective(bill) <= 0) {
            throw new CustomsException("单据修改失败");
        }
        // 2. 重置票据明细
        billDetailService.delDataByBillId(bill.getId());
        for (BillDetail billdetail : detailList) {
            billdetail.setBillId(bill.getId());
        }
        billDetailService.insertListData(detailList);
    }

    @Override
    public String getBillNo(BillCatEnum billCatEnum) throws CustomsException {
        return billOrderNoService.getBillCode(billCatEnum.getCode());
    }

    @Override
    public Bill getBillById(Long billId) {
        return super.selectByPrimaryKey(billId);
    }

    @Override
    public BillAndDetailsVo<Bill> getBillAndDetailsById(Long billId) {
        Bill bill = billMapper.getDataById(billId);
        List<BillDetail> detailList = billDetailService.listMoreDataByBillId(billId);
        BillAndDetailsVo<Bill> billBillAndDetailsVo = new BillAndDetailsVo<>();
        billBillAndDetailsVo.setBill(bill);
        billBillAndDetailsVo.setBillDetailList(detailList);
        return billBillAndDetailsVo;
    }

    @Override
    public IPage<Bill> listWaiteReturnBillsPage(PageParams<Bill> pageParams, BillCatEnum catEnum) {
        Page<Bill> page = pageParams.getPage();
        page.setRecords(billMapper.listWaiteReturnBills(page, catEnum.getCode()));
        return page;
    }

    @Override
    public IPage<Bill> listPage(Page<Bill> page, Map<String, Object> data) {
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        if (!ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            // 非管理员
            SysDataPermission dataPermission = userInfo.getDataPermission();
            // 1. 不允许查询别人的（只能看自己的）
            if (!"1".equals(dataPermission.getIsCanViewOtherUserBill())) {
                Long userId = (Long) request.getAttribute("userId");
                data.put("createUserId", userId);
            }
        }
        page.setRecords(billMapper.listCustom(page, data));
        return page;
    }
}
