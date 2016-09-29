
-- ----------------------------
-- Describe: 修改代运营字段
-- Author: dengxiyan
-- Date: 2015-12-11 17:16:18
ALTER TABLE `steel_cbms`.`report_org_day`
  CHANGE `row_id` `row_id` VARCHAR(45) CHARSET utf8 COLLATE utf8_general_ci NULL,
  CHANGE `parent_row_id` `parent_row_id` VARCHAR(45) CHARSET utf8 COLLATE utf8_general_ci NULL;

ALTER TABLE `steel_cbms`.`report_precipitation_funds`
  CHANGE `row_id` `row_id` VARCHAR(45) CHARSET utf8 COLLATE utf8_general_ci NULL,
  CHANGE `parent_row_id` `parent_row_id` VARCHAR(45) CHARSET utf8 COLLATE utf8_general_ci NULL;

ALTER TABLE `steel_cbms`.`report_org_day`
 ADD COLUMN `remark` VARCHAR(200) NULL   COMMENT '统计数据时间段备注' AFTER `total_consign_seller_account`;