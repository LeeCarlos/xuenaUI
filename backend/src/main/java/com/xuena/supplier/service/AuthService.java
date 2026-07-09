package com.xuena.supplier.service;

import com.xuena.supplier.vo.request.LoginRequest;
import com.xuena.supplier.vo.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String token);

    LoginResponse refresh(String refreshToken);
}