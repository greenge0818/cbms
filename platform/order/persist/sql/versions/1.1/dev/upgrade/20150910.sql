-- ----------------------------
-- Describe:
-- Author: Rolyer Luo
-- Date: 2015-09-10 10:58
-- ----------------------------
ALTER TABLE `cust_account`
ADD COLUMN `invoice_data_status`  varchar(10) NULL COMMENT '买家开票资料状态1.审核通过 2.待审核 3.资料不全 4。审核失败 ' AFTER `status`,
ADD COLUMN `invoice_data_decline_reason`  varchar(255) NULL COMMENT '买家开票资料审核失败原因' AFTER `invoice_data_status`;