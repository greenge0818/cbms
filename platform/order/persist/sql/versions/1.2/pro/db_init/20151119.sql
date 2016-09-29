-- ----------------------------
-- Describe: 增加短信模板-待打印付款申请核算会计打回到待申请付款
-- Author: kongbinheng
-- Date: 2015-11-19 10:47:36
-- ----------------------------
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('SmsTemplateCallBack', '短信模板-核算会计打回到待申请付款', '#tradername#尾号#endcode#的代运营交易单#applyamount#元付款已被财务打回，详情请看交易单详情！', '', '核算会计打回通知交易员', '短信模板', '1', sysdate(), 'kongbinheng', sysdate(), 'kongbinheng', '0');

-- ----------------------------
-- Describe: 修改系统设置枚举类变量同步订正数据
-- Author: kongbinheng
-- Date: 2015-11-19 10:47:36
-- ----------------------------
UPDATE base_sys_setting SET setting_type = 'Invoice' WHERE id = 1 and setting_type = 'invoice';
UPDATE base_sys_setting SET setting_type = 'Transaction' WHERE id in (5, 7, 8, 9, 10, 11, 12, 13) and setting_type = 'transaction';
UPDATE base_sys_setting SET setting_type = 'BuyerAllowAmountTolerance' WHERE id = 26 and setting_type = 'order_buyer_allow_amount_tolerance';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateAddOrder' WHERE id = 28 and setting_type = 'sms_template_addorder';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateOrderAccept' WHERE id = 29 and setting_type = 'sms_template_orderaccept';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateApplyPay' WHERE id = 30 and setting_type = 'sms_template_applypay';
UPDATE base_sys_setting SET setting_type = 'SmsTemplatePayAccept' WHERE id = 31 and setting_type = 'sms_template_payaccept';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateRefusePay' WHERE id = 32 and setting_type = 'sms_template_refusepay';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateOrderRefuse' WHERE id = 33 and setting_type = 'sms_template_orderrefuse';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateOrderClose' WHERE id = 34 and setting_type = 'sms_template_orderclose';
UPDATE base_sys_setting SET setting_type = 'SmsTemplatePayConfirm' WHERE id = 35 and setting_type = 'sms_template_payconfirm';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateTypeInpickup' WHERE id = 36 and setting_type = 'sms_template_typeinpickup';
UPDATE base_sys_setting SET setting_type = 'SmsTemplateSettle' WHERE id = 37 and setting_type = 'sms_template_settle';

-- ----------------------------
-- Describe: 增加客户银行账号审核权限
-- Author: kongbinheng
-- Date: 2015-11-20 15:45:10
-- ----------------------------
insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`)
values('客户银行账号审核','order:query:bankcode:confirm','/order/query/bankcodeverify.html','3',sysdate(),'admin',sysdate(),'admin','0');

-- ----------------------------
-- Describe: 增加系统设置销项票申请开票二次结算额度控制
-- Author: kongbinheng
-- Date: 2015-11-24 11:46:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('InvoiceOutApplySecond', '销项票申请开票二次结算额度控制', '1000', '', '销项票申请开票二次结算额度控制', '销项票申请开票', '1', sysdate(), 'kongbinheng', sysdate(), 'kongbinheng', '0');
