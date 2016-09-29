-- ----------------------------
-- Describe: 增加发票税负税额配置
-- Author: zhoukun
-- Date: 2015-09-17 10:20:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_expect_tax_rate','预期税负','5','0','预期税负比率，最大值10000','预期税负（万分之几）',1,
	NOW(),'zhoukun',NOW(),'zhoukun',0);

INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_expect_tax_amount','预期税额','0','0','预期税额','预期税额',1,
	NOW(),'zhoukun',NOW(),'zhoukun',0);