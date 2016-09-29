-- ----------------------------
-- Describe: 添加买家开票发票状态字段
-- Author: DQ
-- Date: 11/09/2015 17:09
-- ----------------------------
ALTER TABLE `steel_cbms`.`cust_account`  
  ADD COLUMN `invoice_type` VARCHAR(45) DEFAULT 'PRIVATE'  NULL   COMMENT '买家开票发票状态(专用：PRIVATE、普通：NORMAL)' AFTER `status`,
  CHANGE `invoice_data_status` `invoice_data_status` VARCHAR(10) CHARSET utf8 COLLATE utf8_general_ci NULL   COMMENT '买家开票资料状态1.审核通过 2.待审核 3.资料不全 4。审核失败 ';

-- ----------------------------
-- Describe: 添加确认付款tab中确认动作的权限，原来只有显示的权限,修改二结付款列表的显示名称，原来叫“二次结算付款申请”，让人看不懂是干嘛的
-- Author: Green.Ge
-- Date: 11/09/2015 14:00
-- ----------------------------
INSERT INTO `acl_permission` VALUES ('258', '二结付款确认', 'order:query:secondpaysettlement:confirm', '/order/query/secondpaysettlementconfirm', '96', '2015-11-09 13:52:34', 'cbadmin', '2015-11-09 13:52:34', 'cbadmin', '0', null, null);
UPDATE `acl_permission` SET name ='二结付款列表' where id=96;
UPDATE `acl_permission` SET name ='提现付款确认' where id=174;
UPDATE `acl_permission` SET name ='提现付款列表' where id=57;