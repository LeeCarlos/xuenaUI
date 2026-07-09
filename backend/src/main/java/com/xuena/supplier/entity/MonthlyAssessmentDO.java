package com.xuena.supplier.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MonthlyAssessmentDO {

    private Long id;
    private String yearMonth;
    private String supplierName;
    private String category;
    private BigDecimal total;
    private String grade;
    private BigDecimal dimensionA;
    private BigDecimal dimensionB;
    private BigDecimal dimensionC;
    private BigDecimal dimensionD;
    private BigDecimal subA1;
    private BigDecimal subA2;
    private BigDecimal subA3;
    private BigDecimal subA4;
    private BigDecimal subB1;
    private BigDecimal subB2;
    private BigDecimal subB3;
    private BigDecimal subB4;
    private BigDecimal subB5;
    private BigDecimal subC1;
    private BigDecimal subC2;
    private BigDecimal subC3;
    private BigDecimal subD1a;
    private BigDecimal subD1b;
    private BigDecimal subD2a;
    private BigDecimal subD2b;
    private BigDecimal subD2c;
    private BigDecimal subD2d;
    private BigDecimal subD2e;
    private String conclusion;
    private String exceptionQuality;
    private String exceptionCost;
    private String exceptionDelivery;
    private String exceptionServiceProduct;
    private String exceptionServicePackage;
    private String exceptionOther;
    private String status;
    private String fileName;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}