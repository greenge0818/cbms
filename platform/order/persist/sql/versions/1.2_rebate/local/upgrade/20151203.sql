-- ----------------------------
-- Describe: 折让 -折让方式权限 修改
-- Author: dq
-- Date: 2015-12-03 21:18:12
-- ----------------------------
UPDATE acl_permission SET url = '/allowance/addseller.html#weight'
WHERE CODE = 'allowance:add:type:weight';

UPDATE acl_permission SET url = '/allowance/addseller.html#amount'
WHERE CODE = 'allowance:add:type:amount';

UPDATE acl_permission SET url = '/allowance/addseller.html#all'
WHERE CODE = 'allowance:add:type:all';