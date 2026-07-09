package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RolePermissionDO {

    private Long id;
    private Long roleId;
    private Long permissionId;
    private Date gmtCreate;
}