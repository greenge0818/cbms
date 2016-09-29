-- ----------------------------
-- Describe: 规格型号符号
-- Author: kongbinheng
-- Date: 2015-09-10 15:20:18
-- ----------------------------
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('typeofspec', '规格型号', 'none', 'none', '无', '无', 1, sysdate(), 'system', sysdate(), 'system', '0');
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('typeofspec', '规格型号', 'mm', 'mm', '直径', 'Φ', 2, sysdate(), 'system', sysdate(), 'system', '0');
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('typeofspec', '规格型号', 'radix', 'radix', '根', '△', 3, sysdate(), 'system', sysdate(), 'system', '0');
INSERT INTO base_sys_setting (setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number)
VALUES ('typeofspec', '规格型号', 'rebate', 'rebate', '折扣', '折扣', 4, sysdate(), 'system', sysdate(), 'system', '0');
