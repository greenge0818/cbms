-- ----------------------------
-- Describe: 曹操
-- Author: Rolyer Luo
-- Date: 08/07/2015 11:31
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order_contract`
CHANGE COLUMN `bank_name_main` `bank_name_main` VARCHAR(45) NULL COMMENT '开户银行主行 如 工商银行' ,
CHANGE COLUMN `bank_account_code` `bank_account_code` VARCHAR(45) NULL COMMENT '银行账号' ,
CHANGE COLUMN `customer_name` `customer_name` VARCHAR(45) NULL COMMENT '客户单位名称' ,
CHANGE COLUMN `customer_tel` `customer_tel` VARCHAR(45) NULL COMMENT '客户联系电话' ;

-- ----------------------------
-- Describe: 银企直销银行流水记账明细增加处理状态字段
-- Author: kongbinheng
-- Date: 08/07/2015 10:30
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_bank_transaction_original_detail`
ADD COLUMN `status` int NULL DEFAULT '0' COMMENT '0未处理1处理完2处理中' AFTER `begin_date`;
