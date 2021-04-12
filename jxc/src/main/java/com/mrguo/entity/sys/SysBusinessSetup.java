package com.mrguo.entity.sys;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "sys_business_setup")
public class SysBusinessSetup {

    /**
     * key-value形式的string值
     */
    private String value;
}