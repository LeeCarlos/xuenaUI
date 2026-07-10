package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserRoleDO {

    private String id;
    private String userId;
    private String roleId;
    private Date gmtCreate;
}