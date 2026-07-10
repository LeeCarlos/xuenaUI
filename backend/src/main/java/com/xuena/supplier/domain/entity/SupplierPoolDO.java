package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SupplierPoolDO {

    private String id;
    private String name;
    private String category;
    private Integer isDisabled;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}