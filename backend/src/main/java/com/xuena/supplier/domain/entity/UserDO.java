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
    private String createName;
    private String createId;
    private Date createDate;
    private String updateName;
    private String updateId;
    private Date updateDate;
}
