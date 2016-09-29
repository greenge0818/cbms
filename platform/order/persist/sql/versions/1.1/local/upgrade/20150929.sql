-- ----------------------------
-- Describe: inv_invoice_out_checklist_detail添加开票金额与开票重量
-- Author: lichaowei
-- Date: 09/29/2015 11:40
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_out_checklist_detail`
  ADD COLUMN `amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '清单开票金额' AFTER `item_detail_id`,
  ADD COLUMN `weight` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '清单开票重量' AFTER `amount`;
