-- ----------------------------
-- Describe: 平台资金字段默认值修改
-- Author: dengxiyan
-- Date: 2015-12-16 12:00:10
ALTER TABLE `steel_cbms`.`report_precipitation_funds`
  CHANGE `is_deleted` `is_deleted` TINYINT(4) DEFAULT 0  NULL   COMMENT '是否删除： 0 未删除；1 已删除。';

-- ----------------------------
-- Describe: 供应商进项报表
-- Author: dq
-- Date: 2015-12-16 12:00:10
CREATE TABLE `report_seller_invoice_in` (
  `id` BIGINT(20) NOT NULL DEFAULT '20',
  `happen_time` DATETIME NOT NULL COMMENT '发生时间',
  `seller_id` BIGINT(20) NOT NULL DEFAULT '20' COMMENT '卖家ID',
  `seller_name` VARCHAR(45) NOT NULL COMMENT '卖家名称',
  `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
  `order_code` VARCHAR(45) DEFAULT NULL COMMENT '订单号',
  `contract_code` VARCHAR(45) DEFAULT NULL COMMENT '合同号',
  `order_amount` DECIMAL(18,6) DEFAULT '0.000000' COMMENT '交易发生金额',
  `invoice_in_amount` DECIMAL(18,6) DEFAULT '0.000000' COMMENT '进项发生金额',
  `invoice_in_balance` DECIMAL(18,6) DEFAULT '0.000000' COMMENT '应收进项余额',
  `operation_type` VARCHAR(45) NOT NULL COMMENT '操作类型',
  `remark` VARCHAR(200) NOT NULL COMMENT '摘要',
  `created` DATETIME DEFAULT NULL,
  `created_by` VARCHAR(45) DEFAULT NULL,
  `last_updated` DATETIME DEFAULT NULL,
  `last_updated_by` VARCHAR(45) DEFAULT NULL,
  `modification_number` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='供应商进项报表';

-- ----------------------------
-- Describe: 供应商进项报表
-- Author: txm
-- Date: 2015-12-16 12:00:10
CREATE TABLE `report_buyer_invoice_out` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `buyer_id` BIGINT(20) NOT NULL DEFAULT '20' COMMENT '卖家ID',
  `buy_name` VARCHAR(45) NOT NULL COMMENT '卖家名称',
  `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单id',
  `order_code` VARCHAR(45) DEFAULT NULL COMMENT '订单code',
  `contract_code` VARCHAR(45) DEFAULT NULL COMMENT '合同单号',
  `order_amount` DECIMAL(18,6) DEFAULT NULL COMMENT '交易发生额',
  `invoice_out_amount` DECIMAL(18,6) DEFAULT NULL COMMENT '销项票发生额',
  `invoice_out_balance` DECIMAL(18,6) DEFAULT NULL COMMENT '应开销项余额',
  `operation_type` VARCHAR(45) NOT NULL COMMENT '操作类型',
  `remark` VARCHAR(50) DEFAULT NULL COMMENT '摘要',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间',
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT '0',
  `row_id` VARCHAR(45) DEFAULT NULL,
  `parent_row_id` VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='客户销报表';

-- ----------------------------
-- Describe: 修改银票清单背书天数和背书次数的read_times和adjust_date_count字段默认值为O
-- Author: wangxiao
-- Date: 2015-12-16 19:16:10
alter table busi_accept_draft_list alter column read_times set default 0;
alter table busi_accept_draft_list alter column adjust_date_count set default 0;

-- ----------------------------
-- Describe: 工行银企直连表增加时间戳
-- Author: kongbinheng
-- Date: 2015-12-16 19:17:36
ALTER TABLE `steel_cbms`.`busi_icbc_bdl_detail`
ADD COLUMN `TimeStamp`  datetime NULL DEFAULT now() COMMENT '时间戳' AFTER `AddInfo`;

-- ----------------------------
-- Describe: 更新历史数据，默认是创建时间
-- Author: kongbinheng
-- Date: 2015-12-16 19:17:36
UPDATE busi_icbc_bdl_detail SET TimeStamp = created;

-- ----------------------------
-- Describe: 增加出纳确认付款错误设置时间
-- Author: kongbinheng
-- Date: 2015-12-16 19:17:36
INSERT INTO base_sys_setting(`setting_type`, `setting_name`, `setting_value`, `default_value`, `description`, `display_name`, `created_by`, `created`, `last_updated_by`, `last_updated`)
VALUES('PayError', '出纳确认付款错误', '72', '72', '出纳确认付款错误', '出纳确认付款错误', 'kongbinheng', now(), 'kongbinheng', now());
