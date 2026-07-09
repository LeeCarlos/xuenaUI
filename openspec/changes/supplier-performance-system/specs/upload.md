# Spec: 数据上传与解析

## 1. 能力概述

支持从 Excel 文件导入月度考核数据，自动解析、验证并存储数据，支持多部门协同打分文件合并。

## 2. 验收场景

### 2.1 文件上传

**场景 1：上传有效 Excel 文件**

- 输入：有效的供应商绩效打分 Excel 文件
- 操作：POST `/api/upload/excel`
- 预期：返回解析结果，包含供应商数量、成功导入数量、跳过数量、验证警告

**场景 2：上传多个 Excel 文件**

- 输入：多个部门的 Excel 文件（质量、计划、包开、采购）
- 操作：POST `/api/upload/excel`
- 预期：分别解析每个文件，返回各自的解析结果

**场景 3：上传无效文件格式**

- 输入：非 Excel 文件（如 .txt、.pdf）
- 操作：POST `/api/upload/excel`
- 预期：返回 `code=400`，提示 "文件格式错误"

**场景 4：上传损坏的 Excel 文件**

- 输入：损坏或不完整的 Excel 文件
- 操作：POST `/api/upload/excel`
- 预期：返回 `code=500`，提示 "文件解析失败"

### 2.2 数据验证

**场景 1：分数超出范围**

- 输入：Excel 中 a1 验货合格率 = 8.5（满分 7）
- 操作：上传文件
- 预期：返回验证警告，包含行号、供应商名称、错误描述

**场景 2：缺失必填字段**

- 输入：Excel 中供应商名称为空
- 操作：上传文件
- 预期：返回验证警告，提示缺失字段

**场景 3：重复数据检测**

- 输入：已存在的年月和供应商数据
- 操作：上传文件
- 预期：提示重复数据，支持覆盖或跳过

### 2.3 模板下载

**场景 1：下载完整模板**

- 输入：`type=full`
- 操作：GET `/api/upload/template`
- 预期：返回完整模板 Excel 文件下载

**场景 2：下载简化模板**

- 输入：`type=simplified`
- 操作：GET `/api/upload/template`
- 预期：返回简化模板 Excel 文件下载

**场景 3：下载部门特定模板**

- 输入：`department=质量`
- 操作：GET `/api/upload/template`
- 预期：返回质量部专用模板

### 2.4 多部门数据合并

**场景 1：合并所有部门数据**

- 输入：`yearMonth=2026-03, departments=[计划,采购,质量,包开]`
- 操作：POST `/api/upload/merge`
- 预期：成功合并，返回合并后的供应商数据、各维度来源、冲突信息

**场景 2：合并部分部门数据**

- 输入：`yearMonth=2026-03, departments=[质量,计划]`
- 操作：POST `/api/upload/merge`
- 预期：合并已有部门数据，其他维度为空

**场景 3：无数据可合并**

- 输入：`yearMonth=2026-03`（该年月无任何部门提交数据）
- 操作：POST `/api/upload/merge`
- 预期：返回 `code=400`，提示 "无数据可合并"

### 2.5 数据删除

**场景 1：删除指定年月数据**

- 输入：`yearMonth=2026-03`
- 操作：DELETE `/api/upload/year-month/2026-03`
- 预期：成功删除该年月所有数据

**场景 2：删除不存在的年月数据**

- 输入：`yearMonth=2026-13`
- 操作：DELETE `/api/upload/year-month/2026-13`
- 预期：返回 `code=404`，提示 "数据不存在"

## 3. 数据结构

### 3.1 请求结构

**上传请求**（multipart/form-data）：
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| files | File[] | 是 | Excel 文件列表 |
| yearMonth | string | 否 | 年月（YYYY-MM） |
| department | string | 否 | 部门标识 |

**合并请求**：
```json
{
  "yearMonth": "string (必填, YYYY-MM)",
  "departments": ["string (计划/采购/质量/包开)"],
  "supplierNames": ["string (可选)"]
}
```

### 3.2 响应结构

**上传响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "string",
      "department": "string",
      "supplierCount": "number",
      "fileName": "string",
      "status": "string (imported/partial/failed)",
      "templateType": "string (full/simplified)",
      "validRows": "number",
      "skippedRows": "number",
      "validationWarnings": [
        {
          "rowIndex": "number",
          "supplierName": "string",
          "message": "string"
        }
      ]
    }
  ]
}
```

**合并响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "string",
    "mergedCount": "number",
    "departmentsMerged": ["string"],
    "suppliers": [
      {
        "name": "string",
        "category": "string",
        "total": "number",
        "grade": "string",
        "A": "number",
        "B": "number",
        "C": "number",
        "D": "number",
        "sourceDepartments": {"A": "string", "B": "string", ...},
        "dimensionScores": [{"dimension": "string", "score": "number", "department": "string", "fullScore": "number"}]
      }
    ],
    "conflicts": [
      {
        "supplierName": "string",
        "dimension": "string",
        "departments": ["string"],
        "resolution": "string"
      }
    ]
  }
}
```

## 4. Excel 模板规范

### 4.1 完整模板列结构（30列）

| 列索引 | 列名 | 数据类型 | 满分 |
|--------|------|----------|------|
| 0 | 序号 | 整数 | - |
| 1 | 供应商名称 | 字符串 | - |
| 2 | 供应商类别 | 字符串 | - |
| 3 | 总分 | 小数 | 100 |
| 4 | 供应商等级 | 字符串 | - |
| 5-8 | A维度子项 (a1-a4) | 小数 | 25 |
| 9 | A总分 | 小数 | 25 |
| 10-14 | B维度子项 (b1-b5) | 小数 | 20 |
| 15 | B总分 | 小数 | 20 |
| 17-19 | C维度子项 (c1-c3) | 小数 | 20 |
| 20 | C总分 | 小数 | 20 |
| 21-27 | D维度子项 (d1a-d2e) | 小数 | 35 |
| 28 | D总分 | 小数 | 35 |
| 29-35 | 异常原因分析 | 文本 | - |

### 4.2 简化模板列结构（9列）

| 列索引 | 列名 | 数据类型 |
|--------|------|----------|
| 0 | 序号 | 整数 |
| 1 | 供应商名称 | 字符串 |
| 2 | 供应商类别 | 字符串 |
| 3 | A总分 | 小数 |
| 4 | B总分 | 小数 |
| 5 | C总分 | 小数 |
| 6 | D总分 | 小数 |
| 7 | 总分 | 小数 |
| 8 | 等级 | 字符串 |

### 4.3 分数验证规则

| 字段 | 最小值 | 最大值 |
|------|--------|--------|
| a1验货合格率 | 0 | 7 |
| a2综合客诉率 | 0 | 8 |
| a3新品开发质量 | 0 | 5 |
| a4质量改善完成率 | 0 | 5 |
| b1价格水平 | 0 | 5 |
| b2供货周期 | 0 | 4 |
| b3付款条件 | 0 | 5 |
| b4报价准确 | 0 | 3 |
| b5成本支持 | 0 | 3 |
| c1交货延迟批次 | 0 | 8 |
| c2交货数量短缺 | 0 | 6 |
| c3订单交付配合度 | 0 | 6 |
| d1a包材技术支持 | 0 | 10 |
| d1b包材技术评估 | 0 | 10 |
| d2a配方技术支持 | 0 | 10 |
| d2b配方技术评估 | 0 | 10 |
| d2c研发打样 | 0 | 5 |
| d2d业务支持度 | 0 | 5 |
| d2e响应配合度 | 0 | 5 |

## 5. 多部门协同规则

| 部门 | 文件名标识 | 负责维度 | 满分 |
|------|-----------|----------|------|
| 计划 | 供应商绩效打分-计划.xlsx | B成本考核、C交货考核 | 40 |
| 采购 | 供应商绩效打分-采购.xlsx | 综合协调 | - |
| 质量 | 供应商绩效打分-质量.xlsx | A品质考核 | 25 |
| 包开 | 供应商绩效打分-包开.xlsx | D服务考核 | 35 |

**合并规则**：
1. 同一供应商在不同部门文件中的分数进行合并
2. 每个维度取对应部门提交的分数
3. 总分 = A + B + C + D
4. 若某维度多部门都有数据，取非零值或取平均值