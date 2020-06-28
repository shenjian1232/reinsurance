-- 添加批处理页面目录
INSERT INTO `reinsurance`.`sys_menu`(`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `depth`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (500, '11', '0,1,11', '任务服务管理', 5121, '/manage/task/index', NULL, 'fa-user-plus', 1, 'sysAdministrative:task:index', 3, NULL, '2019-06-26 15:05:49', NULL, NULL, NULL, 1);
INSERT INTO `reinsurance`.`sys_menu`(`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `depth`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (501, '500', NULL, '修改状态', 5122, '/manage/task/changeStatus', NULL, 'fa-edit', 1, 'sysAdministrative:task:changeStatus', 4, NULL, '2019-07-02 09:59:45', NULL, NULL, NULL, 1);
INSERT INTO `reinsurance`.`sys_menu`(`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `depth`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (502, '500', NULL, '新增', 5123, '/manage/task/add', NULL, 'fa-refresh', 1, 'sysAdministrative:task:add', 4, NULL, '2019-07-02 15:09:57', NULL, NULL, NULL, 1);
INSERT INTO `reinsurance`.`sys_menu`(`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `depth`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (503, '500', NULL, '修改', 5124, '/manage/task/update', NULL, 'fa-edit', 1, 'sysAdministrative:task:edit', 4, NULL, '2019-07-02 15:11:21', NULL, NULL, NULL, 1);
INSERT INTO `reinsurance`.`sys_menu`(`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `depth`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (504, '500', NULL, '删除', 5125, '/manage/task/del', NULL, 'fa-tash', 1, 'sysAdministrative:task:del', 4, NULL, '2019-07-02 15:42:10', NULL, NULL, NULL, 1);

-- 添加批处理页面目录权限
INSERT INTO `reinsurance`.`sys_role_menu`(`role_id`, `menu_id`) VALUES ('1', '500');
INSERT INTO `reinsurance`.`sys_role_menu`(`role_id`, `menu_id`) VALUES ('1', '501');
INSERT INTO `reinsurance`.`sys_role_menu`(`role_id`, `menu_id`) VALUES ('1', '502');
INSERT INTO `reinsurance`.`sys_role_menu`(`role_id`, `menu_id`) VALUES ('1', '503');
INSERT INTO `reinsurance`.`sys_role_menu`(`role_id`, `menu_id`) VALUES ('1', '504');

-- 批处理试例sql
INSERT INTO `reinsurance`.`task_info`(`Id`, `jobName`, `jobGroup`, `jobDescription`, `jobStatus`, `cronExpression`, `createTime`, `milliSeconds`, `repeatCount`, `startDate`, `endDate`, `del_flag`) VALUES (1, 'ReportMailBatch', 'task', '测试案例', '1', '0 0/2 * * * ?', 'Wed Jul 03 17:42:13 CST 2019', '60000', '10', '2019-06-11 00:00:00', '2019-12-31 00:00:00', '1');
INSERT INTO `reinsurance`.`task_info`(`Id`, `jobName`, `jobGroup`, `jobDescription`, `jobStatus`, `cronExpression`, `createTime`, `milliSeconds`, `repeatCount`, `startDate`, `endDate`, `del_flag`) VALUES (3, 'ScheduledTask', 'task', '测试', '0', '0 0/1 * * * ?', 'Wed Jul 03 17:44:40 CST 2019', '', '', '2019-05-06 00:00:00', '', '1');

