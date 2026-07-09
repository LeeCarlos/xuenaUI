package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SupplierPoolDO {

    private Long id;
    private String name;
    private String category;
    private Integer isDisabled;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}