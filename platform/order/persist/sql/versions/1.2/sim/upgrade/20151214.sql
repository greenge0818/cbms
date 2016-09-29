-- ----------------------------
-- Describe: 承兑汇票清单表
-- Author: kongbinheng
-- Date: 2015-12-14 10:27:35
-- ----------------------------
CREATE TABLE IF NOT EXISTS `busi_accept_draft_list` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NOT NULL COMMENT '充值账户id',
  `account_name` VARCHAR(255) NOT NULL COMMENT '充值账户名称',
  `org_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '服务中心id',
  `org_name` VARCHAR(45) NULL COMMENT '服务中心名称',
  `code` VARCHAR(45) NOT NULL COMMENT '票号',
  `acceptance_bank_full_name` VARCHAR(255) NOT NULL COMMENT '承兑行全称',
  `amount` DECIMAL(18,6) NOT NULL COMMENT '金额',
  `end_date` DATETIME NOT NULL COMMENT '到期日',
  `discount_rate` DECIMAL(20,6) NOT NULL COMMENT '贴现率',
  `discount_rate_base` DECIMAL(20,6) NOT NULL COMMENT '贴现率成本',
  `end_date_approve` DATETIME NULL COMMENT '财务审核银票到期时间',
  `code_approve` VARCHAR(45) NULL COMMENT '财务审核票号',
  `amount_approve` DECIMAL(18,6) NULL COMMENT '财务审核金额',
  `status` VARCHAR(45) NOT NULL DEFAULT 'NEW' COMMENT '状态\nNEW 保存待提交\nSUBMITTED 已录入待审核\nCHARGED 审核（双敲）通过，已充值--双敲匹配失败回到待提交\nROLLBACKREQUEST 回滚申请\nRENEW 充值回滚已完成',
  `reason` VARCHAR(45) NULL COMMENT '理由',
  `acceptance_date` DATETIME NULL COMMENT '承兑日',
  `acceptance_bank_code` VARCHAR(45) NULL COMMENT '承兑行行号',
  `drawer_name` VARCHAR(255) NULL COMMENT '出票人名称\n',
  `drawer_account_code` VARCHAR(45) NULL COMMENT '出票人账号',
  `drawer_bank_code` VARCHAR(45) NULL COMMENT '出票人开户行行号\n\n',
  `drawer_bank_full_name` VARCHAR(255) NULL COMMENT '出票人开户行全称\n',
  `receiver_name` VARCHAR(255) NULL COMMENT '收款人名称\n',
  `receiver_account_code` VARCHAR(45) NULL COMMENT '收款人账号',
  `receiver_bank_full_name` VARCHAR(255) NULL COMMENT '收款人开户行全称',
  `adjust_date_count` INT NULL COMMENT '调整天数',
  `read_times` INT NULL COMMENT '背书次数',
  `acceptance_agreement_number` VARCHAR(45) NULL DEFAULT 0 COMMENT '承兑协议编号',
  `is_different_bank` VARCHAR(10) NULL COMMENT '是否我行代开他行签发票据\n',
  `is_payed` BIT(1) NOT NULL DEFAULT 0 COMMENT '是否已经用于支付货款 0 没有   1  有',
  `is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '逻辑删0 正常   1 删除',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT '0',
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uix_busi_accept_draft_list_code` (`code` ASC)
 )
COMMENT = '承兑汇票清单';

-- ----------------------------
-- Describe: 邮件发送报表配置表
-- Author: tuxianming
-- Date: 2015-12-14 10:27:35
-- ----------------------------
CREATE TABLE `base_sys_report_mail_setting` (
	`id`  bigint NOT NULL AUTO_INCREMENT COMMENT '主键' ,
	`title`  varchar(50) NULL COMMENT '邮件标题' ,
	`content`  varchar(3000) NOT NULL COMMENT '邮件内容模版' ,
	`attachment`  varchar(100) NOT NULL COMMENT '邮件附件类型' ,
	`send_time`  varchar(30) NOT NULL DEFAULT '00:00' COMMENT '邮件发送时间' ,
	`receiver`  varchar(500) NOT NULL COMMENT '邮件接收人，多个接收人以英文\";\"分割' ,
	`last_send`  timestamp NULL COMMENT '邮件最后发送时间，用来判断时间是不是已经发送过' ,
	`period_type`  varchar(30) NULL COMMENT '邮件类型： WEEK=\"第几周的：星期一，星期二，星期三...， weeks字段用来存储\"; MONTH1=\"每月的第几天， day:第几天\"； MONTH2=\"每月的第几个星期的第几天, weekDuration: 第几周， weeks: 的值为星期几\"' ,
	`weeks`  varchar(30) NULL COMMENT '当type=\"WEEK\"时，weeks为数组，当为MONTH2时， weeks:为一个数值 ' ,
	`week_duration`  int NULL COMMENT '间隔几个星期', 
	`month_duration` int NULL COMMENT '间隔几个月',
	`day` int NULL COMMENT '第几天',
	`ext`  varchar(100),
	`is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '逻辑删0 正常   1 删除',
	`created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`created_by` VARCHAR(45) NOT NULL,
	`last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`last_updated_by` VARCHAR(45) NOT NULL,
	`modification_number` INT(20) NOT NULL DEFAULT '0',
	`row_id` VARCHAR(45) NULL,
	`parent_row_id` VARCHAR(45) NULL,
	PRIMARY KEY (`id`)
);
