package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionDO {

    private Long id;
    private String name;
    private String code;
    private String module;
    private String type;
    private String description;
    private Long parentId;
    private Integer sortOrder;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}