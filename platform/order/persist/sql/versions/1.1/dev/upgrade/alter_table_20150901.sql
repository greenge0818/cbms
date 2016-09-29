-- ----------------------------
-- Describe: 买家返利增加返利余额字段及修改字段注释
-- Author: Rolyer Luo
-- Date: 09/01/2015 15:19
-- ----------------------------
DELETE FROM steel_cbms.acl_permission WHERE id IN (175,176,177,178);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (175, '打印', 'order:query:pickup:print', '/order/query/pickup.html#print', 21, '2015-08-13 11:44:52', 'cbadmin', '2015-08-13 11:44:52', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (176, '全部', 'order:query:index:page', '/order/query/index.html', 3, '2015-08-28 13:59:39', 'cbadmin', '2015-08-28 13:59:39', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (177, '查询', 'order:query:index:view', '/order/query/index.html#view', 176, '2015-08-28 14:00:58', 'cbadmin', '2015-08-28 14:00:58', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (178, '给自己新开代运营交易单', 'order:create:self', '/order/create.html#self', 51, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe: 买家返利增加返利余额字段及修改字段注释
-- Author: Rolyer Luo
-- Date: 09/01/2015 15:21
-- ----------------------------
ALTER TABLE `steel_cbms`.`base_organization`
ADD COLUMN `address` VARCHAR(45) NULL AFTER `charger`,
ADD COLUMN `fax` VARCHAR(45) NULL AFTER `address`;

-- ----------------------------
-- Describe: 买家返利增加返利余额字段及修改字段注释
-- Author: Rolyer Luo
-- Date: 09/01/2015 15:28
-- ----------------------------
CREATE TABLE `steel_cbms`.`base_organization_delivery_setting` (
`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
`org_id` BIGINT(20) NOT NULL COMMENT '服务中心id',
`delivery_type` VARCHAR(45) NOT NULL COMMENT '可用放货单类型:DELIVERYLETTER DELIVERYLETTER 放货函；DELIVERYORDER　放货单；TRANSFERORDER　货权转让单；TRANSFERLETTER　货权转让函',
`created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
`created_by` varchar(45) NOT NULL,
`last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
`last_updated_by` varchar(45) NOT NULL,
`modification_number` int(20) NOT NULL DEFAULT '0',
`row_id` varchar(45) DEFAULT NULL,
`parent_row_id` varchar(45) DEFAULT NULL,
PRIMARY KEY (`id`))
COMMENT = '服务中心可用放货单类型设置表';

