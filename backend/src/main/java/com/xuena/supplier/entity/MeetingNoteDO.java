package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MeetingNoteDO {

    private Long id;
    private String supplierName;
    private String monthFrom;
    private String monthTo;
    private String note;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}