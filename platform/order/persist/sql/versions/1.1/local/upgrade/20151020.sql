-- ----------------------------
-- Describe: inv_invoice_out_item_detail 添加进项票详情与订单详情关联表ID
-- Author: dq
-- Date: 10/21/2015 16:23
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_out_item_detail`   
  ADD COLUMN `invoice_orderitem_id` BIGINT(20) NOT NULL   COMMENT '进项票详情与订单详情关联表ID' AFTER `inv_invoice_in_detail_id`;
