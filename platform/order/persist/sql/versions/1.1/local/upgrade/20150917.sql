-- ----------------------------
-- Describe: 修改提成设置表
-- Author: Rabbit
-- Date: 2015-9-17 11:35:38
-- ----------------------------
ALTER TABLE `steel_cbms`.`base_reward`
CHANGE COLUMN `category_UUID` `category_UUID` VARCHAR(45) NULL DEFAULT NULL COMMENT '如果type为成交量提成设置，这里是对应品类UUID；如果是订单提成，这里存提成的类型' ,
CHANGE COLUMN `reward_role` `reward_role` DECIMAL(20,2) NULL DEFAULT NULL COMMENT '如果type为成交量提成设置，这里是单位提成（元/吨）；如果是订单提成，这里存提成比例' ,
ADD COLUMN `reward_type` VARCHAR(45) NOT NULL COMMENT '提成类型: 成交量提成设置category, 订单提成order' AFTER `id`;

-- ----------------------------
-- Describe: 进项票表将发票号和区位码一起做为唯一索引
-- Author: zhoukun
-- Date: 2015-9-17 11:35:38
-- ----------------------------
ALTER TABLE `inv_invoice_in`
DROP INDEX `uix_inv_invoice_in_code` ,
ADD UNIQUE INDEX `uix_inv_invoice_in_code` (`code`, `area_code`) USING BTREE ;