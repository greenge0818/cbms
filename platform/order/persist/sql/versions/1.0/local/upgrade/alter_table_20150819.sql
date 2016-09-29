-- ----------------------------
-- Describe: 客户公司账目划转详情增加现金发生余额和现金当前余额字段
-- Author: kongbinheng
-- Date: 08/19/2015 15:40
-- ----------------------------
ALTER TABLE `steel_cbms`.`cust_account_trans_log`
ADD COLUMN `cash_happen_balance`  decimal(18,6) NULL DEFAULT 0.000000 COMMENT '现金发生余额' AFTER `association_type`,
ADD COLUMN `cash_current_balance`  decimal(18,6) NULL DEFAULT 0.000000 COMMENT '现金当前余额' AFTER `cash_happen_balance`,
ADD COLUMN `status` int NULL DEFAULT 1 COMMENT '0未显示1显示' AFTER `cash_current_balance`;

ALTER TABLE cust_account_trans_log MODIFY pay_type ENUM('balance','settlement','freeze') DEFAULT NULL COMMENT '支付类型1.余额(balance)2.结算(settlement)3.冻结(freeze)';