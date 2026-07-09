package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserRoleDO {

    private Long id;
    private Long userId;
    private Long roleId;
    private Date gmtCreate;
}