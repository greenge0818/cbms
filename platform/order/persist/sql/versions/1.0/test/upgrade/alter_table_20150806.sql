-- ----------------------------
-- Describe: 添加bank_code字段
-- Author: Rolyer Luo
-- Date: 08/0６/2015 09:50
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_bank_transaction_info`
ADD COLUMN `bank_code` VARCHAR(45) NULL COMMENT '银行行号' AFTER `payee_bank_name`;

-- ----------------------------
-- Describe: 林中修改
-- Author: Rolyer Luo
-- Date: 08/0６/2015 11:27
-- ----------------------------
ALTER TABLE `busi_consign_order`
MODIFY COLUMN `fillup_status`  int(11) NOT NULL DEFAULT '-1' COMMENT '放货打印状态\n1　已全打印\n0　未全打印\n2    全匹配 -1 初始化' AFTER `pickup_status`;

-- ----------------------------
-- Describe: 流程定义表增加区分取数和二次结算的字段
-- Author: kongbinheng
-- Date: 08/06/2015 20:31
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_process`
ADD COLUMN `type` int NOT NULL DEFAULT '0' COMMENT '0取数1二次结算' AFTER `user_name`;
