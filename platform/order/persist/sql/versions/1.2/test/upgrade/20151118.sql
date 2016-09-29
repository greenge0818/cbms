-- ----------------------------
-- Describe: 订单表增加打回状态和打回理由字段
-- Author: kongbinheng
-- Date: 2015-11-18 9:26:15
-- ----------------------------
ALTER TABLE busi_consign_order
ADD COLUMN call_back_status VARCHAR(30) NULL DEFAULT 'Normal' COMMENT '打回状态Normal正常Back打回' AFTER `fillup_status`,
ADD COLUMN call_back_note VARCHAR(255) NULL COMMENT '打回理由' AFTER `call_back_status`;

-- ----------------------------
-- Describe: 客户表增加客户银行账号审核状态和客户银行账号审核失败原因字段
-- Author: kongbinheng
-- Date: 2015-11-19 14:16:55
-- ----------------------------
ALTER TABLE cust_account
ADD COLUMN bank_data_status VARCHAR(20) NULL DEFAULT 'Requested' COMMENT '客户银行账号审核状态:Requested待审核,Insufficient资料不足,Declined审核不通过,Approved审核通过' AFTER `invoice_data_decline_reason`,
ADD COLUMN bank_data_reason VARCHAR(255) NULL COMMENT '客户银行账号审核失败原因' AFTER `bank_data_status`,
ADD COLUMN bank_data_reminded VARCHAR(20) NULL DEFAULT 'No' COMMENT '客户银行账号审核通过是否提醒:No不提醒Yes提醒' AFTER `bank_data_reason`;
