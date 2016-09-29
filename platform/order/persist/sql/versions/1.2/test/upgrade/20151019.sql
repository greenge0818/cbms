-- ----------------------------
-- Describe: 增加订单表合同关联金额字段
-- Author: dengxiyan
-- Date: 2015-10-19 16:16:18
-- ----------------------------
ALTER TABLE `busi_consign_order`
ADD COLUMN `total_contract_releted_amount`  DECIMAL(18,6) NULL COMMENT '合同关联金额（买家付款金额)' AFTER `total_amount`;

-- ---------------------------------------------------
-- Describe: 订单历史数据合同关联金额初始化：
-- 将已做了关联操作的订单数据的合同关联金额改为订单金额
-- Author: dengxiyan
-- Date: 2015-10-19 16:50:18
-- ---------------------------------------------------
UPDATE busi_consign_order
SET total_contract_releted_amount = `total_amount`
WHERE STATUS IN (4,5,6,7,8,10) ; -- 已关联合同后的数据：已关联、待二次结算、待开票申请、待开票、交易完成