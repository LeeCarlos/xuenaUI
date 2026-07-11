package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DictDO {

    private String id;
    private String item;
    private String key;
    private String value;
    private String description;
    private Integer sortOrder;
    private Integer isEnabled;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
