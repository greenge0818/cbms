-- ----------------------------
-- Describe: 修改订单确认已付款、二结确认已付款、提现确认已付款为浦发或工行
-- Author: kongbinheng
-- Date: 2015-12-07 09:50:15
-- ----------------------------
update acl_permission set name = '确认已付款(浦发)' where code = 'order:query:confirmpayment:confirm';
insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('确认已付款(工行)', 'order:query:confirmpayment:confirmicbc', '/order/query/confirmpayment.html#confirm', '96', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');

update acl_permission set name = '二结付款确认(浦发)' where code = 'order:query:secondpaysettlement:confirm';
insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('二结付款确认(工行)', 'order:query:secondpaysettlement:confirmicbc', '/order/query/secondpaysettlementconfirm', '96', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');

update acl_permission set name = '提现付款确认(浦发)' where code = 'order:query:withdrawconfirm:confirm';
insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('提现付款确认(工行)', 'order:query:withdrawconfirm:confirmicbc', 'order/query/withdrawconfirm.html#confirm', '56', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');
