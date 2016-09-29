-- ----------------------------
-- Describe: 添加系统设置页面的权限
-- Author: Rolyer Luo
-- Date: 2015-10-14 08:32
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (247, '查看系统设置', 'sys:settinglist:view', 'sys/settinglist.html', 61, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (248, '修改系统设置', 'sys:settinglist:edit', 'sys/settinglist.html#edit', 247, '2015-07-21 20:55:03', 'admin', '2015-07-21 20:55:03', 'admin', 0, null, null);
-- Describe: busi_consign_order添加出库费承担方，出库费字段
-- Author: lichaowei
-- Date: 10/14/2015 16:23
-- ----------------------------
ALTER TABLE `steel_cbms`.`busi_consign_order`
  CHANGE `fee_taker` `fee_taker` VARCHAR(45) CHARSET utf8 COLLATE utf8_general_ci NOT NULL   COMMENT '运费承担方',
  ADD COLUMN `outbound_taker` VARCHAR(45) NULL   COMMENT '出库费承担方' AFTER `ship_fee`,
  ADD COLUMN `outbound_fee` DECIMAL(18,6) DEFAULT 0  NULL   COMMENT '出库费' AFTER `outbound_taker`;
