-- ----------------------------
-- Describe: 买家返利增加表字段及修改字段注释
-- Author: dengxiyan
-- Date: 08/31/2015 16:25
-- ----------------------------
ALTER TABLE `report_rebate_record`
MODIFY COLUMN `rebate_amount`  decimal(18,6) NULL DEFAULT NULL COMMENT ' 返利总金额',
ADD COLUMN `org_id`  bigint(11) NULL DEFAULT NULL COMMENT '组织ID' AFTER `account_name`,
ADD COLUMN `org_name`  varchar(255) NULL DEFAULT NULL COMMENT '组织名称' AFTER `org_id`,
ADD COLUMN `manager_id`  bigint(11) NULL DEFAULT NULL COMMENT '交易员ID' AFTER `org_name`,
ADD COLUMN `manager_name`  varchar(255) NULL DEFAULT NULL COMMENT '交易员姓名' AFTER `manager_id`,
ADD COLUMN `category_group_UUID`  varchar(255) NULL DEFAULT NULL COMMENT '大类UUID' AFTER `contact_name`,
ADD COLUMN `category_group_name`  varchar(255) NULL DEFAULT NULL COMMENT '大类名称' AFTER `category_group_UUID`;