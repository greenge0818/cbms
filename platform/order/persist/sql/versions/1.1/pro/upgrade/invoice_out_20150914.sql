-- 销项票相关表更新 --
-- ----------------------------
-- Describe: 修改备注
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_in`
CHANGE COLUMN `relation_status` `relation_status` VARCHAR(45) NULL DEFAULT NULL COMMENT '已确认:(待关联toberelation，已关联hasrelation)，已认证:(已开、未开）',
CHANGE COLUMN `status` `status` VARCHAR(45) NULL DEFAULT NULL COMMENT '发票状态：待寄出RECEIVED，待确认SENT，待认证(已确认)WAIT，已认证ALREADY，已作废CANCEL' ;


-- ----------------------------
-- Describe: 增加字段
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order_items`
ADD COLUMN `used_amount` DECIMAL(18,6) NULL COMMENT '已用发票金额' AFTER `invoice_weight`,
ADD COLUMN `used_weight` DECIMAL(18,6) NULL COMMENT '已用发票重量' AFTER `used_amount`;


-- -----------------------------------------------------
-- Table `steel_cbms`.`inv_invoice_out_apply`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `steel_cbms`.`inv_invoice_out_apply` ;
-- ----------------------------
-- Describe: 新建开票申请主表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_apply` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL(18,6) NULL COMMENT '申请开票金额',
  `actual_amount` DECIMAL(18,6) NULL COMMENT '实开金额',
  `org_id` BIGINT NOT NULL COMMENT '服务中心ID',
  `org_name` VARCHAR(45) NOT NULL COMMENT '服务中心名称',
  `submitter_id` BIGINT NOT NULL COMMENT '提交人ID',
  `submitter_name` VARCHAR(45) NOT NULL COMMENT '提交人项目',
  `status` VARCHAR(45) NULL COMMENT '待提交；\n待审核；\n审核通过；\n审核不通过；\n部分开票；\n撤销；\n已开。',
  `comment` VARCHAR(255) NULL COMMENT '备注',
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
  COMMENT = '开票申请主表';


-- ----------------------------
-- Describe: 新建开票申请详情表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_apply_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `invoice_out_apply_id` BIGINT NOT NULL COMMENT '开票申请编号',
  `owner_id` BIGINT NOT NULL COMMENT '交易员ID',
  `owner_name` VARCHAR(45) NOT NULL COMMENT '交易员姓名',
  `org_id` BIGINT NULL COMMENT '服务中心ID',
  `org_name` VARCHAR(45) NULL COMMENT '服务中心名称',
  `buyer_id` BIGINT NOT NULL COMMENT '买家ID',
  `buyer_name` VARCHAR(45) NOT NULL COMMENT '买家名称',
  `apply_amount` DECIMAL(18,6) NULL COMMENT '申请金额',
  `lend_amount` DECIMAL(18,6) NULL COMMENT '挪用金额',
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
  COMMENT = '开票申请详情';


-- ----------------------------
-- Describe: 新建开票申请详情项表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_item_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `inv_invoice_in_id` BIGINT NOT NULL COMMENT '进项票ID',
  `inv_invoice_in_detail_id` BIGINT NOT NULL COMMENT '进项详情ID',
  `apply_detail_id` BIGINT NOT NULL COMMENT '开票申请详情ID',
  `nsort_name` VARCHAR(45) NOT NULL COMMENT '品名（来自于进项票）',
  `material` VARCHAR(45) NOT NULL COMMENT '材质',
  `spec` VARCHAR(45) NOT NULL COMMENT '规格',
  `weight` DECIMAL(18,6) NOT NULL COMMENT '开票重量',
  `actual_weight` DECIMAL(18,6) NULL COMMENT '实开重量',
  `price` DECIMAL(18,6) NOT NULL COMMENT '单价',
  `no_tax_amount` DECIMAL(18,6) NOT NULL COMMENT '不含税金额',
  `tax_amount` DECIMAL(18,6) NOT NULL COMMENT '税额',
  `amount` DECIMAL(18,6) NULL COMMENT '含税金额',
  `actual_amount` DECIMAL(18,6) NULL COMMENT '实开金额',
  `order_detail_id` BIGINT NOT NULL COMMENT '订单详情ID',
  `status` VARCHAR(45) NULL COMMENT '已开；未开。',
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
  COMMENT = '开票申请详情项';


-- ----------------------------
-- Describe: 新建开票清单表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_checklist` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `org_id` BIGINT NOT NULL COMMENT '服务中心编号',
  `code` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `amount` DECIMAL(18,6) NULL COMMENT '申请开票金额',
  `status` VARCHAR(45) NULL COMMENT '已开；\n部分开票；\n待开；\n暂缓。',
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
  COMMENT = '开票清单';


-- ----------------------------
-- Describe: 新建申请-清单关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`inv_invoice_out_checklist_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `checklist_id` BIGINT ZEROFILL NOT NULL COMMENT '清单编号',
  `item_detail_id` BIGINT NOT NULL COMMENT '申请编号',
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
  COMMENT = '申请-清单关联表';