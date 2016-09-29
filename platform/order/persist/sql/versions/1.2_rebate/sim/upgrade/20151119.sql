-- ----------------------------
-- Describe: 增加折让关联表字段
-- Author: lixiang
-- Date: 2015-11-19 20:13:10
ALTER TABLE `reb_allowance`
ADD COLUMN `audit_date`  datetime NULL COMMENT '审核时间' AFTER `status`;