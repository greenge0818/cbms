-- ----------------------------
-- Describe: 新增客户返利增加是否生效active字段
-- Author: Rabbit
-- Date: 2015-10-18 15:46:01
-- ----------------------------
ALTER TABLE `steel_cbms`.`report_new_user_reward`
ADD COLUMN `active` BIT(1) NOT NULL DEFAULT 0 COMMENT '这笔提成是否生效' AFTER `org_name`;
