CREATE TABLE IF NOT EXISTS `steel_cbms`.`common_category` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `uuid` VARCHAR(45) NOT NULL COMMENT '',
  `name` VARCHAR(45) NOT NULL COMMENT '',
  `priority` INT(11) NULL DEFAULT NULL COMMENT '',
  `is_deleted` BIT(1) NOT NULL COMMENT '',
  `created` DATETIME NOT NULL DEFAULT now() COMMENT '',
  `created_by` VARCHAR(45) NOT NULL COMMENT '',
  `last_updated` DATETIME NOT NULL DEFAULT now() COMMENT '',
  `last_updated_by` VARCHAR(45) NOT NULL COMMENT '',
  `modification_number` INT(20) NOT NULL DEFAULT 0 COMMENT '',
  `row_id` VARCHAR(45) NULL COMMENT '',
  `parent_row_id` VARCHAR(45) NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `steel_cbms`.`common_group_for_category` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '品类与分组对应表',
  `category_uuid` VARCHAR(45) NOT NULL COMMENT '关联的品类UUID',
  `category_group_uuid` VARCHAR(45) NOT NULL COMMENT '关联的分组UUID',
  `is_deleted` BIT(1) NOT NULL COMMENT '逻辑删除',
  `created` DATETIME NOT NULL DEFAULT now() COMMENT '',
  `created_by` VARCHAR(45) NOT NULL COMMENT '',
  `last_updated` DATETIME NOT NULL DEFAULT now() COMMENT '',
  `last_updated_by` VARCHAR(45) NOT NULL COMMENT '',
  `modification_number` INT(20) NOT NULL DEFAULT 0 COMMENT '',
  `row_id` VARCHAR(45) NULL COMMENT '',
  `parent_row_id` VARCHAR(45) NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '')
ENGINE = InnoDB;