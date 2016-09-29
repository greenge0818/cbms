-- ----------------------------
-- Describe: 修改 银票清单 busi_accept_draft_list 表字段 可以为空
-- Author: wangxiao
-- Date: 2015-12-21 13:12:58
-- ----------------------------
ALTER TABLE `busi_accept_draft_list`
CHANGE COLUMN `account_id` `account_id`  bigint(20) NULL default 0 COMMENT '充值账户id' AFTER `id`,
CHANGE COLUMN `account_name` `account_name` varchar(255) NULL  COMMENT '充值账户名称' AFTER `account_id`,
CHANGE COLUMN `org_id` `org_id`  bigint(20) NULL default 0 COMMENT '服务中心id' AFTER `account_name`,
CHANGE COLUMN `code` `code`   varchar(45) NULL COMMENT '票号' AFTER `org_name`,
CHANGE COLUMN `acceptance_bank_full_name` `acceptance_bank_full_name` varchar(255) NULL COMMENT '承兑行全称' AFTER `code`,
CHANGE COLUMN `amount` `amount` decimal(18,6) NULL COMMENT '金额' AFTER `acceptance_bank_full_name`,
CHANGE COLUMN `end_date` `end_date`   datetime NULL COMMENT '到期日' AFTER `amount`,
CHANGE COLUMN `discount_rate` `discount_rate` decimal(20,6) NULL COMMENT '贴现率' AFTER `end_date`,
CHANGE COLUMN`discount_rate_base`  `discount_rate_base` decimal(20,6) NULL COMMENT '贴现率成本' AFTER `discount_rate`,
CHANGE COLUMN `status`  `status`  varchar(45)  NULL DEFAULT 'NEW' COMMENT '状态\nNEW 保存待提交\nSUBMITTED 已录入待审核\nCHARGED 审核（双敲）通过，已充值--双敲匹配失败回到待提交\nROLLBACKREQUEST 回滚申请\nRENEW 充值回滚已完成' AFTER `amount_approve`,
CHANGE COLUMN `is_payed` `is_payed`   bit(1) NULL DEFAULT b'0' COMMENT '是否已经用于支付货款 0 没有   1  有' AFTER `is_different_bank`,
CHANGE COLUMN `last_updated_by` `last_updated_by`  varchar(45)  NULL AFTER `last_updated`,
CHANGE COLUMN `modification_number` `modification_number`  int(20) NULL DEFAULT 0 AFTER `last_updated_by`;