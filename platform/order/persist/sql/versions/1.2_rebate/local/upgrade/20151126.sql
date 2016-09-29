-- ----------------------------
-- Describe: 客户表增加折让金额字段
-- Author: kongbinheng
-- Date: 2015-11-26 21:18:12
-- ----------------------------
ALTER TABLE `cust_account`
ADD COLUMN `balance_rebate` DECIMAL(18,6) DEFAULT 0 NULL COMMENT '折让金额' AFTER `balance_second_settlement_freeze`;

-- ----------------------------
-- Describe: 订单主表增加字段订单所属服务中心id、订单所属服务中心名称、买家所属服务中心id、买家所属服务中心名称
-- Author: kongbinheng
-- Date: 2015-11-27 10:09:12
-- ----------------------------
ALTER TABLE `busi_consign_order`
ADD COLUMN `order_org_id` BIGINT DEFAULT 0 NULL  COMMENT '订单所属服务中心id' AFTER `contract_address`;
ALTER TABLE `busi_consign_order`
ADD COLUMN `order_org_name` VARCHAR(45) NULL COMMENT '订单所属服务中心名称' AFTER `order_org_id`;
ALTER TABLE `busi_consign_order`
ADD COLUMN `buyer_org_id` BIGINT DEFAULT 0 NULL COMMENT '买家所属服务中心id' AFTER `order_org_name`;
ALTER TABLE `busi_consign_order`
ADD COLUMN `buyer_org_name` VARCHAR(45) NULL COMMENT '买家所属服务中心名称' AFTER `buyer_org_id`;

-- ----------------------------
-- Describe: 订单详情表增加字段卖家所属服务中心id、卖家所属服务中心名称
-- Author: kongbinheng
-- Date: 2015-11-27 10:09:12
-- ----------------------------
ALTER TABLE `busi_consign_order_items`
ADD COLUMN `seller_org_id` BIGINT DEFAULT 0 NULL COMMENT '卖家所属服务中心id' AFTER `accept_draft_code`;
ALTER TABLE `busi_consign_order_items`
ADD COLUMN `seller_org_name` VARCHAR(45) NULL COMMENT '卖家所属服务中心名称' AFTER `seller_org_id`;
ALTER TABLE `busi_consign_order_items`
ADD COLUMN `seller_owner_id` BIGINT DEFAULT 0 NULL COMMENT '新开单时卖家当时所管理的交易员id' AFTER `seller_org_name`;
ALTER TABLE `busi_consign_order_items`
ADD COLUMN `seller_owner_name` VARCHAR(45) NULL COMMENT '新开单时卖家当时所管理的交易员姓名' AFTER `seller_owner_id`;

-- ----------------------------
-- Describe: 订正历史数据订单主表字段订单所属服务中心id、订单所属服务中心名称、买家所属服务中心id、买家所属服务中心名称
-- Author: kongbinheng
-- Date: 2015-11-27 15:31:28
-- ----------------------------
UPDATE busi_consign_order o
SET o.order_org_id = (SELECT t1.org_id FROM acl_user t1 WHERE t1.id = o.owner_id),
    o.order_org_name = (SELECT t3.name FROM base_organization t3 where t3.id = (SELECT t2.org_id FROM acl_user t2 WHERE t2.id = o.owner_id)),
	o.buyer_org_id = (SELECT t5.org_id FROM acl_user t5 WHERE t5.id = (SELECT t4.manager_id FROM cust_account t4 WHERE t4.id = o.account_id)),
	o.buyer_org_name = (SELECT t8.name FROM base_organization t8 where t8.id = (SELECT t7.org_id FROM acl_user t7 WHERE t7.id = (SELECT t6.manager_id FROM cust_account t6 WHERE t6.id = o.account_id)));

-- ----------------------------
-- Describe: 订正历史数据订单详情表字段卖家所属服务中心id、卖家所属服务中心名称、新开单时卖家当时所管理的交易员id、新开单时卖家当时所管理的交易员姓名
-- Author: kongbinheng
-- Date: 2015-11-27 15:31:28
-- ----------------------------
UPDATE busi_consign_order_items i
SET i.seller_org_id = (SELECT t2.org_id FROM acl_user t2 WHERE t2.id = (SELECT t1.manager_id FROM cust_account t1 WHERE t1.id = i.seller_id)),
	i.seller_org_name = (SELECT t5.name FROM base_organization t5 where t5.id = (SELECT t4.org_id FROM acl_user t4 WHERE t4.id = (SELECT t3.manager_id FROM cust_account t3 WHERE t3.id = i.seller_id))),
	i.seller_owner_id = (SELECT t6.manager_id FROM cust_account t6 WHERE t6.id = i.seller_id),
	i.seller_owner_name = (SELECT t8.name FROM acl_user t8 WHERE t8.id = (SELECT t7.manager_id FROM cust_account t7 WHERE t7.id = i.seller_id));
