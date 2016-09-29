-- ----------------------------
-- Describe: 增加订单表给卖家支付方式、延时支付天数，是否计算业绩字段
-- Author: wangxianjun
-- Date: 2015-12-18 09:11:18
-- ----------------------------
ALTER TABLE `busi_consign_order`
ADD COLUMN `payment_type`  varchar(1) NULL default '0' COMMENT '支付方式（给卖家的付款方式)' AFTER `parent_row_id`;
ALTER TABLE `busi_consign_order`
ADD COLUMN `delay_num`  int(3) NULL default 0 COMMENT '延时支付天数（给卖家付款)' AFTER `payment_type`;
ALTER TABLE `busi_consign_order`
ADD COLUMN `is_count_achievement`  varchar(1) NULL default '0' COMMENT '是否计算业绩字段' AFTER `delay_num`;
