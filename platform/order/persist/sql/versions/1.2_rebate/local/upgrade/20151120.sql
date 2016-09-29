insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('折让单管理','allowance:list:page','/allowance/list/*.html','3','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

SET @parentid=@@IDENTITY;

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('添加折让单','allowance:list:add','/allowance/list/*.html#add',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('生成买家折让单','allowance:list:generate','/allowance/list/*.html#generate',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('审核','allowance:list:approval','/allowance/list/*.html#approval',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('取消通过','allowance:list:cancelaudit','/allowance/list/*.html#cancelaudit',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('删除','allowance:list:delete','/allowance/list/*.html#delete',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('编辑','allowance:list:edit','/allowance/list/*.html#edit',@parentid,'2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);
