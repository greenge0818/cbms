-- ----------------------------
-- Describe: 增加客户表中某些字符字段的长度
-- Author: gelinzhong
-- Date: 2015-9-24 17:27
-- ----------------------------
ALTER TABLE `cust_account`
MODIFY COLUMN `name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全称' AFTER `code`,
MODIFY COLUMN `addr`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' AFTER `name`,
MODIFY COLUMN `business`  varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '经营品种' AFTER `mobil`,
MODIFY COLUMN `web_site_url`  varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网址' AFTER `business`;

