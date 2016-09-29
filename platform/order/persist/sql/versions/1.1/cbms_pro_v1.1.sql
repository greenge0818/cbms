-- MySQL dump 10.13  Distrib 5.5.44, for debian-linux-gnu (x86_64)
--
-- Host: rds0uar628tjwx3688cs.mysql.rds.aliyuncs.com    Database: steel_cbms
-- ------------------------------------------------------
-- Server version	5.6.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acl_permission`
--

DROP TABLE IF EXISTS `acl_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '权限名，如 查看订单',
  `code` varchar(200) NOT NULL COMMENT '权限编码',
  `url` varchar(200) DEFAULT NULL COMMENT '页面URL地址',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父权限',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=247 DEFAULT CHARSET=utf8 COMMENT='权限目录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_role`
--

DROP TABLE IF EXISTS `acl_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '角色名称',
  `code` varchar(45) NOT NULL COMMENT '角色编码',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父角色，如业务员的父角色为总经理',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '查看类型，1全看/2同级+下级/3仅下级',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态0锁定1正常',
  `role_type` varchar(20) DEFAULT NULL COMMENT '角色类型：\r\n              CEO("CEO"),\r\n	COO("COO"),\r\n	CFO("CFO"),\r\n	Manager("服务中心总经理"),\r\n	Server("内勤"),\r\n	Trader("交易员"),\r\n	Casher("出纳"),\r\n	LeadCasher("总出纳"),\r\n	Accounter("核算会计");',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='角色目录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_role_permission`
--

DROP TABLE IF EXISTS `acl_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_ROLE_ID` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10730 DEFAULT CHARSET=utf8 COMMENT='角色权限详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_shiro_filter_chain`
--

DROP TABLE IF EXISTS `acl_shiro_filter_chain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_shiro_filter_chain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `type` varchar(30) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统初始化过滤配置信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_user`
--

DROP TABLE IF EXISTS `acl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(45) NOT NULL COMMENT '登陆id',
  `name` varchar(45) NOT NULL COMMENT '用户姓名',
  `role_id` bigint(20) NOT NULL COMMENT '角色',
  `tel` varchar(45) DEFAULT NULL COMMENT '手机号码',
  `org_id` bigint(20) NOT NULL COMMENT '组织ID',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：0锁定，1正常',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_acl_user_login_id` (`login_id`),
  UNIQUE KEY `uix_acl_user_tel` (`tel`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8 COMMENT='CBMS登录用户 - 服务中心操作人员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acl_user_permission`
--

DROP TABLE IF EXISTS `acl_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '操作类型 0减权限 1增权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=722 DEFAULT CHARSET=utf8 COMMENT='用户特殊权限详情（结合角色权限一起使用）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_city`
--

DROP TABLE IF EXISTS `base_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '城市名称',
  `province_id` bigint(20) NOT NULL COMMENT '所属省份ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8 COMMENT='城市列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_dict`
--

DROP TABLE IF EXISTS `base_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_dict` (
  `id` int(11) NOT NULL,
  `key` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) DEFAULT NULL,
  `modification_number` int(20) DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_district`
--

DROP TABLE IF EXISTS `base_district`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_district` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '区域名称',
  `city_id` bigint(20) NOT NULL COMMENT '所属城市ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3215 DEFAULT CHARSET=utf8 COMMENT='区县列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_organization`
--

DROP TABLE IF EXISTS `base_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_code` varchar(45) NOT NULL DEFAULT 'GW' COMMENT '合同前缀 如 CS-SH-000001 中的SH',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级组织ID',
  `charger` bigint(20) DEFAULT NULL COMMENT '负责人',
  `address` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0锁定1正常',
  `credit_limit` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '可透支额度',
  `credit_limit_used` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已透支额度',
  `contract_address` varchar(255) DEFAULT NULL COMMENT '合同签订地',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) DEFAULT NULL,
  `modification_number` int(20) DEFAULT NULL,
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_base_organization_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='业务中心组织机构';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_organization_delivery_setting`
--

DROP TABLE IF EXISTS `base_organization_delivery_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_organization_delivery_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL COMMENT '服务中心id',
  `delivery_type` varchar(45) NOT NULL COMMENT '可用放货单类型:DELIVERYLETTER DELIVERYLETTER 放货函；DELIVERYORDER　放货单；TRANSFERORDER　货权转让单；TRANSFERLETTER　货权转让函',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='服务中心可用放货单类型设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_province`
--

DROP TABLE IF EXISTS `base_province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_province` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '省份名称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='省份列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_rebate`
--

DROP TABLE IF EXISTS `base_rebate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_rebate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_UUID` varchar(45) DEFAULT NULL COMMENT '对应品类的UUID',
  `rebate_role` decimal(20,2) DEFAULT NULL COMMENT '返利（元/吨）',
  `rebate_status` varchar(45) DEFAULT NULL COMMENT '返利状态（0已取消，1正在生效，2下个月生效）',
  `effective_time` datetime DEFAULT NULL COMMENT '返利生效日期',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_reward`
--

DROP TABLE IF EXISTS `base_reward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_reward` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reward_type` varchar(45) NOT NULL COMMENT '提成类型: 成交量提成设置category, 订单提成order',
  `category_UUID` varchar(45) DEFAULT NULL COMMENT '如果type为成交量提成设置，这里是对应品类UUID；如果是订单提成，这里存提成的类型',
  `seller_limit` decimal(10,0) DEFAULT NULL COMMENT '卖家月寄售量上限（吨）',
  `reward_role` decimal(20,2) DEFAULT NULL COMMENT '如果type为成交量提成设置，这里是单位提成（元/吨）；如果是订单提成，这里存提成比例',
  `reward_status` varchar(45) DEFAULT NULL COMMENT '提成规则状态(0已失效，1正在生效，2下月生效)',
  `effective_time` datetime DEFAULT NULL COMMENT '提成生效日期',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_sys_feedback`
--

DROP TABLE IF EXISTS `base_sys_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_sys_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(45) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `source` varchar(255) NOT NULL DEFAULT '' COMMENT '反馈来源 web网站，app手机',
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '反馈内容',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(45) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统反馈表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_sys_setting`
--

DROP TABLE IF EXISTS `base_sys_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_sys_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `setting_type` varchar(45) NOT NULL COMMENT '设置类型',
  `setting_name` varchar(100) NOT NULL COMMENT '设置名称',
  `setting_value` varchar(100) NOT NULL COMMENT '设置值',
  `default_value` varchar(100) NOT NULL COMMENT '默认设置值',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `display_name` varchar(100) DEFAULT NULL COMMENT '显示',
  `sequence` int(11) DEFAULT NULL COMMENT '序号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(45) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='系统设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_system_opration_log`
--

DROP TABLE IF EXISTS `base_system_opration_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base_system_opration_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator_id` bigint(20) NOT NULL COMMENT '操作者ID',
  `operator_name` varchar(45) NOT NULL COMMENT '操作者姓名',
  `operation_key` varchar(100) NOT NULL COMMENT '操作类型的key',
  `operation_name` varchar(100) NOT NULL COMMENT '操作名称',
  `operation_level` varchar(100) NOT NULL COMMENT '操作级别：Safe,Warning,Dangerous,Damning',
  `operation_level_value` int(11) DEFAULT NULL COMMENT '操作等级的数字值，值越高表示越危险，用于按危险等级排序',
  `parameters` varchar(1000) NOT NULL DEFAULT '' COMMENT '提交的参数',
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `last_updated` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) DEFAULT NULL,
  `modification_number` int(20) DEFAULT NULL,
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8076 DEFAULT CHARSET=utf8 COMMENT='系统操作日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_bank_transaction_info`
--

DROP TABLE IF EXISTS `busi_bank_transaction_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_bank_transaction_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_number` varchar(45) NOT NULL COMMENT '银行交易号',
  `serial_number` varchar(45) NOT NULL COMMENT '银行流水号',
  `payee_bank_name` varchar(255) NOT NULL COMMENT '付款银行',
  `bank_code` varchar(45) DEFAULT NULL COMMENT '银行行号',
  `payee_name` varchar(100) NOT NULL COMMENT '客户名称',
  `payee_acctount_number` varchar(45) NOT NULL COMMENT '银行账号',
  `transaction_amount` decimal(18,6) NOT NULL COMMENT '到账金额',
  `transaction_time` datetime NOT NULL COMMENT '到账时间',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_related` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否关联，周鹏2015/7/17 20:04这列不要了',
  `status` varchar(30) NOT NULL COMMENT '状态 \n\nnormal  正常\n\nunprocessed 未处理\n\nrefund 已处理-退款\n\ncharge 已处理-充值',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3025 DEFAULT CHARSET=utf8 COMMENT='银行到账流水明细（用于到账异常处理及到账关联）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_bank_transaction_original_detail`
--

DROP TABLE IF EXISTS `busi_bank_transaction_original_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_bank_transaction_original_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `header_id` bigint(20) NOT NULL COMMENT '表头ID',
  `voucherNo` varchar(13) DEFAULT NULL COMMENT '凭证号',
  `seqNo` varchar(12) DEFAULT NULL COMMENT '交易流水号:记账流水',
  `txAmount` varchar(18) DEFAULT NULL COMMENT '交易金额',
  `balance` varchar(18) DEFAULT NULL COMMENT '账户余额',
  `tranFlag` varchar(1) DEFAULT NULL COMMENT '借贷标志\n0：借\n1：贷\n',
  `transDate` varchar(10) DEFAULT NULL COMMENT '交易日期',
  `transTime` varchar(10) DEFAULT NULL COMMENT '交易时间\nHHMMSS，\n最长6位，最短只有1位\n',
  `note` varchar(60) DEFAULT NULL COMMENT '备注 :客户附言',
  `remark` varchar(22) DEFAULT NULL COMMENT '摘要代码',
  `payeeBankNo` varchar(12) DEFAULT NULL COMMENT 'payeeBankNo',
  `payeeBankName` varchar(255) DEFAULT NULL COMMENT '对方行名',
  `payeeAcctNo` varchar(32) DEFAULT NULL COMMENT '对方账号',
  `payeeName` varchar(100) DEFAULT NULL COMMENT '对方户名',
  `transCode` varchar(4) DEFAULT NULL COMMENT '交易代码',
  `branchId` varchar(4) DEFAULT NULL COMMENT '分行ID',
  `customerAcctNo` varchar(20) DEFAULT NULL COMMENT '客户账号',
  `payeeAcctType` varchar(1) DEFAULT NULL COMMENT '对方账号类型\n\n**KHZHLX        客户账号类型\n0-对公账号\n1-卡\n2-活期一本通\n3-定期一本通\n4-定期存折\n5-存单\n6-国债\n7-外系统账号\n8-活期存折\n9-内部帐/表外帐\nS-对私内部账号\nT-个人保证金\nU-浦发花旗信用卡卡号\nZ-客户号\n',
  `transCounter` varchar(8) DEFAULT NULL COMMENT '交易柜员',
  `authCounter` varchar(8) DEFAULT NULL COMMENT '授权柜员',
  `otherChar10` varchar(10) DEFAULT NULL COMMENT '备用域10位',
  `otherChar40` varchar(10) DEFAULT NULL COMMENT '备用域40位',
  `seqNum` varchar(45) DEFAULT NULL COMMENT '传票序号:记账流水的序号',
  `revFlag` varchar(1) DEFAULT NULL COMMENT '冲补标志\n0-无关\n1-当日抹帐\n2-隔日抹帐\n',
  `begin_number` int(11) NOT NULL DEFAULT '0' COMMENT '查询起始笔数',
  `begin_date` varchar(30) DEFAULT NULL COMMENT '查询起始日期',
  `status` int(11) DEFAULT '0' COMMENT '0未处理1处理完2处理中',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6623 DEFAULT CHARSET=utf8 COMMENT='银企直销银行流水记账明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_bank_transaction_original_header`
--

DROP TABLE IF EXISTS `busi_bank_transaction_original_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_bank_transaction_original_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `req_acctNo` varchar(20) DEFAULT NULL COMMENT '账号',
  `req_beginDate` date DEFAULT NULL COMMENT '开始日期',
  `req_endDate` date DEFAULT NULL COMMENT '结束日期',
  `req_queryNumber` int(11) DEFAULT NULL COMMENT '查询的笔数',
  `req_beginNumber` int(11) DEFAULT NULL COMMENT '查询的起始笔数',
  `req_transAmount` decimal(18,6) DEFAULT NULL COMMENT '交易金额',
  `req_subAccount` char(32) DEFAULT NULL COMMENT '对方帐号',
  `req_subAcctName` char(62) DEFAULT NULL COMMENT '对方户名',
  `res_totalCount` int(11) DEFAULT NULL COMMENT '交易总笔数',
  `res_acctNo` varchar(20) DEFAULT NULL COMMENT '账号',
  `res_acctName` varchar(60) DEFAULT NULL COMMENT '账号名称',
  `res_currency` int(2) DEFAULT NULL COMMENT '账户币种',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2233 DEFAULT CHARSET=utf8 COMMENT='银企直联查询日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_bill_sequence`
--

DROP TABLE IF EXISTS `busi_bill_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_bill_sequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `org_id` bigint(20) NOT NULL COMMENT '部门ID',
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `seq_type` varchar(45) NOT NULL COMMENT '类型\nJS 寄售订单\nDH 订货\nXS 销售\nJS-HZ-000001-1506-300',
  `current_value` int(11) NOT NULL COMMENT '当前值',
  `seq_date` varchar(8) DEFAULT NULL COMMENT '序列年月 如 1504',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3445 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_order`
--

DROP TABLE IF EXISTS `busi_consign_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL COMMENT '单号',
  `account_id` bigint(20) NOT NULL COMMENT '买家ID',
  `account_name` varchar(45) NOT NULL COMMENT '买家企业名称',
  `owner_id` bigint(20) NOT NULL COMMENT '交易员ID',
  `owner_name` varchar(45) NOT NULL COMMENT '交易员姓名',
  `contact_id` bigint(20) NOT NULL COMMENT '联系人ID',
  `contact_name` varchar(45) NOT NULL COMMENT '联系人姓名',
  `contact_tel` varchar(45) NOT NULL COMMENT '联系人电话',
  `delivery_type` enum('SELFPICK','DISPATCH') NOT NULL COMMENT 'SELFPICK：自提+DISPATCH:配送',
  `delivery_start_date` datetime DEFAULT NULL COMMENT '交货开始时间',
  `delivery_end_date` datetime DEFAULT NULL COMMENT '交货结束时间',
  `fee_taker` varchar(45) NOT NULL COMMENT '运费承担方',
  `ship_fee` decimal(18,6) DEFAULT NULL COMMENT '运费',
  `outbound_taker` varchar(45) DEFAULT NULL COMMENT '出库费承担方',
  `outbound_fee` decimal(18,6) DEFAULT '0.000000' COMMENT '出库费',
  `total_quantity` int(11) NOT NULL COMMENT '总件数',
  `total_weight` decimal(18,6) NOT NULL COMMENT '总重量',
  `total_amount` decimal(18,6) NOT NULL COMMENT '总金额',
  `status` varchar(45) NOT NULL COMMENT '1 NEW 新建（待审核） 待审核\n-1 NEWDECLINED 订单关闭-审核不通过\n2 NEWAPPROVED 审核通过(待关联) 待关联\n3 CLOSEREQUEST1 请求关闭1\n-2 CLOSE1APPROVED 订单关闭-请求1通过\n4 RELATED 已关联\n5 CLOSEREQUEST2 请求关闭2\n-2 CLOSE2APPROVED 订单关闭-请求2通过\n6 SECONDSETTLE 待二次结算\n7 INVOICEREQUEST 待开票申请\n8 INVOICE 待开票\n10 FINISH 交易完成\n-3 SYSCLOSED 订单关闭-5点半未关联（待关联）订单',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `contract_code` varchar(45) DEFAULT NULL COMMENT '采购合同号',
  `pay_status` varchar(45) NOT NULL DEFAULT 'APPLY' COMMENT '付款状态\nAPPLY 待申请（未申请／审核不通过）\nREQUESTED 已申请/待审核\nAPPROVED 已通过审核待确认付款\nPAYED已确认付款',
  `pickup_status` int(11) NOT NULL DEFAULT '1' COMMENT '提货状态：\n1 未录入\n2 部分录入\n3 全录入',
  `fillup_status` int(11) NOT NULL DEFAULT '-1' COMMENT '放货打印状态\n1　已全打印\n0　未全打印\n2    全匹配 -1 初始化',
  `consign_type` varchar(45) NOT NULL DEFAULT 'consign' COMMENT '寄售类型\nconsign 寄售\ntemp  临采',
  `operator_id` bigint(20) NOT NULL COMMENT '创建人Id',
  `operator_name` varchar(45) NOT NULL COMMENT '创建人名称',
  `contract_address` varchar(255) DEFAULT NULL COMMENT '合同签订地',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_busi_consign_order_code` (`code`),
  KEY `IDX_OPERATOR_ID` (`operator_id`),
  KEY `IDX_OPERATOR_ID_STATUS` (`operator_id`,`status`),
  KEY `IDX_account_id_owner_id_STATUS` (`account_id`,`owner_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=8517 DEFAULT CHARSET=utf8 COMMENT='寄售交易订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_order_attachment`
--

DROP TABLE IF EXISTS `busi_consign_order_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_order_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `consign_order_id` bigint(20) NOT NULL COMMENT '订单号',
  `type` varchar(30) NOT NULL COMMENT '类型，如 合同',
  `file_url` varchar(150) NOT NULL COMMENT '附件路径',
  `relate_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联ID，如：寄售订单合同表ID',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 \n0有效 默认\n1无效（逻辑删除）',
  `note` varchar(100) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7744 DEFAULT CHARSET=utf8 COMMENT='寄售订单合同附件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_order_audit_trail`
--

DROP TABLE IF EXISTS `busi_consign_order_audit_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_order_audit_trail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `set_to_status` varchar(45) DEFAULT NULL COMMENT '设置成状态',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员ID，如果为空表示系统自动关闭',
  `operator_name` varchar(45) DEFAULT NULL COMMENT '操作员姓名',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `status_type` enum('MAIN','PAY','SECONDPAY','PICKUP','FILLUP') DEFAULT NULL COMMENT '状态类别：\nMAIN 订单状态\nPAY 付款状态\nSECONDPAY 二次结算付款\nPICKUP 提货状态\nFILLUP  放货状态\n\n',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59996 DEFAULT CHARSET=utf8 COMMENT='寄售订单审核状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_order_contract`
--

DROP TABLE IF EXISTS `busi_consign_order_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_order_contract` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `consign_order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '寄售订单ID',
  `contract_code_auto` varchar(45) NOT NULL COMMENT '合同编号（自动生成）',
  `contract_code_cust` varchar(45) DEFAULT NULL COMMENT '合同编号（客户自定义）',
  `bank_name_main` varchar(45) DEFAULT NULL COMMENT '开户银行主行 如 工商银行',
  `bank_name_branch` varchar(45) DEFAULT NULL COMMENT '开户银行支行 如XX支行',
  `bank_account_code` varchar(45) DEFAULT NULL COMMENT '银行账号',
  `customer_name` varchar(45) DEFAULT NULL COMMENT '客户单位名称',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  `customer_addr` varchar(45) DEFAULT NULL COMMENT '客户单位注册地址',
  `customer_tel` varchar(45) DEFAULT NULL COMMENT '客户联系电话',
  `order_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '订单金额',
  `balance_second_settlement` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '二次结算记账余额',
  `apply_pay_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '申请付款金额',
  `status` varchar(45) NOT NULL DEFAULT '0' COMMENT '状态\n0.未申请付款合同未上\n1.未申请付款合同已上传\n2.已申请付款',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7803 DEFAULT CHARSET=utf8 COMMENT='寄售订单合同';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_order_items`
--

DROP TABLE IF EXISTS `busi_consign_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单号',
  `seller_id` bigint(20) DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(45) DEFAULT NULL COMMENT '卖家名称',
  `nsort_name` varchar(45) DEFAULT NULL COMMENT '品名',
  `material` varchar(45) DEFAULT NULL COMMENT '材质',
  `spec` varchar(45) DEFAULT NULL COMMENT '规格',
  `factory` varchar(45) DEFAULT NULL COMMENT '厂家',
  `city` varchar(45) DEFAULT NULL COMMENT '城市',
  `warehouse` varchar(45) DEFAULT NULL COMMENT '仓库',
  `quantity` int(11) DEFAULT NULL COMMENT '数量 件数',
  `weight` decimal(18,6) DEFAULT NULL COMMENT '重量',
  `weight_concept` varchar(45) DEFAULT NULL COMMENT '理计，磅计，抄码',
  `cost_price` decimal(18,6) DEFAULT NULL COMMENT '底价',
  `deal_price` decimal(18,6) DEFAULT NULL COMMENT '成交价',
  `amount` decimal(18,6) DEFAULT NULL COMMENT '金额',
  `invoice_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '已开票金额（可以一次开部分金额）',
  `invoice_weight` decimal(18,6) DEFAULT '0.000000' COMMENT '已开票重量',
  `used_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '已用发票金额',
  `used_weight` decimal(18,6) DEFAULT '0.000000' COMMENT '已用发票重量',
  `actual_pick_weight_salesman` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '交易员录入的实际提货重量（用于二次结算）',
  `actual_pick_weight_server` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '内勤员录入的实际提货重量（用于二次结算）',
  `actual_pick_quantity_salesman` int(11) NOT NULL DEFAULT '0' COMMENT '交易员录入的实际提货件数（用于二次结算）',
  `actual_pick_quantity_server` int(11) NOT NULL DEFAULT '0' COMMENT '内勤员录入的实际提货件数（用于二次结算）',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '匹配状态\n0未匹配\n1已匹配',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_ORDER_ID` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14352 DEFAULT CHARSET=utf8 COMMENT='寄售订单商品明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_consign_process`
--

DROP TABLE IF EXISTS `busi_consign_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_consign_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组织机构ID',
  `org_name` varchar(45) NOT NULL COMMENT '组织机构名称',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易员ID',
  `user_name` varchar(45) NOT NULL COMMENT '交易员名称',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '0取数1二次结算',
  `order_status_code` varchar(45) NOT NULL COMMENT '订单状态',
  `order_status_name` varchar(45) NOT NULL,
  `operator_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '流程操作员ID',
  `operator_name` varchar(45) NOT NULL COMMENT '流程操作员姓名',
  `operator_mobile` varchar(45) NOT NULL COMMENT '流程操作员手机',
  `operator_role_name` varchar(45) NOT NULL COMMENT '流程操作员角色',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_OPERATOR_ID_USER_ID` (`operator_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3121 DEFAULT CHARSET=utf8 COMMENT='寄售流程定义';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_delivery_bill`
--

DROP TABLE IF EXISTS `busi_delivery_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_delivery_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL COMMENT '提货单号',
  `consign_order_id` bigint(20) DEFAULT NULL COMMENT '寄售单ID',
  `consign_order_code` varchar(45) NOT NULL COMMENT '寄售单号',
  `pickup_id` bigint(20) DEFAULT NULL COMMENT '提货单ID',
  `pickup_code` varchar(45) DEFAULT NULL COMMENT '提货单号',
  `buyer_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '卖家全称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `seller_name` varchar(45) NOT NULL COMMENT '卖家名称',
  `delivery_type` varchar(30) NOT NULL COMMENT '提货方式',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `total_weight` decimal(18,6) DEFAULT NULL COMMENT '总重量',
  `total_quantity` int(11) DEFAULT NULL COMMENT '总数量',
  `total_amount` decimal(18,6) DEFAULT NULL COMMENT '总金额',
  `print_times` int(11) DEFAULT NULL COMMENT '打印次数，大于0则为已打印',
  `delivery_order_code` varchar(45) DEFAULT NULL COMMENT '放货单编号',
  `delivery_order_print_times` int(11) DEFAULT NULL COMMENT '放货单打印次数',
  `buyer_pickupbill_path` varchar(100) DEFAULT NULL COMMENT '买家提货单图片保存路径',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\n1有效\n0无效',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_busi_delivery_bill_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5090 DEFAULT CHARSET=utf8 COMMENT='放货单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_delivery_items`
--

DROP TABLE IF EXISTS `busi_delivery_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_delivery_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '放货单ID',
  `pickup_id` bigint(20) DEFAULT NULL COMMENT '提货单ID',
  `consign_order_item_id` bigint(20) NOT NULL COMMENT '寄售单明细ID',
  `picked_quantity` int(11) NOT NULL COMMENT '已提数量',
  `pick_quantity` int(11) NOT NULL COMMENT '提货数量',
  `pick_weight` decimal(18,6) NOT NULL COMMENT '提货重量',
  `package_no` varchar(45) DEFAULT NULL COMMENT '捆包号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9529 DEFAULT CHARSET=utf8 COMMENT='放货单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pay_request`
--

DROP TABLE IF EXISTS `busi_pay_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pay_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL COMMENT '付款申请单号',
  `consign_order_id` bigint(20) DEFAULT NULL COMMENT '寄售单号 订单初次付款申请，二次结算时勾选申请付款这两种情况下使用,提现不需要',
  `consign_order_code` varchar(45) DEFAULT NULL COMMENT '寄售单号 订单初次付款申请，二次结算时勾选申请付款这两种情况下使用,提现不需要',
  `contract_code` varchar(45) DEFAULT NULL COMMENT '寄售单采购合同号 订单初次付款申请，二次结算时勾选申请付款这两种情况下使用,提现不需要',
  `contract_url` varchar(100) DEFAULT NULL COMMENT '上传合同地址',
  `requester_id` bigint(20) DEFAULT NULL COMMENT '申请人id',
  `requester_name` varchar(45) DEFAULT NULL COMMENT '申请人姓名',
  `org_id` bigint(20) DEFAULT NULL COMMENT '申请部门ID',
  `org_name` varchar(45) DEFAULT NULL COMMENT '申请部门名称',
  `buyer_id` bigint(20) DEFAULT NULL COMMENT '买家客户ID  订单初次付款申请，二次结算时勾选申请付款这两种情况下使用,提现不需要',
  `buyer_name` varchar(45) DEFAULT NULL COMMENT '收款客户名称',
  `total_amount` decimal(18,6) DEFAULT NULL COMMENT '付款总金额',
  `status` varchar(45) NOT NULL COMMENT '状态\nREQUESTED 已申请 待审核\nAPPROVED 已通过审核\nDECLINED 未通过\nCONFIRMEDPAY 已确认付款',
  `decline_reason` varchar(200) DEFAULT NULL COMMENT '未通过审核原因',
  `type` varchar(45) DEFAULT NULL COMMENT '类型，\n1.初次付款\n2.二次结算\n3.提现',
  `print_times` int(11) NOT NULL DEFAULT '0' COMMENT '打印次数',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL COMMENT '最近一次打印的操作人',
  `ext2` varchar(45) DEFAULT NULL COMMENT '最近一次打印的IP',
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5770 DEFAULT CHARSET=utf8 COMMENT='付款申请单(一次,二次,提现)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pay_request_items`
--

DROP TABLE IF EXISTS `busi_pay_request_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pay_request_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `request_id` bigint(20) DEFAULT NULL COMMENT '申请单ID',
  `receiver_id` bigint(20) DEFAULT NULL COMMENT '收款客户id',
  `receiver_name` varchar(45) DEFAULT NULL COMMENT '收款单位',
  `receiver_bank_code` varchar(45) DEFAULT NULL COMMENT '收款单位银行行号',
  `receiver_bank_name` varchar(45) DEFAULT NULL COMMENT '收款单位开户行',
  `receiver_bank_city` varchar(45) DEFAULT NULL COMMENT '收款人银行所在城市',
  `receiver_branch_bank_name` varchar(45) DEFAULT NULL,
  `receiver_account_code` varchar(45) DEFAULT NULL COMMENT '收款银行账号',
  `receiver_tel` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `receiver_reg_addr` varchar(45) DEFAULT NULL COMMENT '注册地址',
  `pay_amount` decimal(18,6) DEFAULT NULL COMMENT '付款金额',
  `second_balance_takeout` decimal(18,6) DEFAULT '0.000000' COMMENT '抵扣二次结算余额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5773 DEFAULT CHARSET=utf8 COMMENT='付款申请明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pickup_attachment`
--

DROP TABLE IF EXISTS `busi_pickup_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pickup_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pickup_bill_id` bigint(20) NOT NULL COMMENT '提货单号',
  `file_url` varchar(150) NOT NULL COMMENT '文件地址',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\n1有效\n0无效',
  `note` varchar(100) DEFAULT NULL COMMENT '备注，用于说明附件的用途',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提货单附件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pickup_bill`
--

DROP TABLE IF EXISTS `busi_pickup_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pickup_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL COMMENT '提货单号',
  `consign_order_id` bigint(20) DEFAULT NULL COMMENT '寄售单ID',
  `consign_order_code` varchar(45) NOT NULL COMMENT '寄售单号',
  `buyer_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '卖家全称',
  `delivery_type` varchar(30) NOT NULL COMMENT '提货方式',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `total_weight` decimal(18,6) DEFAULT NULL COMMENT '总重量',
  `total_quantity` int(11) DEFAULT NULL COMMENT '总数量',
  `total_amount` decimal(18,6) DEFAULT NULL COMMENT '总金额',
  `print_times` int(11) DEFAULT NULL COMMENT '打印次数，大于0则为已打印',
  `buyer_pickupbill_path` varchar(100) DEFAULT NULL COMMENT '买家提货单图片保存路径',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\n1有效\n0无效',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_busi_pickup_bill_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5208 DEFAULT CHARSET=utf8 COMMENT='提货单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pickup_items`
--

DROP TABLE IF EXISTS `busi_pickup_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pickup_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '提货单ID',
  `seller_id` bigint(20) DEFAULT NULL,
  `seller_name` varchar(45) DEFAULT NULL COMMENT '卖家名称',
  `consign_order_item_id` bigint(20) NOT NULL COMMENT '寄售单明细ID',
  `picked_quantity` int(11) NOT NULL COMMENT '已提数量',
  `pick_quantity` int(11) NOT NULL COMMENT '提货数量',
  `pick_weight` decimal(18,6) NOT NULL COMMENT '提货重量',
  `package_no` varchar(45) DEFAULT NULL COMMENT '捆包号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10941 DEFAULT CHARSET=utf8 COMMENT='提货单明细';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_pickup_person`
--

DROP TABLE IF EXISTS `busi_pickup_person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_pickup_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `valid_code` varchar(45) NOT NULL COMMENT '证件号，如车牌号，身份证号',
  `pickup_bill_id` bigint(20) NOT NULL COMMENT '提货单号',
  `name` varchar(45) NOT NULL COMMENT '提货人姓名',
  `mobil` varchar(45) NOT NULL COMMENT '联系方式',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5352 DEFAULT CHARSET=utf8 COMMENT='提货人';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_print_log`
--

DROP TABLE IF EXISTS `busi_print_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_print_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_code` varchar(45) NOT NULL COMMENT '打印单据代号或编号',
  `bill_type` varchar(45) NOT NULL COMMENT '打印单据类型',
  `user_id` bigint(20) NOT NULL COMMENT '打印人ID',
  `user_name` varchar(45) NOT NULL COMMENT '打印人名称',
  `ip_address` varchar(45) NOT NULL COMMENT 'IP地址',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='打印日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `busi_refund_request`
--

DROP TABLE IF EXISTS `busi_refund_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `busi_refund_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_bank_serial_number` varchar(45) NOT NULL COMMENT '相关银行流水号',
  `type` varchar(45) NOT NULL COMMENT '业务类型',
  `receiver_name` varchar(45) NOT NULL COMMENT '收款单位',
  `receiver_account_name` varchar(45) NOT NULL COMMENT '收款单位开户行',
  `receiver_account_code` varchar(45) NOT NULL COMMENT '收款单位银行账号',
  `bank_code` varchar(45) DEFAULT NULL COMMENT '银行行号',
  `amount_upper_case` varchar(45) NOT NULL COMMENT '金额大写',
  `amount_lower_case` decimal(18,6) NOT NULL COMMENT '金额',
  `reason` varchar(255) DEFAULT NULL COMMENT '原因',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_category`
--

DROP TABLE IF EXISTS `common_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `priority` int(11) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3079 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_category_group`
--

DROP TABLE IF EXISTS `common_category_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_category_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(45) NOT NULL COMMENT '全站唯一标识',
  `parent_uuid` varchar(45) NOT NULL COMMENT '父级uuid',
  `name` varchar(45) NOT NULL COMMENT '分组名称',
  `site_uuid` varchar(45) NOT NULL COMMENT '分组对应的站点',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid_UNIQUE` (`uuid`),
  KEY `common_category_group_site_idx` (`site_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=2513 DEFAULT CHARSET=utf8 COMMENT='品名分组，分组,  可以存在上下级关系(板材-热轧/冷轧)分组跟对应的站点关联起来（超市，CBMS）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_category_materials`
--

DROP TABLE IF EXISTS `common_category_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_category_materials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_uuid` varchar(45) NOT NULL COMMENT '关联的品类id',
  `materials_uuid` varchar(45) NOT NULL COMMENT '关联的材质id',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `common_category_materials_category_idx` (`category_uuid`),
  KEY `common_category_materials_materials_idx` (`materials_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3349 DEFAULT CHARSET=utf8 COMMENT='品类对应的材质列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_category_norms`
--

DROP TABLE IF EXISTS `common_category_norms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_category_norms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_uuid` varchar(45) NOT NULL COMMENT '关联的品类ID',
  `norms_uuid` varchar(45) NOT NULL COMMENT '关联的规格ID',
  `range` varchar(1000) NOT NULL COMMENT '该规格的取值范围',
  `range_type` varchar(45) NOT NULL COMMENT '范围计算类型\nInteger: range是以-号分隔的两个整数\nDecimal: range是以-号分隔的两个浮点数\nString: range是以,号分隔的字符串',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `common_category_norms_category_idx` (`category_uuid`),
  KEY `common_category_norms_norms_idx` (`norms_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=918 DEFAULT CHARSET=utf8 COMMENT='品名对应的规格表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_group_for_category`
--

DROP TABLE IF EXISTS `common_group_for_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_group_for_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '品类与分组对应表',
  `category_uuid` varchar(45) NOT NULL COMMENT '关联的品类UUID',
  `category_group_uuid` varchar(45) NOT NULL COMMENT '关联的分组UUID',
  `is_deleted` bit(1) NOT NULL COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=840 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_materials`
--

DROP TABLE IF EXISTS `common_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_materials` (
  `id` int(11) NOT NULL,
  `uuid` varchar(45) NOT NULL COMMENT '全站唯一标识',
  `name` varchar(45) NOT NULL COMMENT '材质名称',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid_UNIQUE` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='钢铁材质表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_norms`
--

DROP TABLE IF EXISTS `common_norms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_norms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(45) NOT NULL COMMENT '全站唯一标识',
  `name` varchar(45) NOT NULL COMMENT '规格名称（长、宽、高...）',
  `unit` varchar(45) NOT NULL COMMENT '衡量单位（毫米、厘米...）',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid_UNIQUE` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规格表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_site`
--

DROP TABLE IF EXISTS `common_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(45) NOT NULL COMMENT '全站唯一标识',
  `name` varchar(45) DEFAULT NULL COMMENT '站点名称',
  `is_deleted` bit(1) DEFAULT NULL COMMENT '逻辑删除',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid_UNIQUE` (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='平台站点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account`
--

DROP TABLE IF EXISTS `cust_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL COMMENT '公司代码',
  `name` varchar(100) NOT NULL COMMENT '全称',
  `addr` varchar(200) DEFAULT NULL COMMENT '地址',
  `zip` varchar(45) DEFAULT NULL COMMENT '邮编',
  `tel` varchar(45) DEFAULT NULL COMMENT '公司电话',
  `balance` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '现金余额',
  `balance_freeze` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '冻结余额',
  `balance_second_settlement` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '二次结算记账余额',
  `balance_second_settlement_freeze` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '二次结算冻结余额',
  `fax` varchar(45) DEFAULT NULL COMMENT '传真',
  `legal_person_name` varchar(45) DEFAULT NULL COMMENT '法人姓名',
  `mobil` varchar(45) DEFAULT NULL COMMENT '手机',
  `business` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '经营品种',
  `web_site_url` varchar(500) DEFAULT NULL COMMENT '网址',
  `business_type` enum('merchant','factory','terminal','next_terminal','other') DEFAULT NULL COMMENT '客户类型',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份ID',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市ID',
  `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID',
  `proxy_factory` varchar(45) DEFAULT NULL COMMENT '代理钢厂',
  `proxy_qty` decimal(18,6) DEFAULT '0.000000' COMMENT '代理数量 eg：1111.1111吨',
  `license_code` varchar(45) DEFAULT NULL COMMENT '营业执照注册号',
  `reg_address` varchar(100) DEFAULT NULL COMMENT '公司注册地址',
  `org_code` varchar(45) DEFAULT NULL COMMENT '组织机构代码',
  `bank_code` varchar(45) DEFAULT NULL COMMENT '开户银行行号',
  `bank_name_main` varchar(45) DEFAULT NULL COMMENT '开户银行主行 如 工商银行',
  `bank_name_branch` varchar(45) DEFAULT NULL COMMENT '开户银行支行 如XX支行',
  `bank_province_id` bigint(20) DEFAULT NULL COMMENT '银行所在省',
  `bank_city_id` bigint(20) DEFAULT NULL COMMENT '银行所在城市',
  `account_code` varchar(45) DEFAULT NULL COMMENT '银行账号',
  `tax_code` varchar(45) DEFAULT NULL COMMENT '税号',
  `reg_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间 其实就是录入时间',
  `last_bill_time` datetime DEFAULT NULL COMMENT '最后下单时间(买家)',
  `buyer_deal_total` int(11) NOT NULL DEFAULT '0' COMMENT '成交次数',
  `seller_deal_total` int(11) NOT NULL DEFAULT '0',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `consign_type` varchar(45) DEFAULT NULL COMMENT '寄售类型\nconsign 寄售\ntemp  临采',
  `manager_id` bigint(20) DEFAULT NULL COMMENT '现任交易管理员',
  `status` int(11) DEFAULT '1' COMMENT '0锁定1解锁 2 买家开票资料待审核 3买家开票资料不全 4买家开票资料审核不通过',
  `invoice_data_status` varchar(10) DEFAULT NULL COMMENT '买家开票资料状态1.审核通过 2.待审核 3.资料不全 4。审核失败 ',
  `invoice_data_decline_reason` varchar(255) DEFAULT NULL COMMENT '买家开票资料审核失败原因',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_cust_account_code` (`code`),
  UNIQUE KEY `uix_cust_account_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2320 DEFAULT CHARSET=utf8 COMMENT='客户公司列表及详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_assign_logs`
--

DROP TABLE IF EXISTS `cust_account_assign_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_assign_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `reg_time` datetime NOT NULL COMMENT '注册时间',
  `manager_ex_id` bigint(20) NOT NULL COMMENT '前管理员ID',
  `manager_ex_name` varchar(45) NOT NULL COMMENT '前管理员姓名',
  `manager_next_id` bigint(20) NOT NULL COMMENT '现管理员ID',
  `manager_next_name` varchar(45) NOT NULL COMMENT '现管理员姓名',
  `assigner_id` bigint(20) NOT NULL COMMENT '分配人ID',
  `assigner_name` varchar(45) NOT NULL COMMENT '分配人姓名',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `created` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) DEFAULT NULL,
  `last_updated` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) DEFAULT NULL,
  `modification_number` int(20) DEFAULT NULL,
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1120 DEFAULT CHARSET=utf8 COMMENT='客户公司划转日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_attachment`
--

DROP TABLE IF EXISTS `cust_account_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `url` varchar(100) NOT NULL COMMENT '附件地址',
  `type` enum('license','org_code','tax_reg','taxpayer_evidence','invoice_data','legal_ID','payment_data','consign_contract','enterprise_survey','open_account_license') NOT NULL COMMENT '附件类型\npic_license("license", "营业执照"), \n	pic_org_code("org_code", "组织机构代码证"), \n	pic_tax_reg("tax_reg", "税务登记证"),\n pic_taxpayer_evidence("taxpayer_evidence", "一般纳税人证明"), \n	pic_invoice_data("invoice_data", "开票资料"), \n	pic_legal_ID("legal_ID", "法人身份证"), \n	pic_payment_data("payment_data", "打款资料"), \n	pic_consign_contract("consign_contract", "寄售合同"), \n	pic_enterprise_survey("enterprise_survey","企业调查表"),\n	pic_open_account_license("open_account_license","开户许可证");',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7504 DEFAULT CHARSET=utf8 COMMENT='客户公司附件信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_bak_20151021`
--

DROP TABLE IF EXISTS `cust_account_bak_20151021`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_bak_20151021` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `code` varchar(45) NOT NULL COMMENT '公司代码',
  `name` varchar(100) NOT NULL COMMENT '全称',
  `addr` varchar(200) DEFAULT NULL COMMENT '地址',
  `zip` varchar(45) DEFAULT NULL COMMENT '邮编',
  `tel` varchar(45) DEFAULT NULL COMMENT '公司电话',
  `balance` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '现金余额',
  `balance_freeze` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '冻结余额',
  `balance_second_settlement` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '二次结算记账余额',
  `balance_second_settlement_freeze` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '二次结算冻结余额',
  `fax` varchar(45) DEFAULT NULL COMMENT '传真',
  `legal_person_name` varchar(45) DEFAULT NULL COMMENT '法人姓名',
  `mobil` varchar(45) DEFAULT NULL COMMENT '手机',
  `business` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '经营品种',
  `web_site_url` varchar(500) DEFAULT NULL COMMENT '网址',
  `business_type` enum('merchant','factory','terminal','next_terminal','other') DEFAULT NULL COMMENT '客户类型',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份ID',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市ID',
  `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID',
  `proxy_factory` varchar(45) DEFAULT NULL COMMENT '代理钢厂',
  `proxy_qty` decimal(18,6) DEFAULT '0.000000' COMMENT '代理数量 eg：1111.1111吨',
  `license_code` varchar(45) DEFAULT NULL COMMENT '营业执照注册号',
  `reg_address` varchar(100) DEFAULT NULL COMMENT '公司注册地址',
  `org_code` varchar(45) DEFAULT NULL COMMENT '组织机构代码',
  `bank_code` varchar(45) DEFAULT NULL COMMENT '开户银行行号',
  `bank_name_main` varchar(45) DEFAULT NULL COMMENT '开户银行主行 如 工商银行',
  `bank_name_branch` varchar(45) DEFAULT NULL COMMENT '开户银行支行 如XX支行',
  `bank_province_id` bigint(20) DEFAULT NULL COMMENT '银行所在省',
  `bank_city_id` bigint(20) DEFAULT NULL COMMENT '银行所在城市',
  `account_code` varchar(45) DEFAULT NULL COMMENT '银行账号',
  `tax_code` varchar(45) DEFAULT NULL COMMENT '税号',
  `reg_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间 其实就是录入时间',
  `last_bill_time` datetime DEFAULT NULL COMMENT '最后下单时间(买家)',
  `buyer_deal_total` int(11) NOT NULL DEFAULT '0' COMMENT '成交次数',
  `seller_deal_total` int(11) NOT NULL DEFAULT '0',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `consign_type` varchar(45) DEFAULT NULL COMMENT '寄售类型\nconsign 寄售\ntemp  临采',
  `manager_id` bigint(20) DEFAULT NULL COMMENT '现任交易管理员',
  `status` int(11) DEFAULT '1' COMMENT '0锁定1解锁 2 买家开票资料待审核 3买家开票资料不全 4买家开票资料审核不通过',
  `invoice_data_status` varchar(10) DEFAULT NULL COMMENT '买家开票资料状态1.审核通过 2.待审核 3.资料不全 4。审核失败 ',
  `invoice_data_decline_reason` varchar(255) DEFAULT NULL COMMENT '买家开票资料审核失败原因',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_bank`
--

DROP TABLE IF EXISTS `cust_account_bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_bank` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `bank_code` varchar(45) NOT NULL COMMENT '开户行编号',
  `bank_name` varchar(45) NOT NULL COMMENT '开户银行主行 如 工商银行',
  `bank_name_branch` varchar(45) DEFAULT NULL COMMENT '开户银行支行 如XX支行',
  `bank_province_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '银行所在省',
  `bank_city_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '银行所在城市',
  `bank_province_name` varchar(45) DEFAULT NULL COMMENT '银行所在省',
  `bank_city_name` varchar(45) DEFAULT NULL COMMENT '银行所在城市',
  `bank_account_code` varchar(45) NOT NULL COMMENT '银行账号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_cust_account_bank_code` (`bank_account_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2199 DEFAULT CHARSET=utf8 COMMENT='客户公司银行账号';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_contact`
--

DROP TABLE IF EXISTS `cust_account_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `name` varchar(45) NOT NULL COMMENT '姓名',
  `tel` varchar(45) NOT NULL COMMENT '联系电话',
  `dept_name` varchar(45) DEFAULT NULL COMMENT '部门',
  `qq` varchar(45) DEFAULT NULL COMMENT 'QQ',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `is_main` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主要联系人',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0锁定1正常',
  `manager` bigint(20) NOT NULL COMMENT '钢为管理员ID',
  `note` varchar(45) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL,
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL COMMENT '用户身份类型，buyer seller 买家卖家',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1563 DEFAULT CHARSET=utf8 COMMENT='客户公司联系人';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_contact_assign_logs`
--

DROP TABLE IF EXISTS `cust_account_contact_assign_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_contact_assign_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `reg_time` datetime NOT NULL COMMENT '注册时间',
  `contact_name` varchar(45) NOT NULL COMMENT '客户联系人',
  `manager_ex_id` bigint(20) NOT NULL COMMENT '前管理员ID',
  `manager_ex_name` varchar(45) NOT NULL COMMENT '前管理员姓名',
  `manager_next_id` bigint(20) NOT NULL COMMENT '现管理员ID',
  `manager_next_name` varchar(45) NOT NULL COMMENT '现管理员姓名',
  `assigner_id` bigint(20) NOT NULL COMMENT '分配人ID',
  `assigner_name` varchar(45) NOT NULL COMMENT '分配人姓名',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=620 DEFAULT CHARSET=utf8 COMMENT='客户公司联系人划转日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_contract_template`
--

DROP TABLE IF EXISTS `cust_account_contract_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_contract_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户ID',
  `name` varchar(45) DEFAULT NULL COMMENT '模板名称',
  `thumbnail_url` varchar(100) DEFAULT NULL COMMENT '缩略图地址',
  `content` text NOT NULL COMMENT '合同模板',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `enabled` int(11) NOT NULL DEFAULT '0' COMMENT '是否启用 0未启用 1启用',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1273 DEFAULT CHARSET=utf8 COMMENT='客户公司合同模板';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cust_account_trans_log`
--

DROP TABLE IF EXISTS `cust_account_trans_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cust_account_trans_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL COMMENT '客户',
  `consign_order_code` varchar(45) DEFAULT NULL COMMENT '寄售单号',
  `serial_number` varchar(45) DEFAULT NULL COMMENT '相关流水号',
  `apply_type` varchar(30) DEFAULT NULL COMMENT '申请类型:\n1 充值到资金账户\n2 支付合同货款\n3 二次结算\n4 资金账户转出\n5 抵扣二次结算账户欠款\n6 二次结算账户余额转入资金账户\n7 抵扣二次结算账户欠款\n8 合同款到账\n9 二次结算账户余额转入资金账户\n',
  `amount` decimal(18,6) NOT NULL COMMENT '发生金额',
  `applyer_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `applyer_name` varchar(45) NOT NULL COMMENT '申请人姓名',
  `serial_time` datetime NOT NULL COMMENT '流水时间',
  `type` enum('buyer','seller','both') DEFAULT NULL COMMENT '类型（买家、卖家、两者都是）',
  `current_balance` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '当前余额',
  `pay_type` enum('balance','settlement','freeze') DEFAULT NULL COMMENT '支付类型1.余额(balance)2.结算(settlement)3.冻结(freeze)',
  `association_type` varchar(30) DEFAULT NULL COMMENT '关联类型：\n１付款单号\n２寄售交易单号\n３银行流水号\n4 还款流水号',
  `cash_happen_balance` decimal(18,6) DEFAULT '0.000000' COMMENT '现金发生余额',
  `cash_current_balance` decimal(18,6) DEFAULT '0.000000' COMMENT '现金当前余额',
  `status` int(11) DEFAULT '1' COMMENT '0未显示1显示',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT '0',
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(45) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27536 DEFAULT CHARSET=utf8 COMMENT='客户公司账目划转详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_express`
--

DROP TABLE IF EXISTS `inv_express`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company` varchar(45) NOT NULL COMMENT '快递公司名称 顺丰,EMS,人肉（自己带）',
  `express_name` varchar(45) NOT NULL COMMENT '快递单号，快递公司类型为人肉则是运送人姓名',
  `type` varchar(45) NOT NULL COMMENT '类型 IN进项票OUT销项票',
  `send_time` datetime DEFAULT NULL COMMENT '寄出时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=397 DEFAULT CHARSET=utf8 COMMENT='快递单信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_import_temp`
--

DROP TABLE IF EXISTS `inv_import_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_import_temp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_detail_id` bigint(20) NOT NULL COMMENT '订单详情ID',
  `code` varchar(45) NOT NULL COMMENT '发票号',
  `invoice_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发票日期',
  `nsort_name_comb` varchar(100) DEFAULT NULL COMMENT '货物或应税劳务、服务名称',
  `spec` varchar(45) DEFAULT NULL COMMENT '规格',
  `weight` decimal(18,6) NOT NULL COMMENT '数量',
  `no_tax_amount` decimal(18,6) NOT NULL COMMENT '金额',
  `tax_amount` decimal(18,6) NOT NULL COMMENT '税额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1114 DEFAULT CHARSET=utf8 COMMENT='角色目录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_in`
--

DROP TABLE IF EXISTS `inv_invoice_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(45) DEFAULT NULL COMMENT '区位码（10位）',
  `code` varchar(45) DEFAULT NULL COMMENT '发票号',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `seller_name` varchar(45) NOT NULL COMMENT '卖家名称',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总重量',
  `check_total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '核实总金额',
  `check_total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '核实总重量',
  `invoice_date` datetime DEFAULT NULL COMMENT '发票日期',
  `check_date` datetime DEFAULT NULL COMMENT '核算日期',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '核实员ID',
  `check_user_name` varchar(45) DEFAULT NULL COMMENT '核实人姓名',
  `check_user_mobil` varchar(45) DEFAULT NULL COMMENT '核实员电话',
  `input_user_id` bigint(20) DEFAULT NULL COMMENT '录入员ID',
  `input_user_name` varchar(45) DEFAULT NULL COMMENT '录入人姓名',
  `input_user_mobil` varchar(45) DEFAULT NULL COMMENT '录入员电话',
  `status` varchar(45) DEFAULT NULL COMMENT '发票状态：待寄出RECEIVED，待确认SENT，待认证(已确认)WAIT，已认证ALREADY，已作废CANCEL',
  `is_defer` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否延迟1(延迟)0(正常)',
  `relation_status` varchar(45) DEFAULT NULL COMMENT '已确认:(待关联toberelation，已关联hasrelation)，已认证:(已开、未开）',
  `express_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '快递单ID',
  `print_status` varchar(45) DEFAULT NULL COMMENT '打印状态',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uix_inv_invoice_in_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3145 DEFAULT CHARSET=utf8 COMMENT='进项票主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_in_detail`
--

DROP TABLE IF EXISTS `inv_invoice_in_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_in_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_in_id` bigint(20) NOT NULL,
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '重量',
  `no_tax_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '不含税金额',
  `tax_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '税额',
  `amount` decimal(18,6) DEFAULT '0.000000' COMMENT '含税金额',
  `nsort_name_comb` varchar(200) DEFAULT NULL COMMENT '品名与材质组合（组合中间会有空格,不能出现“详见清单”字样)',
  `type_of_spec` varchar(45) DEFAULT NULL COMMENT '参考值： 直径，折扣',
  `actual_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '实收金额（关联订单子项会有偏差）',
  `status` varchar(45) DEFAULT NULL COMMENT '待关联toberelation，已关联hasrelation',
  `check_weight` decimal(18,6) DEFAULT '0.000000' COMMENT '核算重量',
  `check_no_tax_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '核实发票无税金额',
  `check_tax_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '核实发票税金',
  `check_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '核算金额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5876 DEFAULT CHARSET=utf8 COMMENT='进项票主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_in_detail_orderitem`
--

DROP TABLE IF EXISTS `inv_invoice_in_detail_orderitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_in_detail_orderitem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_detail_id` bigint(20) NOT NULL COMMENT '进项票子项与交易单子项关联表',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `orderitem_id` bigint(20) NOT NULL COMMENT '订单明细id',
  `weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '开票重量（默认是全部，但也存在未开完《=的情况）',
  `amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '开票金额',
  `active` int(11) DEFAULT '1' COMMENT '1: 有效，0：无效（因财务修改发票子项导致）',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_orderitem_id_invoice_detail_id_active` (`orderitem_id`,`invoice_detail_id`,`active`)
) ENGINE=InnoDB AUTO_INCREMENT=8629 DEFAULT CHARSET=utf8 COMMENT='进项票和订单明细关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_in_owner_transition`
--

DROP TABLE IF EXISTS `inv_invoice_in_owner_transition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_in_owner_transition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_from_id` bigint(20) NOT NULL COMMENT '所有权转出服务中心ID',
  `org_from_name` varchar(45) NOT NULL COMMENT '所有权转出服务中心名称',
  `org_to_id` bigint(20) NOT NULL COMMENT '所有权转入服务中心ID',
  `org_to_name` varchar(45) NOT NULL COMMENT '所有权转入服务中心名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `seller_name` varchar(45) NOT NULL COMMENT '卖家名称',
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `not_invoiced_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未开票金额 二次结算时增加，进项票进来时减少',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='二次结算进项票所有权转移表，跨服务中心交易时产生的进项票所有权';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_in_owner_transition_detail`
--

DROP TABLE IF EXISTS `inv_invoice_in_owner_transition_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_in_owner_transition_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_transition_id` bigint(20) NOT NULL COMMENT '主表外键',
  `type` varchar(45) NOT NULL COMMENT '数据来源类型,ORDER订单,INVOICE发票',
  `source_id` bigint(20) NOT NULL COMMENT '来源票据明细ID，订单：busi_consign_order_item.id 进项票 inv_invoice_in_detail.id',
  `amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '金额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out`
--

DROP TABLE IF EXISTS `inv_invoice_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) DEFAULT NULL COMMENT '销项票真实票号',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '买家名称',
  `amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '发票金额',
  `check_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '内勤核算录入金额',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '核对内勤人员ID',
  `check_user_name` varchar(45) DEFAULT NULL COMMENT '核对内勤人员姓名',
  `check_date` datetime DEFAULT NULL COMMENT '核对日期',
  `invoice_date` datetime DEFAULT NULL COMMENT '发票日期',
  `input_user_id` bigint(20) DEFAULT NULL COMMENT '录入员ID',
  `input_user_name` varchar(45) DEFAULT NULL COMMENT '录入人姓名',
  `input_user_mobil` varchar(45) DEFAULT NULL COMMENT '录入员电话',
  `express_id` bigint(20) DEFAULT NULL COMMENT '快递单外键',
  `status` varchar(45) DEFAULT NULL COMMENT '发票状态 1待寄出 2 未确认 3确认通过 4确认失败',
  `print_status` varchar(45) DEFAULT NULL COMMENT '打印状态',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销项票主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_apply`
--

DROP TABLE IF EXISTS `inv_invoice_out_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(18,6) DEFAULT '0.000000' COMMENT '申请开票金额',
  `actual_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '实开金额',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `submitter_id` bigint(20) NOT NULL COMMENT '提交人ID',
  `submitter_name` varchar(45) NOT NULL COMMENT '提交人项目',
  `status` varchar(45) DEFAULT NULL COMMENT '待提交；\n待审核；\n审核通过；\n审核不通过；\n部分开票；\n撤销；\n已开。',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_invoice_out_apply_STATUS_is_deleted` (`status`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='开票申请主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_apply_detail`
--

DROP TABLE IF EXISTS `inv_invoice_out_apply_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_apply_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_out_apply_id` bigint(20) NOT NULL COMMENT '开票申请编号',
  `owner_id` bigint(20) NOT NULL COMMENT '交易员ID',
  `owner_name` varchar(45) NOT NULL COMMENT '交易员姓名',
  `org_id` bigint(20) DEFAULT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) DEFAULT NULL COMMENT '服务中心名称',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '买家名称',
  `apply_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '申请金额',
  `lend_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '挪用金额',
  `uninvoice_amount` decimal(18,6) DEFAULT '0.000000',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_invoice_out_apply_id_is_deleted` (`invoice_out_apply_id`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=utf8 COMMENT='开票申请详情';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_checklist`
--

DROP TABLE IF EXISTS `inv_invoice_out_checklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_checklist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL COMMENT '服务中心编号',
  `code` varchar(45) NOT NULL COMMENT '订单编号',
  `amount` decimal(18,6) DEFAULT '0.000000' COMMENT '申请开票金额',
  `status` varchar(45) DEFAULT NULL COMMENT '已开；\n部分开票；\n待开；\n暂缓。',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开票清单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_checklist_detail`
--

DROP TABLE IF EXISTS `inv_invoice_out_checklist_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_checklist_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `checklist_id` bigint(20) unsigned zerofill NOT NULL COMMENT '清单编号',
  `item_detail_id` bigint(20) NOT NULL COMMENT '申请编号',
  `amount` decimal(18,6) DEFAULT '0.000000' COMMENT '清单开票金额',
  `weight` decimal(18,6) DEFAULT '0.000000' COMMENT '清单开票重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请-清单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_item_detail`
--

DROP TABLE IF EXISTS `inv_invoice_out_item_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_item_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inv_invoice_in_id` bigint(20) NOT NULL COMMENT '进项票ID',
  `inv_invoice_in_detail_id` bigint(20) NOT NULL COMMENT '进项详情ID',
  `invoice_orderitem_id` bigint(20) NOT NULL COMMENT '进项票详情与订单详情关联表ID',
  `apply_detail_id` bigint(20) NOT NULL COMMENT '开票申请详情ID',
  `nsort_name` varchar(45) NOT NULL COMMENT '品名（来自于进项票）',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '开票重量',
  `actual_weight` decimal(18,6) DEFAULT '0.000000' COMMENT '实开重量',
  `price` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '单价',
  `no_tax_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '不含税金额',
  `tax_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '税额',
  `amount` decimal(18,6) DEFAULT '0.000000' COMMENT '含税金额',
  `actual_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '实开金额',
  `order_detail_id` bigint(20) NOT NULL COMMENT '订单详情ID',
  `status` varchar(45) DEFAULT NULL COMMENT '已开；未开。',
  `is_defer` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否延迟1(延迟)0(正常)',
  `is_deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除： 0 未删除；1 已删除。',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_inv_invoice_in_detail_id_apply_detail_id_is_deleted` (`inv_invoice_in_detail_id`,`apply_detail_id`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2681 DEFAULT CHARSET=utf8 COMMENT='开票申请详情项';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_out_receipt`
--

DROP TABLE IF EXISTS `inv_invoice_out_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_out_receipt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `apply_detail_id` bigint(20) NOT NULL COMMENT '申请ID',
  `item_detail_id` bigint(20) NOT NULL COMMENT '详情项iID',
  `checklist_id` bigint(20) NOT NULL COMMENT '清单ID',
  `amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '开票金额',
  `weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '开票重量',
  `djh` varchar(45) NOT NULL COMMENT '单据号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='票据关联表（爱信诺票据信息）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_invoice_pool`
--

DROP TABLE IF EXISTS `inv_invoice_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_invoice_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) DEFAULT NULL COMMENT '发票类型 IN进项票 OUT销项票(系统生成)',
  `invoice_code` varchar(45) NOT NULL COMMENT '发票号',
  `org_id` bigint(20) NOT NULL,
  `org_name` varchar(45) NOT NULL COMMENT '归属服务中心',
  `sort_name` varchar(45) DEFAULT NULL COMMENT '大类',
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `no_tax_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '不含税金额',
  `tax_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '税额',
  `amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '含税金额',
  `weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '重量',
  `invoice_date` datetime NOT NULL COMMENT '开票确认日期，注：这里的日期不是发票的开票日期，是确认日期，如果一张进项票开票时间是7月，确认日期是8月，算作8月的开票，此字段用于统计当月进销项票总额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发票池';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_org_invoice_out_apply`
--

DROP TABLE IF EXISTS `inv_org_invoice_out_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_org_invoice_out_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(18,6) DEFAULT NULL COMMENT '申请开票金额',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `submitter_id` bigint(20) NOT NULL COMMENT '提交人ID',
  `submitter_name` varchar(45) NOT NULL COMMENT '提交人项目',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务中心开票申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_org_invoice_out_balance_monthly`
--

DROP TABLE IF EXISTS `inv_org_invoice_out_balance_monthly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_org_invoice_out_balance_monthly` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `month` varchar(45) DEFAULT NULL COMMENT 'YYYYMM月份',
  `org_id` bigint(20) DEFAULT NULL,
  `org_name` varchar(45) DEFAULT NULL,
  `invoice_in_amount` decimal(18,6) DEFAULT NULL COMMENT '当月进项金额',
  `invoice_out_amount` decimal(18,6) DEFAULT NULL COMMENT '当月销项金额',
  `invoice_out_balance` decimal(18,6) DEFAULT NULL COMMENT '历史可开销项票结余',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务中心每月销项票余额记录 每月定时job计算';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_org_invoice_out_item_detail`
--

DROP TABLE IF EXISTS `inv_org_invoice_out_item_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_org_invoice_out_item_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_out_main_id` bigint(20) NOT NULL COMMENT '发票编号ID 外键',
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `weight` decimal(18,6) NOT NULL COMMENT '重量',
  `price` decimal(18,6) NOT NULL COMMENT '单价',
  `no_tax_amount` decimal(18,6) NOT NULL COMMENT '不含税金额',
  `tax_amount` decimal(18,6) NOT NULL COMMENT '税额',
  `amount` decimal(18,6) DEFAULT NULL COMMENT '含税金额',
  `export_time` datetime DEFAULT NULL COMMENT '首次导出实际',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开票计算结果（品规级）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_org_invoice_out_main`
--

DROP TABLE IF EXISTS `inv_org_invoice_out_main`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_org_invoice_out_main` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_apply_id` bigint(20) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL COMMENT '系统生成发票编号',
  `real_code` varchar(45) DEFAULT NULL COMMENT '真实销项发票号',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '买家名称',
  `total_no_tax_amount` decimal(18,6) DEFAULT NULL COMMENT '总不含税金额',
  `total_tax_amount` decimal(18,6) DEFAULT NULL COMMENT '总税额',
  `total_amount` decimal(18,6) NOT NULL COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL COMMENT '总重量',
  `real_invoice_date` datetime DEFAULT NULL COMMENT '真实发票开票日期',
  `status` varchar(45) DEFAULT NULL COMMENT '0财务未开票，1财务已开票',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='销项票发票号（系统生成）表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_org_invoice_out_order_item`
--

DROP TABLE IF EXISTS `inv_org_invoice_out_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_org_invoice_out_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_detail_id` bigint(20) NOT NULL COMMENT '外键 开票结果品规级主键',
  `order_item_id` bigint(20) NOT NULL COMMENT '订单明细ID',
  `weight` decimal(18,6) NOT NULL COMMENT '开票重量',
  `amount` decimal(18,6) NOT NULL COMMENT '开票金额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开票计算结果(订单明细级)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_in`
--

DROP TABLE IF EXISTS `inv_pool_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_in` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家ID',
  `seller_name` varchar(45) NOT NULL COMMENT '卖家名称',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总重量',
  `total_received_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已收总金额',
  `total_received_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已收总重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1970 DEFAULT CHARSET=utf8 COMMENT='进项池主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_in_detail`
--

DROP TABLE IF EXISTS `inv_pool_in_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_in_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pool_in_id` bigint(20) NOT NULL,
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总重量',
  `received_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已收金额',
  `received_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已收重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7637 DEFAULT CHARSET=utf8 COMMENT='进项池明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_out`
--

DROP TABLE IF EXISTS `inv_pool_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `owner_id` bigint(20) NOT NULL COMMENT '交易员ID',
  `owner_name` varchar(45) NOT NULL COMMENT '交易员姓名',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '买家名称',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总重量',
  `total_sent_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已付总金额',
  `total_sent_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已付总重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1132 DEFAULT CHARSET=utf8 COMMENT='进项池主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_out_detail`
--

DROP TABLE IF EXISTS `inv_pool_out_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_out_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pool_out_id` bigint(20) NOT NULL,
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未开票总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未开票总重量',
  `sent_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已开票金额',
  `sent_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已开票重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4702 DEFAULT CHARSET=utf8 COMMENT='进项池明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_out_detail_temp`
--

DROP TABLE IF EXISTS `inv_pool_out_detail_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_out_detail_temp` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `pool_out_id` bigint(20) NOT NULL,
  `nsort_name` varchar(45) NOT NULL COMMENT '品名',
  `material` varchar(45) NOT NULL COMMENT '材质',
  `spec` varchar(45) NOT NULL COMMENT '规格',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未开票总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未开票总重量',
  `sent_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已开票金额',
  `sent_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已开票重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inv_pool_out_temp`
--

DROP TABLE IF EXISTS `inv_pool_out_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_pool_out_temp` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `org_name` varchar(45) NOT NULL COMMENT '服务中心名称',
  `owner_id` bigint(20) NOT NULL COMMENT '交易员ID',
  `owner_name` varchar(45) NOT NULL COMMENT '交易员姓名',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(45) NOT NULL COMMENT '买家名称',
  `total_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总金额',
  `total_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '总重量',
  `total_sent_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已付总金额',
  `total_sent_weight` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '已付总重量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(20) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(255) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_new_user_reward`
--

DROP TABLE IF EXISTS `report_new_user_reward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_new_user_reward` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `add_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提成日期（按天统计）',
  `manager_id` bigint(20) DEFAULT NULL COMMENT '交易员ID',
  `manager_name` varchar(255) DEFAULT NULL COMMENT '交易员姓名',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `org_name` varchar(255) DEFAULT NULL COMMENT '组织名称',
  `active` bit(1) NOT NULL DEFAULT b'0' COMMENT '这笔提成是否生效',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `add_new_buyer` bigint(20) DEFAULT NULL COMMENT '新增买家id',
  `add_new_seller` bigint(20) DEFAULT NULL COMMENT '新增卖家id',
  `seller_reward_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增卖家提成金额',
  `buyer_reward_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增买家提成金额',
  `buyer_reward_role` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增买家提成规则',
  `seller_reward_role` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增卖家提成规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8 COMMENT='添加新客户的提成报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_rebate_record`
--

DROP TABLE IF EXISTS `report_rebate_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_rebate_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rebate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '返利时间',
  `account_id` bigint(20) DEFAULT NULL COMMENT '公司ID',
  `account_name` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `org_id` bigint(11) DEFAULT NULL COMMENT '组织ID',
  `org_name` varchar(255) DEFAULT NULL COMMENT '组织名称',
  `manager_id` bigint(11) DEFAULT NULL COMMENT '交易员ID',
  `manager_name` varchar(255) DEFAULT NULL COMMENT '交易员姓名',
  `contact_id` bigint(20) DEFAULT NULL COMMENT '联系人ID',
  `contact_name` varchar(255) DEFAULT NULL COMMENT '联系人姓名',
  `category_group_UUID` varchar(255) DEFAULT NULL COMMENT '大类UUID',
  `category_group_name` varchar(255) DEFAULT NULL COMMENT '大类名称',
  `weight` decimal(18,6) DEFAULT NULL COMMENT '重量',
  `amount` decimal(18,6) DEFAULT NULL COMMENT '金额',
  `rebate_amount` decimal(18,6) DEFAULT NULL COMMENT '返利总金额',
  `rebate_balance` decimal(18,6) DEFAULT NULL COMMENT '返利余额',
  `code` varchar(45) DEFAULT NULL COMMENT '单号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8917 DEFAULT CHARSET=utf8 COMMENT='买家返利报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_reward_record`
--

DROP TABLE IF EXISTS `report_reward_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_reward_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `order_items_id` bigint(20) NOT NULL COMMENT '订单详情id',
  `reward_date` datetime DEFAULT NULL COMMENT '提成日期（按天统计）',
  `category_group_UUID` varchar(255) DEFAULT NULL COMMENT '大类名称',
  `category_group_name` varchar(255) DEFAULT NULL COMMENT '大类名称',
  `buyer_manager_id` bigint(20) DEFAULT NULL COMMENT '买家交易员ID',
  `buyer_manager_name` varchar(255) DEFAULT NULL COMMENT '买家交易员姓名',
  `category_rewart_role_id` bigint(20) DEFAULT NULL COMMENT '品类提成规则id',
  `category_rewart_role` decimal(18,6) DEFAULT NULL COMMENT '品类提成规则（元/吨）',
  `buyer_rewart_role_id` bigint(20) DEFAULT NULL COMMENT '买家提成规则id',
  `seller_rewart_role_id` bigint(20) DEFAULT NULL COMMENT '卖家提成规则id',
  `status` varchar(45) DEFAULT NULL COMMENT '预留字段，是否已发放该提成  NO还未发放  YES已发放',
  `is_deleted` bit(1) DEFAULT NULL,
  `buyer_manager_org_id` bigint(20) DEFAULT NULL COMMENT '买家交易员组织ID',
  `buyer_manager_org_name` varchar(255) DEFAULT NULL COMMENT '买家交易员组织名称',
  `seller_manager_id` bigint(20) DEFAULT NULL COMMENT '卖家交易员id',
  `seller_manager_name` varchar(255) DEFAULT NULL COMMENT '卖家交易员名字',
  `seller_manager_org_id` bigint(20) DEFAULT NULL COMMENT '卖家交易员组织ID',
  `seller_manager_org_name` varchar(255) DEFAULT NULL COMMENT '卖家交易员组织名称',
  `consign_type` varchar(45) DEFAULT NULL COMMENT '卖家类型consign寄售卖家temp临采卖家',
  `weight` decimal(18,6) DEFAULT NULL COMMENT '实提重量',
  `buyer_rewart_role` decimal(18,6) DEFAULT NULL COMMENT '买家提成规则百分比',
  `seller_rewart_role` decimal(18,6) DEFAULT NULL COMMENT '卖家提成规则百分比',
  `buyer_reward_amount` decimal(18,6) DEFAULT NULL COMMENT '买家提成总金额',
  `seller_reward_amount` decimal(18,6) DEFAULT NULL COMMENT '卖家提成总金额',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11879 DEFAULT CHARSET=utf8 COMMENT='提成报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_withdraw_record`
--

DROP TABLE IF EXISTS `report_withdraw_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_withdraw_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `withdraw_date` datetime DEFAULT NULL COMMENT '提现时间',
  `account_id` bigint(100) DEFAULT '0' COMMENT '公司ID',
  `contact_id` varchar(255) DEFAULT '0' COMMENT '用户ID',
  `contact_name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `contact_tel` varchar(50) DEFAULT NULL COMMENT '用户电话（来自IV）',
  `withdraw_amount` decimal(18,6) DEFAULT '0.000000' COMMENT '提现金额',
  `balance` decimal(18,6) DEFAULT '0.000000' COMMENT '提现后余额',
  `sync_id` int(11) DEFAULT NULL COMMENT '同步ID，IV数据主键，用于同步记录到cbms',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='提现记录报表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temp`
--

DROP TABLE IF EXISTS `temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `money` decimal(18,6) DEFAULT NULL,
  `hit` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=269 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `temp_inv_pool`
--

DROP TABLE IF EXISTS `temp_inv_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `temp_inv_pool` (
  `row_id` double DEFAULT NULL,
  `rownum` double DEFAULT NULL,
  `buy_id` bigint(20) COMMENT '买家ID',
  `buy_name` varchar(45) COMMENT '买家企业名称',
  `owner_id` bigint(20) COMMENT '交易员ID',
  `owner_name` varchar(45) COMMENT '用户姓名',
  `org_id` bigint(20) COMMENT '组织ID',
  `org_name` varchar(45) COMMENT '名称',
  `nsort_name` varchar(45) DEFAULT NULL COMMENT '品名',
  `material` varchar(45) DEFAULT NULL COMMENT '材质',
  `spec` varchar(45) DEFAULT NULL COMMENT '规格',
  `total_weight` decimal(40,6) DEFAULT NULL,
  `total_amount` decimal(49,2) DEFAULT NULL,
  `pool_id` int(6) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-23 16:52:54
