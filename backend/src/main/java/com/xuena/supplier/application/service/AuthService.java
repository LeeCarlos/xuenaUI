package com.xuena.supplier.application.service;

import com.xuena.supplier.interfaces.vo.request.LoginRequest;
import com.xuena.supplier.interfaces.vo.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String token);

    LoginResponse refresh(String refreshToken);
}