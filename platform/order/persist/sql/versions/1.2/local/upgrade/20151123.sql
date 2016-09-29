
-- ----------------------------
-- Describe: 银票0.1.1开发
-- Author: Rabbit
-- Date: 2015年11月23日10:24:12
-- ----------------------------
ALTER TABLE `busi_accept_draft`
ADD COLUMN `reason` VARCHAR(45) NULL COMMENT '理由' AFTER `status`;

ALTER TABLE `busi_accept_draft` 
ADD COLUMN `org_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '服务中心id' AFTER `account_name`;

ALTER TABLE `busi_accept_draft` 
ADD COLUMN `org_name` VARCHAR(45) NULL COMMENT '服务中心名称' AFTER `org_id`;
