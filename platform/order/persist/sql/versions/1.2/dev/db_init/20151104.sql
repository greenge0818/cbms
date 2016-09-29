-- ----------------------------
-- Describe: 去掉数据库中已经存在的带空格的数据
-- Author: Rabbit
-- Date: 2015-11-4 18:05:44
-- ----------------------------
update cust_account set name = replace(replace(name,' ',''), '　','');
update cust_account_contact set name = replace(replace(name,' ',''), '　','');