package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.UserDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.mapper.UserMapper;
import com.xuena.supplier.infrastructure.mapper.UserRoleMapper;
import com.xuena.supplier.infrastructure.util.PasswordUtil;
import com.xuena.supplier.interfaces.vo.request.LoginRequest;
import com.xuena.supplier.interfaces.vo.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordUtil passwordUtil;

    private String testUsername;

    @BeforeEach
    void setUp() {
        testUsername = "test_admin_" + System.currentTimeMillis();
        
        UserDO admin = new UserDO();
        admin.setUsername(testUsername);
        admin.setPassword(passwordUtil.encrypt("admin123"));
        admin.setDepartment("计划");
        admin.setRealName("测试管理员");
        admin.setEmail("test@xuena.com");
        admin.setIsEnabled(1);
        admin.setIsDeleted(0);
        userMapper.insert(admin);
        
        UserDO createdUser = userMapper.selectByUsername(testUsername);
        if (createdUser != null) {
            userRoleMapper.insert(createdUser.getId(), "1");
        }
    }

    @Test
    @DisplayName("用户登录 - 成功")
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername(testUsername);
        request.setPassword("admin123");
        
        LoginResponse response = authService.login(request);
        
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals(testUsername, response.getUser().getUsername());
        assertEquals("测试管理员", response.getUser().getRealName());
    }

    @Test
    @DisplayName("用户登录 - 错误用户名")
    void login_WrongUsername_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("invalid_" + System.currentTimeMillis());
        request.setPassword("admin123");
        
        assertThrows(BusinessException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("用户登录 - 错误密码")
    void login_WrongPassword_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername(testUsername);
        request.setPassword("wrong");
        
        assertThrows(BusinessException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("Token刷新 - 成功")
    void refresh_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername(testUsername);
        request.setPassword("admin123");
        
        LoginResponse loginResponse = authService.login(request);
        String refreshToken = loginResponse.getRefreshToken();
        
        LoginResponse refreshResponse = authService.refresh(refreshToken);
        
        assertNotNull(refreshResponse);
        assertNotNull(refreshResponse.getToken());
        assertNotNull(refreshResponse.getRefreshToken());
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
        request.setUsername(testUsername);
        request.setPassword("admin123");
        
        LoginResponse response = authService.login(request);
        
        assertNotNull(response.getUser().getRoles());
        assertNotNull(response.getUser().getPermissions());
    }
}