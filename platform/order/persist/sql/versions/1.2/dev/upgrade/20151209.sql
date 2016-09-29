-- ----------------------------
-- Describe: 业务报表开发
-- Author: dengxiyan
-- Date: 2015年12月09日15:33:12
-- ----------------------------
CREATE TABLE `steel_cbms`.`report_org_day`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `org_id` BIGINT(20) COMMENT '服务中心ID',
  `org_name` VARCHAR(45) COMMENT '服务中心名称',
  `calculate_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '计算当天日期',
  `releted_total_sale_amount` DECIMAL(18,6) COMMENT '关联订单销售总额',
  `releted_total_purchase_amount` DECIMAL(18,6) COMMENT '关联订单采购总额',
  `releted_total_weight` DECIMAL(18,6) COMMENT '关联订单总重量',
  `releted_total_order` BIGINT(20) COMMENT '关联订单总笔数',
  `releted_total_consign_order` BIGINT(20) COMMENT '关联订单代运营总笔数',
  `second_total_sale_amount` DECIMAL(18,6) COMMENT '二结订单销售结算总额',
  `second_total_purchase_amount` DECIMAL(18,6) COMMENT '二结订单采购结算总额',
  `second_total_weight` DECIMAL(18,6) COMMENT '二结订单实提总重量',
  `second_total_order` BIGINT(20) COMMENT '二结订单总笔数',
  `second_total_consign_order` BIGINT(20) COMMENT '二结订单代运营总笔数',
  `total_consign_seller_account` BIGINT(20) COMMENT '截止当天日期所有已签约卖家总数',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45),
  `last_updated` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45),
  `modification_number` BIGINT(20) DEFAULT 0,
  `row_id` VARCHAR(45) NOT NULL,
  `parent_row_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8
COMMENT='代运营业务服务中心日报';

CREATE TABLE `steel_cbms`.`report_precipitation_funds`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `calculate_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '计算当天日期',
  `total_account_balance` DECIMAL(18,6) COMMENT '所有资金账户余额',
  `credit_limit` DECIMAL(18,6) COMMENT '信用额度',
  `precipitation_funds` DECIMAL(18,6) COMMENT '沉淀资金=所有资金账户余额 - 信用额度',
  `is_deleted` TINYINT(4) COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45),
  `last_updated` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45),
  `modification_number` BIGINT(20) DEFAULT 0,
  `row_id` VARCHAR(45) NOT NULL,
  `parent_row_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8
COMMENT='平台沉淀资金';