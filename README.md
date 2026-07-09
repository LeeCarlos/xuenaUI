# 供应商绩效分析系统

> 供应商绩效分析系统是一个基于 Spring Boot + React + MySQL 的企业级供应商评估管理平台，支持多部门协同打分、数据可视化分析、供应商池管理等功能。

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Node Version](https://img.shields.io/badge/Node-20-green.svg)](https://nodejs.org/)
[![MySQL Version](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

## 📋 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)1
- [快速启动](#快速启动)
- [数据库设计](#数据库设计)
- [API 概览](#api-概览)
- [编码规范](#编码规范)
- [贡献指南](#贡献指南)

---

## ✨ 功能特性

### 📊 数据可视化

| 模块 | 功能 |
|------|------|
| 总览趋势 | 供应商数量统计、等级分布饼图、总分趋势图 |
| 维度对比 | 各维度平均分对比、维度雷达图、月度维度对比 |
| 排名变动 | 供应商排名列表、环比变化、升降级统计 |
| 明细数据 | 供应商考核明细表、筛选导出、热力图 |

### 📝 业务管理

| 模块 | 功能 |
|------|------|
| 供应商池管理 | 供应商信息维护、类别管理、启用/禁用 |
| 手动打分 | 年月选择、多部门协同、Excel模板上传 |
| 会议记录 | 供应商会议记录管理、时间范围筛选 |

### 🔄 多部门协同

- **质量部**：负责品质考核（A维度）
- **计划部**：负责交货考核（C维度）
- **包开部**：负责服务考核-包材（D1维度）
- **采购部**：负责成本考核（B维度）、服务考核-业务支持（D2维度）及数据合并

---

## 🛠 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 应用框架 |
| Spring Security | 6.2.x | 安全认证 |
| MyBatis Plus | 3.5.x | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| EasyExcel | 3.3.x | Excel解析 |
| JWT | - | Token认证 |

### 前端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| React | 18.x | UI框架 |
| Ant Design | 5.x | UI组件库 |
| ECharts | 5.x | 图表库 |
| Vite | 5.x | 构建工具 |
| Axios | 1.x | HTTP客户端 |

---

## 📁 项目结构

```
xuenaUI/
├── backend/                    # 后端项目
│   ├── src/main/java/com/xuena/supplier/
│   │   ├── controller/         # REST API控制器
│   │   ├── service/            # 业务逻辑接口
│   │   ├── service/impl/       # 业务逻辑实现
│   │   ├── mapper/             # MyBatis数据访问层
│   │   ├── entity/             # 数据库实体(DO)
│   │   ├── dto/                # 数据传输对象
│   │   ├── vo/                 # 视图对象
│   │   ├── enums/              # 枚举类
│   │   ├── config/             # 配置类
│   │   ├── security/           # 安全相关
│   │   ├── exception/          # 异常处理
│   │   ├── util/               # 工具类
│   │   └── constant/           # 常量类
│   └── src/main/resources/
│       ├── mapper/             # MyBatis XML映射
│       └── application.yml     # 应用配置
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── components/         # 通用组件
│   │   ├── pages/              # 页面组件
│   │   ├── services/           # API服务
│   │   ├── utils/              # 工具函数
│   │   └── store/              # 状态管理
│   └── package.json
├── requirements.md             # 需求文档
└── README.md                   # 项目说明
```

---

## 🚀 快速启动

### 环境要求

- JDK 21+
- Node.js 20+
- MySQL 8.0+
- Maven 3.9+

### 步骤

1. **克隆项目**

```bash
git clone https://github.com/LeeCarlos/xuenaUI.git
cd xuenaUI
```

2. **配置数据库**

创建MySQL数据库：

```sql
CREATE DATABASE xuena_supplier CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `backend/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xuena_supplier?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: your_username
    password: your_password
```

3. **启动后端**

```bash
cd backend
mvn spring-boot:run
```

4. **启动前端**

```bash
cd frontend
npm install
npm run dev
```

5. **访问系统**

打开浏览器访问：`http://localhost:5173`

---

## 🗄 数据库设计

### 核心表结构

| 表名 | 说明 |
|------|------|
| sp_supplier_pool | 供应商池表 |
| sp_monthly_assessment | 月度考核汇总记录表 |
| sp_department_score | 部门打分明细表 |
| sp_meeting_note | 会议记录表 |
| sp_category | 类别管理表 |
| sp_user | 用户表 |

### 打分维度

| 维度 | 名称 | 满分 | 负责部门 |
|------|------|------|----------|
| A | 品质考核 | 25 | 质量部 |
| B | 成本考核 | 20 | 采购部 |
| C | 交货考核 | 20 | 计划部 |
| D1 | 服务考核-包材 | 20 | 包开部 |
| D2 | 服务考核-业务支持 | 15 | 采购部 |

---

## 🔌 API 概览

| 模块 | 接口前缀 | 说明 |
|------|----------|------|
| 认证 | `/api/auth` | 登录、注册、Token刷新 |
| 数据上传 | `/api/upload` | Excel上传、模板下载、数据合并 |
| 总览趋势 | `/api/overview` | 统计数据、趋势图表 |
| 维度对比 | `/api/compare` | 维度分析、雷达图 |
| 排名变动 | `/api/rank` | 排名列表、环比变化 |
| 明细数据 | `/api/detail` | 考核明细、热力图 |
| 供应商池 | `/api/pool` | 供应商管理 |
| 手动打分 | `/api/scoring` | 打分管理、状态查询 |
| 会议记录 | `/api/meeting-note` | 会议记录管理 |

---

## 📖 编码规范

本项目遵循 **阿里巴巴Java开发手册**，核心规范如下：

### 命名规范

- **表名**：`业务缩写_表作用`，如 `sp_supplier_pool`
- **字段名**：下划线分隔，如 `gmt_create`、`is_deleted`
- **类名**：大驼峰，如 `SupplierPoolDO`
- **方法名**：小驼峰，动宾结构，如 `getSupplierList()`
- **常量名**：全大写，下划线分隔，如 `MAX_PAGE_SIZE`

### 数据库规范

- 必备字段：`id`（BIGINT UNSIGNED）、`gmt_create`、`gmt_modified`、`is_deleted`
- 索引命名：`uk_`（唯一索引）、`idx_`（普通索引）
- 小数类型：`DECIMAL`，禁止使用 `FLOAT`/`DOUBLE`

### POJO规范

- 属性使用包装数据类型
- 不设定默认值
- 必须重写 `toString()` 方法

---

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feature/xxx`
3. 提交代码：`git commit -m "feat: xxx"`
4. 推送到分支：`git push origin feature/xxx`
5. 创建 Pull Request

---

## 📄 License

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情