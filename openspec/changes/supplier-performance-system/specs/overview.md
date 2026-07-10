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
- 预期：返回指定类别的趋势数据，支持多选类别

**场景 3：按月份筛选趋势**

- 输入：`yearMonths=[2026-03, 2026-04]`
- 操作：GET `/api/overview/trend`
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