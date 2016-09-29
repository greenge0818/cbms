-- ----------------------------
-- Describe: 用户行为日志记录添加字段
-- Author: zhoukun
-- Date: 08/20/2015 16:25
-- ----------------------------
ALTER TABLE `base_system_opration_log`
CHANGE COLUMN `url` `operation_key`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型的key' AFTER `operator_name`,
CHANGE COLUMN `method` `operation_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作名称' AFTER `operation_key`,
ADD COLUMN `operation_level`  varchar(100) NOT NULL COMMENT '操作级别：Safe,Warning,Dangerous,Damning' AFTER `operation_name`,
ADD COLUMN `operation_level_value`  int NULL COMMENT '操作等级的数字值，值越高表示越危险，用于按危险等级排序' AFTER `operation_level`;

