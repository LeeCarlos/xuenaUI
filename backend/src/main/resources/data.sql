INSERT INTO sp_role (name, code, description, is_system, is_enabled) VALUES
('超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1, 1),
('管理员', 'ADMIN', '拥有大部分管理权限', 1, 1),
('质量部管理员', 'QUALITY_ADMIN', '质量部打分管理权限', 1, 1),
('计划部管理员', 'PLANNING_ADMIN', '计划部打分管理权限', 1, 1),
('包开部管理员', 'PACKAGING_ADMIN', '包开部打分管理权限', 1, 1),
('采购部管理员', 'PROCUREMENT_ADMIN', '采购部打分管理权限', 1, 1),
('普通用户', 'USER', '仅查看权限', 1, 1);

INSERT INTO sp_menu (name, path, component, icon, parent_id, sort_order, type, permission_code, is_visible) VALUES
('总览趋势', '/dashboard', 'pages/Dashboard', 'dashboard', 0, 1, 'MENU', 'overview:view', 1),
('维度对比', '/compare', 'pages/Compare', 'bar-chart', 0, 2, 'MENU', 'compare:view', 1),
('排名变动', '/rank', 'pages/Rank', 'trending-up', 0, 3, 'MENU', 'rank:view', 1),
('明细数据', '/detail', 'pages/Detail', 'file-text', 0, 4, 'MENU', 'detail:view', 1),
('供应商管理', NULL, NULL, 'user', 0, 5, 'DIRECTORY', NULL, 1),
('供应商池', '/pool', 'pages/Pool', 'database', 5, 1, 'MENU', 'pool:list', 1),
('手动打分', '/scoring', 'pages/Scoring', 'edit', 0, 6, 'MENU', 'scoring:view', 1),
('系统管理', NULL, NULL, 'setting', 0, 7, 'DIRECTORY', NULL, 1),
('用户管理', '/system/user', 'pages/System/User', 'user', 8, 1, 'MENU', 'user:list', 1),
('角色管理', '/system/role', 'pages/System/Role', 'folder', 8, 2, 'MENU', 'role:list', 1),
('权限管理', '/system/permission', 'pages/System/Permission', 'key', 8, 3, 'MENU', 'permission:list', 1),
('菜单管理', '/system/menu', 'pages/System/Menu', 'setting', 8, 4, 'MENU', 'menu:list', 1);

INSERT INTO sp_permission (name, code, module, type, description, parent_id, sort_order) VALUES
('总览趋势-查看', 'overview:view', 'overview', 'FUNCTION', '查看总览趋势', 0, 1),
('维度对比-查看', 'compare:view', 'compare', 'FUNCTION', '查看维度对比', 0, 2),
('排名变动-查看', 'rank:view', 'rank', 'FUNCTION', '查看排名变动', 0, 3),
('明细数据-查看', 'detail:view', 'detail', 'FUNCTION', '查看明细数据', 0, 4),
('供应商池-列表', 'pool:list', 'pool', 'FUNCTION', '查看供应商池列表', 0, 5),
('供应商池-新增', 'pool:add', 'pool', 'FUNCTION', '新增供应商', 0, 6),
('供应商池-编辑', 'pool:edit', 'pool', 'FUNCTION', '编辑供应商', 0, 7),
('供应商池-删除', 'pool:delete', 'pool', 'FUNCTION', '删除供应商', 0, 8),
('手动打分-查看', 'scoring:view', 'scoring', 'FUNCTION', '查看手动打分页面', 0, 9),
('手动打分-上传', 'scoring:upload', 'scoring', 'FUNCTION', '上传打分文件', 0, 10),
('手动打分-提交', 'scoring:submit', 'scoring', 'FUNCTION', '提交打分数据', 0, 11),
('用户管理-列表', 'user:list', 'system', 'FUNCTION', '查看用户列表', 0, 12),
('用户管理-新增', 'user:add', 'system', 'FUNCTION', '新增用户', 0, 13),
('用户管理-编辑', 'user:edit', 'system', 'FUNCTION', '编辑用户', 0, 14),
('用户管理-删除', 'user:delete', 'system', 'FUNCTION', '删除用户', 0, 15),
('用户管理-分配角色', 'user:assign-role', 'system', 'FUNCTION', '分配角色给用户', 0, 16),
('角色管理-列表', 'role:list', 'system', 'FUNCTION', '查看角色列表', 0, 17),
('角色管理-新增', 'role:add', 'system', 'FUNCTION', '新增角色', 0, 18),
('角色管理-编辑', 'role:edit', 'system', 'FUNCTION', '编辑角色', 0, 19),
('角色管理-删除', 'role:delete', 'system', 'FUNCTION', '删除角色', 0, 20),
('角色管理-分配权限', 'role:assign-permission', 'system', 'FUNCTION', '分配权限给角色', 0, 21),
('角色管理-分配菜单', 'role:assign-menu', 'system', 'FUNCTION', '分配菜单给角色', 0, 22),
('权限管理-列表', 'permission:list', 'system', 'FUNCTION', '查看权限列表', 0, 23),
('权限管理-新增', 'permission:add', 'system', 'FUNCTION', '新增权限', 0, 24),
('权限管理-编辑', 'permission:edit', 'system', 'FUNCTION', '编辑权限', 0, 25),
('权限管理-删除', 'permission:delete', 'system', 'FUNCTION', '删除权限', 0, 26),
('菜单管理-列表', 'menu:list', 'system', 'FUNCTION', '查看菜单列表', 0, 27),
('菜单管理-新增', 'menu:add', 'system', 'FUNCTION', '新增菜单', 0, 28),
('菜单管理-编辑', 'menu:edit', 'system', 'FUNCTION', '编辑菜单', 0, 29),
('菜单管理-删除', 'menu:delete', 'system', 'FUNCTION', '删除菜单', 0, 30),
('数据上传-上传', 'upload:upload', 'upload', 'FUNCTION', '上传数据', 0, 31),
('数据上传-合并', 'upload:merge', 'upload', 'FUNCTION', '合并数据', 0, 32);

INSERT INTO sp_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 6), (4, 7),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), (5, 7),
(6, 1), (6, 2), (6, 3), (6, 4), (6, 5), (6, 6), (6, 7),
(7, 1), (7, 2), (7, 3), (7, 4), (7, 5), (7, 6);

INSERT INTO sp_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11),
(1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20), (1, 21),
(1, 22), (1, 23), (1, 24), (1, 25), (1, 26), (1, 27), (1, 28), (1, 29), (1, 30), (1, 31), (1, 32),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11),
(2, 12), (2, 13), (2, 14), (2, 15), (2, 16), (2, 17), (2, 18), (2, 19), (2, 20), (2, 21),
(2, 22), (2, 23), (2, 24), (2, 25), (2, 26), (2, 27), (2, 28), (2, 29), (2, 30), (2, 31), (2, 32),
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 9), (3, 10), (3, 11),
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 9), (4, 10), (4, 11),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 9), (5, 10), (5, 11),
(6, 1), (6, 2), (6, 3), (6, 4), (6, 5), (6, 9), (6, 10), (6, 11),
(7, 1), (7, 2), (7, 3), (7, 4), (7, 5);

INSERT INTO sp_user (username, password, department, real_name, email, is_enabled) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', '采购', '管理员', 'admin@example.com', 1);

INSERT INTO sp_user_role (user_id, role_id) VALUES
(1, 1);

INSERT INTO sp_category (name, description) VALUES
('牙刷', '牙刷类供应商'),
('包装', '包装材料供应商'),
('成品', '成品供应商'),
('配件', '配件供应商'),
('其他', '其他类别');

INSERT INTO sp_supplier_pool (name, category) VALUES
('供应商A', '牙刷'),
('供应商B', '牙刷'),
('供应商C', '包装'),
('供应商D', '成品'),
('供应商E', '配件'),
('供应商F', '牙刷'),
('供应商G', '包装'),
('供应商H', '成品');