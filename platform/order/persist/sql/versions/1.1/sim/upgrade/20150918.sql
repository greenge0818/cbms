-- ----------------------------
-- Describe: 修改新增客户提成表
-- 把外键类型都改成bigint
-- Author: Rabbit
-- Date: 2015-9-17 11:35:38
-- ----------------------------
ALTER TABLE `steel_cbms`.`report_new_user_reward`
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT  ,
CHANGE COLUMN `manager_id` `manager_id` BIGINT(20) NULL COMMENT '交易员ID' ,
CHANGE COLUMN `org_id` `org_id` BIGINT(20) NULL DEFAULT NULL COMMENT '组织ID' ,
CHANGE COLUMN `add_new_buyer` `add_new_buyer` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '新增买家id' ,
CHANGE COLUMN `add_new_seller` `add_new_seller` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '新增卖家id' ;