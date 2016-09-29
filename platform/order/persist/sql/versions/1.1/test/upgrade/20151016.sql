-- ----------------------------
-- Describe: 增加记录放货单打印次数
-- Author: Rabbit
-- Date: 2015-10-16 13:50:59
-- ----------------------------
CREATE TABLE `busi_delivery_print_times`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `delivery_id` BIGINT(20) NOT NULL COMMENT '放货单id',
  `delivery_type` VARCHAR(45) NOT NULL COMMENT '放货单类型',
  `print_times` INT NOT NULL COMMENT '打印次数',
  `is_deleted` BIT(1) NULL,
  `created` DATETIME NOT NULL,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT NOT NULL,
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
COMMENT = '记录放货单打印次数';
-- Describe: 增设索引
-- Author: Rolyer Luo
-- Date: 10/16/2015 13:10
-- ----------------------------
ALTER TABLE busi_consign_order ADD INDEX IDX_OPERATOR_ID (OPERATOR_ID);
ALTER TABLE busi_consign_order ADD INDEX IDX_OPERATOR_ID_STATUS (OPERATOR_ID,STATUS);
ALTER TABLE busi_consign_process ADD INDEX IDX_OPERATOR_ID_USER_ID (operator_id,user_id);
ALTER TABLE busi_consign_order_items ADD INDEX IDX_ORDER_ID (order_id);
ALTER TABLE acl_role_permission ADD INDEX IDX_ROLE_ID (role_id);
