DROP TABLE IF EXISTS `reb_allowance`;
CREATE TABLE `steel_cbms`.`reb_allowance`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `allowance_code` VARCHAR(45) COMMENT '折让单号',
  `allowance_type` VARCHAR(45) COMMENT '折让类型:seller(卖家折让单)、buyer(买家折让单)',
  `allowance_id` BIGINT(20) NULL COMMENT '卖家折让单id',
  `account_id` BIGINT(20) COMMENT '客户id',
  `account_name` VARCHAR(100) COMMENT '客户名称',
  `total_quantity` INT(11) COMMENT '总数量',
  `total_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '总重量',
  `total_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '总金额',
  `actual_total_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '实提总重量',
  `actual_total_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '实提总金额',
  `allowance_total_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '折让总重量',
  `allowance_total_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '折让总金额',
  `allowance_manner` VARCHAR(45) COMMENT '折让方式:weight(重量)、amount(金额)、all(金额和重量)',
  `img_url` VARCHAR(200) COMMENT '附件图片URL',
  `status` VARCHAR(45) COMMENT '折让状态:',
  `remark` VARCHAR(200) COMMENT '备注:审核理由',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `reb_allowance_order_item`;
CREATE TABLE `steel_cbms`.`reb_allowance_order_item`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `allowance_id` BIGINT(20) NOT NULL COMMENT '折让单id',
  `order_id` BIGINT(20) NOT NULL COMMENT '订单id',
  `order_code` VARCHAR(45) NOT NULL COMMENT '交易单号',
  `weight` DECIMAL(18,6) DEFAULT 0 COMMENT '订单重量',
  `amount` DECIMAL(18,6) DEFAULT 0 COMMENT '订单金额',
  `actual_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '实提重量',
  `actual_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '实提金额',
  `allowance_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '折让重量',
  `allowance_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '折让金额',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `reb_allowance_order_detail_item`;
CREATE TABLE `steel_cbms`.`reb_allowance_order_detail_item`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `allowance_order_id` BIGINT(20) NOT NULL COMMENT '折让单与订单关联id',
  `order_detail_id` BIGINT(20) NOT NULL COMMENT '订单详情id',
  `nsort_name` VARCHAR(45) NULL COMMENT '品名',
  `material` VARCHAR(45) NULL COMMENT '材质',
  `spec` VARCHAR(45) NULL COMMENT '规格',
  `actual_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '实提重量',
  `actual_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '实提金额',
  `allowance_weight` DECIMAL(18,6) DEFAULT 0 COMMENT '折让重量',
  `allowance_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '折让金额',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Describe: 增加折让关联表字段
-- Author: lixiang
-- Date: 2015-11-17 10:53:10
ALTER TABLE `reb_allowance_order_item`
ADD COLUMN `order_time`  datetime NOT NULL COMMENT '开单时间' AFTER `order_code`;

-- ----------------------------
-- Describe: 增加折让详情表字段
-- Author: lixiang
-- Date: 2015-11-17 10:53:18
-- ----------------------------
ALTER TABLE `reb_allowance_order_detail_item`
ADD COLUMN `buyer_id`  bigint(20) NULL COMMENT '买家id' AFTER `order_detail_id`,
ADD COLUMN `buyer_name`  varchar(45) NULL COMMENT '买家名称' AFTER `buyer_id`;