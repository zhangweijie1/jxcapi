package com.mrguo.entity.bsd;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "bsd_customer_supplier")
public class CustomerSupplier {
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "customer_id")
    private Long customerId;
}