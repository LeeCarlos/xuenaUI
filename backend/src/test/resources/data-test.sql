INSERT INTO sp_category (name, description) VALUES ('牙刷', '牙刷类别');
INSERT INTO sp_category (name, description) VALUES ('牙膏', '牙膏类别');

INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商A', '牙刷', 0);
INSERT INTO sp_supplier_pool (name, category, is_disabled) VALUES ('供应商B', '牙膏', 0);

INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '采购', '管理员', 'admin@test.com', 1);

INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES ('超级管理员', 'admin', '超级管理员角色', 1, 1);

INSERT INTO sp_permission (name, code, module, type, description) VALUES ('供应商池查看', 'pool:view', '供应商', 'menu', '查看供应商池');

INSERT INTO sp_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO sp_role_permission (role_id, permission_id) VALUES (1, 1);