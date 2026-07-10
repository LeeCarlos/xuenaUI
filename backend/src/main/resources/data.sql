INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES ('admin', '$2a$10$.D/1FzKoZGAV.qv7dT.98OwxKDTqM5abFpREzvAluI/Hbjgu2dW42', '计划', '管理员', 'admin@xuena.com', 1);
INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES ('user1', '$2a$10$VLvJ8LD5L23Wt2T0uxVXBO1Su1hXusdRS8H/dRnj7wbXXfUP6m/gi', '质量', '张三', 'user1@xuena.com', 1);

INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES ('管理员', 'admin', '系统管理员', 1, 1);
INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES ('普通用户', 'user', '普通用户', 1, 1);

INSERT INTO sp_permission (name, code, module, type, description) VALUES ('供应商管理', 'supplier:manage', '供应商', 'FUNCTION', '供应商管理权限');
INSERT INTO sp_permission (name, code, module, type, description) VALUES ('考核管理', 'assessment:manage', '考核', 'FUNCTION', '考核管理权限');

INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('供应商管理', '/supplier', 'Supplier', 'User', 0, 1, 'MENU');
INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type) VALUES ('考核管理', '/assessment', 'Assessment', 'BarChart', 0, 2, 'MENU');

INSERT INTO sp_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO sp_user_role (user_id, role_id) VALUES (2, 2);

INSERT INTO sp_role_permission (role_id, permission_id) VALUES (1, 1);
INSERT INTO sp_role_permission (role_id, permission_id) VALUES (1, 2);
INSERT INTO sp_role_permission (role_id, permission_id) VALUES (2, 1);

INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 1);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (1, 2);
INSERT INTO sp_role_menu (role_id, menu_id) VALUES (2, 1);

INSERT INTO sp_category (name, description) VALUES ('牙刷', '牙刷类别');
INSERT INTO sp_category (name, description) VALUES ('牙膏', '牙膏类别');

INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商A', '牙刷', 0);
INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商B', '牙膏', 0);