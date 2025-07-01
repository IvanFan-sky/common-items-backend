-- ==========================================
-- 通用管理系统数据库初始化脚本 - DML
-- 版本: v1.0
-- 创建时间: 2025-01-07
-- 说明: Stage 1 核心基础功能初始化数据
-- ==========================================

USE `common_items_db`;




-- ==========================================
-- 2. 用户表基础数据
-- ==========================================
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `avatar`, `gender`, `status`, `remark`, `create_by`, `update_by`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSOnIggJAVqGOsKdnP8Ss6gGsOe/p2EjO3mFjnhFUJB2', '超级管理员', 'admin@sparktech.com', '13800138000', NULL, 1, 1, '系统默认超级管理员', 1, 1),
(2, 'sparkfan', '$2a$10$7JB720yubVSOnIggJAVqGOsKdnP8Ss6gGsOe/p2EjO3mFjnhFUJB2', 'SparkFan', 'sparkfan@sparktech.com', '13800138001', NULL, 1, 1, '系统开发者', 1, 1),
(3, 'testuser', '$2a$10$7JB720yubVSOnIggJAVqGOsKdnP8Ss6gGsOe/p2EjO3mFjnhFUJB2', '测试用户', 'test@sparktech.com', '13800138999', NULL, 0, 1, '系统测试用户', 1, 1);

-- ==========================================
-- 3. 角色表基础数据
-- ==========================================
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `role_sort`, `data_scope`, `status`, `remark`, `create_by`, `update_by`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', 0, 1, 1, '超级管理员，拥有系统所有权限', 1, 1),
(2, '系统管理员', 'ADMIN', 1, 2, 1, '系统管理员，拥有大部分管理权限', 1, 1),

(4, '普通用户', 'USER', 3, 5, 1, '普通用户，基础查看权限', 1, 1),
(5, '开发人员', 'DEVELOPER', 4, 4, 1, '开发人员角色，技术相关权限', 1, 1),
(6, '测试人员', 'TESTER', 5, 4, 1, '测试人员角色，测试相关权限', 1, 1);

-- ==========================================
-- 4. 菜单表基础数据
-- ==========================================
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `permission`, `icon`, `order_num`, `status`, `visible`, `remark`, `create_by`, `update_by`) VALUES
-- 一级菜单
(1, 0, '系统管理', 1, '/system', NULL, NULL, 'system', 1, 1, 1, '系统管理目录', 1, 1),
(2, 0, '用户管理', 1, '/user', NULL, NULL, 'user', 2, 1, 1, '用户管理目录', 1, 1),
(3, 0, '监控管理', 1, '/monitor', NULL, NULL, 'monitor', 3, 1, 1, '监控管理目录', 1, 1),
(4, 0, '工具管理', 1, '/tool', NULL, NULL, 'tool', 4, 1, 1, '工具管理目录', 1, 1),

-- 系统管理子菜单
(101, 1, '用户管理', 2, '/system/user', 'system/user/index', 'system:user:list', 'user', 1, 1, 1, '用户管理菜单', 1, 1),
(102, 1, '角色管理', 2, '/system/role', 'system/role/index', 'system:role:list', 'peoples', 2, 1, 1, '角色管理菜单', 1, 1),
(103, 1, '菜单管理', 2, '/system/menu', 'system/menu/index', 'system:menu:list', 'tree-table', 3, 1, 1, '菜单管理菜单', 1, 1),

(105, 1, '字典管理', 2, '/system/dict', 'system/dict/index', 'system:dict:list', 'dict', 5, 1, 1, '字典管理菜单', 1, 1),
(106, 1, '参数设置', 2, '/system/config', 'system/config/index', 'system:config:list', 'edit', 6, 1, 1, '参数设置菜单', 1, 1),

-- 用户管理按钮
(1001, 101, '用户查询', 3, '', '', 'system:user:query', '#', 1, 1, 1, '', 1, 1),
(1002, 101, '用户新增', 3, '', '', 'system:user:add', '#', 2, 1, 1, '', 1, 1),
(1003, 101, '用户修改', 3, '', '', 'system:user:edit', '#', 3, 1, 1, '', 1, 1),
(1004, 101, '用户删除', 3, '', '', 'system:user:remove', '#', 4, 1, 1, '', 1, 1),
(1005, 101, '用户导出', 3, '', '', 'system:user:export', '#', 5, 1, 1, '', 1, 1),
(1006, 101, '用户导入', 3, '', '', 'system:user:import', '#', 6, 1, 1, '', 1, 1),
(1007, 101, '重置密码', 3, '', '', 'system:user:resetPwd', '#', 7, 1, 1, '', 1, 1),

-- 角色管理按钮
(1008, 102, '角色查询', 3, '', '', 'system:role:query', '#', 1, 1, 1, '', 1, 1),
(1009, 102, '角色新增', 3, '', '', 'system:role:add', '#', 2, 1, 1, '', 1, 1),
(1010, 102, '角色修改', 3, '', '', 'system:role:edit', '#', 3, 1, 1, '', 1, 1),
(1011, 102, '角色删除', 3, '', '', 'system:role:remove', '#', 4, 1, 1, '', 1, 1),
(1012, 102, '角色导出', 3, '', '', 'system:role:export', '#', 5, 1, 1, '', 1, 1),

-- 菜单管理按钮
(1013, 103, '菜单查询', 3, '', '', 'system:menu:query', '#', 1, 1, 1, '', 1, 1),
(1014, 103, '菜单新增', 3, '', '', 'system:menu:add', '#', 2, 1, 1, '', 1, 1),
(1015, 103, '菜单修改', 3, '', '', 'system:menu:edit', '#', 3, 1, 1, '', 1, 1),
(1016, 103, '菜单删除', 3, '', '', 'system:menu:remove', '#', 4, 1, 1, '', 1, 1),



-- 字典管理按钮
(1021, 105, '字典查询', 3, '', '', 'system:dict:query', '#', 1, 1, 1, '', 1, 1),
(1022, 105, '字典新增', 3, '', '', 'system:dict:add', '#', 2, 1, 1, '', 1, 1),
(1023, 105, '字典修改', 3, '', '', 'system:dict:edit', '#', 3, 1, 1, '', 1, 1),
(1024, 105, '字典删除', 3, '', '', 'system:dict:remove', '#', 4, 1, 1, '', 1, 1),
(1025, 105, '字典导出', 3, '', '', 'system:dict:export', '#', 5, 1, 1, '', 1, 1),

-- 参数设置按钮
(1026, 106, '参数查询', 3, '', '', 'system:config:query', '#', 1, 1, 1, '', 1, 1),
(1027, 106, '参数新增', 3, '', '', 'system:config:add', '#', 2, 1, 1, '', 1, 1),
(1028, 106, '参数修改', 3, '', '', 'system:config:edit', '#', 3, 1, 1, '', 1, 1),
(1029, 106, '参数删除', 3, '', '', 'system:config:remove', '#', 4, 1, 1, '', 1, 1),
(1030, 106, '参数导出', 3, '', '', 'system:config:export', '#', 5, 1, 1, '', 1, 1);

-- ==========================================
-- 5. 权限表基础数据(基于菜单)
-- ==========================================
INSERT INTO `sys_permission` (`id`, `parent_id`, `permission_name`, `permission_code`, `permission_type`, `path`, `component`, `icon`, `order_num`, `status`, `visible`, `remark`, `create_by`, `update_by`) VALUES
-- 系统管理权限
(1, 0, '系统管理', 'system', 1, '/system', NULL, 'system', 1, 1, 1, '系统管理权限', 1, 1),
(2, 1, '用户管理', 'system:user', 1, '/system/user', 'system/user/index', 'user', 1, 1, 1, '用户管理权限', 1, 1),
(3, 1, '角色管理', 'system:role', 1, '/system/role', 'system/role/index', 'peoples', 2, 1, 1, '角色管理权限', 1, 1),
(4, 1, '权限管理', 'system:permission', 1, '/system/permission', 'system/permission/index', 'lock', 3, 1, 1, '权限管理权限', 1, 1),


-- 用户管理操作权限
(21, 2, '用户查询', 'system:user:query', 2, '', '', '', 1, 1, 0, '用户查询权限', 1, 1),
(22, 2, '用户新增', 'system:user:add', 2, '', '', '', 2, 1, 0, '用户新增权限', 1, 1),
(23, 2, '用户修改', 'system:user:edit', 2, '', '', '', 3, 1, 0, '用户修改权限', 1, 1),
(24, 2, '用户删除', 'system:user:remove', 2, '', '', '', 4, 1, 0, '用户删除权限', 1, 1),

-- 角色管理操作权限
(31, 3, '角色查询', 'system:role:query', 2, '', '', '', 1, 1, 0, '角色查询权限', 1, 1),
(32, 3, '角色新增', 'system:role:add', 2, '', '', '', 2, 1, 0, '角色新增权限', 1, 1),
(33, 3, '角色修改', 'system:role:edit', 2, '', '', '', 3, 1, 0, '角色修改权限', 1, 1),
(34, 3, '角色删除', 'system:role:remove', 2, '', '', '', 4, 1, 0, '角色删除权限', 1, 1),



-- ==========================================
-- 6. 用户角色关联数据
-- ==========================================
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_by`) VALUES
(1, 1, 1, 1),  -- admin用户 -> 超级管理员角色
(2, 2, 5, 1),  -- sparkfan用户 -> 开发人员角色
(3, 3, 4, 1);  -- testuser用户 -> 普通用户角色

-- ==========================================
-- 7. 角色权限关联数据
-- ==========================================
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `create_by`) VALUES
-- 超级管理员拥有所有权限
(1, 1, 1, 1), (2, 1, 2, 1), (3, 1, 3, 1), (4, 1, 4, 1),
(6, 1, 21, 1), (7, 1, 22, 1), (8, 1, 23, 1), (9, 1, 24, 1),
(10, 1, 31, 1), (11, 1, 32, 1), (12, 1, 33, 1), (13, 1, 34, 1),

-- 开发人员拥有查询权限
(21, 5, 1, 1), (22, 5, 2, 1), (23, 5, 3, 1),
(25, 5, 21, 1), (26, 5, 31, 1),

-- 普通用户只有基础查询权限
(31, 4, 1, 1), (32, 4, 2, 1), (33, 4, 21, 1);

-- ==========================================
-- 8. 字典类型基础数据
-- ==========================================
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `update_by`) VALUES
(1, '用户性别', 'sys_user_sex', 1, '用户性别列表', 1, 1),
(2, '菜单状态', 'sys_show_hide', 1, '菜单状态列表', 1, 1),
(3, '系统开关', 'sys_normal_disable', 1, '系统开关列表', 1, 1),
(4, '任务状态', 'sys_job_status', 1, '任务状态列表', 1, 1),
(5, '任务分组', 'sys_job_group', 1, '任务分组列表', 1, 1),
(6, '系统状态', 'sys_common_status', 1, '登录状态列表', 1, 1),
(7, '通知类型', 'sys_notice_type', 1, '通知类型列表', 1, 1),
(8, '通知状态', 'sys_notice_status', 1, '通知状态列表', 1, 1),
(9, '操作类型', 'sys_oper_type', 1, '操作类型列表', 1, 1),
(10, '数据范围', 'sys_data_scope', 1, '数据范围列表', 1, 1),
(11, '社交平台类型', 'sys_social_type', 1, '第三方社交平台类型列表', 1, 1),
(12, '社交绑定状态', 'sys_social_status', 1, '社交账号绑定状态列表', 1, 1);

-- ==========================================
-- 9. 字典数据基础数据
-- ==========================================
INSERT INTO `sys_dict_data` (`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `remark`, `create_by`, `update_by`) VALUES
-- 用户性别
(1, 1, '男', '1', 'sys_user_sex', '', '', 1, 1, '性别男', 1, 1),
(2, 2, '女', '2', 'sys_user_sex', '', '', 0, 1, '性别女', 1, 1),
(3, 3, '未知', '0', 'sys_user_sex', '', '', 0, 1, '性别未知', 1, 1),

-- 菜单状态
(4, 1, '显示', '1', 'sys_show_hide', '', 'primary', 1, 1, '显示菜单', 1, 1),
(5, 2, '隐藏', '0', 'sys_show_hide', '', 'danger', 0, 1, '隐藏菜单', 1, 1),

-- 系统开关
(6, 1, '正常', '1', 'sys_normal_disable', '', 'primary', 1, 1, '正常状态', 1, 1),
(7, 2, '停用', '0', 'sys_normal_disable', '', 'danger', 0, 1, '停用状态', 1, 1),

-- 系统状态
(8, 1, '成功', '1', 'sys_common_status', '', 'primary', 1, 1, '正常状态', 1, 1),
(9, 2, '失败', '0', 'sys_common_status', '', 'danger', 0, 1, '停用状态', 1, 1),

-- 数据范围
(10, 1, '全部数据权限', '1', 'sys_data_scope', '', '', 1, 1, '全部数据权限', 1, 1),
(11, 2, '自定义数据权限', '2', 'sys_data_scope', '', '', 0, 1, '自定义数据权限', 1, 1),

(14, 5, '仅本人数据权限', '5', 'sys_data_scope', '', '', 0, 1, '仅本人数据权限', 1, 1),

-- 社交平台类型
(15, 1, '微信登录', 'wechat', 'sys_social_type', '', 'success', 1, 1, '微信第三方登录', 1, 1),
(16, 2, 'QQ登录', 'qq', 'sys_social_type', '', 'primary', 0, 1, 'QQ第三方登录', 1, 1),
(17, 3, '微博登录', 'weibo', 'sys_social_type', '', 'warning', 0, 1, '微博第三方登录', 1, 1),
(18, 4, '支付宝登录', 'alipay', 'sys_social_type', '', 'info', 0, 1, '支付宝第三方登录', 1, 1),
(19, 5, 'GitHub登录', 'github', 'sys_social_type', '', 'dark', 0, 1, 'GitHub第三方登录', 1, 1),
(20, 6, 'Gitee登录', 'gitee', 'sys_social_type', '', 'danger', 0, 1, 'Gitee第三方登录', 1, 1),
(21, 7, '钉钉登录', 'dingtalk', 'sys_social_type', '', 'cyan', 0, 1, '钉钉第三方登录', 1, 1),

-- 社交绑定状态
(22, 1, '已绑定', '1', 'sys_social_status', '', 'success', 1, 1, '社交账号已绑定', 1, 1),
(23, 2, '已解绑', '0', 'sys_social_status', '', 'danger', 0, 1, '社交账号已解绑', 1, 1);

-- ==========================================
-- 10. 系统配置基础数据
-- ==========================================
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_by`, `update_by`) VALUES
(1, '主框架页面-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 1, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 1, 1),
(2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 1, '初始化密码 123456', 1, 1),
(3, '主框架页面-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 1, '深色主题theme-dark，浅色主题theme-light', 1, 1),
(4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 1, '是否开启验证码功能（true开启，false关闭）', 1, 1),
(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 1, '是否开启注册用户功能（true开启，false关闭）', 1, 1),
(6, '文件上传-文件大小限制', 'sys.upload.maxSize', '50', 1, '文件上传大小限制（默认50MB）', 1, 1),
(7, '文件上传-文件类型限制', 'sys.upload.allowTypes', 'doc,docx,zip,pdf,txt,xlsx,xls,jpg,jpeg,png,gif', 1, '允许上传的文件类型', 1, 1),
(8, '系统版本号', 'sys.version', 'v1.0.0', 1, '当前系统版本号', 1, 1),
(9, '系统名称', 'sys.name', '通用管理系统', 1, '系统名称', 1, 1),
(10, '系统作者', 'sys.author', 'SparkFan', 1, '系统作者', 1, 1);

-- ==========================================
-- 脚本执行完成提示
-- ==========================================
SELECT 'Database data initialization completed successfully!' AS status;
SELECT '默认管理员账号: admin, 密码: 123456' AS admin_info;
SELECT '默认开发者账号: sparkfan, 密码: 123456' AS developer_info;
SELECT '默认测试账号: testuser, 密码: 123456' AS test_info; 