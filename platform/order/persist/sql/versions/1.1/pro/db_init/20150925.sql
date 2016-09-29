-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema cbms_receipt
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cbms_receipt
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cbms_receipt` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
-- -----------------------------------------------------
-- Schema test
-- -----------------------------------------------------
USE `cbms_receipt` ;

-- -----------------------------------------------------
-- Table `cbms_receipt`.`XSDJ_Head`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cbms_receipt`.`XSDJ_Head` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `KHSH` VARCHAR(20) NULL COMMENT '客户税号（专票必须）',
  `KHMC` VARCHAR(100) NOT NULL COMMENT '客户名称',
  `KHDZ` VARCHAR(85) NULL COMMENT '客户地址（专票必须）',
  `KHDH` VARCHAR(15) NULL COMMENT '客户电话（专票必须）',
  `KHYH` VARCHAR(100) NULL COMMENT '客户银行帐号（专票必须）',
  `BZ` VARCHAR(160) NULL COMMENT '备注',
  `DJRQ` DATE NULL COMMENT '单据日期',
  `DJH` VARCHAR(255) NOT NULL COMMENT '单据号（必需是唯一值）',
  `DJZL` VARCHAR(255) NOT NULL COMMENT '发票种类：中文：“专用发票”和“普通发票”',
  `DUBZ` VARCHAR(255) NOT NULL COMMENT '读取标志: “0”—未读取 “1”—已读取',
  `KPJH` VARCHAR(255) NOT NULL COMMENT '开票机号（0-主机，1-分1，…）',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
COMMENT = '销售单据头';

CREATE UNIQUE INDEX `uix_XSDJ_Head_DJH` ON `cbms_receipt`.`XSDJ_Head` (`DJH` ASC);


-- -----------------------------------------------------
-- Table `cbms_receipt`.`XSDJ_MX`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cbms_receipt`.`XSDJ_MX` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `DJH` VARCHAR(255) NOT NULL COMMENT '单据号',
  `SPMC` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `JLDW` VARCHAR(40) NOT NULL COMMENT '计量单位',
  `GGXH` VARCHAR(40) NULL COMMENT '规格型号',
  `SLIAN` DECIMAL(18,6) NOT NULL COMMENT '数量',
  `SL` DECIMAL(18,6) NOT NULL COMMENT '税率',
  `HSJE` DECIMAL(18,6) NOT NULL COMMENT '含税金额',
  `HSDJ` DECIMAL(18,6) NOT NULL COMMENT '含税单价',
  `ZKJE` DECIMAL(18,6) NULL COMMENT '折扣金额',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '销售单据明细';


-- -----------------------------------------------------
-- Table `cbms_receipt`.`FPHC`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cbms_receipt`.`FPHC` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `DJH` VARCHAR(255) NOT NULL COMMENT '单据号',
  `FPHM` VARCHAR(1000) NOT NULL COMMENT '发票号码',
  `KPRQ` VARCHAR(1000) NOT NULL COMMENT '开票日期',
  `ZF` INT NOT NULL COMMENT '0正常开票 1 作废',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '发票回传';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
