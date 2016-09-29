-- ----------------------------
-- Describe: 添加变更状态字段
-- Author: Rolyer Luo
-- Date: 2015-10-27 10:39:00
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order`
ADD COLUMN `change_status` VARCHAR(45) NOT NULL DEFAULT 'NONE' COMMENT 'NONE   无变更\nCHANGED 已变更\nAPPROVE 通过' AFTER `status`;

-- ----------------------------
-- Describe: 进项票已作废列表页增加删除发票功能
-- Author: Rolyer Luo
-- Date: 2015-10-27 20:51:23
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (253, '删除', 'invoice:in:cancel:delete', '/invoice/in/cancel.html#delete', 181, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

-- Describe: 增加客户银行表是否默认银行字段
-- Author: lixiang
-- Date: 2015-10-27 15:16:18
-- ----------------------------
ALTER TABLE `cust_account_bank`
ADD COLUMN `is_default`  bit(1) NULL DEFAULT b'0' COMMENT '0 \"否\" ，1 “是”' AFTER `bank_account_code`;


-- ----------------------------
-- Describe: 增加待打印订单，业务节点设置，用户服务中心管理模块 操作权限
-- Author: Green Ge
-- Date: 2015-10-27 14:36:00
-- ----------------------------
INSERT INTO `acl_permission` VALUES (254,'待打印付款申请', 'order:query:printpendingpayapply:page', '/order/query/printpendingpayapply.html', 3, '2015-10-21 10:24:48', 'cbadmin', '2015-10-21 10:24:48', 'cbadmin', 0, NULL, NULL);
INSERT INTO `acl_permission` VALUES (255,'业务节点设置', 'business:process:setting', '/sys/busiprocess/index.html', 1, '2015-10-24 00:10:10', 'cbadmin', '2015-10-24 00:10:10', 'cbadmin', 0, NULL, NULL);
INSERT INTO `acl_permission` VALUES (256,'用户服务中心管理', 'user:org:manage:setting', 'sys/userorg/index.html', 1, '2015-10-24 00:11:08', 'cbadmin', '2015-10-24 00:11:08', 'cbadmin', 0, NULL, NULL);
update acl_permission set parent_id = (select id from acl_permission where name='待打印付款申请') where name='打印付款申请单'

-- ----------------------------
-- Table structure for acl_user_org
-- ----------------------------
DROP TABLE IF EXISTS `acl_user_org`;
CREATE TABLE `acl_user_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `org_id` bigint(20) NOT NULL COMMENT '服务中心ID',
  `created` datetime NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `last_updated_by` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;