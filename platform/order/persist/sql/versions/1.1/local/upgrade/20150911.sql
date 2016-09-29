-- ----------------------------
-- Describe: 新增权限数据
-- Author: Rolyer Luo
-- Date: 2015-09-11 09:22:50
-- ----------------------------
DELETE FROM acl_permission WHERE id IN (179, 180, 181, 182, 183, 184, 185);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (179, '待认证', 'invoice:in:authentication:view', '/invoice/in/authentication.html', 121, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (180, '已认证', 'invoice:in:authenticationed:view', '/invoice/in/authenticationed.html', 121, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (181, '已作废', 'invoice:in:cancel:view', '/invoice/in/cancel.html', 121, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (182, '是否通过认证', 'invoice:in:authentication:inauthentication', '/invoice/in/authentication.html#inauthentication', 179, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (183, '取消认证', 'invoice:in:authenticationed:outauthentication', '/invoice/in/authenticationed.html#outauthentication', 180, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (184, '取消作废', 'invoice:in:cancel:outcancel', '/invoice/in/cancel.html#outcancel', 181, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id) VALUES (185, '访问APP', 'sys:app:visit', '/app/user/login.html', 61, '2015-08-29 11:01:30', 'cbadmin', '2015-08-29 11:01:30', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe: 系统反馈表
-- Author: Rolyer Luo
-- Date: 2015-09-11 15:30:20
-- ----------------------------
DROP TABLE IF EXISTS `base_sys_feedback`;
CREATE TABLE `base_sys_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(45) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `source` varchar(255) NOT NULL DEFAULT '' COMMENT '反馈来源 web网站，app手机',
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '反馈内容',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(45) NOT NULL,
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(45) NOT NULL,
  `modification_number` int(20) NOT NULL DEFAULT '0',
  `row_id` varchar(45) DEFAULT NULL,
  `parent_row_id` varchar(45) DEFAULT NULL,
  `ext1` varchar(45) DEFAULT NULL,
  `ext2` varchar(45) DEFAULT NULL,
  `ext3` varchar(45) DEFAULT NULL,
  `ext4` int(11) DEFAULT NULL,
  `ext5` int(11) DEFAULT NULL,
  `ext6` int(11) DEFAULT NULL,
  `ext7` datetime DEFAULT NULL,
  `ext8` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统反馈表';
