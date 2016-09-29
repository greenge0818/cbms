-- ----------------------------
-- Describe: 新增receiver_bank_code字段
-- Author: YuanHong
-- Date: 08/11/2015 21:50
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_pay_request_items` 
ADD COLUMN `receiver_bank_code` VARCHAR(45) NULL COMMENT '收款单位银行行号' AFTER `receiver_name`;

