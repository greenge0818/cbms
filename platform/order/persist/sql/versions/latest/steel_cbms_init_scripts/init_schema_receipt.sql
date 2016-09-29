/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : cbms_receipt

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2015-11-04 20:38:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cbms_receipt
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `cbms_receipt` ;

-- -----------------------------------------------------
-- Schema steel_cbms
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cbms_receipt` DEFAULT CHARACTER SET utf8 ;
USE `cbms_receipt` ;

-- ----------------------------
-- Table structure for `fphc`
-- ----------------------------
DROP TABLE IF EXISTS `fphc`;
CREATE TABLE `fphc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DJH` varchar(255) NOT NULL COMMENT '单据号',
  `FPHM` varchar(1000) NOT NULL COMMENT '发票号码',
  `KPRQ` varchar(1000) NOT NULL COMMENT '开票日期',
  `ZF` int(11) NOT NULL COMMENT '0正常开票 1 作废',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票回传';

-- ----------------------------
-- Records of fphc
-- ----------------------------

-- ----------------------------
-- Table structure for `xsdj_head`
-- ----------------------------
DROP TABLE IF EXISTS `xsdj_head`;
CREATE TABLE `xsdj_head` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `KHSH` varchar(20) DEFAULT NULL COMMENT '客户税号（专票必须）',
  `KHMC` varchar(100) NOT NULL COMMENT '客户名称',
  `KHDZ` varchar(85) DEFAULT NULL COMMENT '客户地址（专票必须）',
  `KHDH` varchar(15) DEFAULT NULL COMMENT '客户电话（专票必须）',
  `KHYH` varchar(100) DEFAULT NULL COMMENT '客户银行帐号（专票必须）',
  `BZ` varchar(160) DEFAULT NULL COMMENT '备注',
  `DJRQ` date DEFAULT NULL COMMENT '单据日期',
  `DJH` varchar(255) NOT NULL COMMENT '单据号（必需是唯一值）',
  `DJZL` varchar(255) NOT NULL COMMENT '发票种类：中文：“专用发票”和“普通发票”',
  `DUBZ` varchar(255) NOT NULL COMMENT '读取标志: “0”—未读取 “1”—已读取',
  `KPJH` varchar(255) NOT NULL COMMENT '开票机号（0-主机，1-分1，…）',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uix_XSDJ_Head_DJH` (`DJH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售单据头';

-- ----------------------------
-- Records of xsdj_head
-- ----------------------------

-- ----------------------------
-- Table structure for `xsdj_mx`
-- ----------------------------
DROP TABLE IF EXISTS `xsdj_mx`;
CREATE TABLE `xsdj_mx` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DJH` varchar(255) NOT NULL COMMENT '单据号',
  `SPMC` varchar(100) NOT NULL COMMENT '商品名称',
  `JLDW` varchar(40) NOT NULL COMMENT '计量单位',
  `GGXH` varchar(40) DEFAULT NULL COMMENT '规格型号',
  `SLIAN` decimal(18,6) NOT NULL COMMENT '数量',
  `SL` decimal(18,6) NOT NULL COMMENT '税率',
  `HSJE` decimal(18,6) NOT NULL COMMENT '含税金额',
  `HSDJ` decimal(18,6) NOT NULL COMMENT '含税单价',
  `ZKJE` decimal(18,6) DEFAULT NULL COMMENT '折扣金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销售单据明细';

-- ----------------------------
-- Records of xsdj_mx
-- ----------------------------
