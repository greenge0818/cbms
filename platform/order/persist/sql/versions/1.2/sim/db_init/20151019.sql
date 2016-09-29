
-- ----------------------------
-- Describe: 增加买家金额容差配置
-- Author: dengxiyan
-- Date: 2015-10-19 11:53:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type,setting_name,setting_value,default_value,description,display_name,sequence,
	created,created_by,last_updated,last_updated_by,modification_number)
VALUES('order_buyer_allow_amount_tolerance','买家金额容差设置','100','100','关联合同可用金额与订单金额之间允许的容差值','买家金额容差',1,
	NOW(),'system',NOW(),'system',0);