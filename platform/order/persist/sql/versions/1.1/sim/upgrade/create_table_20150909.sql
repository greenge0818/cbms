DROP TABLE IF EXISTS `report_withdraw_record`;
CREATE TABLE `report_withdraw_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `withdraw_date` datetime DEFAULT NULL COMMENT '提现时间',
  `contact_id` varchar(255) DEFAULT 0 COMMENT '用户ID',
  `contact_name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `contact_tel` varchar(50) DEFAULT NULL COMMENT '用户电话（来自IV）',
  `withdraw_amount` decimal(18,6) DEFAULT 0 COMMENT '提现金额',
  `balance` decimal(18,6) DEFAULT 0 COMMENT '提现后余额',
  `sync_id` int(11) DEFAULT NULL COMMENT '同步ID，IV数据主键，用于同步记录到cbms',
  `created` datetime NOT NULL DEFAULT now(),
  `is_deleted` bit(1) DEFAULT NULL,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT now(),
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT = '提现记录报表';

