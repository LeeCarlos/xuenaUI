# Design: 供应商绩效分析系统技术实现方案

---
change: supplier-performance-system
role: technical-design
canonical_spec: openspec
---

## 1. 架构设计

### 1.1 整体架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端层 (React)                           │
│  ┌──────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────────┐ │
│  │Login │ │Dashboard│ │ Compare │ │  Rank   │ │   Scoring   │ │
│  └──┬───┘ └────┬────┘ └────┬────┘ └────┬────┘ └──────┬──────┘ │
│     │          │           │           │              │        │
│  ┌──┴──────────┴───────────┴───────────┴──────────────┴──────┐ │
│  │                    Layout (Header + Sidebar)               │ │
│  └──────────────────────────────┬─────────────────────────────┘ │
└─────────────────────────────────┼───────────────────────────────┘
                                  │ RESTful API
┌─────────────────────────────────▼───────────────────────────────┐
│                       网关层 (Spring Boot)                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────────┐    │
│  │AuthFilter│ │CorsFilter│ │RateLimit │ │    Exception     │    │
│  │(JWT)     │ │          │ │          │ │   Handler        │    │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └────────┬─────────┘    │
└───────┼────────────┼────────────┼─────────────────┼──────────────┘
        │            │            │                 │
┌───────▼────────────▼────────────▼─────────────────▼──────────────┐
│                         业务层                                  │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌──────────────┐  │
│  │AuthService │ │UploadService││ScoreService│ │SystemService │  │
│  └──────┬─────┘ └──────┬─────┘ └──────┬─────┘ └──────┬───────┘  │
│         │              │              │               │          │
│  ┌──────▼─────┐ ┌──────▼─────┐ ┌──────▼─────┐ ┌──────▼───────┐  │
│  │UserService │ │PoolService │ │CompareService││MenuService  │  │
│  └──────┬─────┘ └──────┬─────┘ └──────┬─────┘ └──────┬───────┘  │
└─────────┼──────────────┼──────────────┼──────────────┼──────────┘
          │              │              │              │
┌─────────▼──────────────▼──────────────▼──────────────▼──────────┐
│                        数据访问层                               │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌──────────────┐ │
│  │  Mapper    │ │  Mapper    │ │  Mapper    │ │   Mapper     │ │
│  │ (MyBatis)  │ │ (MyBatis)  │ │ (MyBatis)  │ │  (MyBatis)   │ │
│  └──────┬─────┘ └──────┬─────┘ └──────┬─────┘ └──────┬───────┘ │
└─────────┼──────────────┼──────────────┼──────────────┼──────────┘
          │              │              │              │
┌─────────▼──────────────▼──────────────▼──────────────▼──────────┐
│                      MySQL 8.0                                 │
│  sp_supplier_pool │ sp_monthly_assessment │ sp_department_score │
│  sp_user          │ sp_role               │ sp_permission       │
│  sp_menu          │ sp_category           │ sp_meeting_note     │
└─────────────────────────────────────────────────────────────────┘
```

### 1.2 技术选型

| 层级 | 技术 | 版本 | 选型理由 |
|------|------|------|----------|
| 前端框架 | React | 18+ | 生态成熟，组件化开发效率高 |
| UI组件库 | Ant Design | 5+ | 企业级组件库，开箱即用 |
| 图表库 | ECharts | 5+ | 功能强大，支持多种图表类型 |
| 前端构建 | Vite | 6+ | 构建速度快，开发体验好 |
| 后端框架 | Spring Boot | 3.2+ | 社区成熟，生态完善 |
| ORM框架 | MyBatis | 3.5+ | SQL 可控，性能优秀 |
| Excel处理 | EasyExcel | 3.3+ | 内存友好，API 简洁 |
| 认证方式 | JWT | - | 无状态，易于水平扩展 |
| 数据库 | MySQL | 8.0+ | 稳定可靠，生态成熟 |

### 1.3 模块划分

| 模块 | 职责 | 包路径 |
|------|------|--------|
| auth | 用户认证与授权 | `com.xuena.supplier.auth` |
| upload | Excel 数据上传与解析 | `com.xuena.supplier.upload` |
| overview | 总览趋势展示 | `com.xuena.supplier.overview` |
| compare | 维度对比分析 | `com.xuena.supplier.compare` |
| rank | 排名变动分析 | `com.xuena.supplier.rank` |
| detail | 明细数据查询 | `com.xuena.supplier.detail` |
| pool | 供应商池管理 | `com.xuena.supplier.pool` |
| scoring | 手动打分流程 | `com.xuena.supplier.scoring` |
| system | 系统管理（用户/角色/权限/菜单） | `com.xuena.supplier.system` |

## 2. 目录结构

### 2.1 后端目录结构

```
backend/
├── src/main/java/com/xuena/supplier/
│   ├── controller/          # REST API 控制层
│   │   ├── AuthController.java
│   │   ├── UploadController.java
│   │   ├── OverviewController.java
│   │   ├── CompareController.java
│   │   ├── RankController.java
│   │   ├── DetailController.java
│   │   ├── PoolController.java
│   │   ├── ScoringController.java
│   │   └── system/
│   │       ├── UserController.java
│   │       ├── RoleController.java
│   │       ├── PermissionController.java
│   │       └── MenuController.java
│   ├── service/             # 业务接口层
│   │   ├── AuthService.java
│   │   ├── UploadService.java
│   │   ├── OverviewService.java
│   │   ├── CompareService.java
│   │   ├── RankService.java
│   │   ├── DetailService.java
│   │   ├── PoolService.java
│   │   ├── ScoringService.java
│   │   └── system/
│   │       ├── UserService.java
│   │       ├── RoleService.java
│   │       ├── PermissionService.java
│   │       └── MenuService.java
│   ├── service/impl/        # 业务实现层
│   │   └── ... (对应 service 接口)
│   ├── mapper/              # MyBatis Mapper 接口
│   │   └── ...
│   ├── entity/              # 数据库实体 DO
│   │   ├── SupplierPoolDO.java
│   │   ├── MonthlyAssessmentDO.java
│   │   ├── DepartmentScoreDO.java
│   │   ├── MeetingNoteDO.java
│   │   ├── CategoryDO.java
│   │   ├── UserDO.java
│   │   ├── RoleDO.java
│   │   ├── PermissionDO.java
│   │   ├── MenuDO.java
│   │   ├── UserRoleDO.java
│   │   ├── RolePermissionDO.java
│   │   └── RoleMenuDO.java
│   ├── vo/                  # 视图对象
│   │   ├── request/         # 请求 VO
│   │   └── response/        # 响应 VO
│   ├── enums/               # 枚举类
│   │   ├── DepartmentEnum.java
│   │   ├── DimensionEnum.java
│   │   ├── GradeEnum.java
│   │   └── StatusEnum.java
│   ├── config/              # 配置类
│   │   ├── MyBatisConfig.java
│   │   ├── EasyExcelConfig.java
│   │   ├── CorsConfig.java
│   │   └── SecurityConfig.java
│   ├── util/                # 工具类
│   │   ├── EasyExcelUtil.java
│   │   ├── JwtUtil.java
│   │   └── PasswordUtil.java
│   ├── constant/            # 常量定义
│   │   └── Constants.java
│   ├── exception/           # 异常处理
│   │   ├── GlobalExceptionHandler.java
│   │   └── BusinessException.java
│   ├── filter/              # 过滤器
│   │   └── JwtAuthenticationFilter.java
│   └── SupplierApplication.java
├── src/main/resources/
│   ├── application.yml      # 应用配置
│   ├── mapper/              # MyBatis XML 映射文件
│   │   └── ...
│   └── schema.sql           # 数据库初始化脚本
└── pom.xml                  # Maven 依赖管理
```

### 2.2 前端目录结构

```
frontend/
├── src/
│   ├── components/          # 公共组件
│   │   ├── Layout/          # 布局组件
│   │   │   ├── Header.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── Layout.jsx
│   │   ├── Upload/          # 上传组件
│   │   │   └── UploadArea.jsx
│   │   ├── Stats/           # 统计卡片
│   │   │   └── StatsCard.jsx
│   │   ├── Charts/          # 图表组件
│   │   │   ├── TrendChart.jsx
│   │   │   ├── GradeChart.jsx
│   │   │   ├── DimAvgChart.jsx
│   │   │   ├── RadarChart.jsx
│   │   │   └── HeatmapChart.jsx
│   │   ├── Tables/          # 表格组件
│   │   │   ├── RankTable.jsx
│   │   │   ├── DetailTable.jsx
│   │   │   ├── PoolTable.jsx
│   │   │   ├── ScoringTable.jsx
│   │   │   ├── UserTable.jsx
│   │   │   ├── RoleTable.jsx
│   │   │   ├── PermissionTable.jsx
│   │   │   └── MenuTable.jsx
│   │   ├── Dropdowns/       # 下拉选择组件
│   │   │   ├── MultiSelect.jsx
│   │   │   └── YearMonthPicker.jsx
│   │   ├── Tree/            # 树形组件
│   │   │   ├── PermissionTree.jsx
│   │   │   └── MenuTree.jsx
│   │   └── Modal/           # 弹窗组件
│   │       ├── PoolModal.jsx
│   │       ├── UserModal.jsx
│   │       ├── RoleModal.jsx
│   │       ├── PermissionModal.jsx
│   │       └── MenuModal.jsx
│   ├── pages/               # 页面组件
│   │   ├── Login.jsx
│   │   ├── Dashboard.jsx
│   │   ├── Compare.jsx
│   │   ├── Rank.jsx
│   │   ├── Detail.jsx
│   │   ├── Pool.jsx
│   │   ├── Scoring.jsx
│   │   └── System/
│   │       ├── User.jsx
│   │       ├── Role.jsx
│   │       ├── Permission.jsx
│   │       └── Menu.jsx
│   ├── services/            # API 服务
│   │   ├── auth.js
│   │   ├── upload.js
│   │   ├── overview.js
│   │   ├── compare.js
│   │   ├── rank.js
│   │   ├── detail.js
│   │   ├── pool.js
│   │   ├── scoring.js
│   │   ├── user.js
│   │   ├── role.js
│   │   ├── permission.js
│   │   └── menu.js
│   ├── utils/               # 工具函数
│   │   ├── request.js       # 请求封装
│   │   ├── echarts.js       # ECharts 封装
│   │   ├── helpers.js       # 辅助函数
│   │   └── permission.js    # 权限校验工具
│   ├── store/               # 状态管理
│   │   ├── index.js
│   │   └── permission.js
│   ├── hooks/               # 自定义 hooks
│   │   └── usePermission.js
│   ├── App.jsx              # 主应用组件
│   ├── main.jsx             # 入口文件
│   └── index.css            # 全局样式
├── public/                  # 静态资源
├── index.html               # HTML 模板
├── package.json             # 依赖配置
├── vite.config.js           # Vite 配置
└── .env                     # 环境变量
```

## 3. 数据库设计

### 3.1 ER 关系图

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

┌──────────────────────┐     N:M     ┌──────────────────────┐     N:M     ┌──────────────────────┐
│   sp_user            │─────────────►│   sp_user_role       │◄─────────────│   sp_role            │
└──────────────────────┘             └──────────────────────┘             └──────────────────────┘
                                             │                              │
                                             │                              │ N:M
                                             │                              ▼
                                             │                  ┌──────────────────────┐
                                             │                  │   sp_role_menu       │
                                             │                  └──────────────────────┘
                                             │                              │
                                             │                              │ N:1
                                             │                              ▼
                                             │                  ┌──────────────────────┐
                                             │                  │   sp_menu            │
                                             │                  └──────────────────────┘
                                             │
                                             │ N:M
                                             ▼
                                   ┌──────────────────────┐     N:M     ┌──────────────────────┐
                                   │   sp_role_permission │◄─────────────│   sp_permission      │
                                   └──────────────────────┘             └──────────────────────┘
```

### 3.2 核心表结构

#### 3.2.1 sp_supplier_pool（供应商池表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PK, AUTO_INCREMENT | 主键 |
| name | VARCHAR(255) | NOT NULL | 供应商名称 |
| category | VARCHAR(100) | NULL | 供应商类别 |
| is_disabled | TINYINT UNSIGNED | DEFAULT 0 | 是否禁用 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除 |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**：`uk_supplier_name`(name), `idx_category`(category)

#### 3.2.2 sp_monthly_assessment（月度考核记录表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PK, AUTO_INCREMENT | 主键 |
| year_month | VARCHAR(7) | NOT NULL | 年月（YYYY-MM） |
| supplier_name | VARCHAR(255) | NOT NULL | 供应商名称 |
| category | VARCHAR(100) | NULL | 供应商类别 |
| total | DECIMAL(5,1) | NULL | 总分 |
| grade | VARCHAR(20) | NULL | 等级 |
| dimension_a | DECIMAL(5,1) | NULL | A维度总分 |
| dimension_b | DECIMAL(5,1) | NULL | B维度总分 |
| dimension_c | DECIMAL(5,1) | NULL | C维度总分 |
| dimension_d | DECIMAL(5,1) | NULL | D维度总分 |
| sub_a1 ~ sub_a4 | DECIMAL(5,1) | NULL | A维度子项 |
| sub_b1 ~ sub_b5 | DECIMAL(5,1) | NULL | B维度子项 |
| sub_c1 ~ sub_c3 | DECIMAL(5,1) | NULL | C维度子项 |
| sub_d1a ~ sub_d2e | DECIMAL(5,1) | NULL | D维度子项 |
| conclusion | TEXT | NULL | 会议结论 |
| exception_* | TEXT | NULL | 异常原因 |
| status | VARCHAR(20) | DEFAULT 'DRAFT' | 状态 |
| file_name | VARCHAR(255) | NULL | 原始文件名 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除 |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**：`uk_year_month_supplier`(year_month, supplier_name), `idx_year_month`(year_month)

#### 3.2.3 sp_department_score（部门打分明细表）

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT UNSIGNED | PK, AUTO_INCREMENT | 主键 |
| year_month | VARCHAR(7) | NOT NULL | 年月 |
| supplier_name | VARCHAR(255) | NOT NULL | 供应商名称 |
| department | VARCHAR(50) | NOT NULL | 提交部门 |
| dimension_group | VARCHAR(20) | NOT NULL | 维度组(A/B/C/D1/D2) |
| dimension_score | DECIMAL(5,1) | NULL | 维度总分 |
| sub_scores | JSON | NULL | 子项分数JSON |
| exception_reason | TEXT | NULL | 异常原因 |
| status | VARCHAR(20) | DEFAULT 'DRAFT' | 状态 |
| file_name | VARCHAR(255) | NULL | 原始文件名 |
| is_deleted | TINYINT UNSIGNED | DEFAULT 0 | 是否删除 |
| gmt_create | DATETIME | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| gmt_modified | DATETIME | DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

**索引**：`uk_year_month_supplier_dept`(year_month, supplier_name, department)

#### 3.2.4 用户与权限相关表

| 表名 | 说明 | 核心字段 |
|------|------|----------|
| sp_user | 用户表 | id, username, password, department, real_name, is_enabled |
| sp_role | 角色表 | id, name, code, description, is_system |
| sp_permission | 权限表 | id, name, code, module, type, parent_id |
| sp_menu | 菜单表 | id, name, path, component, icon, parent_id, type |
| sp_user_role | 用户角色关联 | user_id, role_id |
| sp_role_permission | 角色权限关联 | role_id, permission_id |
| sp_role_menu | 角色菜单关联 | role_id, menu_id |

## 4. API 接口设计

### 4.1 认证接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/logout` | POST | 用户登出 |
| `/api/auth/refresh` | POST | 刷新 Token |

### 4.2 数据上传接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/upload/excel` | POST | 上传 Excel 文件 |
| `/api/upload/template` | GET | 下载标准模板 |
| `/api/upload/merge` | POST | 合并多部门数据 |

### 4.3 业务接口

| 模块 | 接口前缀 | 主要功能 |
|------|----------|----------|
| 总览趋势 | `/api/overview` | 统计数据、趋势图表 |
| 维度对比 | `/api/compare` | 雷达图、热力图 |
| 排名变动 | `/api/rank` | 排名对比分析 |
| 明细数据 | `/api/detail` | 月度明细查询 |
| 供应商池 | `/api/pool` | 供应商 CRUD |
| 手动打分 | `/api/scoring` | 打分流程管理 |
| 会议记录 | `/api/meeting-note` | 会议记录管理 |

### 4.4 系统管理接口

| 模块 | 接口前缀 | 主要功能 |
|------|----------|----------|
| 用户管理 | `/api/user` | 用户 CRUD、角色分配 |
| 角色管理 | `/api/role` | 角色 CRUD、权限/菜单分配 |
| 权限管理 | `/api/permission` | 权限 CRUD、树形结构 |
| 菜单管理 | `/api/menu` | 菜单 CRUD、树形结构 |

## 5. 关键技术实现

### 5.1 JWT 认证机制

```
流程：
1. 用户登录 → 验证用户名密码 → 生成 Access Token + Refresh Token
2. 后续请求携带 Token → 过滤器验证 Token 有效性 → 提取用户信息 → 注入上下文
3. Token 过期 → 使用 Refresh Token 刷新 → 获取新的 Access Token
```

**Token 结构**：
- Access Token：有效期 1 小时，包含用户 ID、用户名、角色、权限
- Refresh Token：有效期 7 天，存储在数据库，支持注销

### 5.2 EasyExcel 解析

**解析策略**：
- 根据文件名自动识别部门（计划/采购/质量/包开）
- 每个部门使用独立的 Excel 实体类和列映射规则
- 解析后数据直接存入 `sp_department_score` 表，不进行自动计算
- 支持完整模板和简化模板两种格式

**异常处理**：
- 文件格式错误：返回错误信息
- 数据验证失败：返回具体行号和错误原因
- 重复数据：支持覆盖或跳过

### 5.3 多部门数据合并

**合并规则**：
1. 同一供应商在不同部门文件中的分数进行合并
2. 每个维度取对应部门提交的分数
3. 总分 = A + B + C + D
4. 若某维度多部门都有数据，取非零值或最新提交

**合并流程**：
```
1. 查询指定年月各部门提交的数据
2. 按供应商名称分组
3. 合并各维度分数
4. 生成汇总记录存入 sp_monthly_assessment
5. 返回合并结果和冲突信息
```

### 5.4 权限控制

**权限模型**：RBAC（基于角色的访问控制）

**权限类型**：
- MENU：菜单权限（控制菜单显示）
- FUNCTION：功能权限（控制按钮操作）
- DATA：数据权限（控制数据范围）

**权限校验**：
- 前端：路由守卫 + 按钮权限校验
- 后端：注解 + 拦截器校验

## 6. 前端实现要点

### 6.1 路由配置

```
/
├── /login          # 登录页面
├── /dashboard      # 总览趋势
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

### 6.2 请求封装

- 统一配置 baseURL、超时时间、请求拦截器、响应拦截器
- 请求拦截器：自动添加 Token、处理加载状态
- 响应拦截器：统一错误处理、Token 过期自动刷新

### 6.3 状态管理

- 用户信息：存储用户基本信息、角色、权限
- 菜单数据：存储当前用户可访问的菜单树
- 全局状态：加载状态、消息提示、权限缓存

### 6.4 权限校验

- 路由级校验：根据用户权限动态生成路由
- 组件级校验：通过自定义 Hook 校验按钮权限
- 数据级校验：根据部门限制可查看的数据范围

## 7. 部署与运维

### 7.1 环境配置

| 环境 | 配置文件 | 用途 |
|------|----------|------|
| 开发 | application-dev.yml | 本地开发 |
| 测试 | application-test.yml | 测试环境 |
| 生产 | application-prod.yml | 生产环境 |

### 7.2 数据库初始化

- 执行 `schema.sql` 创建数据库表结构
- 插入初始数据（管理员用户、预设角色、基础菜单）

### 7.3 启动方式

**后端**：
```bash
cd backend
mvn spring-boot:run
```

**前端**：
```bash
cd frontend
npm install
npm run dev
```

### 7.4 数据备份

- 每日自动备份数据库
- 备份文件存储在指定目录，保留 30 天

## 8. 技术风险与缓解措施

| 风险 | 影响 | 缓解措施 |
|------|------|----------|
| Excel 格式不统一 | 解析失败 | 提供标准模板，严格验证 |
| 多部门数据冲突 | 数据不一致 | 唯一索引约束，合并规则明确 |
| 权限控制复杂 | 安全漏洞 | 基于角色的细粒度权限管理 |
| 大数据量性能 | 查询缓慢 | 索引优化，分页查询 |
| Token 泄露 | 安全风险 | 短有效期 + Refresh Token 机制 |
| 文件上传攻击 | 服务器安全 | 文件类型校验，大小限制 |

## 9. 测试策略

### 9.1 后端测试

| 测试类型 | 覆盖范围 | 工具 |
|----------|----------|------|
| 单元测试 | Service 层逻辑 | JUnit 5 + Mockito |
| 集成测试 | Controller + Service + Mapper | Spring Boot Test |
| API 测试 | 接口功能验证 | Postman / JMeter |
| 性能测试 | 并发请求、响应时间 | JMeter |

### 9.2 前端测试

| 测试类型 | 覆盖范围 | 工具 |
|----------|----------|------|
| 单元测试 | 组件逻辑、工具函数 | Jest |
| 集成测试 | 页面功能、路由跳转 | React Testing Library |
| E2E 测试 | 完整业务流程 | Cypress |

### 9.3 测试覆盖目标

| 模块 | 覆盖率目标 |
|------|------------|
| Service 层 | ≥ 80% |
| Controller 层 | ≥ 70% |
| 核心业务逻辑 | ≥ 90% |

## 10. 扩展计划

### 10.1 短期扩展（1-3个月）

- 数据导出格式扩展（PDF、CSV）
- 邮件通知功能
- 数据看板自定义配置

### 10.2 中期扩展（3-6个月）

- 供应商考核预警机制
- 移动端适配
- 数据可视化增强（3D图表、地理分布）

### 10.3 长期扩展（6个月以上）

- 与 ERP 系统集成
- 机器学习预测供应商绩效
- 多租户支持