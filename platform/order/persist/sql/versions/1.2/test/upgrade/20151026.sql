-- ----------------------------
-- Describe: 新增表：寄售订单商品变更历史(更新表结构)
-- Author: Rolyer Luo
-- Date: 2015-10-27 09:44:00
-- ----------------------------
DROP TABLE steel_cbms.busi_consign_order_items_history;
CREATE TABLE IF NOT EXISTS `steel_cbms`.`busi_consign_order_items_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(45) NOT NULL,
  `order_id` BIGINT NULL COMMENT '订单号',
  `seller_id` BIGINT NULL COMMENT '卖家ID',
  `seller_name` VARCHAR(45) NULL COMMENT '卖家名称',
  `nsort_name` VARCHAR(45) NULL COMMENT '品名',
  `material` VARCHAR(45) NULL COMMENT '材质',
  `spec` VARCHAR(45) NULL COMMENT '规格',
  `factory` VARCHAR(45) NULL COMMENT '厂家',
  `city` VARCHAR(45) NULL COMMENT '城市',
  `warehouse` VARCHAR(45) NULL COMMENT '仓库',
  `quantity` INT NULL COMMENT '数量 件数',
  `weight` DECIMAL(18,6) NULL COMMENT '重量',
  `weight_concept` VARCHAR(45) NULL COMMENT '理计，磅计，抄码',
  `cost_price` DECIMAL(18,6) NULL COMMENT '底价',
  `deal_price` DECIMAL(18,6) NULL COMMENT '成交价',
  `amount` DECIMAL(18,6) NULL COMMENT '金额',
  `purchase_amount` DECIMAL(18,6) NULL COMMENT '采购金额',
  `invoice_amount` DECIMAL(18,6) NULL COMMENT '已开票金额（可以一次开部分金额）',
  `invoice_weight` DECIMAL(18,6) NULL DEFAULT 0 COMMENT '已开票重量',
  `used_amount` DECIMAL(18,6) NULL COMMENT '已用发票金额',
  `used_weight` DECIMAL(18,6) NULL COMMENT '已用开票重量',
  `actual_pick_weight_salesman` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '交易员录入的实际提货重量（用于二次结算）',
  `actual_pick_weight_server` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '内勤员录入的实际提货重量（用于二次结算）',
  `actual_pick_quantity_salesman` INT NOT NULL DEFAULT 0 COMMENT '交易员录入的实际提货件数（用于二次结算）',
  `actual_pick_quantity_server` INT NOT NULL DEFAULT 0 COMMENT '内勤员录入的实际提货件数（用于二次结算）',
  `status` INT NOT NULL DEFAULT 0 COMMENT '匹配状态\n0未匹配\n1已匹配',
  `created` DATETIME NOT NULL DEFAULT now(),
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT now(),
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
COMMENT = '寄售订单商品变更历史'

-- ----------------------------
-- Describe: 服务中心添加开票主机设置
-- Author: Rolyer Luo
-- Date: 2015-10-26 21:22:00
-- ----------------------------
ALTER TABLE `steel_cbms`.`base_organization`
ADD COLUMN `invoiced_host` VARCHAR(45) NULL DEFAULT 0 COMMENT '开票主机设置（多个主机是逗号分隔，如：0,1,2）' AFTER `contract_address`;
