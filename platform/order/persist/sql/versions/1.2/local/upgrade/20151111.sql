-- ----------------------------
-- Describe: 承兑汇票表
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
CREATE TABLE IF NOT EXISTS `busi_accept_draft` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NOT NULL COMMENT '充值账户id',
  `account_name` VARCHAR(255) NOT NULL COMMENT '充值账户名称',
  `code` VARCHAR(45) NOT NULL COMMENT '票号',
  `acceptance_bank_full_name` VARCHAR(255) NOT NULL COMMENT '承兑行全称',
  `amount` DECIMAL(18,6) NOT NULL COMMENT '金额',
  `end_date` DATETIME NOT NULL COMMENT '到期日',
  `discount_rate` DECIMAL(20,6) NOT NULL COMMENT '贴现率',
  `discount_rate_base` DECIMAL(20,6) NOT NULL COMMENT '贴现率成本',
  `acceptance_bank_full_name_code_approve` VARCHAR(255) NULL COMMENT '财务审核承兑行全称',
  `code_approve` VARCHAR(45) NULL COMMENT '财务审核票号',
  `amount_code_approve` DECIMAL(18,6) NULL COMMENT '财务审核金额',
  `status` VARCHAR(45) NOT NULL DEFAULT 'NEW' COMMENT '状态\nNEW 保存待提交\nSUBMITTED 已录入待审核\nCHARGED 审核（双敲）通过，已充值--双敲匹配失败回到待提交\nROLLBACKREQUEST 回滚申请\nRENEW 充值回滚已完成',
  `acceptance_date` DATETIME NULL COMMENT '承兑日',
  `acceptance_bank_code` VARCHAR(45) NULL COMMENT '承兑行行号',
  `drawer_name` VARCHAR(255) NULL COMMENT '出票人名称\n',
  `drawer_account_code` VARCHAR(45) NULL COMMENT '出票人账号',
  `drawer_bank_code` VARCHAR(45) NULL COMMENT '出票人开户行行号\n\n',
  `drawer_bank_full_name` VARCHAR(255) NULL COMMENT '出票人开户行全称\n',
  `receiver_name` VARCHAR(255) NULL COMMENT '收款人名称\n',
  `receiver_account_code` VARCHAR(45) NULL COMMENT '收款人账号',
  `receiver_bank_full_name` VARCHAR(255) NULL COMMENT '收款人开户行全称',
  `adjust_date_count` SMALLINT NULL COMMENT '调整天数',
  `read_times` TINYINT NULL COMMENT '背书次数',
  `acceptance_agreement_number` VARCHAR(45) NULL DEFAULT 0 COMMENT '承兑协议编号',
  `is_different_bank` BIT(1) NULL COMMENT '是否我行代开他行签发票据\n',
  `is_payed` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否已经用于支付货款 0 没有   1  有',
  `is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '逻辑删0 正常   1 删除',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT '0',
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uix_busi_delivery_bill_code` (`code` ASC)
 )
COMMENT = '承兑汇票';


-- ----------------------------
-- Describe: 承兑汇票附件表
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
CREATE TABLE IF NOT EXISTS `busi_accept_draft_attachment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `accept_draft_id` BIGINT(20) NOT NULL COMMENT '承兑汇票ID',
  `url` VARCHAR(100) NOT NULL COMMENT '附件地址',
  `type` VARCHAR(45) NOT NULL COMMENT '附件类型\nMAIN 正面\nTRANSFERLOG 粘单',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT '0',
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
)
COMMENT = '承兑汇票附件信息';


-- ----------------------------
-- Describe: 用户表增加是否使用过承兑汇票标记
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
ALTER TABLE `cust_account`
ADD COLUMN `is_accept_draft_charged` BIT(1) NULL DEFAULT 0 COMMENT '是否用过承兑汇票\n0 没用过     1   用过' AFTER `status`;


-- ----------------------------
-- Describe: 充值记录表增加申请类型和关联类型枚举
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
ALTER TABLE `cust_account_trans_log`
CHANGE COLUMN `apply_type` `apply_type` VARCHAR(30) NULL COMMENT '申请类型:\n1 充值到资金账户\n2 支付合同货款\n3 二次结算\n4 资金账户转出\n5 抵扣二次结算账户欠款\n6 二次结算账户余额转入资金账户\n7 抵扣二次结算账户欠款\n8 合同款到账\n9 二次结算账户余额转入资金账户\n10  承兑汇票充值\n11  承兑汇票回滚' ,
CHANGE COLUMN `association_type` `association_type` VARCHAR(30) NULL DEFAULT NULL COMMENT '关联类型：\n１付款单号\n２寄售交易单号\n３银行流水号\n4 还款流水号\n5 承兑汇票号' ;


-- ----------------------------
-- Describe: 订单明细表增加使用承兑汇票标记，并记录使用的汇票号
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
ALTER TABLE `busi_consign_order_items`
ADD COLUMN `is_payed_by_accept_draft` BIT(1) NULL DEFAULT 0 COMMENT '是否使用承兑汇票支付标记\n0   不使用, 1  使用' AFTER `status`,
ADD COLUMN `accept_draft_id` BIGINT(20) NULL COMMENT '使用的承兑汇票id' AFTER `is_payed_by_accept_draft`,
ADD COLUMN `accept_draft_code` VARCHAR(45) NULL COMMENT '使用的承兑汇票号' AFTER `accept_draft_id`;


-- ----------------------------
-- Describe: 订单状态变化流水表新增状态类别:承兑汇票
-- Author: Rabbit
-- Date: 2015-11-11 08:48:59
-- ----------------------------
ALTER TABLE `busi_consign_order_audit_trail`
CHANGE COLUMN `status_type` `status_type` ENUM('MAIN','PAY','SECONDPAY','PICKUP','FILLUP','ACCEPTDRAFT') NULL DEFAULT NULL COMMENT '状态类别：\nMAIN 订单状态\nPAY 付款状态\nSECONDPAY 二次结算付款\nPICKUP 提货状态\nFILLUP  放货状态\nACCEPTDRAFT  承兑汇票状态\n' ;
