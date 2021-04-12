package com.mrguo.service.impl.bill.uils;

import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName BillHandleRelationServiceImpl
 * @Description 处理关联单据：
 * 进货订单 -> 进货单 进货订单的change_qty修改
 * 作废时：进货订单的change_qty对应修改
 * @date 2019/12/175:08 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:08 PM
 */
@Service
public class BillHandleRelationQtyServiceImpl {

    @Autowired
    private BillDetailServiceImpl billDetailService;


    public void handleRelationChangeQty(List<BillDetail> detailListPost, List<BillDetail> thisDetails) throws CustomsException {
        handleChangeQty(detailListPost, thisDetails, "1");
    }

    public void handleRelationChangeQty(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        if (billRelationId == null) {
            return;
        }
        List<BillDetail> billDetailPost = billDetailService.listDataByBillId(billRelationId);
        handleChangeQty(billDetailPost, thisDetails, "1");
    }

    public void handleRelationChangeQty2(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        handleChangeQty2(billRelationId, thisDetails, "1");
    }

    /**
     * 处理关联的单据的returnQty的变化
     * 如：借出单，归还了returnQty数量，则修改returnQty
     *
     * @param billRelationId
     * @param thisDetails
     * @throws CustomsException
     */
    public void handleRelationReturnQty(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        handleReturnQty(billRelationId, thisDetails, "1");
    }

    /**
     * 回滚return_qty
     */
    public void rollBackReturnQty(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        handleReturnQty(billRelationId, thisDetails, "0");
    }

    /**
     * 回滚change_qty
     */
    public void rollBackChangeQty(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        List<BillDetail> billDetailPost = billDetailService.listDataByBillId(billRelationId);
        handleChangeQty(billDetailPost, thisDetails, "0");
    }

    /**
     * 回滚change_qty2
     *
     * @param billRelationId
     * @param thisDetails
     * @throws CustomsException
     */
    public void rollBackChangeQty2(Long billRelationId, List<BillDetail> thisDetails) throws CustomsException {
        handleChangeQty2(billRelationId, thisDetails, "0");
    }

    /**
     * 处理单据明细的 returnQty
     *
     * @param billRelationId  关联单据ID
     * @param thisBillDetails 此次单据的billDetails
     * @param oper            1增加数量，0减少数量
     */
    private void handleReturnQty(Long billRelationId,
                                 List<BillDetail> thisBillDetails,
                                 String oper) throws CustomsException {
        if (billRelationId != null) {
            // relation的明细
            List<BillDetail> detailRelationList = billDetailService.listDataByBillId(billRelationId);
            if (detailRelationList.size() != thisBillDetails.size()) {
                throw new CustomsException("单据明细异常！");
            }
            for (BillDetail detailRelation : detailRelationList) {
                for (BillDetail thisDetail : thisBillDetails) {
                    if (detailRelation.getId().equals(thisDetail.getBillRelationId())) {
                        BigDecimal qty = detailRelation.getQuantity() == null
                                ? BigDecimal.ZERO
                                : detailRelation.getQuantity();
                        BigDecimal returnQty = detailRelation.getReturnQuantity() == null
                                ? BigDecimal.ZERO
                                : detailRelation.getReturnQuantity();
                        BigDecimal changeQty = detailRelation.getChangeQuantity() == null
                                ? BigDecimal.ZERO
                                : detailRelation.getChangeQuantity();
                        if ("1".equals(oper)) {
                            BigDecimal maxQty = qty.subtract(returnQty.add(changeQty));
                            if (thisDetail.getQuantity().compareTo(maxQty) > 0) {
                                throw new CustomsException("已超出商品[ " + thisDetail.getName() + " ], 可退数量" + maxQty);
                            }
                            BigDecimal targetReturnQty = returnQty.add(thisDetail.getQuantity());
                            detailRelation.setReturnQuantity(targetReturnQty);
                        } else {
                            detailRelation.setReturnQuantity(returnQty.subtract(thisDetail.getQuantity()));
                        }
                    }
                }
            }
            billDetailService.updateListSelectiveData(detailRelationList);
        }
    }


    /**
     * 处理单据明细的 changeQty
     *
     * @param detailRelationList 关联的单据的明细
     * @param thisBillDetails    此次单据的billDetails
     * @param oper               1增加数量，0减少数量
     */
    private void handleChangeQty(List<BillDetail> detailRelationList,
                                 List<BillDetail> thisBillDetails,
                                 String oper) throws CustomsException {

        for (BillDetail detailRelation : detailRelationList) {
            for (BillDetail thisDetail : thisBillDetails) {
                if (detailRelation.getId().equals(thisDetail.getBillRelationId())) {
                    BigDecimal qty = detailRelation.getQuantity() == null
                            ? BigDecimal.ZERO
                            : detailRelation.getQuantity();
                    BigDecimal returnQty = detailRelation.getReturnQuantity() == null
                            ? BigDecimal.ZERO
                            : detailRelation.getReturnQuantity();
                    BigDecimal changeQty = detailRelation.getChangeQuantity() == null
                            ? BigDecimal.ZERO
                            : detailRelation.getChangeQuantity();
                    if ("1".equals(oper)) {
                        BigDecimal maxQty = qty.subtract(returnQty.add(changeQty));
                        if (thisDetail.getQuantity().compareTo(maxQty) > 0) {
                            throw new CustomsException("已超出商品[ " + thisDetail.getName() + " ], 可转数量" + maxQty);
                        }
                        detailRelation.setChangeQuantity(changeQty.add(thisDetail.getQuantity()));
                    } else {
                        detailRelation.setChangeQuantity(changeQty.subtract(thisDetail.getQuantity()));
                    }
                }
            }
        }
        billDetailService.updateListSelectiveData(detailRelationList);
    }


    /**
     * 处理单据明细的 changeQty
     *
     * @param billRelationId  单据ID
     * @param thisBillDetails 此次单据的billDetails
     * @param oper            1增加数量，0减少数量
     */
    private void handleChangeQty2(Long billRelationId,
                                  List<BillDetail> thisBillDetails,
                                  String oper) throws CustomsException {
        if (billRelationId != null) {
            // relation的明细
            List<BillDetail> detailRelationList = billDetailService.listDataByBillId(billRelationId);
            if (detailRelationList.size() != thisBillDetails.size()) {
                throw new CustomsException("单据明细异常！");
            }
            for (BillDetail detailRelation : detailRelationList) {
                for (BillDetail thisDetail : thisBillDetails) {
                    if (detailRelation.getId().equals(thisDetail.getBillRelationId())) {
                        BigDecimal relationChangeQty2 = detailRelation.getChangeQuantity2() == null
                                ? BigDecimal.ZERO
                                : detailRelation.getChangeQuantity2();
                        if ("1".equals(oper)) {
                            detailRelation.setChangeQuantity2(relationChangeQty2.add(thisDetail.getQuantity()));
                        } else {
                            detailRelation.setChangeQuantity2(relationChangeQty2.subtract(thisDetail.getQuantity()));
                        }
                    }
                }
            }
            billDetailService.updateListSelectiveData(detailRelationList);
        }
    }
}
