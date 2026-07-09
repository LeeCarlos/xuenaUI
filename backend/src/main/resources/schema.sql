CREATE TABLE IF NOT EXISTS sp_supplier_pool (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(255) NOT NULL COMMENT '供应商名称',
    category VARCHAR(100) NULL COMMENT '供应商类别',
    is_disabled TINYINT UNSIGNED DEFAULT 0 COMMENT '是否禁用（0-启用，1-禁用）',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category),
    UNIQUE INDEX uk_supplier_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商池表';

CREATE TABLE IF NOT EXISTS sp_category (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '类别名称',
    description VARCHAR(500) NULL COMMENT '类别描述',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX uk_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类别管理表';

CREATE TABLE IF NOT EXISTS sp_monthly_assessment (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `year_month` VARCHAR(7) NOT NULL COMMENT '年月（格式：YYYY-MM）',
    supplier_name VARCHAR(255) NOT NULL COMMENT '供应商名称',
    category VARCHAR(100) NULL COMMENT '供应商类别',
    total DECIMAL(5,1) NULL COMMENT '总分',
    grade VARCHAR(20) NULL COMMENT '等级',
    dimension_a DECIMAL(5,1) NULL COMMENT 'A维度总分（品质考核）',
    dimension_b DECIMAL(5,1) NULL COMMENT 'B维度总分（成本考核）',
    dimension_c DECIMAL(5,1) NULL COMMENT 'C维度总分（交货考核）',
    dimension_d DECIMAL(5,1) NULL COMMENT 'D维度总分（服务考核）',
    sub_a1 DECIMAL(5,1) NULL COMMENT 'a1验货合格率',
    sub_a2 DECIMAL(5,1) NULL COMMENT 'a2综合客诉率',
    sub_a3 DECIMAL(5,1) NULL COMMENT 'a3新品开发质量',
    sub_a4 DECIMAL(5,1) NULL COMMENT 'a4质量改善完成率',
    sub_b1 DECIMAL(5,1) NULL COMMENT 'b1价格水平',
    sub_b2 DECIMAL(5,1) NULL COMMENT 'b2供货周期',
    sub_b3 DECIMAL(5,1) NULL COMMENT 'b3付款条件',
    sub_b4 DECIMAL(5,1) NULL COMMENT 'b4报价准确',
    sub_b5 DECIMAL(5,1) NULL COMMENT 'b5成本支持',
    sub_c1 DECIMAL(5,1) NULL COMMENT 'c1交货延迟批次',
    sub_c2 DECIMAL(5,1) NULL COMMENT 'c2交货数量短缺',
    sub_c3 DECIMAL(5,1) NULL COMMENT 'c3订单交付配合度',
    sub_d1a DECIMAL(5,1) NULL COMMENT 'd1a包材技术支持',
    sub_d1b DECIMAL(5,1) NULL COMMENT 'd1b包材技术评估',
    sub_d2a DECIMAL(5,1) NULL COMMENT 'd2a成品技术支持',
    sub_d2b DECIMAL(5,1) NULL COMMENT 'd2b成品技术评估',
    sub_d2c DECIMAL(5,1) NULL COMMENT 'd2c研发打样',
    sub_d2d DECIMAL(5,1) NULL COMMENT 'd2d业务支持度',
    sub_d2e DECIMAL(5,1) NULL COMMENT 'd2e响应配合度',
    conclusion TEXT NULL COMMENT '会议结论',
    exception_quality TEXT NULL COMMENT '品质考核异常原因',
    exception_cost TEXT NULL COMMENT '成本考核异常原因',
    exception_delivery TEXT NULL COMMENT '交货考核异常原因',
    exception_service_product TEXT NULL COMMENT '服务考核-成品异常原因',
    exception_service_package TEXT NULL COMMENT '服务考核-包材异常原因',
    exception_other TEXT NULL COMMENT '其他异常原因',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态（DRAFT-草稿，LOCKED-已提交）',
    file_name VARCHAR(255) NULL COMMENT '原始文件名',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_year_month (`year_month`),
    INDEX idx_category (category),
    INDEX idx_grade (grade),
    UNIQUE INDEX uk_year_month_supplier (`year_month`, supplier_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='月度考核记录表';

CREATE TABLE IF NOT EXISTS sp_department_score (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `year_month` VARCHAR(7) NOT NULL COMMENT '年月（格式：YYYY-MM）',
    supplier_name VARCHAR(255) NOT NULL COMMENT '供应商名称',
    department VARCHAR(50) NOT NULL COMMENT '提交部门（计划/采购/质量/包开）',
    dimension_group VARCHAR(20) NOT NULL COMMENT '维度组（A/B/C/D1/D2）',
    dimension_score DECIMAL(5,1) NULL COMMENT '维度总分',
    sub_scores JSON NULL COMMENT '子项分数JSON',
    exception_reason TEXT NULL COMMENT '异常原因说明',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态（DRAFT-草稿，SUBMITTED-已提交）',
    file_name VARCHAR(255) NULL COMMENT '原始文件名',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_year_month_dept (`year_month`, department),
    INDEX idx_dimension_group (dimension_group),
    UNIQUE INDEX uk_year_month_supplier_dept (`year_month`, supplier_name, department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门打分明细表';

CREATE TABLE IF NOT EXISTS sp_meeting_note (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    supplier_name VARCHAR(255) NOT NULL COMMENT '供应商名称',
    month_from VARCHAR(7) NOT NULL COMMENT '起始年月（格式：YYYY-MM）',
    month_to VARCHAR(7) NOT NULL COMMENT '结束年月（格式：YYYY-MM）',
    note TEXT NULL COMMENT '会议记录内容',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_supplier_month_range (supplier_name, month_from, month_to)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议记录表';

CREATE TABLE IF NOT EXISTS sp_user (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    department VARCHAR(50) NULL COMMENT '所属部门（计划/采购/质量/包开）',
    real_name VARCHAR(100) NULL COMMENT '真实姓名',
    email VARCHAR(255) NULL COMMENT '邮箱',
    is_enabled TINYINT UNSIGNED DEFAULT 1 COMMENT '是否启用（0-禁用，1-启用）',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_department (department),
    UNIQUE INDEX uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS sp_role (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(500) NULL COMMENT '角色描述',
    is_system TINYINT UNSIGNED DEFAULT 0 COMMENT '是否系统角色（0-自定义，1-系统内置）',
    is_enabled TINYINT UNSIGNED DEFAULT 1 COMMENT '是否启用（0-禁用，1-启用）',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role_name (name),
    UNIQUE INDEX uk_role_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS sp_permission (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '权限名称',
    code VARCHAR(100) NOT NULL COMMENT '权限编码',
    module VARCHAR(50) NOT NULL COMMENT '所属模块',
    type VARCHAR(20) DEFAULT 'FUNCTION' COMMENT '权限类型（MENU-菜单权限，FUNCTION-功能权限，DATA-数据权限）',
    description VARCHAR(500) NULL COMMENT '权限描述',
    parent_id BIGINT UNSIGNED DEFAULT 0 COMMENT '父权限ID（0表示顶级）',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_module (module),
    INDEX idx_parent_id (parent_id),
    UNIQUE INDEX uk_permission_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

CREATE TABLE IF NOT EXISTS sp_menu (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '菜单名称',
    path VARCHAR(255) NULL COMMENT '路由路径',
    component VARCHAR(255) NULL COMMENT '组件路径',
    icon VARCHAR(100) NULL COMMENT '菜单图标',
    parent_id BIGINT UNSIGNED DEFAULT 0 COMMENT '父菜单ID（0表示顶级菜单）',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    type VARCHAR(20) DEFAULT 'MENU' COMMENT '菜单类型（MENU-菜单，DIRECTORY-目录，BUTTON-按钮）',
    permission_code VARCHAR(100) NULL COMMENT '关联权限编码',
    is_visible TINYINT UNSIGNED DEFAULT 1 COMMENT '是否显示（0-隐藏，1-显示）',
    is_deleted TINYINT UNSIGNED DEFAULT 0 COMMENT '是否删除（0-未删除，1-已删除）',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    gmt_modified DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    UNIQUE INDEX uk_menu_path (path)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS sp_user_role (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    role_id BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id),
    UNIQUE INDEX uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS sp_role_permission (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    role_id BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    permission_id BIGINT UNSIGNED NOT NULL COMMENT '权限ID',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id),
    UNIQUE INDEX uk_role_permission (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

CREATE TABLE IF NOT EXISTS sp_role_menu (
    id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    role_id BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
    menu_id BIGINT UNSIGNED NOT NULL COMMENT '菜单ID',
    gmt_create DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id),
    UNIQUE INDEX uk_role_menu (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';