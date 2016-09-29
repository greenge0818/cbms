/**
Green.GE
2015-11-28
添加表索引
*/
ALTER TABLE `cust_resource_norms`
add UNIQUE KEY `id` (`id`) USING BTREE ,
add  KEY `resource_id` (`resource_id`) USING BTREE

ALTER TABLE `cust_resource`
ADD INDEX `idx_category_uuid` (`category_uuid`) USING BTREE ,
ADD INDEX `idx_account_id` (`account_id`) USING BTREE ,
ADD INDEX `idx_material_uuid` (`material_uuid`) USING BTREE ,
ADD INDEX `idx_warehouse_id` (`warehouse_id`) USING BTREE ;

ALTER TABLE `base_factory`
ADD UNIQUE INDEX `idx_id` (`id`) USING BTREE ;

ALTER TABLE `base_warehouse`
ADD UNIQUE INDEX `idx_id` (`id`) USING BTREE ;

ALTER TABLE `common_category`
ADD INDEX `idx_uuid` (`uuid`)  

ALTER TABLE `common_materials`
DROP INDEX `uuid_UNIQUE` ,
ADD UNIQUE INDEX `idx_uuid` (`uuid`) USING BTREE ;

-- 采购单明细表添加采购ID索引
ALTER TABLE `busi_purchase_order_items`
ADD INDEX `purchase_order_id` (`purchase_order_id`) USING BTREE ;
