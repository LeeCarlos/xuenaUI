# Tasks: 供应商绩效分析系统实现任务

---
change: supplier-performance-system
design-doc: openspec/changes/supplier-performance-system/design.md
base-ref: HEAD
---

## 任务总览

共 7 个阶段，42 个任务

| 阶段 | 名称 | 任务数 | 依赖 |
|------|------|--------|------|
| Phase 1 | 项目基础搭建 | 5 | 无 |
| Phase 2 | 数据库设计与初始化 | 4 | Phase 1 |
| Phase 3 | 用户认证与权限模块 | 8 | Phase 2 |
| Phase 4 | 供应商池管理模块 | 5 | Phase 3 |
| Phase 5 | 数据上传与手动打分模块 | 10 | Phase 4 |
| Phase 6 | 数据分析与可视化模块 | 6 | Phase 5 |
| Phase 7 | 系统管理模块 | 4 | Phase 6 |

---

## Phase 1: 项目基础搭建

### T1.1 初始化后端项目

- [ ] 创建 Maven 项目结构
- [ ] 添加 Spring Boot 3.2 依赖
- [ ] 添加 MyBatis、EasyExcel、JWT 依赖
- [ ] 创建 application.yml 配置文件
- [ ] 创建主启动类 SupplierApplication.java

### T1.2 初始化前端项目

- [ ] 使用 Vite 创建 React 项目
- [ ] 添加 Ant Design 5、ECharts 依赖
- [ ] 配置路由、状态管理
- [ ] 创建基础布局组件（Header、Sidebar、Layout）
- [ ] 配置 axios 请求封装

### T1.3 创建工具类

- [ ] JwtUtil.java - JWT 工具类
- [ ] PasswordUtil.java - 密码加密工具
- [ ] EasyExcelUtil.java - Excel 处理工具
- [ ] 前端 request.js - 请求封装
- [ ] 前端 permission.js - 权限校验工具

### T1.4 创建配置类

- [ ] MyBatisConfig.java - MyBatis 配置
- [ ] EasyExcelConfig.java - EasyExcel 配置
- [ ] CorsConfig.java - CORS 配置
- [ ] SecurityConfig.java - 安全配置

### T1.5 创建异常处理

- [ ] BusinessException.java - 业务异常类
- [ ] GlobalExceptionHandler.java - 全局异常处理器
- [ ] 统一响应结构 ResultVO

---

## Phase 2: 数据库设计与初始化

### T2.1 创建数据库表结构

- [ ] 创建 sp_supplier_pool（供应商池表）
- [ ] 创建 sp_monthly_assessment（月度考核记录表）
- [ ] 创建 sp_department_score（部门打分明细表）
- [ ] 创建 sp_category（类别管理表）
- [ ] 创建 sp_meeting_note（会议记录表）

### T2.2 创建权限相关表

- [ ] 创建 sp_user（用户表）
- [ ] 创建 sp_role（角色表）
- [ ] 创建 sp_permission（权限表）
- [ ] 创建 sp_menu（菜单表）
- [ ] 创建 sp_user_role、sp_role_permission、sp_role_menu（关联表）

### T2.3 创建实体类 DO

- [ ] SupplierPoolDO.java
- [ ] MonthlyAssessmentDO.java
- [ ] DepartmentScoreDO.java
- [ ] MeetingNoteDO.java
- [ ] CategoryDO.java
- [ ] UserDO、RoleDO、PermissionDO、MenuDO 等

### T2.4 初始化基础数据

- [ ] 插入预设角色（超级管理员、各部门管理员）
- [ ] 插入预设权限
- [ ] 插入预设菜单
- [ ] 创建管理员用户（admin/admin123）
- [ ] 插入测试供应商数据

---

## Phase 3: 用户认证与权限模块

### T3.1 JWT 认证过滤器

- [ ] 创建 JwtAuthenticationFilter.java
- [ ] 实现 Token 解析与验证逻辑
- [ ] 实现用户信息注入上下文
- [ ] 配置过滤器链

### T3.2 登录接口实现

- [ ] 创建 AuthController.java
- [ ] 实现 POST /api/auth/login
- [ ] 实现 POST /api/auth/logout
- [ ] 实现 POST /api/auth/refresh
- [ ] 创建 AuthService 接口与实现

### T3.3 角色服务实现

- [ ] 创建 RoleService 接口与实现
- [ ] 实现角色 CRUD
- [ ] 实现权限分配
- [ ] 实现菜单分配

### T3.4 权限服务实现

- [ ] 创建 PermissionService 接口与实现
- [ ] 实现权限 CRUD
- [ ] 实现权限树形结构查询

### T3.5 菜单服务实现

- [ ] 创建 MenuService 接口与实现
- [ ] 实现菜单 CRUD
- [ ] 实现菜单树形结构查询
- [ ] 实现用户菜单查询（根据权限过滤）

### T3.6 登录页面实现

- [ ] 创建 Login.jsx 页面
- [ ] 实现登录表单
- [ ] 实现登录状态管理
- [ ] 实现路由守卫

### T3.7 权限校验实现

- [ ] 实现前端路由级权限校验
- [ ] 实现组件级权限校验（自定义 Hook）
- [ ] 实现按钮级权限校验

### T3.8 权限状态管理

- [ ] 创建权限状态 store
- [ ] 实现登录后权限加载
- [ ] 实现菜单动态渲染

---

## Phase 4: 供应商池管理模块

### T4.1 供应商池接口实现

- [ ] 创建 PoolController.java
- [ ] 实现 GET /api/pool/list（分页查询）
- [ ] 实现 POST /api/pool/create（新增）
- [ ] 实现 PUT /api/pool/update/{id}（更新）
- [ ] 实现 DELETE /api/pool/delete/{id}（删除）
- [ ] 实现 PUT /api/pool/toggle/{id}（启用/禁用）

### T4.2 供应商池服务实现

- [ ] 创建 PoolService 接口与实现
- [ ] 实现供应商 CRUD 逻辑
- [ ] 实现逻辑删除
- [ ] 实现名称唯一性校验

### T4.3 供应商池 Mapper

- [ ] 创建 SupplierPoolMapper.java
- [ ] 创建 SupplierPoolMapper.xml
- [ ] 实现查询、插入、更新、删除 SQL

### T4.4 批量导入导出

- [ ] 实现 POST /api/pool/import（批量导入）
- [ ] 实现 POST /api/pool/export（导出）
- [ ] 创建 Excel 导入实体类
- [ ] 实现导入数据验证

### T4.5 供应商池前端页面

- [ ] 创建 Pool.jsx 页面
- [ ] 实现供应商列表表格
- [ ] 实现新增/编辑弹窗
- [ ] 实现搜索、筛选功能
- [ ] 实现导入/导出按钮

---

## Phase 5: 数据上传与手动打分模块

### T5.1 数据上传接口实现

- [ ] 创建 UploadController.java
- [ ] 实现 POST /api/upload/excel（文件上传）
- [ ] 实现 GET /api/upload/template（模板下载）
- [ ] 实现 POST /api/upload/merge（数据合并）
- [ ] 实现 DELETE /api/upload/year-month/{yearMonth}（删除数据）

### T5.2 数据上传服务实现

- [ ] 创建 UploadService 接口与实现
- [ ] 实现 Excel 文件解析
- [ ] 实现部门自动识别（从文件名）
- [ ] 实现数据验证逻辑
- [ ] 实现重复数据检测

### T5.3 Excel 实体类

- [ ] 创建完整模板实体类（FullTemplateExcel.java）
- [ ] 创建简化模板实体类（SimplifiedTemplateExcel.java）
- [ ] 创建各部门专用实体类（QualityExcel、PlanningExcel、PackagingExcel、ProcurementExcel）
- [ ] 使用 @ExcelProperty 注解配置列映射

### T5.4 手动打分接口实现

- [ ] 创建 ScoringController.java
- [ ] 实现 GET /api/scoring/years（年份列表）
- [ ] 实现 GET /api/scoring/months/{year}（月份列表）
- [ ] 实现 GET /api/scoring/status/{yearMonth}（打分状态）
- [ ] 实现 POST /api/scoring/upload（上传打分文件）
- [ ] 实现 GET /api/scoring/template（下载部门模板）
- [ ] 实现 POST /api/scoring/submit/{yearMonth}（提交打分）
- [ ] 实现 POST /api/scoring/reset/{yearMonth}（重置）

### T5.5 手动打分服务实现

- [ ] 创建 ScoringService 接口与实现
- [ ] 实现部门模板生成
- [ ] 实现各部门数据解析
- [ ] 实现打分状态管理
- [ ] 实现权限控制（各部门仅能操作自己的数据）

### T5.6 部门打分明细 Mapper

- [ ] 创建 DepartmentScoreMapper.java
- [ ] 创建 DepartmentScoreMapper.xml
- [ ] 实现按年月、部门查询
- [ ] 实现数据合并查询

### T5.7 手动打分前端页面

- [ ] 创建 Scoring.jsx 页面
- [ ] 实现年月选择器组件
- [ ] 实现部门选择器
- [ ] 实现模板下载按钮
- [ ] 实现文件上传区域

### T5.8 打分预览功能

- [ ] 实现上传后数据预览表格
- [ ] 实现分数编辑（仅当前部门负责维度）
- [ ] 实现提交确认弹窗
- [ ] 实现状态显示区

### T5.9 多部门数据合并

- [ ] 实现合并逻辑（按供应商分组，合并各维度分数）
- [ ] 实现冲突检测与处理
- [ ] 实现合并后数据写入月度考核汇总表

### T5.10 上传组件封装

- [ ] 创建 UploadArea.jsx 组件
- [ ] 实现拖拽上传
- [ ] 实现多文件上传
- [ ] 实现上传进度显示
- [ ] 实现上传结果展示

---

## Phase 6: 数据分析与可视化模块

### T6.1 总览趋势接口

- [ ] 创建 OverviewController.java
- [ ] 实现 GET /api/overview/stats（统计数据）
- [ ] 实现 GET /api/overview/trend（趋势数据）
- [ ] 实现 GET /api/overview/grade-distribution（等级分布）
- [ ] 实现 GET /api/overview/dimension-avg（维度平均分）

### T6.2 总览趋势服务

- [ ] 创建 OverviewService 接口与实现
- [ ] 实现统计数据计算
- [ ] 实现趋势数据查询
- [ ] 实现等级分布统计
- [ ] 实现维度平均分计算

### T6.3 维度对比接口

- [ ] 创建 CompareController.java
- [ ] 实现 GET /api/compare/radar（雷达图数据）
- [ ] 实现 GET /api/compare/heatmap（热力图数据）

### T6.4 排名变动接口

- [ ] 创建 RankController.java
- [ ] 实现 GET /api/rank/compare（排名对比）
- [ ] 实现 POST /api/rank/export（导出）

### T6.5 明细数据接口

- [ ] 创建 DetailController.java
- [ ] 实现 GET /api/detail/monthly（月度明细）
- [ ] 实现导出功能

### T6.6 前端可视化页面

- [ ] 创建 Dashboard.jsx（总览趋势）
- [ ] 创建 Compare.jsx（维度对比）
- [ ] 创建 Rank.jsx（排名变动）
- [ ] 创建 Detail.jsx（明细数据）
- [ ] 创建 ECharts 图表组件（折线图、雷达图、热力图、柱状图）

---

## Phase 7: 系统管理模块

### T7.1 用户管理接口

- [ ] 创建 UserController.java
- [ ] 实现 GET /api/user/list（分页查询）
- [ ] 实现 POST /api/user/add（新增）
- [ ] 实现 PUT /api/user/update（更新）
- [ ] 实现 DELETE /api/user/delete/{id}（删除）
- [ ] 实现 POST /api/user/assign-role（分配角色）

### T7.2 用户管理服务

- [ ] 创建 UserService 接口与实现
- [ ] 实现用户 CRUD 逻辑
- [ ] 实现角色分配
- [ ] 实现密码加密存储

### T7.3 用户管理前端页面

- [ ] 创建 System/User.jsx（用户管理页面）
- [ ] 创建 System/Role.jsx（角色管理页面）
- [ ] 创建 System/Permission.jsx（权限管理页面）
- [ ] 创建 System/Menu.jsx（菜单管理页面）
- [ ] 实现各页面的列表、新增、编辑、删除功能
- [ ] 实现角色分配、权限分配、菜单分配弹窗

### T7.4 系统管理 Mapper

- [ ] 创建 UserMapper、RoleMapper、PermissionMapper、MenuMapper
- [ ] 创建对应的 XML 映射文件
- [ ] 实现关联查询（用户-角色、角色-权限、角色-菜单）

---

## 验证与归档

### V1 接口验证

- [ ] 所有 API 接口功能验证
- [ ] 权限控制验证
- [ ] 数据持久化验证

### V2 前端验证

- [ ] 所有页面功能验证
- [ ] 图表展示验证
- [ ] Excel 导入/导出验证
- [ ] 响应式布局验证

### V3 性能验证

- [ ] 页面加载时间 ≤ 3秒
- [ ] API 响应时间 ≤ 1秒
- [ ] Excel 导入速度 ≤ 10秒/100条

### V4 安全验证

- [ ] SQL 注入防护验证
- [ ] XSS 攻击防护验证
- [ ] 权限越权访问验证

---

## 代码审查检查清单

### 后端代码审查

- [ ] 代码符合阿里巴巴 Java 开发手册
- [ ] 类命名规范（DO 后缀、PascalCase）
- [ ] 方法命名规范（camelCase、动词-宾语结构）
- [ ] POJO 属性使用包装类型
- [ ] 类成员顺序正确（public 方法 → private 方法 → Getter/Setter）
- [ ] 异常处理完整
- [ ] 参数校验完整
- [ ] SQL 语句无注入风险
- [ ] 日志记录规范

### 前端代码审查

- [ ] 组件划分合理
- [ ] 状态管理规范
- [ ] 请求封装统一
- [ ] 权限校验完整
- [ ] 代码注释清晰
- [ ] 样式命名规范
- [ ] 性能优化（按需加载、防抖节流）

### 数据库审查

- [ ] 表名前缀正确（sp_）
- [ ] 字段命名规范（下划线分隔）
- [ ] 必备字段齐全（id、gmt_create、gmt_modified、is_deleted）
- [ ] 索引设计合理
- [ ] 数据类型正确（BIGINT UNSIGNED、DECIMAL(5,1)）