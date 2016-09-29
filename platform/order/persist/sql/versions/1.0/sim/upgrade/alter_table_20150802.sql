-- ----------------------------
-- Describe: 删除bank_id字段
-- Author: Rolyer Luo
-- Date: 08/02/2015 11:43
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_pay_request`
DROP COLUMN `bank_id`;

-- ----------------------------
-- Describe: 修正单词拼写错误
-- Author: Rolyer Luo
-- Date: 08/02/2015 11:46
-- ----------------------------
ALTER TABLE `steel_cbms`.`cust_account_bank`
CHANGE COLUMN `bank_provincie_id` `bank_province_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '银行所在省' ;
