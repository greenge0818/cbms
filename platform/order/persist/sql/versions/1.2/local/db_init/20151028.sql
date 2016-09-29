-- ----------------------------
-- Describe: 更新以前到账查询表的数据都为浦发银行
-- Author: kongbinheng
-- Date: 10/28/2015 10:50
-- ----------------------------
UPDATE busi_bank_transaction_info SET bank_type = 'SPDB';
