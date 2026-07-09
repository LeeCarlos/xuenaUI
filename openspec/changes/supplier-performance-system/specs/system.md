# Spec: 系统管理（用户/角色/权限/菜单）

## 1. 能力概述

提供用户管理、角色管理、权限管理、菜单管理的完整 CRUD 功能，支持基于角色的访问控制（RBAC）。

## 2. 用户管理验收场景

### 2.1 用户列表

**场景 1：分页查询用户列表**

- 输入：`page=1, size=20`
- 操作：GET `/api/user/list`
- 预期：返回用户列表，包含用户名、真实姓名、部门、角色

**场景 2：按用户名搜索**

- 输入：`username=admin`
- 操作：GET `/api/user/list`
- 预期：返回用户名包含"admin"的用户

**场景 3：按部门筛选**

- 输入：`department=质量`
- 操作：GET `/api/user/list`
- 预期：返回质量部的用户

**场景 4：按角色筛选**

- 输入：`roleCode=SUPER_ADMIN`
- 操作：GET `/api/user/list`
- 预期：返回超级管理员角色的用户

### 2.2 新增用户

**场景 1：新增用户成功**

- 输入：`username=newuser, password=123456, realName=新用户, department=质量, roleIds=[2]`
- 操作：POST `/api/user/add`
- 预期：返回 `code=200`，用户创建成功

**场景 2：新增重复用户名**

- 输入：`username=admin, password=123456`（admin已存在）
- 操作：POST `/api/user/add`
- 预期：返回 `code=400`，提示 "用户名已存在"

**场景 3：密码过短**

- 输入：`username=test, password=123`
- 操作：POST `/api/user/add`
- 预期：返回 `code=400`，提示 "密码长度至少6位"

### 2.3 更新用户

**场景 1：更新用户信息**

- 输入：`id=1, realName=管理员, department=采购`
- 操作：PUT `/api/user/update`
- 预期：返回 `code=200`，用户信息已更新

**场景 2：更新不存在的用户**

- 输入：`id=999, realName=测试`
- 操作：PUT `/api/user/update`
- 预期：返回 `code=404`，提示 "用户不存在"

### 2.4 删除用户

**场景 1：删除用户成功**

- 输入：`id=2`
- 操作：DELETE `/api/user/delete/2`
- 预期：返回 `code=200`，用户逻辑删除

**场景 2：删除不存在的用户**

- 输入：`id=999`
- 操作：DELETE `/api/user/delete/999`
- 预期：返回 `code=404`，提示 "用户不存在"

**场景 3：删除超级管理员**

- 输入：`id=1`（超级管理员）
- 操作：DELETE `/api/user/delete/1`
- 预期：返回 `code=400`，提示 "超级管理员不可删除"

### 2.5 分配角色

**场景 1：分配角色成功**

- 输入：`userId=2, roleIds=[2, 3]`
- 操作：POST `/api/user/assign-role`
- 预期：返回 `code=200`，角色分配成功

**场景 2：分配不存在的角色**

- 输入：`userId=2, roleIds=[999]`
- 操作：POST `/api/user/assign-role`
- 预期：返回 `code=400`，提示 "角色不存在"

## 3. 角色管理验收场景

### 3.1 角色列表

**场景 1：查询所有角色**

- 操作：GET `/api/role/list`
- 预期：返回角色列表，包含名称、编码、描述

### 3.2 新增角色

**场景 1：新增角色成功**

- 输入：`name=测试角色, code=TEST_ROLE, description=测试`
- 操作：POST `/api/role/add`
- 预期：返回 `code=200`，角色创建成功

**场景 2：新增重复编码**

- 输入：`name=测试, code=SUPER_ADMIN`（已存在）
- 操作：POST `/api/role/add`
- 预期：返回 `code=400`，提示 "角色编码已存在"

### 3.3 分配权限

**场景 1：分配权限成功**

- 输入：`roleId=1, permissionIds=[1, 2, 3]`
- 操作：POST `/api/role/assign-permission`
- 预期：返回 `code=200`，权限分配成功

### 3.4 分配菜单

**场景 1：分配菜单成功**

- 输入：`roleId=1, menuIds=[1, 2, 3]`
- 操作：POST `/api/role/assign-menu`
- 预期：返回 `code=200`，菜单分配成功

## 4. 权限管理验收场景

### 4.1 权限列表

**场景 1：查询权限列表**

- 操作：GET `/api/permission/list`
- 预期：返回权限列表，包含名称、编码、模块、类型

### 4.2 权限树形结构

**场景 1：获取权限树**

- 操作：GET `/api/permission/tree`
- 预期：返回权限树形结构，支持父子级

### 4.3 新增权限

**场景 1：新增权限成功**

- 输入：`name=测试权限, code=test:view, module=test, type=FUNCTION`
- 操作：POST `/api/permission/add`
- 预期：返回 `code=200`，权限创建成功

**场景 2：新增重复编码**

- 输入：`name=测试, code=user:list`（已存在）
- 操作：POST `/api/permission/add`
- 预期：返回 `code=400`，提示 "权限编码已存在"

## 5. 菜单管理验收场景

### 5.1 菜单列表

**场景 1：查询菜单列表**

- 操作：GET `/api/menu/list`
- 预期：返回菜单列表

### 5.2 菜单树形结构

**场景 1：获取菜单树**

- 操作：GET `/api/menu/tree`
- 预期：返回菜单树形结构

**场景 2：获取当前用户菜单**

- 操作：GET `/api/menu/user-menu`
- 预期：返回当前用户可访问的菜单树

### 5.3 新增菜单

**场景 1：新增顶级菜单**

- 输入：`name=测试菜单, path=/test, component=pages/Test, icon=test, parentId=0`
- 操作：POST `/api/menu/add`
- 预期：返回 `code=200`，菜单创建成功

**场景 2：新增子菜单**

- 输入：`name=子菜单, path=/test/sub, component=pages/TestSub, parentId=1`
- 操作：POST `/api/menu/add`
- 预期：返回 `code=200`，子菜单创建成功

### 5.4 更新菜单

**场景 1：更新菜单信息**

- 输入：`id=1, name=修改后的名称`
- 操作：PUT `/api/menu/update`
- 预期：返回 `code=200`，菜单更新成功

### 5.5 删除菜单

**场景 1：删除菜单成功**

- 输入：`id=1`（无子菜单）
- 操作：DELETE `/api/menu/delete/1`
- 预期：返回 `code=200`，菜单删除成功

**场景 2：删除有子菜单的菜单**

- 输入：`id=1`（有子菜单）
- 操作：DELETE `/api/menu/delete/1`
- 预期：返回 `code=400`，提示 "请先删除子菜单"

## 6. 数据结构

### 6.1 用户相关

**用户列表响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "number",
        "username": "string",
        "realName": "string",
        "department": "string",
        "email": "string",
        "isEnabled": "boolean",
        "roles": ["string"],
        "gmtCreate": "string"
      }
    ],
    "total": "number",
    "page": "number",
    "size": "number"
  }
}
```

**新增用户请求**：
```json
{
  "username": "string (必填)",
  "password": "string (必填, 6-100字符)",
  "realName": "string (可选)",
  "department": "string (可选)",
  "email": "string (可选)",
  "roleIds": ["number (可选)"]
}
```

### 6.2 角色相关

**新增角色请求**：
```json
{
  "name": "string (必填)",
  "code": "string (必填)",
  "description": "string (可选)",
  "permissionIds": ["number (可选)"],
  "menuIds": ["number (可选)"]
}
```

### 6.3 权限相关

**新增权限请求**：
```json
{
  "name": "string (必填)",
  "code": "string (必填)",
  "module": "string (必填)",
  "type": "string (MENU/FUNCTION/DATA)",
  "description": "string (可选)",
  "parentId": "number (默认0)",
  "sortOrder": "number (默认0)"
}
```

### 6.4 菜单相关

**新增菜单请求**：
```json
{
  "name": "string (必填)",
  "path": "string (可选)",
  "component": "string (可选)",
  "icon": "string (可选)",
  "parentId": "number (默认0)",
  "sortOrder": "number (默认0)",
  "type": "string (MENU/DIRECTORY/BUTTON)",
  "permissionCode": "string (可选)",
  "isVisible": "boolean (默认true)"
}
```

## 7. 系统预设数据

### 7.1 预设角色

| code | name | description |
|------|------|-------------|
| SUPER_ADMIN | 超级管理员 | 拥有所有权限 |
| ADMIN | 管理员 | 拥有大部分管理权限 |
| QUALITY_ADMIN | 质量部管理员 | 质量部打分管理权限 |
| PLANNING_ADMIN | 计划部管理员 | 计划部打分管理权限 |
| PACKAGING_ADMIN | 包开部管理员 | 包开部打分管理权限 |
| PROCUREMENT_ADMIN | 采购部管理员 | 采购部打分管理权限 |
| USER | 普通用户 | 仅查看权限 |

### 7.2 预设菜单

| 名称 | 路径 | 类型 | 父菜单 |
|------|------|------|--------|
| 总览趋势 | /dashboard | MENU | 0 |
| 维度对比 | /compare | MENU | 0 |
| 排名变动 | /rank | MENU | 0 |
| 明细数据 | /detail | MENU | 0 |
| 供应商管理 | - | DIRECTORY | 0 |
| 供应商池 | /pool | MENU | 供应商管理 |
| 手动打分 | /scoring | MENU | 0 |
| 系统管理 | - | DIRECTORY | 0 |
| 用户管理 | /system/user | MENU | 系统管理 |
| 角色管理 | /system/role | MENU | 系统管理 |
| 权限管理 | /system/permission | MENU | 系统管理 |
| 菜单管理 | /system/menu | MENU | 系统管理 |