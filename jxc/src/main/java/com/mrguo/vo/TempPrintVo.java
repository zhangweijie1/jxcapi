package com.mrguo.vo;

import lombok.Data;

import java.util.List;

@Data
public class TempPrintVo {

    private List<TempPrintMaster> master;

    private List<TempPrintColumns> cols;
}
