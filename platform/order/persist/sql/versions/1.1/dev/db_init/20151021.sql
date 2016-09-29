-- 销项票数据初始化
UPDATE inv_invoice_in SET is_defer=0;
UPDATE busi_consign_order SET STATUS='7' WHERE STATUS IN('8','10');
UPDATE busi_consign_order_items SET used_amount=0,used_weight=0;
UPDATE inv_pool_out SET total_sent_amount=0,total_sent_weight=0;
UPDATE inv_pool_out_detail SET sent_amount=0,sent_weight=0;
DELETE FROM inv_invoice_out_apply;
DELETE FROM inv_invoice_out_apply_detail;
DELETE FROM inv_invoice_out_item_detail;
DELETE FROM inv_invoice_out_checklist;
DELETE FROM inv_invoice_out_checklist_detail;