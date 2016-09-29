-- ----------------------------
-- Describe: 提现记录表添加记录
-- Author: caochao
-- Date: 09/09/2015 20:25
-- ----------------------------
ALTER TABLE `report_withdraw_record`
ADD COLUMN `account_id` bigint(100) NULL DEFAULT 0 COMMENT '公司ID' AFTER `withdraw_date`;

