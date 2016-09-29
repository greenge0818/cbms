-- ----------------------------
-- Describe: 修改订单详细表，增加已用发票金额和已用发票重量默认值为0
-- Author: kongbinheng
-- Date: 2015-9-21 10:27:45
-- ----------------------------
ALTER TABLE busi_consign_order_items
CHANGE used_amount used_amount DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已用发票金额',
CHANGE used_weight used_weight DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '已用发票重量';

-- ----------------------------
-- Describe: 修改进项票订单详细关联表表名
-- Author: kongbinheng
-- Date: 2015-9-21 11:53:20
-- ----------------------------
RENAME TABLE inv_invoce_in_detail_orderitem TO inv_invoice_in_detail_orderitem;

ALTER TABLE `acl_role`
ADD COLUMN `role_type`  varchar(20) NULL COMMENT '角色类型：\r\n              CEO(\"CEO\"),\r\n	COO(\"COO\"),\r\n	CFO(\"CFO\"),\r\n	Manager(\"服务中心总经理\"),\r\n	Server(\"内勤\"),\r\n	Trader(\"交易员\"),\r\n	Casher(\"出纳\"),\r\n	LeadCasher(\"总出纳\"),\r\n	Accounter(\"核算会计\");' AFTER `status`;
