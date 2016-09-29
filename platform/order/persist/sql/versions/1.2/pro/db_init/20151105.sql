-- ----------------------------
-- Describe: 重新生成返利数据，把客户交易次数清零，提成返利数据清空
-- Author: Rabbit
-- Date: 2015-11-4 18:05:44
-- ----------------------------
update cust_account set buyer_deal_total = 0, seller_deal_total = 0;

TRUNCATE report_rebate_record;

TRUNCATE report_reward_record;