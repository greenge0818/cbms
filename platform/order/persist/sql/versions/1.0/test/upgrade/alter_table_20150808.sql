-- ----------------------------
-- Describe: 付款申请单表打印次数字段不可为空默认0
-- Author: kongbinheng
-- Date: 08/08/2015 14:58
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_pay_request`
CHANGE COLUMN `print_times` `print_times` INT(11) NOT NULL DEFAULT 0 COMMENT '打印次数';
