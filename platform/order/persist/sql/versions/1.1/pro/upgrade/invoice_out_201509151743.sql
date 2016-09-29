-- ----------------------------
-- Describe: 添加is_deleted字段
-- Author: Rolyer Luo
-- Date: 09/15/2015 17:05
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_out_apply`
ADD COLUMN `is_deleted` TINYINT NULL DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。' AFTER `comment`;

ALTER TABLE `steel_cbms`.`inv_invoice_out_apply_detail`
ADD COLUMN `is_deleted` TINYINT NULL DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。' AFTER `lend_amount`;

ALTER TABLE `steel_cbms`.`inv_invoice_out_item_detail`
ADD COLUMN `is_deleted` TINYINT NULL DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。' AFTER `status`;
