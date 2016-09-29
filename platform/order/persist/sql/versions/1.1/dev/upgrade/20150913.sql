-- ----------------------------
-- Describe: account表修改type类型，增加both枚举类型
-- Author: 李超伟
-- Date: 2015-09-13 14:54:22
-- ----------------------------

ALTER TABLE `cust_account`
MODIFY COLUMN `type` ENUM('buyer','seller','both') COMMENT '类型（买家、卖家、两者都是）';

ALTER TABLE `cust_account_assign_logs`
MODIFY COLUMN `type` ENUM('buyer','seller','both') COMMENT '类型（买家、卖家、两者都是）';

ALTER TABLE `cust_account_contact_assign_logs`
MODIFY COLUMN `type` ENUM('buyer','seller','both') COMMENT '类型（买家、卖家、两者都是）';

ALTER TABLE `cust_account_contract_template`
MODIFY COLUMN `type` ENUM('buyer','seller','both') COMMENT '类型（买家、卖家、两者都是）';

ALTER TABLE `cust_account_trans_log`
MODIFY COLUMN `type` ENUM('buyer','seller','both') COMMENT '类型（买家、卖家、两者都是）';
