# Spec: 总览趋势与数据分析

## 1. 能力概述

展示供应商绩效的总体趋势和统计数据，提供可视化图表展示。

## 2. 验收场景

### 2.1 统计数据

**场景 1：获取最新月份统计数据**

- 操作：GET `/api/overview/stats`
- 预期：返回最新月份的平均总分、A级数量、D级数量、最大进步/退步供应商

**场景 2：获取指定月份统计数据**

- 输入：`yearMonth=2026-03`
- 操作：GET `/api/overview/stats`
- 预期：返回指定月份的统计数据

### 2.2 趋势数据

**场景 1：获取所有月份所有类别的默认趋势数据**

- 输入：无（默认查询所有月份、所有类别）
- 操作：GET `/api/overview/trend`
- 预期：返回各月份的指标选项包含（总分、品质考核、成本考核、交货考核、服务考核维度）的平均分和总分数据展示的趋势图

**场景 2：按类别下拉复选筛选趋势**

- 输入：`categories=[牙刷, 牙膏]`
- 操作：GET `/api/overview/trend`
- 类别数据源：从供应商池（sp_supplier_pool）的 category 字段获取所有不重复的类别列表，通过 GET `/api/overview/categories` 接口提供
- 预期：返回指定类别的趋势数据，支持多选类别

**场景 3：按月份筛选趋势**

- 输入：`yearMonths=[2026-03, 2026-04]`
- 操作：GET `/api/overview/trend`
- 月份数据源：从手动打分表（sp_monthly_assessment）中状态为已提交（LOCKED）的数据获取所有不重复的月份列表，通过 GET `/api/overview/year-months` 接口提供
- 预期：返回指定月份的趋势数据，支持多选月份

**场景 4：按类别和月份组合筛选**

- 输入：`categories=[牙刷]&yearMonths=[2026-03, 2026-04]`
- 操作：GET `/api/overview/trend`
- 预期：返回牙刷类别在指定月份的趋势数据

**场景 5：获取维度平均分趋势**

- 输入：`aggregateType=avg`（默认）
- 操作：GET `/api/overview/trend`
- 预期：返回各维度的平均分趋势

**场景 6：获取维度总分趋势**

- 输入：`aggregateType=sum`
- 操作：GET `/api/overview/trend`
- 预期：返回各维度的总分趋势

**场景 7：按供应商下拉筛选趋势**

- 输入：`suppliers=[供应商A, 供应商B]`
- 操作：GET `/api/overview/trend`
- 供应商数据源：从供应商池（sp_supplier_pool）的 供应商名称 字段获取所有不重复的供应商名称列表，通过 GET `/api/overview/suppliers` 接口提供
- 预期：返回指定供应商的趋势数据，支持多选供应商

### 2.3 等级分布

**场景 1：获取等级分布数据**

- 操作：GET `/api/overview/grade-distribution`
- 预期：返回各月份 A/B/C/D 等级的数量分布、供应商总数、各等级占比

**场景 2：按类别筛选等级分布**

- 输入：`category=牙刷`
- 操作：GET `/api/overview/grade-distribution`
- 预期：返回牙刷类别的等级分布、供应商总数、各等级占比

**场景 3：等级分布汇总表格**

- 操作：GET `/api/overview/grade-distribution`
- 预期：返回等级分布数据用于表格展示，包含每个月的供应商总数、A/B/C/D级数量及占比（占比用黑色字体显示，不做底色提示）

### 2.4 维度平均分

**场景 1：获取维度平均分对比**

- 操作：GET `/api/overview/dimension-avg`
- 预期：返回品质、成本、交货、服务四个维度的平均分

**场景 2：按月份筛选**

- 输入：`yearMonths=[2026-03, 2026-04]`
- 操作：GET `/api/overview/dimension-avg`
- 预期：返回指定月份的维度平均分

**场景 3：维度平均分对比柱状图**

- 操作：GET `/api/overview/dimension-avg`
- 预期：返回品质/成本/交货/服务四个板块所有提交月份平均分的数据，用于柱状图展示

## 3. 数据结构

### 3.1 统计数据响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "string",
    "avgTotal": "number",
    "gradeACount": "number",
    "gradeDCount": "number",
    "bestImprover": {"name": "string", "value": "number"},
    "worstDecliner": {"name": "string", "value": "number"},
    "changeFromPrev": "number"
  }
}
```

### 3.2 趋势数据响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "xAxis": ["2026-01", "2026-02", "2026-03"],
    "xAxisLabels": ["1月", "2月", "3月"],
    "categories": ["牙刷", "牙膏"],
    "aggregateType": "avg",
    "series": [
      {"name": "指标总分", "type": "line", "data": [95.2, 94.8, 96.1], "dimension": "total"},
      {"name": "品质考核", "type": "line", "data": [92.5, 93.1, 94.0], "dimension": "dimensionA"},
      {"name": "成本考核", "type": "line", "data": [88.0, 89.5, 90.2], "dimension": "dimensionB"},
      {"name": "交货考核", "type": "line", "data": [96.3, 95.8, 97.0], "dimension": "dimensionC"},
      {"name": "服务考核", "type": "line", "data": [94.1, 94.5, 95.3], "dimension": "dimensionD"}
    ]
  }
}
```

**参数说明**

| 参数 | 类型 | 说明 |
|------|------|------|
| xAxis | string[] | 月份数组（格式：YYYY-MM） |
| xAxisLabels | string[] | 月份标签（格式：X月） |
| categories | string[] | 筛选的类别列表 |
| aggregateType | string | 聚合类型：`avg`（平均分）、`sum`（总分） |
| series | object[] | 趋势数据系列 |
| series[].name | string | 维度名称 |
| series[].type | string | 图表类型：`line` |
| series[].data | number[] | 数据值数组 |
| series[].dimension | string | 维度标识：`total`（指标总分）、`dimensionA`（品质考核）、`dimensionB`（成本考核）、`dimensionC`（交货考核）、`dimensionD`（服务考核） |

### 3.3 等级分布响应

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "string",
      "monthLabel": "string",
      "totalCount": "number",
      "gradeA": "number",
      "gradeAPercent": "string",
      "gradeB": "number",
      "gradeBPercent": "string",
      "gradeC": "number",
      "gradeCPercent": "string",
      "gradeD": "number",
      "gradeDPercent": "string"
    }
  ]
}
```

**参数说明**

| 参数 | 类型 | 说明 |
|------|------|------|
| yearMonth | string | 月份（格式：YYYY-MM） |
| monthLabel | string | 月份标签（格式：X月） |
| totalCount | number | 供应商总数 |
| gradeA/B/C/D | number | 各等级供应商数量 |
| gradeAPercent/BPercent/CPercent/DPercent | string | 各等级占比（格式：XX.X%），黑色字体显示，不做底色提示 |

### 3.4 维度平均分响应

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "string",
      "monthLabel": "string",
      "dimensionA": "number",
      "dimensionB": "number",
      "dimensionC": "number",
      "dimensionD": "number"
    }
  ]
}
```

**参数说明**

| 参数 | 类型 | 说明 |
|------|------|------|
| yearMonth | string | 月份（格式：YYYY-MM） |
| monthLabel | string | 月份标签（格式：X月） |
| dimensionA | number | 品质考核平均分 |
| dimensionB | number | 成本考核平均分 |
| dimensionC | number | 交货考核平均分 |
| dimensionD | number | 服务考核平均分 |

## 4. 图表要求

| 图表类型 | 用途 | 数据源 |
|----------|------|--------|
| 折线图 | 绩效趋势 | `/api/overview/trend` |
| 堆积柱状图 | 等级分布 | `/api/overview/grade-distribution` |
| 表格 | 等级分布汇总（供应商总数、各等级数量及占比） | `/api/overview/grade-distribution` |
| 柱状图 | 各维度平均分对比（品质/成本/交货/服务） | `/api/overview/dimension-avg` |
| 雷达图 | 维度对比 | `/api/compare/radar` |
| 热力图 | 得分率对比 | `/api/compare/heatmap` |