ALTER TABLE `steel_cbms`.`busi_consign_order_items`
  ADD COLUMN `allowance_amount` DECIMAL(18,6) DEFAULT 0.000000  NULL   COMMENT '资源折让的金额' AFTER `actual_pick_quantity_server`,
  ADD COLUMN `allowance_weight` DECIMAL(18,6) DEFAULT 0.000000  NULL   COMMENT '资源折让的重量' AFTER `allowance_amount`;

ALTER TABLE `steel_cbms`.`busi_consign_order_items`
  ADD COLUMN `allowance_buyer_amount` DECIMAL(18,6) DEFAULT 0.000000  NULL   COMMENT '资源折让买家的金额' AFTER `allowance_weight`;

-- ----------------------------
-- Describe: 增加查看全部折让单权限
-- Author: caochao
-- Date: 2015-11-25 9:13:10
SET @parentid=(SELECT id FROM acl_permission WHERE NAME='折让单管理');

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('查看全部折让单','allowance:list:viewall','/allowance/list/*.html#viewall',@parentid,'2015-11-25 09:54:41','admin','2015-11-25 09:54:48','admin','0',NULL,NULL);

ALTER TABLE `steel_cbms`.`reb_allowance`
  ADD COLUMN `org_id` BIGINT(20) NULL   COMMENT '服务中心ID' AFTER `allowance_id`,
  ADD COLUMN `org_name` VARCHAR(45) NULL   COMMENT '服务中心名称' AFTER `org_id`;
