package com.mrguo.entity.bsd;

import lombok.Data;

import javax.persistence.Table;

@Data
public class UnitDto extends Unit {

    private String value;

    private String label;
}