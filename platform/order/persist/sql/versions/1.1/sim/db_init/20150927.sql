-- ----------------------------
-- Describe: 增加发票税率配置
-- Author: zhoukun
-- Date: 2015-09-17 10:20:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_tax_rate','税率','0.17','0','税率，千分之几','税率，千分之几',1,NOW(),'zhoukun',NOW(),'zhoukun',0);

update base_sys_setting 
set setting_value='0.5',description='预期税负比率，最大值1000',display_name='预期税负（千分之几）'
where setting_type='invoice_expect_tax_rate';