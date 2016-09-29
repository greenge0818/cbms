-- ----------------------------
-- Describe: 增加银票清单权限
-- Author: kongbinheng
-- Date: 2015-12-15 11:16:18
-- ----------------------------
update acl_permission set code = 'acceptdraft:view', url = 'acceptdraft' where code = 'acceptdraft:list:view';

insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('银票管理', 'acceptdraft:list:view', '/acceptdraft/list.html', '259', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');

insert acl_permission (name, code, url, parent_id, created, created_by, last_updated, last_updated_by)
values ('银票清单', 'acceptdraft:taelslist:view', '/acceptdraft/taelsList.html', '259', sysdate(), 'admin_cbms', sysdate(), 'admin_cbms');
