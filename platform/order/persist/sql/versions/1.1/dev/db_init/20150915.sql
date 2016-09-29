-- ----------------------------
-- Describe: 增加发票金额重量允许的误差值配置
-- Author: zhoukun
-- Date: 2015-09-15 10:20:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_in_allow_amount_deviation','发票金额误差','0.01','0','进项票票面额与订单合计金额之间允许的误差值','发票金额误差',1,
	NOW(),'zhoukun',NOW(),'zhoukun',0);

INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_in_allow_weight_deviation','发票重量误差','0','0','进项票票面重量与订单合计重量之间允许的误差值','发票重量误差',1,
	NOW(),'zhoukun',NOW(),'zhoukun',0);
	
INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('invoice_in_filter_text','发票过滤文本','详见清单','详见清单','发票录入时输入这些文本时，提示无法保存。多个过滤文件用;分隔','发票过滤文本',1,
	NOW(),'zhoukun',NOW(),'zhoukun',0);