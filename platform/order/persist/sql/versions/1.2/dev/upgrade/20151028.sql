-- ----------------------------
-- Describe: busi_bank_transaction_info 添加到账银行类型字段
-- Author: kongbinheng
-- Date: 10/28/2015 15:50
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_bank_transaction_info`
ADD COLUMN `bank_type` varchar(45) NULL DEFAULT 'SPDB' COMMENT '到账银行类型:SPDB-浦发银行,ICBC-工商银行' AFTER `status`;

-- ----------------------------
-- Describe: busi_icbc_bdl_header 工行银企直连主表
-- Author: kongbinheng
-- Date: 10/28/2015 15:50
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`busi_icbc_bdl_header` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `AccNo` VARCHAR(45) NOT NULL COMMENT '账号',
  `AccName` VARCHAR(200) NULL COMMENT '户名',
  `CurrType` VARCHAR(10) NULL COMMENT '币种',
  `BeginDate` VARCHAR(20) NULL COMMENT '起始日期',
  `EndDate` VARCHAR(20) NULL COMMENT '截止日期',
  `MinAmt` VARCHAR(45) NULL COMMENT '发生额下限',
  `MaxAmt` VARCHAR(45) NULL COMMENT '发生额上限',
  `NextTag` VARCHAR(200) NULL COMMENT '查询下页标识\n',
  `TotalNum` VARCHAR(45) NULL COMMENT '交易条数',
  `RepReserved1` VARCHAR(100) NULL COMMENT '响应备用字段1',
  `RepReserved2` VARCHAR(300) NULL COMMENT '响应备用字段2',
  `status` INT NULL DEFAULT 0 COMMENT '0未处理1处理完2处理中',
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
COMMENT = '工行银企直连主表';

-- ----------------------------
-- Describe: busi_icbc_bdl_detail 工行银企直连明细表
-- Author: kongbinheng
-- Date: 10/28/2015 15:50
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`busi_icbc_bdl_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `Drcrf` VARCHAR(10) NULL COMMENT '借贷标志1:借 2:贷',
  `VouhNo` VARCHAR(45) NULL COMMENT '凭证号',
  `DebitAmount` VARCHAR(45) NULL COMMENT '借方发生额',
  `CreditAmount` VARCHAR(45) NULL COMMENT '贷方发生额',
  `Balance` VARCHAR(45) NULL COMMENT '余额',
  `RecipBkNo` VARCHAR(45) NULL COMMENT '对方行号',
  `RecipBkName` VARCHAR(500) NULL COMMENT '对方行名\n',
  `RecipAccNo` VARCHAR(45) NULL COMMENT '对方账号',
  `RecipName` VARCHAR(500) NULL COMMENT '对方户名',
  `Summary` VARCHAR(100) NULL COMMENT '摘要',
  `UseCN` VARCHAR(100) NULL COMMENT '用途',
  `PostScript` VARCHAR(300) NULL COMMENT '附言',
  `BusCode` VARCHAR(60) NULL COMMENT '业务代码',
  `Date` VARCHAR(20) NULL COMMENT '交易日期',
  `Time` VARCHAR(45) NULL COMMENT '时间戳',
  `Ref` VARCHAR(45) NULL COMMENT '业务编号',
  `Oref` VARCHAR(45) NULL COMMENT '相关业务编号',
  `EnSummary` VARCHAR(100) NULL COMMENT '英文备注',
  `BusType` VARCHAR(45) NULL COMMENT '业务种类',
  `VouhType` VARCHAR(45) NULL COMMENT '凭证种类',
  `AddInfo` VARCHAR(500) NULL COMMENT '附加信息',
  `RepReserved3` VARCHAR(300) NULL COMMENT '响应备用字段3',
  `RepReserved4` VARCHAR(45) NULL COMMENT '响应备用字段4',
  `begin_number` INT NOT NULL DEFAULT 0 COMMENT '查询起始笔数',
  `begin_date` VARCHAR(20) NULL COMMENT '查询起始日期',
  `end_date` VARCHAR(20) NULL COMMENT '查询结束日期',
  `status` INT NULL DEFAULT 0 COMMENT '0未处理1处理完2处理中',
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
COMMENT = '工行银企直连明细表';
