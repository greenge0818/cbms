-- ----------------------------
-- Describe: 增加代运营业务日报、月报界面权限
-- Author: dengxiyan
-- Date: 2015-12-15 9:13:10
SET @parentid=(SELECT id FROM acl_permission WHERE NAME='业务报表');

INSERT INTO `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
VALUES('代运营业务日报','report:business:orgDayReport:page','/report/business/orgDayReport.html',@parentid,CURRENT_TIMESTAMP,'admin',CURRENT_TIMESTAMP,'admin','0',NULL,NULL);

INSERT INTO `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
VALUES('代运营业务月报','report:business:orgMonthReport:page','/report/business/orgMonthReport.html',@parentid,CURRENT_TIMESTAMP,'admin',CURRENT_TIMESTAMP,'admin','0',NULL,NULL);

INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('进项票清单','report:business:invoiceInBordereaux:page','/web/report/business/invoiceinbordereaux.html',@parentid,'2015-11-10 13:50:49','admin','2015-11-10 13:50:49','admin',0,NULL,NULL);
