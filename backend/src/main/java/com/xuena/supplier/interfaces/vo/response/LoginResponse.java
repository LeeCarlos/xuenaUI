package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String token;
    private String refreshToken;
    private UserInfo user;
    private List<MenuInfo> menus;

    @Data
    public static class UserInfo {
        private String id;
        private String username;
        private String realName;
        private String department;
        private List<String> roles;
        private List<String> permissions;
    }

    @Data
    public static class MenuInfo {
        private String id;
        private String name;
        private String path;
        private String component;
        private String icon;
        private String parentId;
        private String type;
        private List<MenuInfo> children;
    }
}