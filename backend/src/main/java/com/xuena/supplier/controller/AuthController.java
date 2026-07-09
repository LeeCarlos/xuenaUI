package com.xuena.supplier.controller;

import com.xuena.supplier.service.AuthService;
import com.xuena.supplier.vo.ResultVO;
import com.xuena.supplier.vo.request.LoginRequest;
import com.xuena.supplier.vo.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResultVO<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResultVO.success(response);
    }

    @PostMapping("/logout")
    public ResultVO<Void> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            authService.logout(token.substring(7));
        }
        return ResultVO.success();
    }

    @PostMapping("/refresh")
    public ResultVO<LoginResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        LoginResponse response = authService.refresh(refreshToken);
        return ResultVO.success(response);
    }
}