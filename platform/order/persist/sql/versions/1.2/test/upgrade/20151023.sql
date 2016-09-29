
-- ----------------------------
-- Describe: 增加付款申请单字段：打印人，最后打印时间，打印IP
-- Author: lixiang
-- Date: 2015-10-23 10:53:18
-- ----------------------------
ALTER TABLE `busi_pay_request`
ADD COLUMN `last_print_ip`  varchar(45) NULL AFTER `parent_row_id`,
ADD COLUMN `print_name`  varchar(45) NULL AFTER `last_print_ip`,
ADD COLUMN `last_print_date`  datetime NULL AFTER `print_name`;

ALTER TABLE `busi_pay_request`
MODIFY COLUMN `ext1`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `last_print_date`,
MODIFY COLUMN `ext2`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `ext1`;

-- ----------------------------
-- Describe: 生产增设索引 1017
-- Author: Roleyr
-- Date: 2015-10-23 16:10
-- ----------------------------
ALTER TABLE inv_invoice_out_apply_detail ADD INDEX IDX_invoice_out_apply_id_is_deleted (invoice_out_apply_id,is_deleted);
ALTER TABLE inv_invoice_out_item_detail ADD INDEX IDX_inv_invoice_in_detail_id_apply_detail_id_is_deleted (inv_invoice_in_detail_id,apply_detail_id,is_deleted);
ALTER TABLE inv_invoice_in_detail_orderitem ADD INDEX IDX_orderitem_id_invoice_detail_id_active (orderitem_id,invoice_detail_id,active);
ALTER TABLE inv_invoice_out_apply ADD INDEX IDX_invoice_out_apply_STATUS_is_deleted (STATUS,is_deleted);
ALTER TABLE busi_consign_order ADD INDEX IDX_account_id_owner_id_STATUS (account_id,owner_id,STATUS);
