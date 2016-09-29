-- ----------------------------
-- Describe: 订单打回权限
-- Author: lichaowei
-- Date: 2015-10-30 16:53
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (257, '订单打回', 'order:query:detail:fightback', '/order/query/detail.html#fightback', 58, '2015-10-29 15:55:03', 'admin', '2015-10-29 15:55:03', 'admin', 0, NULL, NULL);
