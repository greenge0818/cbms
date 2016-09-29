
-- ----------------------------
-- Describe: 增加短信模板配置
-- Author: caochao
-- Date: 2015-10-26 18:53:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_addorder','短信模板-新开单通知审核人','#tradername#已提交#companyname#尾号#endcode#的代运营交易单审核申请，请审核！','','新开单通知审核人',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_orderaccept','短信模板-订单审核通过通知交易员','您提交的#companyname#尾号#endcode#的代运营交易单已通过审核，请关注打款！','','订单审核通过通知交易员',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_applypay','短信模板-申请付款通知审核人','#tradername#已提交尾号#endcode#的代运营交易#applyamount#元的付款申请，请审核！','','申请付款通知审核人',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_payaccept','短信模板-审核付款通过通知财务确认','#tradername#尾号#endcode#的代运营交易#payamount#元的付款已通过申请，请付款！','','审核付款通过通知财务确认',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_refusepay','短信模板-审核付款不通过通知申请者','您提交的#companyname#尾号#endcode#的代运营交易单付款申请未通过审核，请联系#managername#！','','审核付款不通过通知申请者',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_orderrefuse','短信模板-订单审核不通过通知交易员','您提交的#companyname#尾号#endcode#的代运营交易单未通过审核，请联系#tradername#！','','订单审核不通过通知交易员',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_orderclose','短信模板-订单关闭通知交易员','您提交的尾号#endcode#的代运营交易单已由#managername#关闭！','','订单关闭通知交易员',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_payconfirm','短信模板-财务确认付款通知交易员','您提交的尾号#endcode#的代运营交易单已付款，请联系#companyname#提货！','','财务确认付款通知交易员',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_typeinpickup','短信模板-双敲不匹配通知','您录入的尾号#endcode#的代运营交易单的二次结算信息有误，请重新录入！','','双敲不匹配通知',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description,
display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number,
 row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
 VALUES('sms_template_settle','短信模板-二结完成通知交易员','尾号#endcode#的代运营交易单已完成二次结算，#companyname#总计#balancetype##balance#元','','二结完成通知交易员',
'短信模板','1','2015-10-26 11:42:02','caochao','2015-10-26 11:42:16','caochao','0',
NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);