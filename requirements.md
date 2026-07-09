# 供应商绩效考核滚动分析平台 - 需求文档

## 1. 项目概述

### 1.1 项目背景
本系统是一个供应商绩效考核滚动分析平台，用于对供应商进行月度绩效考核，并进行多维度的对比分析和趋势追踪。系统支持 Excel 数据导入、手动打分、自动排名、差异分析等功能。

### 1.2 目标用户
- **管理员**：系统配置、用户管理、数据管理
- **采购专员**：供应商考核数据录入、手动打分
- **部门负责人**：查看考核报表、进行差异分析

### 1.3 技术栈
| 层级 | 技术 | 版本 |
|------|------|------|
| 前端 | React | 18+ |
| 前端框架 | Ant Design | 5+ |
| 图表库 | ECharts | 5+ |
| 后端框架 | Spring Boot | 3.2+ |
| 数据库 | MySQL | 8.0+ |
| Excel处理 | EasyExcel | 3.3+ |
| 认证 | JWT | - |
| 构建工具 | Maven | 3.9+ |
| 前端构建 | Vite | 6+ |

---

## 2. 功能模块需求

### 2.1 数据上传模块

**功能描述**：支持从 Excel 文件导入月度考核数据，系统提供标准模板下载，支持多部门协同打分文件合并

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 文件上传 | 支持拖拽或点击上传 .xlsx/.xls 文件 | 高 |
| 多文件上传 | 支持同时上传多个月份或多个部门的考核文件 | 高 |
| 文件解析 | 自动解析 Excel 数据，支持完整模板和简化模板两种格式 | 高 |
| 月份识别 | 从文件名自动识别月份（如"3月考核.xlsx"） | 中 |
| 部门识别 | 从文件名识别部门（计划/采购/质量/包开） | 高 |
| 数据验证 | 验证数据完整性，提示缺失字段、分数范围异常 | 高 |
| 重复检测 | 检测是否已存在同月数据，支持覆盖或跳过 | 高 |
| 模板下载 | 提供标准打分模板 Excel 下载 | 高 |
| 合并导入 | 支持多部门文件合并导入（计划+采购+质量+包开） | 高 |

#### 2.1.1 Excel 模板规范

**模板类型**：系统支持两种模板格式

| 类型 | 描述 | 适用场景 |
|------|------|----------|
| 完整模板 | 包含所有子项分数列（a1-a4, b1-b5, c1-c3, d1a-d2e） | 手动打分、详细考核 |
| 简化模板 | 仅包含维度总分列（A/B/C/D总分） | 快速导入、汇总数据 |

**完整模板列结构**（共30列）：

| 列索引 | 列名 | 数据类型 | 说明 | 满分 |
|--------|------|----------|------|------|
| 0 | 序号 | 整数 | 行号，从1开始 | - |
| 1 | 供应商名称 | 字符串 | 供应商全称 | - |
| 2 | 供应商类别 | 字符串 | 如：牙刷、包装等 | - |
| 3 | 总分 | 小数 | 自动计算或手动填写 | 100 |
| 4 | 供应商等级 | 字符串 | A/B/C/D级 | - |
| **A维度 - 品质考核（25分）** | | | | |
| 5 | a1验货合格率 | 小数 | 验货合格率评分 | 7 |
| 6 | a2综合客诉率 | 小数 | 综合客诉率评分 | 8 |
| 7 | a3新品开发质量 | 小数 | 新品开发质量评分 | 5 |
| 8 | a4质量改善完成率 | 小数 | 质量改善完成率评分 | 5 |
| 9 | A总分 | 小数 | A维度总分（自动计算） | 25 |
| **B维度 - 成本考核（20分）** | | | | |
| 10 | b1价格水平 | 小数 | 价格水平评分 | 5 |
| 11 | b2供货周期 | 小数 | 供货周期评分 | 4 |
| 12 | b3付款条件 | 小数 | 付款条件评分 | 5 |
| 13 | b4报价准确 | 小数 | 报价准确性评分 | 3 |
| 14 | b5成本支持 | 小数 | 成本支持力度评分 | 3 |
| 15 | B总分 | 小数 | B维度总分（自动计算） | 20 |
| **C维度 - 交货考核（20分）** | | | | |
| 16 | 备用列 | - | 预留列 | - |
| 17 | c1交货延迟批次 | 小数 | 交货延迟批次评分 | 8 |
| 18 | c2交货数量短缺 | 小数 | 交货数量短缺评分 | 6 |
| 19 | c3订单交付配合度 | 小数 | 订单交付配合度评分 | 6 |
| 20 | C总分 | 小数 | C维度总分（自动计算） | 20 |
| **D维度 - 服务考核（35分）** | | | | |
| 21 | d1a包材技术支持 | 小数 | 包材技术支持评分 | 10 |
| 22 | d1b包材技术评估 | 小数 | 包材技术评估评分 | 10 |
| 23 | d2a配方技术支持 | 小数 | 成品/配方技术支持评分 | 10 |
| 24 | d2b配方技术评估 | 小数 | 成品/配方技术评估评分 | 10 |
| 25 | d2c研发打样 | 小数 | 研发打样支持评分 | 5 |
| 26 | d2d业务支持度 | 小数 | 业务支持度评分 | 5 |
| 27 | d2e响应配合度 | 小数 | 响应配合度评分 | 5 |
| 28 | D总分 | 小数 | D维度总分（自动计算） | 35 |
| **异常原因分析** | | | | |
| 29 | 会议结论 | 文本 | 会议讨论结论 | - |
| 30 | 品质考核异常原因 | 文本 | A维度异常原因 | - |
| 31 | 成本考核异常原因 | 文本 | B维度异常原因 | - |
| 32 | 交货考核异常原因 | 文本 | C维度异常原因 | - |
| 33 | 服务考核-成品异常原因 | 文本 | D维度成品相关异常 | - |
| 34 | 服务考核-包材异常原因 | 文本 | D维度包材相关异常 | - |
| 35 | 其他异常原因 | 文本 | 其他异常原因 | - |

**简化模板列结构**（共9列）：

| 列索引 | 列名 | 数据类型 | 说明 |
|--------|------|----------|------|
| 0 | 序号 | 整数 | 行号 |
| 1 | 供应商名称 | 字符串 | 供应商全称 |
| 2 | 供应商类别 | 字符串 | 类别名称 |
| 3 | A总分 | 小数 | 品质考核总分 |
| 4 | B总分 | 小数 | 成本考核总分 |
| 5 | C总分 | 小数 | 交货考核总分 |
| 6 | D总分 | 小数 | 服务考核总分 |
| 7 | 总分 | 小数 | 综合总分 |
| 8 | 等级 | 字符串 | A/B/C/D级 |

**分数验证规则**：

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

#### 2.1.2 多部门协同打分

系统支持四个部门分别提交打分数据，然后进行汇总合并：

| 部门 | 文件名标识 | 负责维度 | 说明 |
|------|-----------|----------|------|
| 计划 | 供应商绩效打分-计划.xlsx | B成本考核、C交货考核 | 负责价格、周期、付款、交货相关评分 |
| 采购 | 供应商绩效打分-采购.xlsx | 综合协调 | 负责整体评分协调和汇总 |
| 质量 | 供应商绩效打分-质量.xlsx | A品质考核 | 负责验货合格率、客诉率、质量改善相关评分 |
| 包开 | 供应商绩效打分-包开.xlsx | D服务考核 | 负责包材技术、配方技术、研发打样相关评分 |

**合并规则**：
1. 同一供应商在不同部门文件中的分数进行合并
2. 每个维度取对应部门提交的分数
3. 总分 = A + B + C + D
4. 若某维度多部门都有数据，取非零值或取平均值

### 2.2 总览趋势模块

**功能描述**：展示供应商绩效的总体趋势和统计数据

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 统计卡片 | 展示平均总分、A级数量、D级数量、最大进步/退步供应商 | 高 |
| 趋势折线图 | 展示供应商绩效趋势（支持按类别、指标、月份、供应商筛选） | 高 |
| 等级分布柱状图 | 展示各月份 A/B/C/D 等级分布 | 高 |
| 维度平均分对比图 | 展示品质、成本、交货、服务四个维度的平均分对比 | 高 |

### 2.3 维度对比模块

**功能描述**：对供应商进行多维度对比分析

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 雷达图对比 | 展示单个供应商不同月份或多个供应商同一月份的雷达图对比 | 高 |
| 热力图对比 | 展示供应商各维度得分率的热力图 | 高 |
| 筛选功能 | 支持按类别、供应商、月份筛选 | 高 |

### 2.4 排名变动模块

**功能描述**：分析供应商在不同月份之间的排名变动

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 月份选择 | 选择两个对比月份 | 高 |
| 排名对比表 | 展示两个月份的排名、分数、等级对比 | 高 |
| 排名变化 | 计算排名上升/下降的数量 | 高 |
| 分数变化 | 计算分数变化值 | 高 |
| 维度差异 | 展示各维度的分数变化 | 高 |
| 差异原因 | 自动生成差异原因分析 | 高 |
| 趋势Sparkline | 展示各维度趋势迷你图 | 高 |
| 会议记录 | 支持记录会议结论和备注 | 高 |
| 筛选功能 | 支持按等级变化（升分/降分）和供应商名称搜索 | 中 |
| 导出Excel | 导出排名分析数据 | 中 |

### 2.5 明细数据模块

**功能描述**：展示完整的月度考核明细数据

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 月份选择 | 选择查看的月份 | 高 |
| 等级筛选 | 按 A/B/C/D 等级筛选 | 高 |
| 搜索功能 | 按供应商名称或类别搜索 | 高 |
| 明细表格 | 展示完整的考核明细，包括所有子项分数 | 高 |
| 异常原因 | 展示异常原因分析字段 | 高 |
| 导出当月 | 导出当前月份数据 | 中 |
| 导出全部 | 导出所有月份数据 | 中 |

### 2.6 供应商池模块

**功能描述**：管理供应商基本信息

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 供应商列表 | 展示所有供应商（启用/禁用状态） | 高 |
| 新增供应商 | 添加新供应商名称和类别 | 高 |
| 编辑供应商 | 修改供应商信息 | 高 |
| 删除供应商 | 删除供应商（逻辑删除） | 高 |
| 启用/禁用 | 切换供应商状态 | 高 |
| 批量导入 | 从 Excel 批量导入供应商 | 中 |
| 导出供应商池 | 导出供应商列表 | 中 |

### 2.7 手动打分模块

**功能描述**：各业务部门通过上传专属Excel模板进行月度考核打分，不进行自动计算，直接使用模板中的打分值

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 年月选择 | 选择考核年月（年+月组合），支持下拉选择或日期选择器 | 高 |
| 部门选择 | 选择当前登录用户所属部门（计划/采购/质量/包开） | 高 |
| 模板下载 | 根据选择的部门，下载对应的打分模板（按部门生成） | 高 |
| Excel上传 | 上传本部门的打分Excel文件，直接使用模板中的打分值 | 高 |
| 打分预览 | 预览上传的打分数据，确认无误后提交 | 高 |
| 分数验证 | 验证分数是否在有效范围内 | 高 |
| 部门权限控制 | 各部门仅能上传和查看自己负责维度的分数 | 高 |
| 保存草稿 | 保存未提交的打分数据 | 高 |
| 提交审核 | 提交打分数据，提交后不可修改 | 高 |
| 状态管理 | 草稿状态、已提交状态 | 高 |
| 重置数据 | 重置所有打分为0 | 低 |

#### 2.7.1 维度权限分配

**根据实际 Excel 文件解析结果，各部门职责如下：**

| 部门 | 可编辑维度 | 负责子项 | 满分 | 文件标识 |
|------|-----------|----------|------|----------|
| 质量部 | A品质考核 | a1验货合格率, a2综合客诉率, a3新品开发质量, a4质量改善完成率 | 25 | 供应商绩效打分-质量.xlsx |
| 计划部 | C交货考核 | c1交货延迟批次, c2交货数量短缺, c3订单交付配合度 | 20 | 供应商绩效打分-计划.xlsx |
| 包开部 | D1服务考核（包材） | d1a包材技术支持, d1b包材技术评估 | 20 | 供应商绩效打分-包开.xlsx |
| 采购部 | B成本考核 + D2服务考核（业务支持） | b1价格水平, b2供货周期, b3付款条件, b4报价准确, b5成本支持, d2a配方技术支持, d2b配方技术评估, d2c研发打样, d2d业务支持度, d2e响应配合度 | 35 | 供应商绩效打分-采购.xlsx |

**各部门模板结构说明：**

**质量部模板（供应商绩效打分-质量.xlsx）**：
```
列结构：序号 | 供应商名称 | 供应商类别 | A品质考核(25) | 异常原因分析
子项：a1验货合格率(7) | a2综合客诉率(8) | a3新品开发质量(5) | a4质量改善完成率(5)
说明：直接使用模板中填写的分数值，不进行自动计算
```

**计划部模板（供应商绩效打分-计划.xlsx）**：
```
列结构：序号 | 供应商名称 | 供应商类别 | C交货考核(20) | 交货考核异常
子项：c1交货延迟批次(8) | c2交货数量短缺(6) | c3订单交付配合度(6)
说明：直接使用模板中填写的分数值，不进行自动计算
```

**包开部模板（供应商绩效打分-包开.xlsx）**：
```
列结构：序号 | 供应商名称 | 供应商类别 | D1服务考核(20) | 服务考核异常原因(d1包材供应商)
子项：d1a包材技术支持(10) | d1b包材技术评估(10)
说明：直接使用模板中填写的分数值，不进行自动计算
```

**采购部模板（供应商绩效打分-采购.xlsx）**：
```
列结构：序号 | 供应商名称 | 供应商类别 | B成本考核(20) | d2业务支持(15) | 成本考核异常原因 | 服务考核异常原因
子项B：b1价格水平(5) | b2供货周期(4) | b3付款条件(5) | b4报价准确(3) | b5成本支持(3)
子项D2：d2a配方技术支持(10) | d2b配方技术评估(10) | d2c研发打样(5) | d2d业务支持度(5) | d2e响应配合度(5)
说明：直接使用模板中填写的分数值，不进行自动计算
```

#### 2.7.2 打分规则说明

**分数来源**：各部门上传的 Excel 模板中已包含完整的打分值，系统直接读取并使用模板中的数值，**不进行自动计算**。

**等级判定**：系统**不进行自动等级判定**，等级由各部门在 Excel 模板中自行填写。

**总分计算**：系统**不进行自动总分计算**。各部门模板中填写的维度总分（A/B/C/D1/D2）直接使用。合并时系统会将各部门的维度分数汇总，但**最终总分和等级由采购部在合并确认时统一填写**。

**年月选择组件**：
- 组件类型：年+月组合选择器（年份下拉 + 月份下拉，或日期选择器限制为月份）
- 数据范围：支持选择当前年份及之前年份的所有月份
- 默认值：当前年月
- 联动效果：选择年月后，页面刷新显示该年月下各部门的打分状态

**打分流程**：
1. 选择考核年月
2. 选择所属部门
3. 下载对应部门的打分模板
4. 在 Excel 中填写打分数据（包括子项分数、维度总分、等级）
5. 上传填写完成的 Excel 文件
6. 预览上传的数据，确认无误后提交

---

## 3. 数据模型设计

### 3.1 ER 关系图

#### 业务数据关系

```
┌──────────────────────┐     1:N     ┌────────────────────────────┐
│  sp_supplier_pool    │─────────────►│ sp_monthly_assessment     │
│   (供应商池)          │             │   (月度考核汇总记录)        │
└──────────────────────┘             └────────────────────────────┘
        │                                     │
        │ 1:N                                 │ 1:N
        ▼                                     ▼
┌──────────────────────┐             ┌────────────────────────────┐
│  sp_category         │◄────────────│ sp_department_score       │
│   (类别管理)          │  1:N        │   (部门打分明细)           │
└──────────────────────┘             └────────────────────────────┘
                                             │
                                             │ 1:N
                                             ▼
                                   ┌────────────────────────────┐
                                   │ sp_meeting_note            │
                                   │   (会议记录)                │
                                   └────────────────────────────┘
```

#### 权限管理关系

```
┌──────────────────────┐     N:M     ┌──────────────────────┐     N:M     ┌──────────────────────┐
│   sp_user            │─────────────►│   sp_user_role       │◄─────────────│   sp_role            │
│   (用户表)            │             │   (用户角色关联)       │             │   (角色表)            │
└──────────────────────┘             └──────────────────────┘             └──────────────────────┘
                                              │                              │
                                              │                              │ N:M
                                              │                              ▼
                                              │                  ┌──────────────────────┐
                                              │                  │   sp_role_menu       │
                                              │                  │   (角色菜单关联)       │
                                              │                  └──────────────────────┘
                                              │                              │
                                              │                              │ N:1
                                              │                              ▼
                                              │                  ┌──────────────────────┐
                                              │                  │   sp_menu            │
                                              │                  │   (菜单表)            │
                                              │                  └──────────────────────┘
                                              │
                                              │ N:M
                                              ▼
                                    ┌──────────────────────┐     N:M     ┌──────────────────────┐
                                    │   sp_role_permission │◄─────────────│   sp_permission      │
                                    │   (角色权限关联)       │             │   (权限表)            │
                                    └──────────────────────┘             └──────────────────────┘
```

### 3.2 数据库表结构

> **设计说明**：遵循阿里巴巴Java开发手册MySQL规约，表名采用`业务缩写_表作用`命名，字段名使用下划线分隔，必备字段`id`、`gmt_create`、`gmt_modified`，逻辑删除使用`is_deleted`字段。

#### 3.2.1 sp_supplier_pool（供应商池表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| name | VARCHAR(255) | NOT NULL | 供应商名称 |
| category | VARCHAR(100) | NULL | 供应商类别 |
| is_disabled | TINYINT UNSIGNED | DEFAULT 0 | 是否禁用（0-启用，1-禁用） |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_supplier_name`：`(name)` 唯一索引
- `idx_category`：`(category)` 普通索引

#### 3.2.2 sp_monthly_assessment（月度考核记录表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| year_month | VARCHAR(7) | NOT NULL | 年月（格式：YYYY-MM） |
| supplier_name | VARCHAR(255) | NOT NULL | 供应商名称 |
| category | VARCHAR(100) | NULL | 供应商类别 |
| total | DECIMAL(5,1) | NULL | 总分 |
| grade | VARCHAR(20) | NULL | 等级 |
| dimension_a | DECIMAL(5,1) | NULL | A维度总分（品质考核） |
| dimension_b | DECIMAL(5,1) | NULL | B维度总分（成本考核） |
| dimension_c | DECIMAL(5,1) | NULL | C维度总分（交货考核） |
| dimension_d | DECIMAL(5,1) | NULL | D维度总分（服务考核） |
| sub_a1 | DECIMAL(5,1) | NULL | a1验货合格率 |
| sub_a2 | DECIMAL(5,1) | NULL | a2综合客诉率 |
| sub_a3 | DECIMAL(5,1) | NULL | a3新品开发质量 |
| sub_a4 | DECIMAL(5,1) | NULL | a4质量改善完成率 |
| sub_b1 | DECIMAL(5,1) | NULL | b1价格水平 |
| sub_b2 | DECIMAL(5,1) | NULL | b2供货周期 |
| sub_b3 | DECIMAL(5,1) | NULL | b3付款条件 |
| sub_b4 | DECIMAL(5,1) | NULL | b4报价准确 |
| sub_b5 | DECIMAL(5,1) | NULL | b5成本支持 |
| sub_c1 | DECIMAL(5,1) | NULL | c1交货延迟批次 |
| sub_c2 | DECIMAL(5,1) | NULL | c2交货数量短缺 |
| sub_c3 | DECIMAL(5,1) | NULL | c3订单交付配合度 |
| sub_d1a | DECIMAL(5,1) | NULL | d1a包材技术支持 |
| sub_d1b | DECIMAL(5,1) | NULL | d1b包材技术评估 |
| sub_d2a | DECIMAL(5,1) | NULL | d2a成品技术支持 |
| sub_d2b | DECIMAL(5,1) | NULL | d2b成品技术评估 |
| sub_d2c | DECIMAL(5,1) | NULL | d2c研发打样 |
| sub_d2d | DECIMAL(5,1) | NULL | d2d业务支持度 |
| sub_d2e | DECIMAL(5,1) | NULL | d2e响应配合度 |
| conclusion | TEXT | NULL | 会议结论 |
| exception_quality | TEXT | NULL | 品质考核异常原因 |
| exception_cost | TEXT | NULL | 成本考核异常原因 |
| exception_delivery | TEXT | NULL | 交货考核异常原因 |
| exception_service_product | TEXT | NULL | 服务考核-成品异常原因 |
| exception_service_package | TEXT | NULL | 服务考核-包材异常原因 |
| exception_other | TEXT | NULL | 其他异常原因 |
| status | VARCHAR(20) | DEFAULT 'DRAFT' | 状态（DRAFT-草稿，LOCKED-已提交） |
| file_name | VARCHAR(255) | NULL | 原始文件名 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_year_month_supplier`：`(year_month, supplier_name)` 唯一索引
- `idx_year_month`：`(year_month)` 普通索引
- `idx_category`：`(category)` 普通索引
- `idx_grade`：`(grade)` 普通索引

#### 3.2.3 sp_department_score（部门打分明细表）

**说明**：用于记录各部门单独提交的打分数据，支持多部门协同打分，最终汇总到 sp_monthly_assessment 表。

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| year_month | VARCHAR(7) | NOT NULL | 年月（格式：YYYY-MM） |
| supplier_name | VARCHAR(255) | NOT NULL | 供应商名称 |
| department | VARCHAR(50) | NOT NULL | 提交部门（计划/采购/质量/包开） |
| dimension_group | VARCHAR(20) | NOT NULL | 维度组（A/B/C/D1/D2） |
| dimension_score | DECIMAL(5,1) | NULL | 维度总分 |
| sub_scores | JSON | NULL | 子项分数JSON（如 {"a1":7,"a2":8}） |
| exception_reason | TEXT | NULL | 异常原因说明 |
| status | VARCHAR(20) | DEFAULT 'DRAFT' | 状态（DRAFT-草稿，SUBMITTED-已提交） |
| file_name | VARCHAR(255) | NULL | 原始文件名 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_year_month_supplier_dept`：`(year_month, supplier_name, department)` 唯一索引
- `idx_year_month_dept`：`(year_month, department)` 普通索引
- `idx_dimension_group`：`(dimension_group)` 普通索引

**维度组与部门映射**：

| dimension_group | 维度名称 | 所属部门 | 满分 |
|-----------------|----------|----------|------|
| A | 品质考核 | 质量部 | 25 |
| B | 成本考核 | 采购部 | 20 |
| C | 交货考核 | 计划部 | 20 |
| D1 | 服务考核-包材 | 包开部 | 20 |
| D2 | 服务考核-业务支持 | 采购部 | 15 |

#### 3.2.4 sp_meeting_note（会议记录表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| supplier_name | VARCHAR(255) | NOT NULL | 供应商名称 |
| month_from | VARCHAR(7) | NOT NULL | 起始年月（格式：YYYY-MM） |
| month_to | VARCHAR(7) | NOT NULL | 结束年月（格式：YYYY-MM） |
| note | TEXT | NULL | 会议记录内容 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `idx_supplier_month_range`：`(supplier_name, month_from, month_to)` 普通索引

#### 3.2.5 sp_category（类别管理表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 类别名称 |
| description | VARCHAR(500) | NULL | 类别描述 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_category_name`：`(name)` 唯一索引

#### 3.2.6 sp_user（用户表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| username | VARCHAR(100) | NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码（加密存储） |
| department | VARCHAR(50) | NULL | 所属部门（计划/采购/质量/包开） |
| real_name | VARCHAR(100) | NULL | 真实姓名 |
| email | VARCHAR(255) | NULL | 邮箱 |
| is_enabled | TINYINT UNSIGNED | DEFAULT 1 | 是否启用（0-禁用，1-启用） |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_username`：`(username)` 唯一索引
- `idx_department`：`(department)` 普通索引

#### 3.2.7 sp_role（角色表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 角色名称（如：超级管理员、质量部管理员） |
| code | VARCHAR(50) | NOT NULL | 角色编码（如：SUPER_ADMIN、QUALITY_ADMIN） |
| description | VARCHAR(500) | NULL | 角色描述 |
| is_system | TINYINT UNSIGNED | DEFAULT 0 | 是否系统角色（0-自定义，1-系统内置） |
| is_enabled | TINYINT UNSIGNED | DEFAULT 1 | 是否启用（0-禁用，1-启用） |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_role_code`：`(code)` 唯一索引
- `idx_role_name`：`(name)` 普通索引

**系统预设角色**：

| code | name | description |
|------|------|-------------|
| SUPER_ADMIN | 超级管理员 | 拥有所有权限 |
| ADMIN | 管理员 | 拥有大部分管理权限 |
| QUALITY_ADMIN | 质量部管理员 | 质量部打分管理权限 |
| PLANNING_ADMIN | 计划部管理员 | 计划部打分管理权限 |
| PACKAGING_ADMIN | 包开部管理员 | 包开部打分管理权限 |
| PROCUREMENT_ADMIN | 采购部管理员 | 采购部打分管理权限 |
| USER | 普通用户 | 仅查看权限 |

#### 3.2.8 sp_permission（权限表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 权限名称（如：用户管理-新增） |
| code | VARCHAR(100) | NOT NULL | 权限编码（如：user:add） |
| module | VARCHAR(50) | NOT NULL | 所属模块（如：system、assessment） |
| type | VARCHAR(20) | DEFAULT 'FUNCTION' | 权限类型（MENU-菜单权限，FUNCTION-功能权限，DATA-数据权限） |
| description | VARCHAR(500) | NULL | 权限描述 |
| parent_id | BIGINT UNSIGNED | DEFAULT 0 | 父权限ID（0表示顶级） |
| sort_order | INT | DEFAULT 0 | 排序号 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_permission_code`：`(code)` 唯一索引
- `idx_module`：`(module)` 普通索引
- `idx_parent_id`：`(parent_id)` 普通索引

**权限编码规范**：`模块:资源:操作`，如 `user:list`、`scoring:upload`

#### 3.2.9 sp_menu（菜单表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 菜单名称 |
| path | VARCHAR(255) | NULL | 路由路径（如：/dashboard） |
| component | VARCHAR(255) | NULL | 组件路径（如：pages/Dashboard） |
| icon | VARCHAR(100) | NULL | 菜单图标（如：dashboard） |
| parent_id | BIGINT UNSIGNED | DEFAULT 0 | 父菜单ID（0表示顶级菜单） |
| sort_order | INT | DEFAULT 0 | 排序号 |
| type | VARCHAR(20) | DEFAULT 'MENU' | 菜单类型（MENU-菜单，DIRECTORY-目录，BUTTON-按钮） |
| permission_code | VARCHAR(100) | NULL | 关联权限编码 |
| is_visible | TINYINT UNSIGNED | DEFAULT 1 | 是否显示（0-隐藏，1-显示） |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除（0-未删除，1-已删除） |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- `uk_menu_path`：`(path)` 唯一索引（仅限非空路径）
- `idx_parent_id`：`(parent_id)` 普通索引

#### 3.2.10 sp_user_role（用户角色关联表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| user_id | BIGINT UNSIGNED | NOT NULL | 用户ID |
| role_id | BIGINT UNSIGNED | NOT NULL | 角色ID |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- `uk_user_role`：`(user_id, role_id)` 唯一索引
- `idx_user_id`：`(user_id)` 普通索引
- `idx_role_id`：`(role_id)` 普通索引

#### 3.2.11 sp_role_permission（角色权限关联表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| role_id | BIGINT UNSIGNED | NOT NULL | 角色ID |
| permission_id | BIGINT UNSIGNED | NOT NULL | 权限ID |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- `uk_role_permission`：`(role_id, permission_id)` 唯一索引
- `idx_role_id`：`(role_id)` 普通索引
- `idx_permission_id`：`(permission_id)` 普通索引

#### 3.2.12 sp_role_menu（角色菜单关联表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PRIMARY KEY, AUTO_INCREMENT | 主键 |
| role_id | BIGINT UNSIGNED | NOT NULL | 角色ID |
| menu_id | BIGINT UNSIGNED | NOT NULL | 菜单ID |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- `uk_role_menu`：`(role_id, menu_id)` 唯一索引
- `idx_role_id`：`(role_id)` 普通索引
- `idx_menu_id`：`(menu_id)` 普通索引

---

## 4. API 接口设计

### 4.1 认证接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/logout` | POST | 用户登出 |
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/refresh` | POST | 刷新 Token |

#### POST /api/auth/login

请求体：
```json
{
  "username": "string",
  "password": "string"
}
```

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "string",
    "refreshToken": "string",
    "user": {
      "id": 1,
      "username": "string",
      "realName": "string",
      "department": "string",
      "roles": ["SUPER_ADMIN", "ADMIN"],
      "permissions": ["user:list", "user:add", "role:list"]
    },
    "menus": []
  }
}
```

### 4.2 数据上传接口

**说明**：用于上传汇总考核文件或历史数据导入。各部门日常打分请使用手动打分接口（4.8）。

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/upload/excel` | POST | 上传 Excel 文件解析（汇总文件/历史数据） |
| `/api/upload/template` | GET | 下载标准汇总模板 |
| `/api/upload/year-months` | GET | 获取已加载的年月列表 |
| `/api/upload/year-month/{yearMonth}` | DELETE | 删除指定年月数据 |
| `/api/upload/merge` | POST | 合并多部门打分数据生成汇总记录 |

#### POST /api/upload/excel

请求：`multipart/form-data`
| 字段 | 类型 | 说明 |
|------|------|------|
| files | File[] | Excel 文件列表 |
| yearMonth | string | 年月（格式：YYYY-MM，可选，优先从文件名识别） |
| department | string | 部门标识（计划/采购/质量/包开，可选） |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "yearMonth": "2026-03",
      "department": "质量",
      "supplierCount": 20,
      "fileName": "供应商绩效打分-质量.xlsx",
      "status": "imported",
      "templateType": "full",
      "validRows": 18,
      "skippedRows": 2,
      "validationWarnings": [
        {
          "rowIndex": 15,
          "supplierName": "供应商X",
          "message": "a1验货合格率超出范围(0-7)，当前值: 8.5"
        }
      ]
    }
  ]
}
```

#### GET /api/upload/template

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| type | string | 模板类型（full/simplified，默认full） |
| department | string | 部门标识（计划/采购/质量/包开，可选） |

响应：Excel 文件下载流

#### POST /api/upload/merge

**说明**：合并各部门提交的打分数据，生成月度考核汇总记录。总分和等级由采购部在合并时确认。

请求体：
```json
{
  "yearMonth": "2026-03",
  "departments": ["计划", "采购", "质量", "包开"],
  "supplierNames": ["供应商A", "供应商B", "供应商C"]
}
```

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "mergedCount": 20,
    "departmentsMerged": ["计划", "采购", "质量", "包开"],
    "suppliers": [
      {
        "name": "供应商A",
        "category": "牙刷",
        "total": 85.5,
        "grade": "B级",
        "A": 22,
        "B": 18,
        "C": 18,
        "D": 27.5,
        "D1": 20,
        "D2": 7.5,
        "sourceDepartments": {
          "A": "质量",
          "B": "采购",
          "C": "计划",
          "D1": "包开",
          "D2": "采购"
        },
        "dimensionScores": [
          { "dimension": "A", "score": 22, "department": "质量", "fullScore": 25 },
          { "dimension": "B", "score": 18, "department": "采购", "fullScore": 20 },
          { "dimension": "C", "score": 18, "department": "计划", "fullScore": 20 },
          { "dimension": "D1", "score": 20, "department": "包开", "fullScore": 20 },
          { "dimension": "D2", "score": 7.5, "department": "采购", "fullScore": 15 }
        ]
      }
    ],
    "conflicts": [
      {
        "supplierName": "供应商B",
        "dimension": "D2",
        "departments": ["采购", "包开"],
        "resolution": "取非零值或最新提交"
      }
    ]
  }
}
```

### 4.3 总览趋势接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/overview/stats` | GET | 获取统计数据 |
| `/api/overview/trend` | GET | 获取趋势数据 |
| `/api/overview/grade-distribution` | GET | 获取等级分布数据 |
| `/api/overview/dimension-avg` | GET | 获取维度平均分数据 |

#### GET /api/overview/stats

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| yearMonth | string | 年月（格式：YYYY-MM，可选，默认最新月份） |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "avgTotal": 85.5,
    "gradeACount": 5,
    "gradeDCount": 2,
    "bestImprover": {
      "name": "供应商A",
      "value": 5.2
    },
    "worstDecliner": {
      "name": "供应商B",
      "value": -3.8
    },
    "changeFromPrev": 1.2
  }
}
```

#### GET /api/overview/trend

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| categories | string[] | 类别筛选（可选） |
| metrics | string[] | 指标筛选（total/A/B/C/D） |
| yearMonths | string[] | 年月筛选（格式：YYYY-MM，可选） |
| suppliers | string[] | 供应商筛选（可选） |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "xAxis": ["2026-03", "2026-04", "2026-05"],
    "xAxisLabels": ["3月", "4月", "5月"],
    "series": [
      {
        "name": "供应商A",
        "type": "line",
        "data": [92, 87, 88]
      }
    ]
  }
}
```

### 4.4 维度对比接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/compare/radar` | GET | 获取雷达图数据 |
| `/api/compare/heatmap` | GET | 获取热力图数据 |
| `/api/compare/categories` | GET | 获取类别列表 |
| `/api/compare/suppliers` | GET | 获取供应商列表 |

#### GET /api/compare/radar

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| category | string | 类别筛选（可选） |
| suppliers | string[] | 供应商列表 |
| months | string[] | 月份列表 |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "indicator": [
      {"name": "品质(25)", "max": 25},
      {"name": "成本(20)", "max": 20},
      {"name": "交货(20)", "max": 20},
      {"name": "服务(35)", "max": 35}
    ],
    "series": [
      {
        "name": "供应商A-3月",
        "value": [23, 18, 19, 32]
      }
    ]
  }
}
```

### 4.5 排名变动接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/rank/compare` | GET | 获取排名对比数据 |
| `/api/rank/export` | POST | 导出排名分析 Excel |

#### GET /api/rank/compare

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| yearMonth1 | string | 起始年月（格式：YYYY-MM） |
| yearMonth2 | string | 结束年月（格式：YYYY-MM） |
| gradeFilter | string | 等级筛选（all/up/down） |
| searchText | string | 搜索关键词 |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "supplierName": "供应商A",
      "category": "牙刷",
      "yearMonth1": "2026-03",
      "yearMonth2": "2026-04",
      "month1": {
        "rank": 1,
        "score": 92,
        "grade": "A级"
      },
      "month2": {
        "rank": 2,
        "score": 87,
        "grade": "A级"
      },
      "rankChange": -1,
      "scoreChange": -5,
      "dimensionDiff": {
        "A": -0,
        "B": 0,
        "C": -2,
        "D": -3
      },
      "reason": "交货-2，服务-3",
      "meetingNote": ""
    }
  ]
}
```

### 4.6 明细数据接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/detail/monthly` | GET | 获取月度明细数据 |
| `/api/detail/export/current` | POST | 导出当月数据 |
| `/api/detail/export/all` | POST | 导出所有月份数据 |

#### GET /api/detail/monthly

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| yearMonth | string | 年月（格式：YYYY-MM） |
| grade | string | 等级筛选（all/A/B/C/D） |
| searchText | string | 搜索关键词 |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "monthLabel": "3月",
    "suppliers": [
      {
        "name": "供应商A",
        "category": "牙刷",
        "total": 92,
        "grade": "A级（优秀）",
        "A": 25, "B": 18, "C": 20, "D": 29,
        "a1": 7, "a2": 8, "a3": 5, "a4": 5,
        "b1": 5, "b2": 4, "b3": 3, "b4": 3, "b5": 3,
        "c1": 8, "c2": 6, "c3": 6,
        "d1a": 10, "d1b": 10, "d2a": 8, "d2b": 10, "d2c": 5, "d2d": 5, "d2e": 5,
        "conclusion": "",
        "exceptionQuality": "",
        "exceptionCost": "",
        "exceptionDelivery": "",
        "exceptionServiceProduct": "",
        "exceptionServicePackage": "",
        "exceptionOther": ""
      }
    ]
  }
}
```

### 4.7 供应商池接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/pool/list` | GET | 获取供应商列表 |
| `/api/pool/create` | POST | 创建供应商 |
| `/api/pool/update/{id}` | PUT | 更新供应商 |
| `/api/pool/delete/{id}` | DELETE | 删除供应商 |
| `/api/pool/toggle/{id}` | PUT | 切换启用/禁用状态 |
| `/api/pool/export` | POST | 导出供应商池 |
| `/api/pool/import` | POST | 批量导入供应商 |

#### POST /api/pool/create

请求体：
```json
{
  "name": "string",
  "category": "string"
}
```

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "供应商A",
    "category": "牙刷",
    "disabled": false
  }
}
```

### 4.8 手动打分接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/scoring/years` | GET | 获取可选择的年份列表 |
| `/api/scoring/months/{year}` | GET | 获取指定年份的月份列表及状态 |
| `/api/scoring/status/{yearMonth}` | GET | 获取指定年月的打分状态 |
| `/api/scoring/data/{yearMonth}` | GET | 获取指定年月的打分数据 |
| `/api/scoring/upload` | POST | 上传本部门打分Excel文件（直接使用模板中的打分值） |
| `/api/scoring/template` | GET | 下载指定部门的打分模板 |
| `/api/scoring/submit/{yearMonth}` | POST | 提交打分数据 |
| `/api/scoring/reset/{yearMonth}` | POST | 重置指定年月的打分为0 |

#### GET /api/scoring/years

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [2024, 2025, 2026]
}
```

#### GET /api/scoring/months/{year}

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"month": 1, "monthName": "1月", "yearMonth": "2026-01", "status": "COMPLETED"},
    {"month": 2, "monthName": "2月", "yearMonth": "2026-02", "status": "COMPLETED"},
    {"month": 3, "monthName": "3月", "yearMonth": "2026-03", "status": "IN_PROGRESS"},
    {"month": 4, "monthName": "4月", "yearMonth": "2026-04", "status": "PENDING"},
    {"month": 5, "monthName": "5月", "yearMonth": "2026-05", "status": "PENDING"}
  ]
}
```

**状态说明**：
- `PENDING`：未开始打分
- `IN_PROGRESS`：部分部门已提交
- `COMPLETED`：所有部门已提交

#### GET /api/scoring/status/{yearMonth}

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "overallStatus": "IN_PROGRESS",
    "departmentStatus": [
      {"department": "质量", "status": "SUBMITTED", "submitTime": "2026-03-15 10:30"},
      {"department": "计划", "status": "SUBMITTED", "submitTime": "2026-03-16 14:20"},
      {"department": "包开", "status": "DRAFT", "submitTime": null},
      {"department": "采购", "status": "PENDING", "submitTime": null}
    ]
  }
}
```

#### POST /api/scoring/upload

请求：`multipart/form-data`
| 字段 | 类型 | 说明 |
|------|------|------|
| file | File | Excel 文件 |
| yearMonth | string | 年月（格式：YYYY-MM） |
| department | string | 部门（计划/采购/质量/包开） |

**说明**：系统直接读取 Excel 模板中的打分值，不进行自动计算。

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "department": "质量",
    "supplierCount": 20,
    "status": "DRAFT",
    "parsedData": [
      {
        "supplierName": "供应商A",
        "category": "牙刷",
        "dimensionScores": [
          {"dimension": "A", "score": 22, "subScores": {"a1": 7, "a2": 8, "a3": 5, "a4": 2}}
        ],
        "grade": "A级"
      }
    ]
  }
}
```

#### GET /api/scoring/template

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| department | string | 部门（计划/采购/质量/包开） |
| yearMonth | string | 年月（格式：YYYY-MM） |

响应：Excel 文件下载流

#### POST /api/scoring/submit/{yearMonth}

请求体：
```json
{
  "department": "质量",
  "confirm": true
}
```

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "yearMonth": "2026-03",
    "department": "质量",
    "status": "SUBMITTED",
    "submitTime": "2026-03-15 10:30"
  }
}
```

### 4.9 会议记录接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/meeting-note/save` | POST | 保存会议记录 |
| `/api/meeting-note/get` | GET | 获取会议记录 |

#### POST /api/meeting-note/save

请求体：
```json
{
  "supplierName": "供应商A",
  "monthFrom": "2026-03",
  "monthTo": "2026-04",
  "note": "会议记录内容..."
}
```

### 4.10 用户管理接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/user/list` | GET | 分页查询用户列表 |
| `/api/user/get/{id}` | GET | 根据ID查询用户 |
| `/api/user/add` | POST | 新增用户 |
| `/api/user/update` | PUT | 更新用户 |
| `/api/user/delete/{id}` | DELETE | 删除用户 |
| `/api/user/assign-role` | POST | 分配角色给用户 |
| `/api/user/get-roles/{userId}` | GET | 获取用户的角色列表 |

#### GET /api/user/list

请求参数：
| 参数 | 类型 | 说明 |
|------|------|------|
| page | int | 页码，默认1 |
| size | int | 每页条数，默认20 |
| username | string | 用户名搜索（可选） |
| department | string | 部门筛选（可选） |
| roleCode | string | 角色编码筛选（可选） |

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "admin",
        "realName": "管理员",
        "department": "采购",
        "email": "admin@example.com",
        "isEnabled": true,
        "roles": ["SUPER_ADMIN"],
        "gmtCreate": "2026-01-01 00:00:00"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 20
  }
}
```

#### POST /api/user/add

请求体：
```json
{
  "username": "newuser",
  "password": "password123",
  "realName": "新用户",
  "department": "质量",
  "email": "newuser@example.com",
  "roleIds": [2, 3]
}
```

#### POST /api/user/assign-role

请求体：
```json
{
  "userId": 1,
  "roleIds": [1, 2]
}
```

### 4.11 角色管理接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/role/list` | GET | 查询角色列表 |
| `/api/role/get/{id}` | GET | 根据ID查询角色 |
| `/api/role/add` | POST | 新增角色 |
| `/api/role/update` | PUT | 更新角色 |
| `/api/role/delete/{id}` | DELETE | 删除角色 |
| `/api/role/assign-permission` | POST | 分配权限给角色 |
| `/api/role/assign-menu` | POST | 分配菜单给角色 |
| `/api/role/get-permissions/{roleId}` | GET | 获取角色的权限列表 |
| `/api/role/get-menus/{roleId}` | GET | 获取角色的菜单列表 |

#### POST /api/role/add

请求体：
```json
{
  "name": "质量部管理员",
  "code": "QUALITY_ADMIN",
  "description": "质量部打分管理权限",
  "permissionIds": [1, 2, 3],
  "menuIds": [4, 5, 6]
}
```

#### POST /api/role/assign-permission

请求体：
```json
{
  "roleId": 1,
  "permissionIds": [1, 2, 3, 4, 5]
}
```

#### POST /api/role/assign-menu

请求体：
```json
{
  "roleId": 1,
  "menuIds": [1, 2, 3, 4, 5]
}
```

### 4.12 权限管理接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/permission/list` | GET | 查询权限列表 |
| `/api/permission/get/{id}` | GET | 根据ID查询权限 |
| `/api/permission/add` | POST | 新增权限 |
| `/api/permission/update` | PUT | 更新权限 |
| `/api/permission/delete/{id}` | DELETE | 删除权限 |
| `/api/permission/tree` | GET | 获取权限树形结构 |

#### POST /api/permission/add

请求体：
```json
{
  "name": "用户管理-新增",
  "code": "user:add",
  "module": "system",
  "type": "FUNCTION",
  "description": "新增用户权限",
  "parentId": 0,
  "sortOrder": 1
}
```

#### GET /api/permission/tree

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "system",
      "module": "system",
      "type": "MENU",
      "children": [
        {
          "id": 2,
          "name": "用户管理-列表",
          "code": "user:list",
          "module": "system",
          "type": "FUNCTION",
          "children": []
        },
        {
          "id": 3,
          "name": "用户管理-新增",
          "code": "user:add",
          "module": "system",
          "type": "FUNCTION",
          "children": []
        }
      ]
    }
  ]
}
```

### 4.13 菜单管理接口

| 接口路径 | 方法 | 描述 |
|----------|------|------|
| `/api/menu/list` | GET | 查询菜单列表 |
| `/api/menu/get/{id}` | GET | 根据ID查询菜单 |
| `/api/menu/add` | POST | 新增菜单 |
| `/api/menu/update` | PUT | 更新菜单 |
| `/api/menu/delete/{id}` | DELETE | 删除菜单 |
| `/api/menu/tree` | GET | 获取菜单树形结构 |
| `/api/menu/user-menu` | GET | 获取当前用户的菜单树 |

#### POST /api/menu/add

请求体：
```json
{
  "name": "供应商池管理",
  "path": "/pool",
  "component": "pages/Pool",
  "icon": "database",
  "parentId": 0,
  "sortOrder": 5,
  "type": "MENU",
  "permissionCode": "pool:list",
  "isVisible": true
}
```

#### GET /api/menu/user-menu

响应体：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "总览趋势",
      "path": "/dashboard",
      "component": "pages/Dashboard",
      "icon": "dashboard",
      "parentId": 0,
      "type": "MENU",
      "children": []
    },
    {
      "id": 2,
      "name": "供应商管理",
      "icon": "user",
      "parentId": 0,
      "type": "DIRECTORY",
      "children": [
        {
          "id": 3,
          "name": "供应商池",
          "path": "/pool",
          "component": "pages/Pool",
          "icon": "database",
          "parentId": 2,
          "type": "MENU",
          "children": []
        }
      ]
    }
  ]
}
```

---

## 5. 非功能性需求

### 5.1 性能要求

| 指标 | 要求 |
|------|------|
| 页面加载时间 | ≤ 3秒 |
| API 响应时间 | ≤ 1秒 |
| 支持并发用户 | ≥ 50人 |
| Excel 导入速度 | ≤ 10秒/100条记录 |

### 5.2 安全要求

| 要求 | 说明 |
|------|------|
| 用户认证 | JWT Token 认证 |
| 权限控制 | 角色权限（ADMIN/USER） |
| 密码加密 | BCrypt 加密存储 |
| 接口防护 | 防止 SQL 注入、XSS 攻击 |
| 数据备份 | 每日自动备份数据库 |

### 5.3 可用性要求

| 指标 | 要求 |
|------|------|
| 系统可用性 | ≥ 99.5% |
| 数据持久化 | MySQL 持久化存储 |
| 故障恢复 | ≤ 15分钟 |

### 5.4 可扩展性要求

| 要求 | 说明 |
|------|------|
| 模块分离 | 前后端分离，模块化设计 |
| 接口规范 | RESTful API 规范 |
| 数据库设计 | 支持未来功能扩展 |

---

## 6. 前端页面结构

### 6.1 路由结构

```
/
├── /login          # 登录页面
├── /dashboard      # 首页/总览趋势
├── /compare        # 维度对比
├── /rank           # 排名变动
├── /detail         # 明细数据
├── /pool           # 供应商池管理
├── /scoring        # 手动打分
└── /system         # 系统管理
    ├── /user       # 用户管理
    ├── /role       # 角色管理
    ├── /permission # 权限管理
    └── /menu       # 菜单管理
```

### 6.2 组件结构

```
src/
├── components/
│   ├── Layout/           # 布局组件
│   │   ├── Header.jsx
│   │   ├── Sidebar.jsx
│   │   └── Layout.jsx
│   ├── Upload/           # 上传组件
│   │   └── UploadArea.jsx
│   ├── Stats/            # 统计卡片
│   │   └── StatsCard.jsx
│   ├── Charts/           # 图表组件
│   │   ├── TrendChart.jsx
│   │   ├── GradeChart.jsx
│   │   ├── DimAvgChart.jsx
│   │   ├── RadarChart.jsx
│   │   └── HeatmapChart.jsx
│   ├── Tables/           # 表格组件
│   │   ├── RankTable.jsx
│   │   ├── DetailTable.jsx
│   │   ├── PoolTable.jsx
│   │   ├── ScoringTable.jsx
│   │   ├── UserTable.jsx
│   │   ├── RoleTable.jsx
│   │   ├── PermissionTable.jsx
│   │   └── MenuTable.jsx
│   ├── Dropdowns/        # 下拉选择组件
│   │   ├── MultiSelect.jsx
│   │   └── YearMonthPicker.jsx  # 年月选择器组件
│   ├── Tree/             # 树形组件
│   │   ├── PermissionTree.jsx
│   │   └── MenuTree.jsx
│   └── Modal/            # 弹窗组件
│       ├── PoolModal.jsx
│       ├── UserModal.jsx
│       ├── RoleModal.jsx
│       ├── PermissionModal.jsx
│       └── MenuModal.jsx
├── pages/                # 页面组件
│   ├── Login.jsx
│   ├── Dashboard.jsx
│   ├── Compare.jsx
│   ├── Rank.jsx
│   ├── Detail.jsx
│   ├── Pool.jsx
│   ├── Scoring.jsx
│   └── System/           # 系统管理页面
│       ├── User.jsx
│       ├── Role.jsx
│       ├── Permission.jsx
│       └── Menu.jsx
├── services/             # API 服务
│   ├── auth.js
│   ├── upload.js
│   ├── overview.js
│   ├── compare.js
│   ├── rank.js
│   ├── detail.js
│   ├── pool.js
│   ├── scoring.js
│   ├── user.js
│   ├── role.js
│   ├── permission.js
│   └── menu.js
├── utils/                # 工具函数
│   ├── request.js        # 请求封装
│   ├── echarts.js        # ECharts 封装
│   ├── helpers.js        # 辅助函数
│   └── permission.js     # 权限校验工具
├── store/                # 状态管理
│   ├── index.js
│   └── permission.js     # 权限状态管理
└── hooks/                # 自定义hooks
    └── usePermission.js  # 权限校验hook
```

### 6.3 手动打分页面工作流

**Scoring.jsx 页面结构**：

1. **年月选择区**（页面顶部）
   - 年份下拉选择器
   - 月份下拉选择器（显示状态标识：已完成/进行中/未开始）

2. **部门选择区**
   - 部门下拉选择器（根据用户权限显示可操作的部门）
   - 显示当前部门负责的维度说明

3. **操作按钮区**
   - 下载模板按钮（下载当前部门的打分模板）
   - 上传文件按钮（上传填写完成的 Excel 文件）
   - 提交按钮（预览确认后提交）

4. **打分预览区**
   - 显示上传后的打分数据预览表格
   - 包含供应商名称、类别、维度分数、子项分数
   - 支持编辑修改（仅当前部门负责的维度）

5. **状态显示区**
   - 显示各部门的提交状态
   - 显示当前年月的整体进度

**后端解析策略**：
- 上传时根据文件名模式自动识别部门（文件名包含"计划"/"采购"/"质量"/"包开"）
- 每个部门使用独立的列映射解析规则：
  - 质量部：解析 A 维度子项（a1-a4）
  - 计划部：解析 C 维度子项（c1-c3）
  - 包开部：解析 D1 维度子项（d1a-d1b）
  - 采购部：解析 B 维度子项（b1-b5）和 D2 维度子项（d2a-d2e）
- 解析后的数据直接存入 `sp_department_score` 表，不进行自动计算

### 6.4 系统管理页面工作流

#### 6.4.1 用户管理页面（User.jsx）

1. **查询区**
   - 用户名搜索框
   - 部门下拉筛选
   - 角色下拉筛选
   - 状态筛选（启用/禁用）

2. **操作按钮区**
   - 新增用户按钮
   - 批量删除按钮

3. **用户列表区**
   - 显示用户基本信息（用户名、真实姓名、部门、邮箱、状态）
   - 操作列：编辑、删除、分配角色

4. **分配角色弹窗**
   - 角色树形选择器
   - 已选角色列表
   - 确定/取消按钮

#### 6.4.2 角色管理页面（Role.jsx）

1. **查询区**
   - 角色名称搜索框
   - 角色编码搜索框

2. **操作按钮区**
   - 新增角色按钮

3. **角色列表区**
   - 显示角色信息（名称、编码、描述、状态）
   - 操作列：编辑、删除、分配权限、分配菜单

4. **分配权限弹窗**
   - 权限树形选择器
   - 已选权限列表

5. **分配菜单弹窗**
   - 菜单树形选择器
   - 已选菜单列表

#### 6.4.3 权限管理页面（Permission.jsx）

1. **权限树形展示区**
   - 以树形结构展示所有权限
   - 支持展开/折叠
   - 显示权限名称、编码、类型、所属模块

2. **操作按钮区**
   - 新增权限按钮

3. **新增/编辑弹窗**
   - 权限名称、编码、模块、类型
   - 父权限选择
   - 排序号、描述

#### 6.4.4 菜单管理页面（Menu.jsx）

1. **菜单树形展示区**
   - 以树形结构展示所有菜单
   - 支持拖拽排序
   - 显示菜单名称、路径、图标、类型

2. **操作按钮区**
   - 新增菜单按钮

3. **新增/编辑弹窗**
   - 菜单名称、路径、组件、图标
   - 父菜单选择、排序号
   - 菜单类型（目录/菜单/按钮）
   - 关联权限编码、是否显示

---

## 7. 后端项目结构

> **设计说明**：遵循阿里巴巴Java开发手册工程分层规范，包名使用小写连续单词，实体类以DO后缀，服务接口与实现分离，Mapper层使用MyBatis。

```
backend/
├── src/
│   └── main/
│       ├── java/com/xuena/supplier/
│       │   ├── controller/           # REST API 控制层（对外暴露接口）
│       │   │   ├── AuthController.java
│       │   │   ├── UploadController.java
│       │   │   ├── OverviewController.java
│       │   │   ├── CompareController.java
│       │   │   ├── RankController.java
│       │   │   ├── DetailController.java
│       │   │   ├── PoolController.java
│       │   │   ├── ScoringController.java
│       │   │   ├── MeetingNoteController.java
│       │   │   ├── UserController.java
│       │   │   ├── RoleController.java
│       │   │   ├── PermissionController.java
│       │   │   └── MenuController.java
│       │   ├── service/              # 业务逻辑层接口
│       │   │   ├── AuthService.java
│       │   │   ├── UploadService.java
│       │   │   ├── OverviewService.java
│       │   │   ├── CompareService.java
│       │   │   ├── RankService.java
│       │   │   ├── DetailService.java
│       │   │   ├── PoolService.java
│       │   │   ├── ScoringService.java
│       │   │   ├── MeetingNoteService.java
│       │   │   ├── UserService.java
│       │   │   ├── RoleService.java
│       │   │   ├── PermissionService.java
│       │   │   └── MenuService.java
│       │   ├── service/impl/         # 业务逻辑层实现
│       │   │   ├── AuthServiceImpl.java
│       │   │   ├── UploadServiceImpl.java
│       │   │   ├── OverviewServiceImpl.java
│       │   │   ├── CompareServiceImpl.java
│       │   │   ├── RankServiceImpl.java
│       │   │   ├── DetailServiceImpl.java
│       │   │   ├── PoolServiceImpl.java
│       │   │   ├── ScoringServiceImpl.java
│       │   │   ├── MeetingNoteServiceImpl.java
│       │   │   ├── UserServiceImpl.java
│       │   │   ├── RoleServiceImpl.java
│       │   │   ├── PermissionServiceImpl.java
│       │   │   └── MenuServiceImpl.java
│       │   ├── mapper/               # 数据访问层（MyBatis Mapper）
│       │   │   ├── SupplierPoolMapper.java
│       │   │   ├── MonthlyAssessmentMapper.java
│       │   │   ├── DepartmentScoreMapper.java
│       │   │   ├── MeetingNoteMapper.java
│       │   │   ├── CategoryMapper.java
│       │   │   ├── UserMapper.java
│       │   │   ├── RoleMapper.java
│       │   │   ├── PermissionMapper.java
│       │   │   ├── MenuMapper.java
│       │   │   ├── UserRoleMapper.java
│       │   │   ├── RolePermissionMapper.java
│       │   │   └── RoleMenuMapper.java
│       │   ├── entity/               # 数据库实体类（DO）
│       │   │   ├── SupplierPoolDO.java
│       │   │   ├── MonthlyAssessmentDO.java
│       │   │   ├── DepartmentScoreDO.java
│       │   │   ├── MeetingNoteDO.java
│       │   │   ├── CategoryDO.java
│       │   │   ├── UserDO.java
│       │   │   ├── RoleDO.java
│       │   │   ├── PermissionDO.java
│       │   │   ├── MenuDO.java
│       │   │   ├── UserRoleDO.java
│       │   │   ├── RolePermissionDO.java
│       │   │   └── RoleMenuDO.java
│       │   ├── dto/                  # 数据传输对象
│       │   │   ├── request/          # 请求 DTO
│       │   │   │   ├── LoginRequest.java
│       │   │   │   ├── UploadRequest.java
│       │   │   │   ├── ScoringSubmitRequest.java
│       │   │   │   ├── PoolQueryRequest.java
│       │   │   │   ├── UserAddRequest.java
│       │   │   │   ├── UserUpdateRequest.java
│       │   │   │   ├── RoleAddRequest.java
│       │   │   │   ├── PermissionAddRequest.java
│       │   │   │   ├── MenuAddRequest.java
│       │   │   │   ├── AssignRoleRequest.java
│       │   │   │   ├── AssignPermissionRequest.java
│       │   │   │   └── AssignMenuRequest.java
│       │   │   └── response/         # 响应 DTO
│       │   │       ├── LoginResponse.java
│       │   │       ├── OverviewResponse.java
│       │   │       ├── ScoringDetailResponse.java
│       │   │       ├── PageResponse.java
│       │   │       ├── UserResponse.java
│       │   │       ├── RoleResponse.java
│       │   │       ├── PermissionResponse.java
│       │   │       ├── MenuResponse.java
│       │   │       └── TreeResponse.java
│       │   ├── vo/                   # 视图对象（展示层专用）
│       │   │   ├── SupplierVO.java
│       │   │   ├── AssessmentVO.java
│       │   │   ├── TrendVO.java
│       │   │   ├── UserVO.java
│       │   │   ├── RoleVO.java
│       │   │   ├── PermissionVO.java
│       │   │   └── MenuVO.java
│       │   ├── enums/                # 枚举类
│       │   │   ├── DepartmentEnum.java
│       │   │   ├── DimensionEnum.java
│       │   │   ├── GradeEnum.java
│       │   │   ├── StatusEnum.java
│       │   │   ├── PermissionTypeEnum.java
│       │   │   └── MenuTypeEnum.java
│       │   ├── config/               # 配置类
│       │   │   ├── SecurityConfig.java
│       │   │   ├── WebConfig.java
│       │   │   ├── JwtConfig.java
│       │   │   └── MyBatisConfig.java
│       │   ├── security/             # 安全相关
│       │   │   ├── JwtTokenProvider.java
│       │   │   ├── CustomUserDetailsService.java
│       │   │   └── JwtAuthenticationFilter.java
│       │   ├── exception/            # 异常处理
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   ├── BusinessException.java
│       │   │   └── ErrorCode.java
│       │   ├── util/                 # 工具类
│       │   │   ├── EasyExcelUtil.java
│       │   │   ├── PasswordUtil.java
│       │   │   └── DateUtil.java
│       │   ├── constant/             # 常量类
│       │   │   ├── ApiConstants.java
│       │   │   └── ValidationConstants.java
│       │   └── SupplierPerformanceApplication.java  # 启动类
│       └── resources/
│           ├── application.yml       # 应用配置
│           ├── mapper/               # MyBatis XML 映射文件
│           │   ├── SupplierPoolMapper.xml
│           │   ├── MonthlyAssessmentMapper.xml
│           │   ├── DepartmentScoreMapper.xml
│           │   ├── MeetingNoteMapper.xml
│           │   ├── CategoryMapper.xml
│           │   ├── UserMapper.xml
│           │   ├── RoleMapper.xml
│           │   ├── PermissionMapper.xml
│           │   ├── MenuMapper.xml
│           │   ├── UserRoleMapper.xml
│           │   ├── RolePermissionMapper.xml
│           │   └── RoleMenuMapper.xml
│           └── schema.sql            # 数据库初始化脚本
└── pom.xml                           # Maven 配置
```

### 7.1 代码风格规范

#### 7.1.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 包名 | 小写连续单词，无下划线 | `com.xuena.supplier.controller` |
| 类名 | 大驼峰，名词 | `SupplierPoolDO`、`UploadController` |
| 接口名 | 大驼峰，名词，无 `I` 前缀 | `AuthService` |
| 实现类 | 接口名 + `Impl` | `AuthServiceImpl` |
| 方法名 | 小驼峰，动宾结构 | `getSupplierList`、`uploadExcel` |
| 变量名 | 小驼峰，语义清晰 | `yearMonth`、`supplierName` |
| 常量名 | 全大写，下划线分隔 | `MAX_PAGE_SIZE`、`SCORE_MAX` |
| 枚举名 | 大驼峰，枚举值全大写 | `DepartmentEnum.QUALITY` |

#### 7.1.2 类成员顺序（按优先级）

1. 公有方法 / 保护方法
2. 私有方法
3. Getter / Setter 方法（置于类体最后）

#### 7.1.3 POJO 类规范

- 所有属性使用**包装数据类型**（Integer、Long、Boolean）
- 不设定属性默认值
- 必须重写 `toString()` 方法
- 实现 `Serializable` 接口

#### 7.1.4 异常处理

- 使用自定义 `BusinessException` 封装业务异常
- 使用 `ErrorCode` 枚举统一管理错误码
- 使用 `GlobalExceptionHandler` 统一处理异常

#### 7.1.5 日志规范

- 使用 SLF4J 接口，注入 `Logger`
- 禁止在循环中打印日志
- 日志级别使用：DEBUG（调试）、INFO（业务记录）、WARN（警告）、ERROR（错误）

---

## 8. EasyExcel 配置与使用

### 8.1 依赖配置（pom.xml）

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.3.2</version>
</dependency>
```

### 8.2 核心配置类

**EasyExcelConfig.java**：

```java
@Configuration
public class EasyExcelConfig {
    
    @Bean
    public ExcelBuilderFactory excelBuilderFactory() {
        return new ExcelBuilderFactory();
    }
}
```

### 8.3 Excel 解析工具类

**EasyExcelUtil.java**：

```java
@Component
public class EasyExcelUtil {
    
    public <T> List<T> parseExcel(MultipartFile file, Class<T> clazz, ReadListener<T> listener) throws IOException {
        List<T> dataList = new ArrayList<>();
        
        ReadListener<T> wrapperListener = new ReadListener<T>() {
            @Override
            public void invoke(T data, AnalysisContext context) {
                dataList.add(data);
                listener.invoke(data, context);
            }
            
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                listener.doAfterAllAnalysed(context);
            }
        };
        
        EasyExcel.read(file.getInputStream(), clazz, wrapperListener)
                .sheet()
                .doRead();
        
        return dataList;
    }
    
    public <T> void writeExcel(HttpServletResponse response, List<T> data, Class<T> clazz, String sheetName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(sheetName + ".xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        
        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet(sheetName)
                .doWrite(data);
    }
}
```

### 8.4 Excel 实体类示例

**QualityScoreData.java**（质量部打分模板）：

```java
@Data
public class QualityScoreData {
    
    @ExcelProperty(value = "序号", index = 0)
    private Integer rowNum;
    
    @ExcelProperty(value = "供应商名称", index = 1)
    private String supplierName;
    
    @ExcelProperty(value = "供应商类别", index = 2)
    private String category;
    
    @ExcelProperty(value = "a1验货合格率", index = 3)
    private BigDecimal a1;
    
    @ExcelProperty(value = "a2综合客诉率", index = 4)
    private BigDecimal a2;
    
    @ExcelProperty(value = "a3新品开发质量", index = 5)
    private BigDecimal a3;
    
    @ExcelProperty(value = "a4质量改善完成率", index = 6)
    private BigDecimal a4;
    
    @ExcelProperty(value = "A总分", index = 7)
    private BigDecimal dimensionA;
    
    @ExcelProperty(value = "等级", index = 8)
    private String grade;
    
    @ExcelProperty(value = "异常原因", index = 9)
    private String exceptionReason;
}
```

**PlanningScoreData.java**（计划部打分模板）：

```java
@Data
public class PlanningScoreData {
    
    @ExcelProperty(value = "序号", index = 0)
    private Integer rowNum;
    
    @ExcelProperty(value = "供应商名称", index = 1)
    private String supplierName;
    
    @ExcelProperty(value = "供应商类别", index = 2)
    private String category;
    
    @ExcelProperty(value = "c1交货延迟批次", index = 3)
    private BigDecimal c1;
    
    @ExcelProperty(value = "c2交货数量短缺", index = 4)
    private BigDecimal c2;
    
    @ExcelProperty(value = "c3订单交付配合度", index = 5)
    private BigDecimal c3;
    
    @ExcelProperty(value = "C总分", index = 6)
    private BigDecimal dimensionC;
    
    @ExcelProperty(value = "等级", index = 7)
    private String grade;
    
    @ExcelProperty(value = "异常原因", index = 8)
    private String exceptionReason;
}
```

**PackagingScoreData.java**（包开部打分模板）：

```java
@Data
public class PackagingScoreData {
    
    @ExcelProperty(value = "序号", index = 0)
    private Integer rowNum;
    
    @ExcelProperty(value = "供应商名称", index = 1)
    private String supplierName;
    
    @ExcelProperty(value = "供应商类别", index = 2)
    private String category;
    
    @ExcelProperty(value = "d1a包材技术支持", index = 3)
    private BigDecimal d1a;
    
    @ExcelProperty(value = "d1b包材技术评估", index = 4)
    private BigDecimal d1b;
    
    @ExcelProperty(value = "D1总分", index = 5)
    private BigDecimal dimensionD1;
    
    @ExcelProperty(value = "等级", index = 6)
    private String grade;
    
    @ExcelProperty(value = "异常原因", index = 7)
    private String exceptionReason;
}
```

**ProcurementScoreData.java**（采购部打分模板）：

```java
@Data
public class ProcurementScoreData {
    
    @ExcelProperty(value = "序号", index = 0)
    private Integer rowNum;
    
    @ExcelProperty(value = "供应商名称", index = 1)
    private String supplierName;
    
    @ExcelProperty(value = "供应商类别", index = 2)
    private String category;
    
    @ExcelProperty(value = "b1价格水平", index = 3)
    private BigDecimal b1;
    
    @ExcelProperty(value = "b2供货周期", index = 4)
    private BigDecimal b2;
    
    @ExcelProperty(value = "b3付款条件", index = 5)
    private BigDecimal b3;
    
    @ExcelProperty(value = "b4报价准确", index = 6)
    private BigDecimal b4;
    
    @ExcelProperty(value = "b5成本支持", index = 7)
    private BigDecimal b5;
    
    @ExcelProperty(value = "B总分", index = 8)
    private BigDecimal dimensionB;
    
    @ExcelProperty(value = "d2a配方技术支持", index = 9)
    private BigDecimal d2a;
    
    @ExcelProperty(value = "d2b配方技术评估", index = 10)
    private BigDecimal d2b;
    
    @ExcelProperty(value = "d2c研发打样", index = 11)
    private BigDecimal d2c;
    
    @ExcelProperty(value = "d2d业务支持度", index = 12)
    private BigDecimal d2d;
    
    @ExcelProperty(value = "d2e响应配合度", index = 13)
    private BigDecimal d2e;
    
    @ExcelProperty(value = "D2总分", index = 14)
    private BigDecimal dimensionD2;
    
    @ExcelProperty(value = "总分", index = 15)
    private BigDecimal total;
    
    @ExcelProperty(value = "等级", index = 16)
    private String grade;
    
    @ExcelProperty(value = "会议结论", index = 17)
    private String conclusion;
    
    @ExcelProperty(value = "品质考核异常原因", index = 18)
    private String exceptionQuality;
    
    @ExcelProperty(value = "成本考核异常原因", index = 19)
    private String exceptionCost;
    
    @ExcelProperty(value = "交货考核异常原因", index = 20)
    private String exceptionDelivery;
    
    @ExcelProperty(value = "服务考核-成品异常原因", index = 21)
    private String exceptionServiceProduct;
    
    @ExcelProperty(value = "服务考核-包材异常原因", index = 22)
    private String exceptionServicePackage;
    
    @ExcelProperty(value = "其他异常原因", index = 23)
    private String exceptionOther;
}
```

### 8.5 Excel 解析服务实现

**UploadServiceImpl.java** 核心方法：

```java
@Override
public List<DepartmentScoreDO> parseAndSave(MultipartFile file, String yearMonth, String department) {
    List<DepartmentScoreDO> resultList = new ArrayList<>();
    
    try {
        switch (department) {
            case "质量":
                resultList = parseQualityExcel(file, yearMonth);
                break;
            case "计划":
                resultList = parsePlanningExcel(file, yearMonth);
                break;
            case "包开":
                resultList = parsePackagingExcel(file, yearMonth);
                break;
            case "采购":
                resultList = parseProcurementExcel(file, yearMonth);
                break;
            default:
                throw new BusinessException("未知部门：" + department);
        }
        
        departmentScoreMapper.insertBatch(resultList);
        log.info("成功导入{}条{}部打分数据，年月：{}", resultList.size(), department, yearMonth);
        
    } catch (IOException e) {
        log.error("Excel解析失败", e);
        throw new BusinessException("Excel解析失败：" + e.getMessage());
    }
    
    return resultList;
}

private List<DepartmentScoreDO> parseQualityExcel(MultipartFile file, String yearMonth) throws IOException {
    List<QualityScoreData> dataList = easyExcelUtil.parseExcel(file, QualityScoreData.class, new ReadListener<QualityScoreData>() {
        @Override
        public void invoke(QualityScoreData data, AnalysisContext context) {}
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {}
    });
    
    return dataList.stream().map(data -> {
        DepartmentScoreDO entity = new DepartmentScoreDO();
        entity.setYearMonth(yearMonth);
        entity.setSupplierName(data.getSupplierName());
        entity.setDepartment("质量");
        entity.setDimensionGroup("A");
        entity.setDimensionScore(data.getDimensionA());
        entity.setSubScores(JSON.toJSONString(Map.of("a1", data.getA1(), "a2", data.getA2(), "a3", data.getA3(), "a4", data.getA4())));
        entity.setExceptionReason(data.getExceptionReason());
        entity.setStatus("SUBMITTED");
        entity.setFileName(file.getOriginalFilename());
        return entity;
    }).collect(Collectors.toList());
}

private List<DepartmentScoreDO> parsePlanningExcel(MultipartFile file, String yearMonth) throws IOException {
    List<PlanningScoreData> dataList = easyExcelUtil.parseExcel(file, PlanningScoreData.class, new ReadListener<PlanningScoreData>() {
        @Override
        public void invoke(PlanningScoreData data, AnalysisContext context) {}
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {}
    });
    
    return dataList.stream().map(data -> {
        DepartmentScoreDO entity = new DepartmentScoreDO();
        entity.setYearMonth(yearMonth);
        entity.setSupplierName(data.getSupplierName());
        entity.setDepartment("计划");
        entity.setDimensionGroup("C");
        entity.setDimensionScore(data.getDimensionC());
        entity.setSubScores(JSON.toJSONString(Map.of("c1", data.getC1(), "c2", data.getC2(), "c3", data.getC3())));
        entity.setExceptionReason(data.getExceptionReason());
        entity.setStatus("SUBMITTED");
        entity.setFileName(file.getOriginalFilename());
        return entity;
    }).collect(Collectors.toList());
}

private List<DepartmentScoreDO> parsePackagingExcel(MultipartFile file, String yearMonth) throws IOException {
    List<PackagingScoreData> dataList = easyExcelUtil.parseExcel(file, PackagingScoreData.class, new ReadListener<PackagingScoreData>() {
        @Override
        public void invoke(PackagingScoreData data, AnalysisContext context) {}
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {}
    });
    
    return dataList.stream().map(data -> {
        DepartmentScoreDO entity = new DepartmentScoreDO();
        entity.setYearMonth(yearMonth);
        entity.setSupplierName(data.getSupplierName());
        entity.setDepartment("包开");
        entity.setDimensionGroup("D1");
        entity.setDimensionScore(data.getDimensionD1());
        entity.setSubScores(JSON.toJSONString(Map.of("d1a", data.getD1a(), "d1b", data.getD1b())));
        entity.setExceptionReason(data.getExceptionReason());
        entity.setStatus("SUBMITTED");
        entity.setFileName(file.getOriginalFilename());
        return entity;
    }).collect(Collectors.toList());
}

private List<DepartmentScoreDO> parseProcurementExcel(MultipartFile file, String yearMonth) throws IOException {
    List<ProcurementScoreData> dataList = easyExcelUtil.parseExcel(file, ProcurementScoreData.class, new ReadListener<ProcurementScoreData>() {
        @Override
        public void invoke(ProcurementScoreData data, AnalysisContext context) {}
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {}
    });
    
    List<DepartmentScoreDO> resultList = new ArrayList<>();
    
    for (ProcurementScoreData data : dataList) {
        DepartmentScoreDO bScore = new DepartmentScoreDO();
        bScore.setYearMonth(yearMonth);
        bScore.setSupplierName(data.getSupplierName());
        bScore.setDepartment("采购");
        bScore.setDimensionGroup("B");
        bScore.setDimensionScore(data.getDimensionB());
        bScore.setSubScores(JSON.toJSONString(Map.of("b1", data.getB1(), "b2", data.getB2(), "b3", data.getB3(), "b4", data.getB4(), "b5", data.getB5())));
        bScore.setExceptionReason(data.getExceptionCost());
        bScore.setStatus("SUBMITTED");
        bScore.setFileName(file.getOriginalFilename());
        resultList.add(bScore);
        
        DepartmentScoreDO d2Score = new DepartmentScoreDO();
        d2Score.setYearMonth(yearMonth);
        d2Score.setSupplierName(data.getSupplierName());
        d2Score.setDepartment("采购");
        d2Score.setDimensionGroup("D2");
        d2Score.setDimensionScore(data.getDimensionD2());
        d2Score.setSubScores(JSON.toJSONString(Map.of("d2a", data.getD2a(), "d2b", data.getD2b(), "d2c", data.getD2c(), "d2d", data.getD2d(), "d2e", data.getD2e())));
        d2Score.setExceptionReason(data.getExceptionServiceProduct());
        d2Score.setStatus("SUBMITTED");
        d2Score.setFileName(file.getOriginalFilename());
        resultList.add(d2Score);
    }
    
    return resultList;
}
```

### 8.6 EasyExcel 优势

| 特性 | Apache POI | EasyExcel |
|------|-----------|-----------|
| 内存占用 | 高（整个文件加载到内存） | 低（流式读取，逐行解析） |
| 大文件处理 | 容易OOM | 支持100万+行 |
| API复杂度 | 较复杂 | 简洁易用 |
| 注解支持 | 无 | 支持@ExcelProperty注解 |
| 读写性能 | 一般 | 高性能 |
| 社区支持 | 成熟 | 活跃 |

---

## 9. 部署要求

### 9.1 环境要求

| 环境 | 要求 |
|------|------|
| JDK | 21+ |
| MySQL | 8.0+ |
| Node.js | 20+ |
| Maven | 3.9+ |

### 9.2 配置说明

**数据库连接配置**（application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/example_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: admin
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**JWT 配置**：
```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

---

## 10. 开发计划

### 10.1 第一阶段：基础框架搭建（1-2周）

| 任务 | 描述 |
|------|------|
| 后端初始化 | Spring Boot 项目搭建，基础配置 |
| 前端初始化 | React + Vite 项目搭建，路由配置 |
| 数据库设计 | 创建数据库表结构 |
| 认证模块 | JWT 认证、用户管理 |

### 10.2 第二阶段：核心功能开发（2-3周）

| 任务 | 描述 |
|------|------|
| 数据上传 | EasyExcel 文件上传与解析 |
| 供应商池 | CRUD 功能实现 |
| 手动打分 | 在线打分、多部门协同、提交审核 |
| 明细数据 | 月度明细表展示 |

### 10.3 第三阶段：分析功能开发（2-3周）

| 任务 | 描述 |
|------|------|
| 总览趋势 | 统计卡片、趋势图、等级分布 |
| 维度对比 | 雷达图、热力图 |
| 排名变动 | 排名对比、差异分析、会议记录 |

### 10.4 第四阶段：测试与优化（1周）

| 任务 | 描述 |
|------|------|
| 功能测试 | 各模块功能测试 |
| 性能优化 | 接口性能优化 |
| Bug 修复 | 修复发现的问题 |
| 文档完善 | 完善 API 文档 |
