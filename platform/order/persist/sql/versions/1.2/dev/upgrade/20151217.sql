-- Describe: 销项票权限
-- Author: lixiang
-- Date: 2015-12-16 9:13:10 
SET @parentid=(SELECT id FROM acl_permission WHERE NAME='业务报表');
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('销项票清单','report:business:ticketlist:page','/web/report/business/ticketlist.html',@parentid,'2015-11-10 13:50:49','admin','2015-11-10 13:50:49','admin',0,NULL,NULL);