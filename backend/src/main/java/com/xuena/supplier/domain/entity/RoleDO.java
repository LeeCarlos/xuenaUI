package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDO {

    private String id;
    private String name;
    private String code;
    private String description;
    private Integer isSystem;
    private Integer isEnabled;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
