-- ----------------------------
-- Describe: 修改开票申请详情项表字段的默认值
-- Author: lichaowei
-- Date: 2015-9-22 11:53:20
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_out_item_detail`
  CHANGE `weight` `weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '开票重量',
  CHANGE `actual_weight` `actual_weight` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '实开重量',
  CHANGE `price` `price` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '单价',
  CHANGE `no_tax_amount` `no_tax_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '不含税金额',
  CHANGE `tax_amount` `tax_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '税额',
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '含税金额',
  CHANGE `actual_amount` `actual_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '实开金额';

ALTER TABLE `steel_cbms`.`busi_consign_order_items`
  CHANGE `invoice_amount` `invoice_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已开票金额（可以一次开部分金额）',
  CHANGE `invoice_weight` `invoice_weight` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已开票重量',
  CHANGE `used_amount` `used_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已用发票金额',
  CHANGE `used_weight` `used_weight` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已用发票重量',
  CHANGE `actual_pick_weight_salesman` `actual_pick_weight_salesman` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '交易员录入的实际提货重量（用于二次结算）',
  CHANGE `actual_pick_weight_server` `actual_pick_weight_server` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '内勤员录入的实际提货重量（用于二次结算）';

ALTER TABLE `steel_cbms`.`inv_pool_out`
  CHANGE `total_amount` `total_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '总金额',
  CHANGE `total_weight` `total_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '总重量',
  CHANGE `total_sent_amount` `total_sent_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '已付总金额',
  CHANGE `total_sent_weight` `total_sent_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '已付总重量';

ALTER TABLE `steel_cbms`.`inv_pool_out_detail`
  CHANGE `total_amount` `total_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '未开票总金额',
  CHANGE `total_weight` `total_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '未开票总重量',
  CHANGE `sent_amount` `sent_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '已开票金额',
  CHANGE `sent_weight` `sent_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '已开票重量';

ALTER TABLE `steel_cbms`.`inv_invoice_out_apply`
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '申请开票金额',
  CHANGE `actual_amount` `actual_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '实开金额';

  ALTER TABLE `steel_cbms`.`inv_invoice_out_checklist`
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '申请开票金额';

ALTER TABLE `steel_cbms`.`inv_invoice_in`
  CHANGE `total_amount` `total_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '总金额',
  CHANGE `total_weight` `total_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '总重量',
  CHANGE `check_total_amount` `check_total_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '核实总金额',
  CHANGE `check_total_weight` `check_total_weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '核实总重量';

ALTER TABLE `steel_cbms`.`inv_invoice_in_detail`
  CHANGE `weight` `weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '重量',
  CHANGE `no_tax_amount` `no_tax_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '不含税金额',
  CHANGE `tax_amount` `tax_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '税额',
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '含税金额',
  CHANGE `actual_amount` `actual_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '实收金额（关联订单子项会有偏差）',
  CHANGE `check_weight` `check_weight` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '核算重量',
  CHANGE `check_no_tax_amount` `check_no_tax_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '核实发票无税金额',
  CHANGE `check_tax_amount` `check_tax_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '核实发票税金',
  CHANGE `check_amount` `check_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '核算金额';

ALTER TABLE `steel_cbms`.`inv_invoice_out`
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '发票金额',
  CHANGE `check_amount` `check_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '内勤核算录入金额';

ALTER TABLE `steel_cbms`.`inv_invoice_pool`
  CHANGE `no_tax_amount` `no_tax_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '不含税金额',
  CHANGE `tax_amount` `tax_amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '税额',
  CHANGE `amount` `amount` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '含税金额',
  CHANGE `weight` `weight` DECIMAL(18,6) DEFAULT 0  NOT NULL   COMMENT '重量';

ALTER TABLE `steel_cbms`.`inv_invoice_out_apply_detail`
  CHANGE `apply_amount` `apply_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '申请金额',
  CHANGE `lend_amount` `lend_amount` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '挪用金额';


-- ---------------------------------------
-- Describe: 修改添加新客户的提成报表字段的默认值
-- Author: dengxiyan
-- Date: 2015-9-23 10:06:20
-- ---------------------------------------
ALTER TABLE `report_new_user_reward`
MODIFY COLUMN `add_new_buyer`  bigint(20) NULL DEFAULT NULL COMMENT '新增买家id' AFTER `row_id`,
MODIFY COLUMN `add_new_seller`  bigint(20) NULL DEFAULT NULL COMMENT '新增卖家id' AFTER `add_new_buyer`;
