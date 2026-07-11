package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepartmentScoreDO {

    private String id;
    private String yearMonth;
    private String supplierName;
    private String department;
    private String dimensionGroup;
    private BigDecimal dimensionScore;
    private String subScores;
    private String exceptionReason;
    private String status;
    private String fileName;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
