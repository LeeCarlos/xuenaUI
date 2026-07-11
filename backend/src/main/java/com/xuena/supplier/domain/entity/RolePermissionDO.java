package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RolePermissionDO {

    private String id;
    private String roleId;
    private String permissionId;
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
