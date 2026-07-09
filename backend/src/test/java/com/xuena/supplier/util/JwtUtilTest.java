package com.xuena.supplier.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-jwt-secret-key-1234567890");
        ReflectionTestUtils.setField(jwtUtil, "expire", 3600000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpire", 604800000L);
    }

    @Test
    @DisplayName("生成Token - 成功")
    void generateToken_Success() {
        String token = jwtUtil.generateToken(1L, "testuser", null);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("解析Token - 成功")
    void parseToken_Success() {
        String token = jwtUtil.generateToken(1L, "testuser", null);
        Claims claims = jwtUtil.parseToken(token);
        
        assertNotNull(claims);
        assertEquals("1", claims.getSubject());
        assertEquals("testuser", claims.get("username"));
    }

    @Test
    @DisplayName("从Token获取用户ID - 成功")
    void getUserIdFromToken_Success() {
        String token = jwtUtil.generateToken(123L, "testuser", null);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        assertEquals(123L, userId);
    }

    @Test
    @DisplayName("检查Token过期 - 未过期")
    void isTokenExpired_NotExpired() {
        String token = jwtUtil.generateToken(1L, "testuser", null);
        boolean expired = jwtUtil.isTokenExpired(token);
        
        assertFalse(expired);
    }

    @Test
    @DisplayName("生成RefreshToken - 成功")
    void generateRefreshToken_Success() {
        String refreshToken = jwtUtil.generateRefreshToken(1L);
        
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
        
        Claims claims = jwtUtil.parseToken(refreshToken);
        assertEquals("1", claims.getSubject());
    }

    @Test
    @DisplayName("解析无效Token - 抛出异常")
    void parseToken_InvalidToken_ThrowsException() {
        assertThrows(Exception.class, () -> jwtUtil.parseToken("invalid-token"));
    }
}