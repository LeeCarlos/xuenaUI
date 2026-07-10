package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryDO {

    private String id;
    private String name;
    private String description;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}