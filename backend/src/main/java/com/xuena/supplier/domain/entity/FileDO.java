package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FileDO {

    private String id;
    private String fileName;
    private String storeKey;
    private String fileType;
    private String businessType;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private String description;
    private Integer isDeleted;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
