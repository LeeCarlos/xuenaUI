package com.xuena.supplier.service;

import com.xuena.supplier.exception.BusinessException;
import com.xuena.supplier.vo.request.LoginRequest;
import com.xuena.supplier.vo.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("用户登录 - 成功")
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        
        LoginResponse response = authService.login(request);
        
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals("admin", response.getUser().getUsername());
        assertEquals("管理员", response.getUser().getRealName());
    }

    @Test
    @DisplayName("用户登录 - 错误用户名")
    void login_WrongUsername_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("invalid");
        request.setPassword("admin123");
        
        assertThrows(BusinessException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("用户登录 - 错误密码")
    void login_WrongPassword_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong");
        
        assertThrows(BusinessException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("Token刷新 - 成功")
    void refresh_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        
        LoginResponse loginResponse = authService.login(request);
        String refreshToken = loginResponse.getRefreshToken();
        
        LoginResponse refreshResponse = authService.refresh(refreshToken);
        
        assertNotNull(refreshResponse);
        assertNotNull(refreshResponse.getToken());
        assertNotNull(refreshResponse.getRefreshToken());
        assertNotEquals(loginResponse.getToken(), refreshResponse.getToken());
    }

    @Test
    @DisplayName("Token刷新 - 无效令牌")
    void refresh_InvalidToken_ThrowsException() {
        assertThrows(Exception.class, () -> authService.refresh("invalid-token"));
    }

    @Test
    @DisplayName("登录响应包含角色和权限")
    void login_ContainsRolesAndPermissions() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        
        LoginResponse response = authService.login(request);
        
        assertNotNull(response.getUser().getRoles());
        assertTrue(response.getUser().getRoles().contains("admin"));
        assertNotNull(response.getUser().getPermissions());
    }
}