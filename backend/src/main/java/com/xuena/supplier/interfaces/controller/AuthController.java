package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.application.service.AuthService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.xuena.supplier.interfaces.vo.request.LoginRequest;
import com.xuena.supplier.interfaces.vo.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取Token")
    public ResultVO<LoginResponse> login(
            @Parameter(description = "登录请求") @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResultVO.success(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出")
    public ResultVO<Void> logout(
            @Parameter(description = "Authorization Token") @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            authService.logout(token.substring(7));
        }
        return ResultVO.success();
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用Refresh Token获取新的Access Token")
    public ResultVO<LoginResponse> refresh(
            @Parameter(description = "Refresh Token") @RequestParam("refreshToken") String refreshToken) {
        LoginResponse response = authService.refresh(refreshToken);
        return ResultVO.success(response);
    }
}