-- ----------------------------
-- Describe: 角色权限 数据订正
-- Author: DQ
-- Date: 11/09/2015 17:09
-- ----------------------------
UPDATE acl_role acl SET role_type='Server'
WHERE acl.name LIKE '%内勤%';

UPDATE acl_role acl SET role_type='Manager'
WHERE acl.name LIKE '%总经理%';

UPDATE acl_role acl SET role_type='Casher'
WHERE acl.name LIKE '%出纳%';

UPDATE acl_role acl SET role_type='LeadCasher'
WHERE acl.name LIKE '%总出纳%';

UPDATE acl_role acl SET role_type='Accounter'
WHERE acl.name LIKE '%核算会计%';

UPDATE acl_role acl SET role_type='Trader'
WHERE acl.name LIKE '%交易员%';

UPDATE acl_role acl SET role_type='CEO'
WHERE acl.name LIKE '%CEO%';

UPDATE acl_role acl SET role_type='COO'
WHERE acl.name LIKE '%COO%';

UPDATE acl_role acl SET role_type='CFO'
WHERE acl.name LIKE '%CFO%';
-- ----------------------------
-- Describe: 增加订单审核状态表的索引
-- Author: lixiang
-- Date: 2015-11-10 16:16:18
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order_audit_trail`   
ADD INDEX `order_id` (`order_id`);

-- ---------------------------------------------------
-- Describe: 增加客户公司账目划转详情表的索引
-- Author: lixiang
-- Date: 2015-11-10 16:16:18
-- ---------------------------------------------------
ALTER TABLE `steel_cbms`.`cust_account_trans_log`   
ADD  INDEX `consign_order_code` (`consign_order_code`);
