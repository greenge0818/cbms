DROP TABLE IF EXISTS `report_reward_record`;
CREATE TABLE `report_reward_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reward_date` datetime DEFAULT NULL COMMENT '提成日期（按天统计）',
  `category_group_UUID` varchar(255) DEFAULT NULL COMMENT '大类名称',
  `category_group_name` varchar(255) DEFAULT NULL COMMENT '大类名称',
  `manager_id` int(11) DEFAULT NULL COMMENT '交易员ID',
  `manager_name` varchar(255) DEFAULT NULL COMMENT '交易员姓名',
  `buyer_weight` decimal(18,6) DEFAULT NULL COMMENT '买家重量',
  `buyer_amount` decimal(18,6) DEFAULT NULL COMMENT '买家总金额',
  `buyer_reward_amount` decimal(18,6) DEFAULT NULL COMMENT '买家提成金额',
  `seller_weight` decimal(18,6) DEFAULT NULL COMMENT '卖家重量',
  `seller_amount` decimal(18,6) DEFAULT NULL COMMENT '卖家金额',
  `seller_reward_amount` decimal(18,6) DEFAULT NULL COMMENT '卖家提成总金额',
  `org_id` int(11) DEFAULT NULL COMMENT '组织ID',
  `org_name` varchar(255) DEFAULT NULL COMMENT '组织名称',
  `reward_id` int(11) DEFAULT NULL COMMENT '提成方案ID',
  `rewart_role` decimal(18,6) DEFAULT NULL COMMENT '提成规则（元/吨）',
  `created` datetime NOT NULL DEFAULT now(),
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT now(),
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT = '提成报表';

DROP TABLE IF EXISTS `base_dict`;
CREATE TABLE `base_dict` (
  `id` int(11) NOT NULL,
  `key` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `last_updated` datetime NOT NULL DEFAULT now(),
  `last_updated_by` varchar(45) DEFAULT NULL,
  `modification_number` int(20) DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`)
) COMMENT = '字典表';

DROP TABLE IF EXISTS `report_new_user_reward`;
CREATE TABLE `report_new_user_reward` (
  `id` int(11) NOT NULL,
  `add_date` datetime NOT NULL DEFAULT now() COMMENT '提成日期（按天统计）',
  `manager_id` int(11) DEFAULT NULL COMMENT '交易员ID',
  `manager_name` varchar(255) DEFAULT NULL COMMENT '交易员姓名',
  `org_id` int(11) DEFAULT NULL COMMENT '组织ID',
  `org_name` varchar(255) DEFAULT NULL COMMENT '组织名称',
  `created` datetime NOT NULL DEFAULT now(),
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT now(),
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `add_new_buyer` int(11) NOT NULL DEFAULT '0' COMMENT '新增买家数量',
  `add_new_seller` int(11) NOT NULL DEFAULT '0' COMMENT '新增卖家数量',
  `seller_reward_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增卖家提成金额',
  `buyer_reward_amount` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增买家提成金额',
  `buyer_reward_role` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增买家提成规则',
  `seller_reward_role` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '新增卖家提成规则',
  PRIMARY KEY (`id`)
) COMMENT = '添加新客户的提成报表';