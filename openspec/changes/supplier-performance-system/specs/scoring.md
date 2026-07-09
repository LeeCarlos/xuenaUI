# Spec: 手动打分流程

## 1. 能力概述

各业务部门通过上传专属 Excel 模板进行月度考核打分，系统直接使用模板中的打分值，不进行自动计算。

## 2. 验收场景

### 2.1 年月选择

**场景 1：获取可选择的年份列表**

- 操作：GET `/api/scoring/years`
- 预期：返回年份列表（当前年份及之前年份）

**场景 2：获取指定年份的月份列表及状态**

- 输入：`year=2026`
- 操作：GET `/api/scoring/months/2026`
- 预期：返回该年份所有月份，包含状态（PENDING/IN_PROGRESS/COMPLETED）

**场景 3：获取指定年月的打分状态**

- 输入：`yearMonth=2026-03`
- 操作：GET `/api/scoring/status/2026-03`
- 预期：返回整体状态及各部门提交状态

### 2.2 模板下载

**场景 1：下载质量部模板**

- 输入：`department=质量, yearMonth=2026-03`
- 操作：GET `/api/scoring/template`
- 预期：返回质量部专用模板（包含 A 维度子项）

**场景 2：下载计划部模板**

- 输入：`department=计划, yearMonth=2026-03`
- 操作：GET `/api/scoring/template`
- 预期：返回计划部专用模板（包含 C 维度子项）

**场景 3：下载包开部模板**

- 输入：`department=包开, yearMonth=2026-03`
- 操作：GET `/api/scoring/template`
- 预期：返回包开部专用模板（包含 D1 维度子项）

**场景 4：下载采购部模板**

- 输入：`department=采购, yearMonth=2026-03`
- 操作：GET `/api/scoring/template`
- 预期：返回采购部专用模板（包含 B 维度和 D2 维度子项）

### 2.3 上传打分文件

**场景 1：上传质量部打分文件**

- 输入：质量部 Excel 文件，`yearMonth=2026-03, department=质量`
- 操作：POST `/api/scoring/upload`
- 预期：成功解析并保存 A 维度数据，状态为 DRAFT

**场景 2：上传计划部打分文件**

- 输入：计划部 Excel 文件，`yearMonth=2026-03, department=计划`
- 操作：POST `/api/scoring/upload`
- 预期：成功解析并保存 C 维度数据，状态为 DRAFT

**场景 3：上传包开部打分文件**

- 输入：包开部 Excel 文件，`yearMonth=2026-03, department=包开`
- 操作：POST `/api/scoring/upload`
- 预期：成功解析并保存 D1 维度数据，状态为 DRAFT

**场景 4：上传采购部打分文件**

- 输入：采购部 Excel 文件，`yearMonth=2026-03, department=采购`
- 操作：POST `/api/scoring/upload`
- 预期：成功解析并保存 B 维度和 D2 维度数据，状态为 DRAFT

**场景 5：分数验证失败**

- 输入：Excel 中分数超出范围
- 操作：POST `/api/scoring/upload`
- 预期：返回验证警告，数据不保存

### 2.4 提交打分数据

**场景 1：提交草稿状态的数据**

- 输入：`yearMonth=2026-03, department=质量, confirm=true`
- 操作：POST `/api/scoring/submit/2026-03`
- 预期：状态变为 SUBMITTED，返回提交时间

**场景 2：提交已提交的数据**

- 输入：`yearMonth=2026-03, department=质量`（已提交过）
- 操作：POST `/api/scoring/submit/2026-03`
- 预期：返回 `code=400`，提示 "已提交，不可重复提交"

**场景 3：提交无数据的年月**

- 输入：`yearMonth=2026-03, department=质量`（无数据）
- 操作：POST `/api/scoring/submit/2026-03`
- 预期：返回 `code=400`，提示 "无数据可提交"

### 2.5 获取打分数据

**场景 1：获取指定年月的打分数据**

- 输入：`yearMonth=2026-03`
- 操作：GET `/api/scoring/data/2026-03`
- 预期：返回该年月所有部门的打分数据

**场景 2：获取当前用户部门的数据**

- 操作：GET `/api/scoring/data/2026-03`（当前用户为质量部）
- 预期：返回质量部提交的数据

### 2.6 重置打分数据

**场景 1：重置指定年月的打分为 0**

- 输入：`yearMonth=2026-03`
- 操作：POST `/api/scoring/reset/2026-03`
- 预期：成功重置，数据变为初始状态

**场景 2：重置已提交的数据**

- 输入：`yearMonth=2026-03`（已提交）
- 操作：POST `/api/scoring/reset/2026-03`
- 预期：返回 `code=400`，提示 "已提交的数据不可重置"

## 3. 数据结构

### 3.1 请求结构

**上传请求**（multipart/form-data）：
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | Excel 文件 |
| yearMonth | string | 是 | 年月（YYYY-MM） |
| department | string | 是 | 部门（计划/采购/质量/包开） |

**提交请求**：
```json
{
  "department": "string (必填)",
  "confirm": "boolean (必填, true)"
}
```

### 3.2 响应结构

**上传响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "string",
    "department": "string",
    "supplierCount": "number",
    "status": "string (DRAFT)",
    "parsedData": [
      {
        "supplierName": "string",
        "category": "string",
        "dimensionScores": [{"dimension": "string", "score": "number", "subScores": {}}],
        "grade": "string"
      }
    ]
  }
}
```

**状态响应**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "string",
    "overallStatus": "string (PENDING/IN_PROGRESS/COMPLETED)",
    "departmentStatus": [
      {"department": "string", "status": "string (PENDING/DRAFT/SUBMITTED)", "submitTime": "string"}
    ]
  }
}
```

## 4. 部门模板规范

### 4.1 质量部模板

**列结构**：序号 | 供应商名称 | 供应商类别 | A品质考核(25) | 异常原因分析

**子项**：a1验货合格率(7) | a2综合客诉率(8) | a3新品开发质量(5) | a4质量改善完成率(5)

### 4.2 计划部模板

**列结构**：序号 | 供应商名称 | 供应商类别 | C交货考核(20) | 交货考核异常

**子项**：c1交货延迟批次(8) | c2交货数量短缺(6) | c3订单交付配合度(6)

### 4.3 包开部模板

**列结构**：序号 | 供应商名称 | 供应商类别 | D1服务考核(20) | 服务考核异常原因

**子项**：d1a包材技术支持(10) | d1b包材技术评估(10)

### 4.4 采购部模板

**列结构**：序号 | 供应商名称 | 供应商类别 | B成本考核(20) | d2业务支持(15) | 成本考核异常原因 | 服务考核异常原因

**子项B**：b1价格水平(5) | b2供货周期(4) | b3付款条件(5) | b4报价准确(3) | b5成本支持(3)

**子项D2**：d2a配方技术支持(10) | d2b配方技术评估(10) | d2c研发打样(5) | d2d业务支持度(5) | d2e响应配合度(5)

## 5. 打分规则

| 规则 | 说明 |
|------|------|
| 分数来源 | 直接使用 Excel 模板中的打分值，不进行自动计算 |
| 等级判定 | 等级由各部门在 Excel 模板中自行填写 |
| 总分计算 | 不进行自动总分计算，各维度总分直接使用模板值 |
| 权限控制 | 各部门仅能上传和查看自己负责维度的分数 |
| 状态管理 | DRAFT（草稿）、SUBMITTED（已提交） |