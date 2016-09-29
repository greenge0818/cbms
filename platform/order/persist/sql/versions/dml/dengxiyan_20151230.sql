-- ----------------------------
-- Describe: 清空往来单位报表、客户销项报表、供应商进项报表数据
-- Author: dengxiyan
-- Date: 2015-12-30 19:01:00
-- ----------------------------
DELETE FROM report_account_financial;
DELETE FROM report_seller_invoice_in;
DELETE FROM report_buyer_invoice_out;