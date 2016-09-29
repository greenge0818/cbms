-- ----------------------------
-- Describe: 更改字符长度
-- Author: Rolyer Luo
-- Date: 08/10/2015 17:50
-- ----------------------------
ALTER TABLE `steel_cbms`.`acl_permission`
CHANGE COLUMN `code` `code` VARCHAR(200) NOT NULL COMMENT '权限编码' ;

-- ----------------------------
-- Describe: 订单主表新增代运营类型字段
-- Author: YuanHong
-- Date: 08/10/2015 20:09
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order` 
ADD COLUMN `consign_type` VARCHAR(45) NOT NULL DEFAULT 'consign' COMMENT '代运营类型\nconsign 代运营\ntemp  临采' AFTER `fillup_status`;
