-- ----------------------------
-- Describe: 折让 -折让方式权限
-- Author: dq
-- Date: 2015-12-01 21:18:12
-- ----------------------------
SET @parentid=(SELECT id FROM acl_permission WHERE NAME = '折让单管理');
SELECT @parentid;

INSERT INTO `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
VALUES('显示折让重量的折让方式','allowance:add:type:weight','/allowance/addseller.html',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

INSERT INTO `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
VALUES('显示折让金额的折让方式','allowance:add:type:amount','/allowance/addseller.html',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

INSERT INTO `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
VALUES('显示折让金额和重量的折让方式','allowance:add:type:all','/allowance/addseller.html',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

-- Describe: 增加折让关联表字段
-- Author: lichaowei
-- Date: 2015-12-01 10:13:10
DROP TABLE IF EXISTS `inv_invoice_in_allowance` ;
CREATE TABLE `steel_cbms`.`inv_invoice_in_allowance`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_in_id` bigint(20) NOT NULL COMMENT '进项票Id',
  `cargo_name` varchar(45) NOT NULL COMMENT '货物名称',
  `no_tax_amount` decimal(18,6) NOT NULL DEFAULT 0 COMMENT '不含税金额',
  `tax_amount` decimal(18,6) NOT NULL DEFAULT 0 COMMENT '税额',
  `amount` decimal(18,6) NOT NULL DEFAULT 0 COMMENT '含税金额',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `inv_invoice_in_allowance_item`;
CREATE TABLE `steel_cbms`.`inv_invoice_in_allowance_item`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `allowance_id` BIGINT(20) NOT NULL COMMENT '折让单id',
  `invoice_in_allowance_id` BIGINT(20) NOT NULL COMMENT '折扣行数id',
  `allowance_order_detail_item_id` BIGINT(20) NOT NULL COMMENT '折让单详情id',
  `allowance_amount` DECIMAL(18,6) DEFAULT 0 COMMENT '折让金额',
  `is_deleted` TINYINT(4) DEFAULT 0 COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);
