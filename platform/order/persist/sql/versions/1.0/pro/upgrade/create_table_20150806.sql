-- ----------------------------
-- Author: kongbinheng
-- Date: 08/06/2015 14:40
-- ----------------------------
CREATE TABLE IF NOT EXISTS `steel_cbms`.`base_sys_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `setting_type` varchar(45) NOT NULL COMMENT '设置类型',
  `setting_name` varchar(100) NOT NULL COMMENT '设置名称',
  `setting_value` varchar(100) NOT NULL COMMENT '设置值',
  `default_value` varchar(100) NOT NULL COMMENT '默认设置值',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `display_name` varchar(100) DEFAULT NULL COMMENT '显示',
  `sequence` int(11) DEFAULT NULL COMMENT '序号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(45) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT = '系统设置表';

-- ----------------------------
-- description: 新增发票设置
-- Author: kongbinheng
-- Date: 08/06/2015 14:40
-- ----------------------------
DELETE FROM base_sys_setting WHERE id = 1;
INSERT INTO base_sys_setting (id, setting_type, setting_name, setting_value, default_value, description, display_name, sequence, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id, ext1, ext2, ext3, ext4, ext5, ext6, ext7, ext8)
VALUES (1, 'invoice', '发票设置', '116999.99', '116999.99', '销项发票单张上限额度', '销项发票单张上限额度', '1', sysdate(), 'cbadmin', sysdate(), 'cbadmin', '0', null, null, null, null, null, null, null, null, null, null);
