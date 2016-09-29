-- ----------------------------
-- Describe: 邓喜艳
-- Author: Rolyer Luo
-- Date: 2015-10-22 16:53
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (249, '已二次结算', 'order:query:invoice:page', '/order/query/invoice.html', 3, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (250, '查询', 'order:query:invoice:view', '/order/query/invoice.html#view', 249, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (251, '关闭已二次结算订单', 'order:query:invoice:close', '/order/query/invoice.html#close', 249, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

-- Describe: 添加二结回滚权限
-- Author: caochao
-- Date: 2015-10-22 16:16:18
update steel_cbms.acl_permission set name='申请关闭已二次结算订单' where id=251 and name='关闭已二次结算订单';

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (252, '财务关闭已二次结算订单', 'order:query:detail:rollbackconfirm', '/order/query/detail.html#rollbackconfirm', 249, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

-- Describe: 添加关联订单使用的二次结算金额
-- Author: caochao
-- Date: 2015-10-22 16:16:18
ALTER TABLE `steel_cbms`.`busi_consign_order`
ADD COLUMN `second_balance_takeout` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '使用的二次结算金额' AFTER `total_amount`;

-- Describe: 添加进项票自动推荐开关
-- Author: 李超伟
-- Date: 2015-10-22 11:16:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('invoice_in_auto_recommend','进项票自动推荐功能','false','false','录入进项发票，关联交易单系统自动推荐功能开关',
'自动选择','1','2015-10-20 11:42:02','lichaowei','2015-10-20 11:42:16','lichaowei','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
