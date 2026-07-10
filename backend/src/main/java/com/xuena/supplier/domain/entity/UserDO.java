package com.xuena.supplier.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {

    private String id;
    private String username;
    private String password;
    private String department;
    private String realName;
    private String email;
    private Integer isEnabled;
    private Integer isDeleted;
    private Date gmtCreate;
    private Date gmtModified;
}