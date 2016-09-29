ALTER TABLE `inv_invoice_out_apply_detail`
ADD COLUMN `uninvoice_amount`  decimal(18,6) NULL AFTER `lend_amount`;

