package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryDO {

    private Long id;
    private String name;
    private String description;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}