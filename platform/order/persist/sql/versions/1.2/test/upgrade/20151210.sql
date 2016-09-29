
-- ----------------------------
-- Describe: 设置代运营服务中心日报执行时间节点
-- Author: lixiang
-- Date: 2015-12-10 16:16:18
ALTER TABLE `base_sys_setting`
ADD COLUMN `report_org_day`  bit(1) NULL DEFAULT b'1' COMMENT '是否继续执行Job 0 \"否\" 1\"是\" ' AFTER `sequence`;

insert into base_sys_setting(`setting_type`, `setting_name`, `setting_value`, `default_value`, `description`, `display_name`, `created_by`, `created`, `last_updated_by`, `last_updated`)
values('ReportOrgDay', '代运营服务中心日报执行时间节点', '18:00', '', '用于系统设置代运营服务中心日报可以配置执行时间点及是否继续执行。', '上次配置执行时间', 'lixiang', now(), 'lixiang', now());
