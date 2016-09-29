-- ----------------------------
-- Describe: 增加疑似支付错误列表和处理权限
-- Author: kongbinheng
-- Date: 2015-12-19 15:22:18
-- ----------------------------
insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('疑似支付错误', 'order:banktransaction:payerror:view', '/order/banktransaction/payerror.html', '16', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');
insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('疑似支付处理', 'order:banktransaction:payerror:process', '/order/banktransaction/errordeal.html', '16', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');
