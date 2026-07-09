# Spec: 供应商池管理

## 1. 能力概述

管理供应商基本信息，包括新增、编辑、删除、启用/禁用等操作。

## 2. 验收场景

### 2.1 获取供应商列表

**场景 1：获取所有供应商列表**

- 操作：GET `/api/pool/list`
- 预期：返回供应商列表，包含名称、类别、状态

**场景 2：按类别筛选**

- 输入：`category=牙刷`
- 操作：GET `/api/pool/list`
- 预期：返回牙刷类别的供应商

**场景 3：搜索供应商名称**

- 输入：`searchText=供应商A`
- 操作：GET `/api/pool/list`
- 预期：返回名称包含"供应商A"的供应商

**场景 4：分页查询**

- 输入：`page=1, size=10`
- 操作：GET `/api/pool/list`
- 预期：返回第一页的10条数据，包含总记录数

### 2.2 新增供应商

**场景 1：新增供应商成功**

- 输入：`name=新供应商, category=牙刷`
- 操作：POST `/api/pool/create`
- 预期：返回 `code=200`，包含新供应商信息

**场景 2：新增重复名称**

- 输入：`name=已存在的供应商, category=牙刷`
- 操作：POST `/api/pool/create`
- 预期：返回 `code=400`，提示 "供应商已存在"

**场景 3：名称为空**

- 输入：`name=, category=牙刷`
- 操作：POST `/api/pool/create`
- 预期：返回 `code=400`，提示参数校验失败

### 2.3 编辑供应商

**场景 1：编辑供应商成功**

- 输入：`id=1, name=修改后的名称, category=包装`
- 操作：PUT `/api/pool/update/1`
- 预期：返回 `code=200`，供应商信息已更新

**场景 2：编辑不存在的供应商**

- 输入：`id=999, name=测试`
- 操作：PUT `/api/pool/update/999`
- 预期：返回 `code=404`，提示 "供应商不存在"

**场景 3：编辑为已存在的名称**

- 输入：`id=1, name=供应商B`（供应商B已存在）
- 操作：PUT `/api/pool/update/1`
- 预期：返回 `code=400`，提示 "供应商名称已存在"

### 2.4 删除供应商

**场景 1：删除供应商成功**

- 输入：`id=1`
- 操作：DELETE `/api/pool/delete/1`
- 预期：返回 `code=200`，供应商逻辑删除

**场景 2：删除不存在的供应商**

- 输入：`id=999`
- 操作：DELETE `/api/pool/delete/999`
- 预期：返回 `code=404`，提示 "供应商不存在"

**场景 3：删除有考核数据的供应商**

- 输入：`id=1`（该供应商有月度考核数据）
- 操作：DELETE `/api/pool/delete/1`
- 预期：返回 `code=400`，提示 "该供应商存在考核数据，无法删除"

### 2.5 启用/禁用供应商

**场景 1：禁用供应商**

- 输入：`id=1`（当前状态为启用）
- 操作：PUT `/api/pool/toggle/1`
- 预期：返回 `code=200`，供应商状态变为禁用

**场景 2：启用供应商**

- 输入：`id=1`（当前状态为禁用）
- 操作：PUT `/api/pool/toggle/1`
- 预期：返回 `code=200`，供应商状态变为启用

**场景 3：切换不存在的供应商**

- 输入：`id=999`
- 操作：PUT `/api/pool/toggle/999`
- 预期：返回 `code=404`，提示 "供应商不存在"

### 2.6 批量导入供应商

**场景 1：批量导入成功**

- 输入：包含多个供应商的 Excel 文件
- 操作：POST `/api/pool/import`
- 预期：返回导入结果，包含成功数量、跳过数量

**场景 2：导入重复数据**

- 输入：包含已存在供应商的 Excel 文件
- 操作：POST `/api/pool/import`
- 预期：重复数据被跳过，返回跳过原因

### 2.7 导出供应商池

**场景 1：导出所有供应商**

- 操作：POST `/api/pool/export`
- 预期：返回 Excel 文件下载

**场景 2：按类别筛选导出**

- 输入：`category=牙刷`
- 操作：POST `/api/pool/export`
- 预期：返回牙刷类别的供应商 Excel 文件

## 3. 数据结构

### 3.1 请求结构

**创建请求**：
```json
{
  "name": "string (必填)",
  "category": "string (可选)"
}
```

**更新请求**：
```json
{
  "name": "string (必填)",
  "category": "string (可选)"
}
```

### 3.2 响应结构

**列表响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": "number",
        "name": "string",
        "category": "string",
        "isDisabled": "boolean",
        "gmtCreate": "string",
        "gmtModified": "string"
      }
    ],
    "total": "number",
    "page": "number",
    "size": "number"
  }
}
```

**单条响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "number",
    "name": "string",
    "category": "string",
    "isDisabled": "boolean"
  }
}
```

## 4. 导入模板规范

**列结构**：
| 列索引 | 列名 | 数据类型 | 必填 |
|--------|------|----------|------|
| 0 | 供应商名称 | 字符串 | 是 |
| 1 | 供应商类别 | 字符串 | 否 |