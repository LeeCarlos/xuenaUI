# Spec: 用户认证与授权

## 1. 能力概述

提供用户登录、登出、Token 刷新及权限校验能力。

## 2. 验收场景

### 2.1 用户登录

**场景 1：正确用户名密码登录成功**

- 输入：`username=admin, password=123456`
- 操作：POST `/api/auth/login`
- 预期：返回 `code=200`，包含 token、refreshToken、用户信息、角色列表、权限列表、菜单树

**场景 2：错误用户名登录失败**

- 输入：`username=invalid, password=123456`
- 操作：POST `/api/auth/login`
- 预期：返回 `code=401`，提示 "用户名或密码错误"

**场景 3：错误密码登录失败**

- 输入：`username=admin, password=wrong`
- 操作：POST `/api/auth/login`
- 预期：返回 `code=401`，提示 "用户名或密码错误"

**场景 4：空用户名或密码**

- 输入：`username=, password=`
- 操作：POST `/api/auth/login`
- 预期：返回 `code=400`，提示参数校验失败

### 2.2 Token 刷新

**场景 1：有效 Refresh Token 刷新成功**

- 输入：`refreshToken=有效令牌`
- 操作：POST `/api/auth/refresh`
- 预期：返回新的 accessToken 和 refreshToken

**场景 2：过期 Refresh Token**

- 输入：`refreshToken=已过期令牌`
- 操作：POST `/api/auth/refresh`
- 预期：返回 `code=401`，提示 "请重新登录"

**场景 3：无效 Refresh Token**

- 输入：`refreshToken=伪造令牌`
- 操作：POST `/api/auth/refresh`
- 预期：返回 `code=401`，提示 "请重新登录"

### 2.3 权限校验

**场景 1：有权限访问接口**

- 操作：携带有效 Token 访问 `/api/pool/list`
- 预期：正常返回数据

**场景 2：无权限访问接口**

- 操作：携带无权限用户的 Token 访问 `/api/user/list`
- 预期：返回 `code=403`，提示 "无权限访问"

**场景 3：过期 Token**

- 操作：携带已过期的 Token 访问接口
- 预期：返回 `code=401`，提示 "Token 已过期"

**场景 4：无效 Token**

- 操作：携带伪造的 Token 访问接口
- 预期：返回 `code=401`，提示 "Token 无效"

## 3. 数据结构

### 3.1 请求结构

**登录请求**：
```json
{
  "username": "string (必填, 3-50字符)",
  "password": "string (必填, 6-100字符)"
}
```

**刷新请求**：
```json
{
  "refreshToken": "string (必填)"
}
```

### 3.2 响应结构

**登录成功响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "string (JWT Access Token)",
    "refreshToken": "string (Refresh Token)",
    "user": {
      "id": "number",
      "username": "string",
      "realName": "string",
      "department": "string",
      "roles": ["string"],
      "permissions": ["string"]
    },
    "menus": []
  }
}
```

**错误响应**：
```json
{
  "code": "number (400/401/403)",
  "message": "string (错误描述)",
  "data": null
}
```

## 4. 安全要求

| 要求 | 说明 |
|------|------|
| 密码加密 | BCrypt 加密存储 |
| Token 安全 | HTTPS 传输，HttpOnly Cookie |
| 暴力破解防护 | 登录失败 5 次后锁定账户 15 分钟 |
| 输入校验 | 防止 SQL 注入、XSS 攻击 |