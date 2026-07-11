package com.xuena.supplier.infrastructure.util;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> userName = new ThreadLocal<>();

    public static void setUserId(String id) {
        userId.set(id);
    }

    public static String getUserId() {
        return userId.get();
    }

    public static void setUserName(String name) {
        userName.set(name);
    }

    public static String getUserName() {
        return userName.get();
    }

    public static void clear() {
        userId.remove();
        userName.remove();
    }
}
