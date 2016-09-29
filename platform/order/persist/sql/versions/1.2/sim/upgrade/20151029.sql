-- ----------------------------
-- Describe: 添加订单申请关闭前状态，用于审核不通过还原状态
-- Author: Rabbit
-- Date: 2015-10-29 09:58:28
-- ----------------------------
ALTER TABLE `busi_consign_order`
ADD COLUMN `origin_status` VARCHAR(45) NULL COMMENT '订单申请关闭之前的状态' AFTER `status`;


