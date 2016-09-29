-- ----------------------------
-- Describe: 删除org表系统中没有使用的code和total_member字段
-- Author: Rabbit
-- Date: 2015-9-15 12:44:08
-- ----------------------------

ALTER TABLE `steel_cbms`.`base_organization`
DROP COLUMN `total_member`,
DROP COLUMN `code`;