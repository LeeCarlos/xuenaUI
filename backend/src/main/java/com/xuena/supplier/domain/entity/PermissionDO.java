package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionDO {

    private String id;
    private String name;
    private String code;
    private String module;
    private String type;
    private String description;
    private String parentId;
    private Integer sortOrder;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
