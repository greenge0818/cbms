-- ----------------------------
-- Describe: 林中提供
-- Author: Rolyer Luo
-- Date: 08/04/2015 22:07
-- ----------------------------
ALTER TABLE `cust_account`
CHANGE COLUMN `bank_provincie_id` `bank_province_id`  bigint(20) NULL DEFAULT NULL COMMENT '银行所在省' AFTER `bank_name_branch`,
MODIFY COLUMN `status`  int(11) NOT NULL DEFAULT 1 COMMENT '0锁定1解锁 2 买家开票资料待审核 3买家开票资料不全' AFTER `manager_id`;

ALTER TABLE `cust_account_attachment`
MODIFY COLUMN `type`  enum('license','org_code','tax_reg','taxpayer_evidence','invoice_data','legal_ID','payment_data','consign_contract','enterprise_survey','open_account_license') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '附件类型\npic_license(\"license\", \"营业执照\"), \n	pic_org_code(\"org_code\", \"组织机构代码证\"), \n	pic_tax_reg(\"tax_reg\", \"税务登记证\"),\n pic_taxpayer_evidence(\"taxpayer_evidence\", \"一般纳税人证明\"), \n	pic_invoice_data(\"invoice_data\", \"开票资料\"), \n	pic_legal_ID(\"legal_ID\", \"法人身份证\"), \n	pic_payment_data(\"payment_data\", \"打款资料\"), \n	pic_consign_contract(\"consign_contract\", \"代运营合同\"), \n	pic_enterprise_survey(\"enterprise_survey\",\"企业调查表\"),\n	pic_open_account_license(\"open_account_license\",\"开户许可证\");' AFTER `url`;


-- ----------------------------
-- Describe: 曹操添加字段
-- Author: Rolyer Luo
-- Date: 08/04/2015 22:44
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order_contract`
ADD COLUMN `customer_id` BIGINT NULL COMMENT '客户编号' AFTER `customer_name`;

