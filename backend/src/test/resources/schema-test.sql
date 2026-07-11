CREATE TABLE IF NOT EXISTS sp_supplier_pool (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    is_disabled TINYINT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_monthly_assessment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year_month VARCHAR(7) NOT NULL,
    supplier_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    total DECIMAL(5,1),
    grade VARCHAR(10),
    dimension_a DECIMAL(5,1),
    dimension_b DECIMAL(5,1),
    dimension_c DECIMAL(5,1),
    dimension_d DECIMAL(5,1),
    conclusion TEXT,
    status VARCHAR(20) DEFAULT 'DRAFT',
    file_name VARCHAR(200),
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_department_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year_month VARCHAR(7) NOT NULL,
    supplier_name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    dimension_group VARCHAR(50),
    dimension_score DECIMAL(5,1),
    sub_scores TEXT,
    exception_reason TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    file_name VARCHAR(200),
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    department VARCHAR(50),
    real_name VARCHAR(50),
    email VARCHAR(100),
    is_enabled TINYINT DEFAULT 1,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    is_system TINYINT DEFAULT 0,
    is_enabled TINYINT DEFAULT 1,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    module VARCHAR(50),
    type VARCHAR(20),
    description VARCHAR(255),
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    path VARCHAR(100),
    component VARCHAR(200),
    icon VARCHAR(50),
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    type VARCHAR(20),
    permission_code VARCHAR(100),
    is_visible TINYINT DEFAULT 1,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_role_menu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_meeting_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    month_from VARCHAR(7) NOT NULL,
    month_to VARCHAR(7) NOT NULL,
    note TEXT,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    store_key VARCHAR(255) NOT NULL UNIQUE,
    file_type VARCHAR(20) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100),
    description VARCHAR(500),
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sp_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item VARCHAR(50) NOT NULL,
    key VARCHAR(50) NOT NULL,
    value VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    sort_order INT DEFAULT 0,
    is_enabled TINYINT DEFAULT 1,
    is_deleted TINYINT DEFAULT 0,
    create_name VARCHAR(100),
    create_id VARCHAR(64),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_name VARCHAR(100),
    update_id VARCHAR(64),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_item_key (item, key)
);
