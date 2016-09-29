-- ----------------------------
-- Describe: 修改权限"二次结算付款申请"的url值以防止用户拥有"待打印二次结算付款申请单"权限的情况下仍然不能访问此tab
-- Author: Green.Ge
-- Date: 2015-10-10 16:43:20
-- ----------------------------
update acl_permission set url='/order/query/secondpaysettlement_bak.html' where name='二次结算付款申请'