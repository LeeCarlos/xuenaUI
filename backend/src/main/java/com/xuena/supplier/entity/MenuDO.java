package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MenuDO {

    private Long id;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Long parentId;
    private Integer sortOrder;
    private String type;
    private String permissionCode;
    private Integer isVisible;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}