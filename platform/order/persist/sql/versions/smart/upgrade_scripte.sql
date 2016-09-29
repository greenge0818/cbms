-- ----------------------------
-- Describe: 智能找货醒目相关更新
-- Author: Rolyer Luo
-- Date: 2015-11-16 17:25
-- ----------------------------
ALTER TABLE `common_category_norms`
ADD COLUMN `priority` INT(11) NOT NULL DEFAULT 0 COMMENT '排序（升序）' AFTER `is_deleted`;

ALTER TABLE `common_category`
ADD COLUMN `variety_quantity` INT(11) NOT NULL DEFAULT 0 COMMENT '品种数' AFTER `priority`,
ADD COLUMN `price_deviation` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '价格偏差率' AFTER `variety_quantity`;

-- ----------------------------
-- Describe: 新建表
-- Author: Rolyer Luo
-- Date: 2015-11-18 08:53
-- ----------------------------
CREATE TABLE IF NOT EXISTS `common_attribute` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(45) NOT NULL COMMENT '属性唯一标识',
  `name` VARCHAR(45) NOT NULL COMMENT '属性名称',
  `options` VARCHAR(1024) NULL COMMENT '属性选项\n',
  `type` VARCHAR(45) NOT NULL DEFAULT 'input' COMMENT '选项类型：\ninput,checkbox,select,radio',
  `is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '是否删除：0 否；1 是。',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '品名属性表';

CREATE TABLE IF NOT EXISTS `common_category_attribute` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `category_uuid` VARCHAR(45) NOT NULL,
  `attribute_uuid` VARCHAR(45) NOT NULL,
  `priority` INT(11) NULL DEFAULT 0 COMMENT '排序（升序）',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
COMMENT = '品名属性关联表';

CREATE TABLE IF NOT EXISTS `cust_resource_norms` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `resource_id` BIGINT(20) NOT NULL COMMENT '资源ID',
  `norm_uuid` VARCHAR(45) NOT NULL COMMENT '规格UUID',
  `value` VARCHAR(45) NULL COMMENT '值',
  `priority` INT(11) NULL COMMENT '排序（升序）',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '资源规格关联表';

CREATE TABLE IF NOT EXISTS `cust_resource` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NULL COMMENT '卖家ID',
  `category_uuid` VARCHAR(45) NULL COMMENT '品名',
  `material_uuid` VARCHAR(45) NULL COMMENT '材质',
  `factory_id` VARCHAR(45) NULL COMMENT '钢厂',
  `weight` DECIMAL(18,6) NULL COMMENT '库存量',
  `warehouse_id` BIGINT(20) NULL COMMENT '仓库',
  `weight_concept` VARCHAR(45) NULL COMMENT '计重方式：\n磅计，\n理计，\n抄码。',
  `price` DECIMAL(18,6) NULL COMMENT '单价',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `status` VARCHAR(45) NULL DEFAULT '1' COMMENT '状态：1 挂牌 / 0未挂牌',
  `source_type` VARCHAR(45) NULL COMMENT '资源来源:\nINQUIRY 询价;\nUPLOAD 上传.',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '资源表';

CREATE TABLE IF NOT EXISTS `base_area` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL COMMENT '区域名称',
  `center_city_id` BIGINT(20) NULL DEFAULT NULL COMMENT '中心城市',
  `ref_city_ids` VARCHAR(255) NULL COMMENT '相关城市id，逗号分开',
  `is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '是否删除：0 否；1 是。',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '区域表';

CREATE TABLE IF NOT EXISTS `base_warehouse` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL COMMENT '名称',
  `province_id` BIGINT(20) NULL DEFAULT NULL COMMENT '省份',
  `city_id` BIGINT(20) NULL DEFAULT NULL COMMENT '城市',
  `district_id` BIGINT(20) NULL DEFAULT NULL COMMENT '区域',
  `addr` VARCHAR(255) NULL DEFAULT NULL COMMENT '地址',
  `longitude` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '维度',
  `contact` VARCHAR(45) NULL COMMENT '联系人',
  `mobile` VARCHAR(45) NULL DEFAULT NULL COMMENT '联系人电话',
  `exit_fee` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '出库费',
  `lift_fee` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '吊装费',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NOT NULL,
  `last_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '仓库表';

CREATE TABLE IF NOT EXISTS `common_category_weight` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `factory_id` BIGINT(20) NOT NULL COMMENT '钢厂id',
  `category_uuid` VARCHAR(45) NOT NULL COMMENT '品名uuid',
  `material_uuid` VARCHAR(45) NOT NULL COMMENT '材质uuid',
  `norms_uuid` VARCHAR(255) NOT NULL COMMENT '规格组合uuid1*uuid2*uuid3',
  `norms_name` VARCHAR(255) NOT NULL COMMENT '规格组合名称 如35*8*90',
  `single_weight` DECIMAL(18,6) NOT NULL COMMENT '单件重量',
  `is_deleted` BIT(1) NULL DEFAULT 0 COMMENT '是否删除：0 否；1 是。',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(255) NOT NULL,
  `last_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '品规单件重量表';

CREATE TABLE IF NOT EXISTS `busi_purchase_order_items` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `purchase_order_id` BIGINT(20) NULL,
  `category_uuid` VARCHAR(45) NULL,
  `material_uuid` VARCHAR(45) NULL,
  `spec1` VARCHAR(45) NULL COMMENT '规格：\n单选存单个值；多选存\",\"隔开；范围用\"-\"隔开。',
  `spec2` VARCHAR(45) NULL COMMENT '规格：\n单选存单个值；多选存\",\"隔开；范围用\"-\"隔开。',
  `spec3` VARCHAR(45) NULL COMMENT '规格：\n单选存单个值；多选存\",\"隔开；范围用\"-\"隔开。',
  `spec4` VARCHAR(45) NULL COMMENT '规格：\n单选存单个值；多选存\",\"隔开；范围用\"-\"隔开。',
  `spec5` VARCHAR(45) NULL COMMENT '规格：\n单选存单个值；多选存\",\"隔开；范围用\"-\"隔开。',
  `factory_ids` VARCHAR(45) NULL COMMENT '钢厂ID：\n多个用\",\"隔开',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '采购单明细';

CREATE TABLE IF NOT EXISTS `base_factory` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '名称',
  `province_id` BIGINT(20) NULL COMMENT '省份',
  `city_id` BIGINT(20) NULL DEFAULT NULL COMMENT '城市',
  `district_id` BIGINT(20) NULL DEFAULT NULL COMMENT '区域',
  `addr` VARCHAR(255) NULL DEFAULT NULL COMMENT '地址',
  `contact` VARCHAR(45) NULL DEFAULT NULL COMMENT '联系人',
  `mobile` VARCHAR(45) NULL DEFAULT NULL COMMENT '联系人手机',
  `business` VARCHAR(255) NULL DEFAULT NULL COMMENT '主营',
  `longitude` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '经度',
  `latitude` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '维度',
  `output` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '产量（吨/年）',
  `remark` VARCHAR(255) NULL DEFAULT NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '钢厂表';

CREATE TABLE IF NOT EXISTS `busi_purchase_order` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NOT NULL COMMENT '采购单号',
  `org_id` BIGINT(20) NULL COMMENT '服务中心ID\n',
  `owner_id` BIGINT(20) NULL COMMENT '交易员ID',
  `tel` VARCHAR(45) NULL COMMENT '（买家）联系电话',
  `contact` VARCHAR(45) NULL COMMENT '买家联系人',
  `buyer_name` VARCHAR(100) NULL COMMENT '公司名称',
  `delivery_city_id` BIGINT(20) NULL COMMENT '交货地',
  `purchase_city_ids` VARCHAR(45) NULL COMMENT '采购地城市ID组合(逗号隔开)\n如: 1,2,3',
  `purchase_city_other_id` BIGINT(20) NULL COMMENT '其他采购地城市ID',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `total_weight` DECIMAL(18,6) NULL COMMENT '总重量',
  `specific_seller_id` BIGINT(20) NULL DEFAULT NULL COMMENT '指定卖家',
  `status` VARCHAR(45) NULL DEFAULT 'PENDING_QUOTE' COMMENT '状态:\nPENDING_QUOTE 待报价;\nQUOTED 已报价;\nBILLED 已开单;\nCLOSED 已关闭.',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '采购单';

CREATE TABLE IF NOT EXISTS `busi_inquiry_order_sellers` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NULL COMMENT '客户ID',
  `account_name` VARCHAR(100) NULL COMMENT '客户名称',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '采购单卖家表';

CREATE TABLE IF NOT EXISTS `busi_purchase_order_item_attributes` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `purchase_order_item_id` BIGINT(20) NULL COMMENT '采购单明细ID',
  `attribute_uuid` VARCHAR(45) NULL COMMENT '属性UUID',
  `value` VARCHAR(100) NULL COMMENT '属性值',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '采购单明细属性表';

CREATE TABLE IF NOT EXISTS `busi_inquiry_order` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `purchase_order_id` BIGINT(20) NOT NULL COMMENT '采购单ID',
  `status` VARCHAR(45) NULL COMMENT '状态：\n未询价；\n已询价。',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '询价单';

CREATE TABLE IF NOT EXISTS `busi_quotation_order_items` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `quotation_order_id` BIGINT(20) NOT NULL COMMENT '报价单ID',
  `category_uuid` VARCHAR(45) NULL COMMENT '品名',
  `material_uuid` VARCHAR(45) NULL COMMENT '材质',
  `spec1` VARCHAR(45) NULL COMMENT '规格',
  `spec2` VARCHAR(45) NULL COMMENT '规格',
  `spec3` VARCHAR(45) NULL COMMENT '规格',
  `spec4` VARCHAR(45) NULL COMMENT '规格',
  `spec5` VARCHAR(45) NULL COMMENT '规格',
  `factory_id` BIGINT(20) NULL COMMENT '厂家',
  `warehouse_id` BIGINT(20) NULL COMMENT '仓库',
  `quantity` INT(11) NULL COMMENT '数量(件)',
  `weight` DECIMAL(18,6) NULL COMMENT '重量',
  `price` DECIMAL(18,6) NULL COMMENT '单价',
  `total_amount` DECIMAL(18,6) NULL,
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL,
  PRIMARY KEY (`id`))
COMMENT = '报价单明细';

CREATE TABLE IF NOT EXISTS `busi_inquiry_order_items` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `category_uuid` VARCHAR(45) NULL COMMENT '品名',
  `material_uuid` VARCHAR(45) NULL COMMENT '材质',
  `spec1` VARCHAR(45) NULL COMMENT '规格',
  `spec2` VARCHAR(45) NULL COMMENT '规格',
  `spec3` VARCHAR(45) NULL COMMENT '规格',
  `spec4` VARCHAR(45) NULL COMMENT '规格',
  `spec5` VARCHAR(45) NULL COMMENT '规格',
  `factory_id` BIGINT(20) NULL COMMENT '厂家',
  `warehouse_id` BIGINT(20) NULL COMMENT '仓库',
  `stock` DECIMAL(18,6) NULL COMMENT '库存',
  `quantity` INT(11) NULL COMMENT '数量(件)',
  `weight` DECIMAL(18,6) NULL COMMENT '重量',
  `price` DECIMAL(18,6) NULL COMMENT '单价',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '询价单明细';

CREATE TABLE IF NOT EXISTS `busi_quotation_order` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `purchase_order_id` BIGINT(20) NOT NULL COMMENT '采购单ID',
  `remark` VARCHAR(255) NULL COMMENT '备注',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '报价单';


-- ----------------------------
-- Describe: 字段更新
-- Author: Rolyer Luo
-- Date: 2015-11-25 10:17
-- ----------------------------
ALTER TABLE `common_category_attribute`
CHANGE COLUMN `attribut_uuid` `attribute_uuid` VARCHAR(45) NOT NULL ;

ALTER TABLE  `busi_purchase_order_items`
ADD COLUMN `weight` DECIMAL(18,6) NULL DEFAULT 0 COMMENT '单件资源求购重量' AFTER `factory_ids`;

ALTER TABLE  `cust_account`
ADD COLUMN `invoice_speed` VARCHAR(45) NULL DEFAULT 'NORMAL' COMMENT '开票速度：NORMAL 正常；FAST 快；SLOW 慢' AFTER `invoice_type`;

ALTER TABLE  `busi_inquiry_order_sellers`
ADD COLUMN `inquiry_order_id` BIGINT(20) NULL COMMENT '询价单ID' AFTER `id`;

ALTER TABLE  `busi_inquiry_order_items`
ADD COLUMN `inquiry_order_seller_id` BIGINT(20) NULL COMMENT '采购单卖家表ID' AFTER `id`;



-- ----------------------------
-- Describe: 库存监控相关表
-- Author: Rolyer Luo
-- Date: 2015-11-27 15:40
-- ----------------------------
CREATE TABLE IF NOT EXISTS `report_resource_inventory` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `area_id` BIGINT(20) NULL COMMENT '地区',
  `category_group_uuid` VARCHAR(45) NULL COMMENT '品名',
  `category_group_name` VARCHAR(45) NULL COMMENT '品名',
  `category_uuid` VARCHAR(45) NULL COMMENT '材质',
  `category_name` VARCHAR(45) NULL COMMENT '材质',
  `total_resource` INT(10) NULL COMMENT '资源条数',
  `total_spec` INT(10) NULL COMMENT '品规总数',
  `total_stock_spec` INT(10) NULL COMMENT '库存品规数',
  `spec_coverage_rate` DECIMAL(18,6) NULL COMMENT '品规覆盖率',
  `total_transaction` INT(10) NULL COMMENT '成交条数',
  `transaction_coverage_rate` DECIMAL(18,6) NULL COMMENT '成交覆盖率',
  `total_deviation` INT(10) NULL COMMENT '价格偏差条数',
  `deviation_rate` DECIMAL(18,6) NULL COMMENT '价格偏差率',
  `weight` DECIMAL(18,6) NULL COMMENT '库存量',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '库存监控';

CREATE TABLE IF NOT EXISTS `busi_abnormal_trading_detail` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `report_resource_inventory_id` BIGINT(20) NOT NULL COMMENT '库存监控表ID',
  `category_name` VARCHAR(45) NULL COMMENT '品名',
  `material_name` VARCHAR(45) NULL COMMENT '材质',
  `spec` VARCHAR(45) NULL COMMENT '规格',
  `factory` VARCHAR(45) NULL COMMENT '钢厂',
  `warehouse` VARCHAR(255) NULL COMMENT '仓库',
  `price` DECIMAL(18,6) NULL COMMENT '单价',
  `seller_name` VARCHAR(100) NULL COMMENT '卖家名称',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '异常明细（采购价格异常）';


-- ----------------------------
-- Describe: 库存监控相关表
-- Author: Rolyer Luo
-- Date: 2015-11-28 10:54
-- ----------------------------
ALTER TABLE  `busi_purchase_order_items`
ADD COLUMN `quantity` INT(10) NULL COMMENT '件数' AFTER `price`;

-- ----------------------------
-- Describe: 修正factory_id数据类型
-- Author: Rolyer Luo
-- Date: 2015-11-30 11:20
-- ----------------------------
ALTER TABLE `cust_resource`
CHANGE COLUMN `factory_id` `factory_id` BIGINT(20) NULL DEFAULT NULL COMMENT '钢厂' ;

ALTER TABLE `busi_quotation_order_items`
CHANGE COLUMN `price` `deal_price` DECIMAL(18,6) NULL DEFAULT NULL COMMENT '单价(报价)' ,
ADD COLUMN `cost_price` DECIMAL(18,6) NULL COMMENT '底价' AFTER `deal_price`;

-- ----------------------------
-- Describe: 更新询价详情表：busi_inquiry_order_items
-- Author: Rolyer Luo
-- Date: 2015-12-01 12:01
-- ----------------------------
ALTER TABLE `busi_inquiry_order_items`
DROP COLUMN `stock`,
DROP COLUMN `warehouse_id`,
DROP COLUMN `factory_id`,
DROP COLUMN `spec5`,
DROP COLUMN `spec4`,
DROP COLUMN `spec3`,
DROP COLUMN `spec2`,
DROP COLUMN `spec1`,
DROP COLUMN `material_uuid`,
DROP COLUMN `category_uuid`,
DROP COLUMN `price`,
ADD COLUMN `resource_id` BIGINT(20) NULL COMMENT '资源ID' AFTER `inquiry_order_seller_id`;


-- ----------------------------
-- 字符串拆分函数
 -- Function structure for func_split
 -- ----------------------------
DROP FUNCTION IF EXISTS `func_split`;
DELIMITER ;;
CREATE FUNCTION `func_split`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN 
    # 拆分传入的字符串，返回拆分后的新字符串 
        declare result varchar(255) default ''; 
        set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1)); 
        return result; 
END
;;
DELIMITER ;

-- Describe: 补充字段——件数
-- Author: Rolyer Luo
-- Date: 2015-12-02 08:57
-- ----------------------------
ALTER TABLE `cust_resource`
ADD COLUMN `quantity` INT(20) NULL COMMENT '件数' AFTER `price`;

ALTER TABLE  `busi_inquiry_order_items`
ADD COLUMN `purchase_order_items_id` BIGINT(20) NULL COMMENT '采购单明细表ID' AFTER `id`;

-- ----------------------------
-- Describe: 询价历史表
-- Author: Rolyer Luo
-- Date: 2015-12-02 08:57
-- ----------------------------
CREATE TABLE IF NOT EXISTS `busi_inquiry_history` (
  `id` BIGINT(20) UNIQUE NOT NULL AUTO_INCREMENT,
  `busi_inquiry_id` BIGINT(20) NULL COMMENT '询价单ID',
  `busi_inquiry_item_id` BIGINT(20) NULL COMMENT '询价明细ID',
  `price` DECIMAL(18,6) NULL COMMENT '单价',
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT(11) NULL DEFAULT NULL,
  `ext5` INT(11) NULL DEFAULT NULL,
  `ext6` INT(11) NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '询价历史表';


-- ----------------------------
-- Describe: 服务中心增加省市区关联
-- Author: Rolyer Luo
-- Date: 2015-12-07 09:45
-- ----------------------------
ALTER TABLE  `base_organization`
ADD COLUMN `province_id` BIGINT(20) NULL COMMENT '省份ID' AFTER `invoiced_host`,
ADD COLUMN `city_id` BIGINT(20) NULL COMMENT '城市ID' AFTER `province_id`,
ADD COLUMN `district_id` BIGINT(20) NULL COMMENT '区ID' AFTER `city_id`;


-- ----------------------------
-- Describe: 更新busi_inquiry_order_items和busi_inquiry_history
-- Author: Rolyer Luo
-- Date: 2015-12-07 17:00
-- ----------------------------
ALTER TABLE `busi_inquiry_order_items`
DROP COLUMN `resource_id`,
ADD COLUMN `category_uuid`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品名' AFTER `inquiry_order_seller_id`,
ADD COLUMN `material_uuid`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '材质' AFTER `category_uuid`,
ADD COLUMN `spec`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格' AFTER `material_uuid`,
ADD COLUMN `factory_id`  bigint(20) NULL DEFAULT NULL COMMENT '厂家' AFTER `spec`,
ADD COLUMN `warehouse_id`  bigint(20) NULL DEFAULT NULL COMMENT '仓库' AFTER `factory_id`,
ADD COLUMN `price`  decimal(18,6) NULL AFTER `quantity`;

ALTER TABLE `busi_inquiry_history`
DROP COLUMN `price`,
CHANGE COLUMN `busi_inquiry_id` `inquiry_order_id`  bigint(20) NULL DEFAULT NULL COMMENT '询价单ID' AFTER `id`,
CHANGE COLUMN `busi_inquiry_item_id` `purchase_order_item_id`  bigint(20) NULL DEFAULT NULL COMMENT '询价明细ID' AFTER `inquiry_order_id`,
ADD COLUMN `purchase_order_id`  bigint(20) NULL AFTER `id`,
ADD COLUMN `category_uuid`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品名' AFTER `purchase_order_item_id`,
ADD COLUMN `category_name`  varchar(45) NULL AFTER `category_uuid`,
ADD COLUMN `material_uuid`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '材质' AFTER `category_name`,
ADD COLUMN `material_name`  varchar(45) NULL AFTER `material_uuid`,
ADD COLUMN `spec`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格' AFTER `material_name`,
ADD COLUMN `factory_id`  bigint(20) NULL DEFAULT NULL COMMENT '厂家' AFTER `spec`,
ADD COLUMN `factory_name`  varchar(45) NULL AFTER `factory_id`,
ADD COLUMN `warehouse_id`  bigint(20) NULL DEFAULT NULL COMMENT '仓库' AFTER `factory_name`,
ADD COLUMN `warehouse_name`  varchar(45) NULL AFTER `warehouse_id`,
ADD COLUMN `quantity`  int(11) NULL DEFAULT NULL COMMENT '数量(件)' AFTER `warehouse_name`,
ADD COLUMN `weight`  decimal(18,6) NULL DEFAULT NULL COMMENT '重量' AFTER `quantity`,
ADD COLUMN `price`  decimal(18,6) NULL DEFAULT NULL AFTER `weight`,
ADD COLUMN `remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `price`;


-- ----------------------------
-- Describe: 添加计重方式字段"weight_concept"
-- Author: Rolyer Luo
-- Date: 2015-12-08 15:15
-- ----------------------------
ALTER TABLE `busi_inquiry_order_items`
ADD COLUMN `weight_concept` VARCHAR(45) NULL COMMENT '计重方式：理计，磅计，抄码' AFTER `weight`;


-- ----------------------------
-- Describe: 更新报价单主表字段
-- Author: Rolyer Luo
-- Date: 2015-12-09 15:32
-- ----------------------------
ALTER TABLE `busi_quotation_order_items`
ADD COLUMN `seller_id` BIGINT(20) NULL COMMENT '卖家ID' AFTER `id`,
ADD COLUMN `weight_concept` VARCHAR(45) NULL COMMENT '计重方式：\n磅计，\n理计，\n抄码。' AFTER `weight`;

-- ----------------------------
-- Describe: 修正品名规格表中错误的多余数据
-- Author: Green Ge
-- Date: 2015-12-10 13:48
-- ----------------------------
delete from common_category_norms 
where id in(
select  id from
(
 select a.uuid category_uuid,b.id,c.uuid norm_uuid ,b.norms_uuid
from common_category_norms  b
left join  common_category a
on a.uuid=b.category_uuid
LEFT JOIN common_norms c
on b.norms_uuid=c.uuid
where a.id is null or c.id is null
) x
)

-- ----------------------------
-- Describe: 智能找货权限配置
-- Author: Rolyer Luo
-- Date: 2015-12-11 14:08
-- ----------------------------
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('基础数据','basedata:menu','basedata#menu',0,'2015-11-10 13:50:49','admin','2015-11-10 13:50:49','admin',0,NULL,NULL);

SET @parentid=@@IDENTITY;

INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('区域管理','smartmatch:area:index:page','/smartmatch/area/index.html',@parentid,'2015-11-10 13:50:49','admin','2015-11-10 13:50:49','admin',0,NULL,NULL);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('资源管理','resource:sort:index:page','/resource/sort/index.html',@parentid,'2015-11-17 10:19:25','cbadmin','2015-11-17 10:19:25','cbadmin',0,NULL,NULL);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('仓库管理', 'smartmatch:warehouse:index:page', '/smartmatch/warehouse/index.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('钢厂管理', 'smartmatch:factory:list:page', '/smartmatch/factory/list.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('模板管理', 'resource:templet:index:page', '/resource/templet/index.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('单件重量表', 'smartmatch:categoryweight:list:page', '/smartmatch/categoryweight/list.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('品规设置', 'resource:attribute:index:page', '/resource/attribute/index.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('库存监控', 'smartmatch:inventory:info:page', '/smartmatch/inventory/info.html', @parentid, '2015-11-25 09:07:21', 'cbadmin', '2015-11-25 09:07:21', 'cbadmin', '0', null, null);


INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('智能找货','smartmatch:menu','smartmatch#menu',0,'2015-11-17 14:42:50','admin','2015-11-17 14:42:50','admin',0,NULL,NULL);

SET @parentid=@@IDENTITY;

INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('新增采购单','smartmatch:purchaseorder:created:page','/smartmatch/purchaseorder/created.html',@parentid,'2015-11-17 14:42:50','admin','2015-11-17 14:42:50','admin',0,NULL,NULL);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('采购单管理','smartmatch:purchaseorder:list:page','/smartmatch/purchaseorder/list.html',@parentid,'2015-11-17 14:42:50','admin','2015-11-17 14:42:50','admin',0,NULL,NULL);
INSERT INTO `acl_permission` (`name`,`code`,`url`,`parent_id`,`created`,`created_by`,`last_updated`,`last_updated_by`,`modification_number`,`row_id`,`parent_row_id`)
VALUES ('询价单管理','smartmatch:inquiryorder:list:page','/smartmatch/inquiryorder/list.html',@parentid,'2015-11-17 14:42:50','admin','2015-11-17 14:42:50','admin',0,NULL,NULL);


-- ----------------------------
-- Describe: 数据库表结构更新
-- Author: Rolyer Luo
-- Date: 2015-12-17 8:53
-- ----------------------------
ALTER TABLE `base_warehouse`
ADD COLUMN `type` VARCHAR(45) NULL DEFAULT 'normal' COMMENT '仓库类型：正常添加为normal，资源导入为other' AFTER `lift_fee`;

ALTER TABLE `busi_purchase_order`
CHANGE COLUMN `purchase_city_other_id` `purchase_city_other_id` VARCHAR(45) NULL DEFAULT  NULL COMMENT '其他采购地城市ID';

ALTER TABLE `busi_purchase_order_items`
ADD COLUMN `quantity` INT(11) NULL COMMENT '件数' AFTER `weight`;


-- ----------------------------
-- Describe: 添加resource_id
-- Author: Rolyer Luo
-- Date: 2015-12-18 14:31
-- ----------------------------
ALTER TABLE `busi_inquiry_order_items`
ADD COLUMN `resource_id` BIGINT(20) NULL COMMENT '资源ID' AFTER `inquiry_order_seller_id`;

-- ----------------------------
-- Describe: 添加userIds
-- Author: Green Ge
-- Date: 2015-12-21 09:20
-- ----------------------------
ALTER TABLE `cust_resource`
ADD COLUMN `user_ids`  varchar(255) NOT NULL COMMENT '操作过的用户ID列表如 1,2,3,4' AFTER `source_type`;

ALTER TABLE `busi_purchase_order`
ADD COLUMN `user_ids`  varchar(255) NOT NULL COMMENT '操作过的用户ID列表如 1,2,3,4' AFTER `status`;

-- ----------------------------
-- Describe: 如果数据库该字段无拼写错误，一面一条语句不用执行。
-- ----------------------------
ALTER TABLE `common_category_weight`
CHANGE COLUMN `catagory_uuid` `category_uuid` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品名uuid' AFTER `factory_id`;

-- ----------------------------
-- Describe: 字段更新
-- Author: Rolyer Luo
-- Date: 2015-12-24 09:20
-- ----------------------------
ALTER TABLE `busi_quotation_order`
ADD COLUMN `status` VARCHAR(45) NULL COMMENT '未确认报价unconfirmed， 已确 认报价confirmed' AFTER `purchase_order_id`,
ADD COLUMN `last_save_tab` VARCHAR(45) NULL COMMENT '最后一次生成报价单时保存的 tab页' AFTER `status`;

ALTER TABLE `cust_resource`
ADD COLUMN `warehouse_name`  VARCHAR(45) NULL COMMENT '仓库名称' AFTER `warehouse_id`,
ADD COLUMN `factory_name`  VARCHAR(45) NULL COMMENT '钢厂名称' AFTER `factory_id`;

ALTER TABLE `steel_cbms`.`busi_quotation_order_items`
ADD COLUMN `inquiry_order_items_id` BIGINT(20) NULL AFTER `id`;

-- ----------------------------
-- Describe: 钢厂和仓库增加别名字段
-- Author: Green.Ge
-- Date: 2015-12-31 14:47
-- ----------------------------
ALTER TABLE `base_factory`
ADD COLUMN `alias`  varchar(1000) NULL COMMENT '别名' AFTER `name`;

ALTER TABLE `base_warehouse`
ADD COLUMN `alias`  varchar(1000) NULL COMMENT '别名' AFTER `name`;

