package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MeetingNoteDO {

    private String id;
    private String supplierName;
    private String monthFrom;
    private String monthTo;
    private String note;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
