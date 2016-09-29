-- ----------------------------
-- Describe: 增加折让金额字段
-- Author: lichaowei
-- Date: 2015-12-09 19:33:10
ALTER TABLE `steel_cbms`.`inv_invoice_in_detail_orderitem`
  ADD COLUMN `allowance_amount` DECIMAL(18,6) DEFAULT 0.000000  NULL   COMMENT '折让金额' AFTER `amount`;
