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

**场景 1：获取总分趋势**

- 输入：`metrics=[total]`
- 操作：GET `/api/overview/trend`
- 预期：返回各月份的总分趋势数据

**场景 2：获取多维度趋势**

- 输入：`metrics=[total, A, B, C, D]`
- 操作：GET `/api/overview/trend`
- 预期：返回各维度的趋势数据

**场景 3：按类别筛选趋势**

- 输入：`categories=[牙刷]`
- 操作：GET `/api/overview/trend`
- 预期：返回牙刷类别的趋势数据

**场景 4：按供应商筛选趋势**

- 输入：`suppliers=[供应商A]`
- 操作：GET `/api/overview/trend`
- 预期：返回供应商A的趋势数据

### 2.3 等级分布

**场景 1：获取等级分布数据**

- 操作：GET `/api/overview/grade-distribution`
- 预期：返回各月份 A/B/C/D 等级的数量分布

**场景 2：按类别筛选等级分布**

- 输入：`category=牙刷`
- 操作：GET `/api/overview/grade-distribution`
- 预期：返回牙刷类别的等级分布

### 2.4 维度平均分

**场景 1：获取维度平均分对比**

- 操作：GET `/api/overview/dimension-avg`
- 预期：返回品质、成本、交货、服务四个维度的平均分

**场景 2：按月份筛选**

- 输入：`yearMonths=[2026-03, 2026-04]`
- 操作：GET `/api/overview/dimension-avg`
- 预期：返回指定月份的维度平均分

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
    "xAxis": ["string (YYYY-MM)"],
    "xAxisLabels": ["string (月)"],
    "series": [
      {"name": "string", "type": "line", "data": ["number"]}
    ]
  }
}
```

### 3.3 等级分布响应

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "string",
      "monthLabel": "string",
      "gradeA": "number",
      "gradeB": "number",
      "gradeC": "number",
      "gradeD": "number"
    }
  ]
}
```

### 3.4 维度平均分响应

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "string",
      "dimensionA": "number",
      "dimensionB": "number",
      "dimensionC": "number",
      "dimensionD": "number"
    }
  ]
}
```

## 4. 图表要求

| 图表类型 | 用途 | 数据源 |
|----------|------|--------|
| 折线图 | 绩效趋势 | `/api/overview/trend` |
| 柱状图 | 等级分布 | `/api/overview/grade-distribution` |
| 雷达图 | 维度对比 | `/api/compare/radar` |
| 热力图 | 得分率对比 | `/api/compare/heatmap` |