-- ----------------------------
-- Describe: 调整待打印付款申请tab中三个小tab在权限分配列表中的位置，方便统一操作
-- Author: Green.Ge
-- Date: 2015-11-03 10:58
-- ----------------------------
update acl_permission set parent_id = 254
where code in 
('order:query:confirmpayment:paybillprint','order:query:secondpaysettlementaccounting:page','order:query:withdrawconfirm:print')