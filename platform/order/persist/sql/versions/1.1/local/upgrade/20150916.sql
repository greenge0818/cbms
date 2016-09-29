-- ----------------------------
-- Describe: 订单明细表为invoice_amount字段设置默认值为0
-- 并将以前的为Null的数据更新成0
-- Author: zhoukun
-- Date: 2015-09-16 10:54:22
-- ----------------------------
UPDATE busi_consign_order_items SET invoice_amount=0 WHERE invoice_amount IS NULL;
ALTER TABLE `busi_consign_order_items`
MODIFY COLUMN `invoice_amount`  decimal(18,6) NULL DEFAULT 0 COMMENT '已开票金额（可以一次开部分金额）' AFTER `amount`;