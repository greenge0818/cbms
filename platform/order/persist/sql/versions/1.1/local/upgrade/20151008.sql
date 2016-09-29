-- ----------------------------
-- Describe: busi_consign_order添加合同签订地字段
-- Author: lichaowei
-- Date: 10/08/2015 16:23
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order`   
  ADD COLUMN `contract_address` VARCHAR(255) NULL   COMMENT '合同签订地' AFTER `operator_name`;

ALTER TABLE `steel_cbms`.`base_organization`   
  ADD COLUMN `contract_address` VARCHAR(255) NULL   COMMENT '合同签订地' AFTER `credit_limit_used`;