package com.mrguo.dto;

import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.log.LogAmount;
import lombok.Data;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/910:08 AM
 * @updater 郭成兴
 * @updatedate 2020/7/910:08 AM
 */
@Data
public class LogAmountAndAccountListDto {

    List<LogAmount> logAmountList;

    List<Account> accountList;
}
