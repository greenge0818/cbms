-- ----------------------------
-- Describe: 添加买家开票发票状态字段
-- Author: DQ
-- Date: 2015-11-12 13:31:00
-- ----------------------------
ALTER TABLE `steel_cbms`.`cust_account`
  ADD COLUMN `check_invoice_type` VARCHAR(45) NULL   COMMENT '确定买家开票发票状态(专用：PRIVATE、普通：NORMAL)' AFTER `invoice_type`;
  
  
-- ----------------------------
-- Describe: 银票权限
-- Author: lichaowei
-- Date: 2015-11-12 19:07
-- ----------------------------
insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('259','银票管理','acceptdraft:list:view','/acceptdraft/list.html','0','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('260','新增银票','acceptdraft:create:add','/acceptdraft/create.html#add','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('261','银票提交审核','acceptdraft:create:submit','/acceptdraft/create.html#submit','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('262','撤回充值申请','acceptdraft:auditrecharge:withdraw','/acceptdraft/auditrecharge.html#withdraw','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('263','银票充值审核','acceptdraft:check:submit','/acceptdraft/check.html#submit','259','2015-11-11 15:52:54','admin','2015-11-11 15:53:02','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('264','银票已完成充值','acceptdraft:viewcharged:page','/acceptdraft/*/viewcharged.html','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values
('265','申请取消充值','acceptdraft:charged:applycanclecharged','/acceptdraft/*/viewcharged.html#applycanclecharged','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('266','涉及银票订单二结','order:secondarysettlement:acceptdraft','/order/secondarysettlement/show.html#acceptdraft','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

insert into `acl_permission` (`id`, `name`, `code`, `url`, `parent_id`, `created`, `created_by`, `last_updated`, `last_updated_by`, `modification_number`, `row_id`, `parent_row_id`)
values('267','取消充值审核','acceptdraft:auditrecharge:audit','/acceptdraft/auditrecharge.html#audit','259','2015-11-11 15:54:41','admin','2015-11-11 15:54:48','admin','0',NULL,NULL);

-- ----------------------------
-- Describe: 银票双敲字段更新
-- Author: lichaowei
-- Date: 2015-11-12 19:07
-- ----------------------------
ALTER TABLE `busi_accept_draft` 
CHANGE COLUMN `acceptance_bank_full_name_code_approve` `end_date_approve` DATETIME NULL COMMENT '财务审核银票到期时间' ,
CHANGE COLUMN `amount_code_approve` `amount_approve` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '财务审核金额';
