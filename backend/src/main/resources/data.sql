INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES ('admin', '$2a$10$.D/1FzKoZGAV.qv7dT.98OwxKDTqM5abFpREzvAluI/Hbjgu2dW42', '计划', '管理员', 'admin@xuena.com', 1);
INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES ('user1', '$2a$10$VLvJ8LD5L23Wt2T0uxVXBO1Su1hXusdRS8H/dRnj7wbXXfUP6m/gi', '质量', '张三', 'user1@xuena.com', 1);

INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES ('管理员', 'admin', '系统管理员', 1, 1);
INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES ('普通用户', 'user', '普通用户', 1, 1);

INSERT INTO sp_permission (name, code, module, type, description) VALUES ('供应商管理', 'supplier:manage', '供应商', 'FUNCTION', '供应商管理权限');
INSERT INTO sp_permission (name, code, module, type, description) VALUES ('考核管理', 'assessment:manage', '考核', 'FUNCTION', '考核管理权限');

INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('供应商池', '/supplier/pool', 'SupplierPool', 'DatabaseOutlined', 0, 2, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('类别管理', '/category', 'Category', 'FolderOutlined', 0, 3, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('考核管理', '/assessment', 'Assessment', 'BarChartOutlined', 0, 4, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('手动打分', '/department-score', 'DepartmentScore', 'EditOutlined', 0, 5, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('会议纪要', '/meeting-note', 'MeetingNote', 'FileTextOutlined', 0, 6, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('差异分析', '/compare', 'Compare', 'BarChartOutlined', 0, 7, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('数据汇总', '/summary', 'Summary', 'FileTextOutlined', 0, 8, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('文件管理', '/file-manager', 'FileManager', 'FolderOutlined', 0, 3, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('模板管理', '/template-manager', 'TemplateManager', 'FileTextOutlined', 0, 4, 'MENU');

INSERT INTO sp_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO sp_user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO sp_role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO sp_role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO sp_role_permission (role_id, permission_id) VALUES (2, 1);

INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 1);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 2);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 3);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 4);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (2, 1);

INSERT INTO sp_category (name, description) VALUES ('牙刷', '牙刷类别');
INSERT INTO sp_category (name, description) VALUES ('牙膏', '牙膏类别');

INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商A', '牙刷', 0);
INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商B', '牙膏', 0);

INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('category', '牙刷', '牙刷', '牙刷类别', 1);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('category', '牙膏', '牙膏', '牙膏类别', 2);

INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('status', 'DRAFT', '草稿', '草稿状态', 1);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('status', 'LOCKED', '已提交', '已提交状态', 2);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('status', 'SUBMITTED', '已提交', '部门打分已提交', 3);

INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('grade', 'A', 'A级', 'A级供应商', 1);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('grade', 'B', 'B级', 'B级供应商', 2);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('grade', 'C', 'C级', 'C级供应商', 3);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('grade', 'D', 'D级', 'D级供应商', 4);

INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('department', '计划', '计划', '计划部门', 1);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('department', '采购', '采购', '采购部门', 2);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('department', '质量', '质量', '质量部门', 3);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('department', '包开', '包开', '包材开发部门', 4);

INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('is_disabled', '0', '启用', '启用状态', 1);
INSERT INTO sp_dict (`item`, `key`, `value`, description, sort_order) VALUES ('is_disabled', '1', '禁用', '禁用状态', 2);