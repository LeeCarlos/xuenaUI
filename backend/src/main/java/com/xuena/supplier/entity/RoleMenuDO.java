package com.xuena.supplier.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RoleMenuDO {

    private Long id;
    private Long roleId;
    private Long menuId;
    private Date gmtCreate;
}