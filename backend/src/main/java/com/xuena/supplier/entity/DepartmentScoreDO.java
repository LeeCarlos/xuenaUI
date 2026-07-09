package com.xuena.supplier.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepartmentScoreDO {

    private Long id;
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
    private Date gmtCreate;
    private Date gmtModified;
}