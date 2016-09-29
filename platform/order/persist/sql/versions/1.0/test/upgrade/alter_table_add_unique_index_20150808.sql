-- ----------------------------
-- Describe: 增加惟一索引
-- Author: YuanHong
-- Date: 08/08/2015 19:18
-- ----------------------------

ALTER TABLE `steel_cbms`.`acl_user` 
ADD UNIQUE INDEX `uix_acl_user_login_id` (`login_id` ASC),
ADD UNIQUE INDEX `uix_acl_user_tel` (`tel` ASC);

ALTER TABLE `steel_cbms`.`base_organization` 
#ADD UNIQUE INDEX `uix_base_organization_code` (`code` ASC),
ADD UNIQUE INDEX `uix_base_organization_name` (`name` ASC);

#ALTER TABLE `steel_cbms`.`busi_bank_transaction_original_detail` 
#ADD UNIQUE INDEX `uix_busi_bank_transaction_original_detail_seqNo` (`seqNo` ASC);

ALTER TABLE `steel_cbms`.`cust_account` 
ADD UNIQUE INDEX `uix_cust_account_code` (`code` ASC),
ADD UNIQUE INDEX `uix_cust_account_name` (`name` ASC);

ALTER TABLE `steel_cbms`.`cust_account_bank` 
ADD UNIQUE INDEX `uix_cust_account_bank_code` (`bank_account_code` ASC);

ALTER TABLE `steel_cbms`.`busi_consign_order` 
ADD UNIQUE INDEX `uix_busi_consign_order_code` (`code` ASC);

ALTER TABLE `steel_cbms`.`busi_consign_order_contract` 
ADD UNIQUE INDEX `uix_busi_consign_order_contract_code_auto` (`contract_code_auto` ASC);

ALTER TABLE `steel_cbms`.`busi_bank_transaction_info` 
ADD UNIQUE INDEX `uix_busi_bank_transaction_info_serial_number` (`serial_number` ASC);

ALTER TABLE `steel_cbms`.`busi_pickup_bill` 
ADD UNIQUE INDEX `uix_busi_pickup_bill_code` (`code` ASC);

ALTER TABLE `steel_cbms`.`busi_delivery_bill` 
ADD UNIQUE INDEX `uix_busi_delivery_bill_code` (`code` ASC);

ALTER TABLE `steel_cbms`.`inv_pool_in` 
ADD UNIQUE INDEX `uix_inv_pool_in_seller` (`org_id` ASC, `seller_id` ASC);

ALTER TABLE `steel_cbms`.`inv_pool_in_detail` 
ADD UNIQUE INDEX `uix_inv_pool_in_detail_info` (`pool_in_id` ASC, `nsort_name` ASC, `material` ASC, `spec` ASC);

ALTER TABLE `steel_cbms`.`inv_invoice_in` 
ADD UNIQUE INDEX `uix_inv_invoice_in_code` (`code` ASC);

ALTER TABLE `steel_cbms`.`inv_pool_out` 
ADD UNIQUE INDEX `uix_inv_pool_out_buyer` (`org_id` ASC, `owner_id` ASC, `buyer_id` ASC);

ALTER TABLE `steel_cbms`.`inv_pool_out_detail` 
ADD UNIQUE INDEX `uix_inv_pool_out_detail_info` (`pool_out_id` ASC, `nsort_name` ASC, `material` ASC, `spec` ASC);

ALTER TABLE `steel_cbms`.`inv_org_invoice_out_balance_monthly` 
ADD UNIQUE INDEX `uix_inv_org_invoice_out_balance_monthly` (`month` ASC, `org_id` ASC);

ALTER TABLE `steel_cbms`.`inv_invoice_out` 
ADD UNIQUE INDEX `uix_inv_invoice_out_code` (`code` ASC);
