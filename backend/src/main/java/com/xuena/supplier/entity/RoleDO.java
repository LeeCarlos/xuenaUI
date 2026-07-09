package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer isSystem;
    private Integer isEnabled;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}