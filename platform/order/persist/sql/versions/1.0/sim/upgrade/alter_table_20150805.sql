-- ----------------------------
-- Describe: 林中提供
-- Author: Rolyer Luo
-- Date: 08/05/2015 19:43
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_refund_request`
ADD COLUMN `bank_code` VARCHAR(45) NULL COMMENT '银行行号' AFTER `receiver_account_code`;

