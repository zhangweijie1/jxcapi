package com.mrguo.entity.bill;

import lombok.Data;

import javax.persistence.Column;

/**
 * @Author 郭成兴（wx:512830037）
 * @Description //TODO 
 * @Date  
 * @Param 
 * @return 
 **/
@Data
public class BillBorrow extends Bill {

    @Column(name = "borrow_type")
    private String borrowType;

    @Column(name = "exp_return_time")
    private String expReturnTime;
}
