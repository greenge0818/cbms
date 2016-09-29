-- ----------------------------
-- Describe: 林中
-- Author: Rolyer Luo
-- Date: 08/12/2015 12:25
-- ----------------------------
ALTER TABLE `cust_account`
CHANGE COLUMN `deal_total` `buyer_deal_total`  int(11) NOT NULL DEFAULT 0 COMMENT '成交次数' AFTER `last_bill_time`,
MODIFY COLUMN `status`  int(11) NULL DEFAULT 1 COMMENT '0锁定1解锁 2 买家开票资料待审核 3买家开票资料不全 4买家开票资料审核不通过' AFTER `manager_id`,
ADD COLUMN `seller_deal_total`  int(11) NOT NULL DEFAULT 0 AFTER `buyer_deal_total`;

-- ----------------------------
-- Describe: 曹操
-- Author: Rolyer Luo
-- Date: 08/12/2015 12:28
-- ----------------------------
ALTER TABLE busi_pay_request_items
ADD COLUMN second_balance_takeout DECIMAL(18,6) DEFAULT 0 COMMENT '抵扣二次结算余额' AFTER `pay_amount`;


-- ----------------------------
-- Describe: 删除code惟一索引
-- Author: YuanHong
-- Date: 08/12/2015 21:59
-- ----------------------------
ALTER TABLE `steel_cbms`.`base_organization` 
DROP INDEX `uix_base_organization_code` ;
