-- ----------------------------
-- Describe: 添加表“票据关联表（爱信诺票据信息）”
-- Author: Rolyer Luo
-- Date: 09/28/2015 16:24
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_receipt` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
  `apply_detail_id` BIGINT NOT NULL COMMENT '申请ID',
  `item_detail_id` BIGINT NOT NULL COMMENT '详情项iID',
  `checklist_id` BIGINT NOT NULL COMMENT '清单ID',
  `amount` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '开票金额',
  `weight` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '开票重量',
  `djh` VARCHAR(45) NOT NULL COMMENT '单据号',
  `created` DATETIME NOT NULL DEFAULT now(),
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT now(),
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  `ext1` VARCHAR(20) NULL,
  `ext2` VARCHAR(45) NULL,
  `ext3` VARCHAR(255) NULL,
  `ext4` INT NULL,
  `ext5` INT NULL,
  `ext6` INT NULL,
  `ext7` DATETIME NULL,
  `ext8` BIGINT NULL,
  PRIMARY KEY (`id`))
  COMMENT = '票据关联表（爱信诺票据信息）'