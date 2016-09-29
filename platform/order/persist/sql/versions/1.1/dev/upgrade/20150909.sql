-- ----------------------------
-- Describe: 为订单明细表添加已开票重量
-- Author: zhoukun
-- Date: 2015-09-09 19:54:22
-- ----------------------------

ALTER TABLE `busi_consign_order_items`
ADD COLUMN `invoice_weight`  decimal(18,6) NULL DEFAULT 0 COMMENT '已开票重量' AFTER `invoice_amount`;

