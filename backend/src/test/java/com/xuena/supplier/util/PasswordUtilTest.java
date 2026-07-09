package com.xuena.supplier.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    private final PasswordUtil passwordUtil = new PasswordUtil();

    @Test
    @DisplayName("加密密码 - 成功")
    void encrypt_Success() {
        String rawPassword = "password123";
        String encrypted = passwordUtil.encrypt(rawPassword);
        
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > 0);
        assertFalse(encrypted.equals(rawPassword));
    }

    @Test
    @DisplayName("验证密码 - 匹配成功")
    void matches_Success() {
        String rawPassword = "password123";
        String encrypted = passwordUtil.encrypt(rawPassword);
        
        boolean result = passwordUtil.matches(rawPassword, encrypted);
        
        assertTrue(result);
    }

    @Test
    @DisplayName("验证密码 - 不匹配")
    void matches_NotMatch() {
        String encrypted = passwordUtil.encrypt("password123");
        
        boolean result = passwordUtil.matches("wrongpassword", encrypted);
        
        assertFalse(result);
    }

    @Test
    @DisplayName("加密不同密码 - 结果不同")
    void encrypt_DifferentPasswords_DifferentResults() {
        String encrypted1 = passwordUtil.encrypt("password1");
        String encrypted2 = passwordUtil.encrypt("password2");
        
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("加密相同密码多次 - 结果不同(盐值随机)")
    void encrypt_SamePassword_MultipleTimes_DifferentResults() {
        String encrypted1 = passwordUtil.encrypt("password123");
        String encrypted2 = passwordUtil.encrypt("password123");
        
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    @DisplayName("空密码加密 - 成功")
    void encrypt_EmptyPassword_Success() {
        String encrypted = passwordUtil.encrypt("");
        
        assertNotNull(encrypted);
        assertTrue(encrypted.length() > 0);
    }

    @Test
    @DisplayName("验证空密码 - 匹配成功")
    void matches_EmptyPassword_Success() {
        String encrypted = passwordUtil.encrypt("");
        
        assertTrue(passwordUtil.matches("", encrypted));
    }
}