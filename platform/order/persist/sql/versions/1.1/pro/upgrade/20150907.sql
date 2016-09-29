-- ----------------------------
-- Describe: 新增进项票和订单明细关联表
-- Author: kongbinheng
-- Date: 2015-09-07 13:40:22
-- ----------------------------
CREATE TABLE IF NOT EXISTS inv_invoce_in_detail_orderitem (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `invoice_detail_id` BIGINT NOT NULL COMMENT '进项票子项与交易单子项关联表',
  `order_id` BIGINT NOT NULL COMMENT '订单id',
  `orderitem_id` BIGINT NOT NULL COMMENT '订单明细id',
  `weight` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '开票重量（默认是全部，但也存在未开完《=的情况）',
  `amount` DECIMAL(18,6) NOT NULL DEFAULT 0 COMMENT '开票金额',
  `active` INT NULL DEFAULT 1 COMMENT '1: 有效，0：无效（因财务修改发票子项导致）',
  `created` DATETIME NOT NULL DEFAULT now(),
  `created_by` VARCHAR(45) NOT NULL,
  `last_updated` DATETIME NOT NULL DEFAULT now(),
  `last_updated_by` VARCHAR(45) NOT NULL,
  `modification_number` INT(20) NOT NULL DEFAULT 0,
  `row_id` VARCHAR(45) NULL DEFAULT NULL,
  `parent_row_id` VARCHAR(45) NULL DEFAULT NULL,
  `ext1` VARCHAR(20) NULL DEFAULT NULL,
  `ext2` VARCHAR(45) NULL DEFAULT NULL,
  `ext3` VARCHAR(255) NULL DEFAULT NULL,
  `ext4` INT NULL DEFAULT NULL,
  `ext5` INT NULL DEFAULT NULL,
  `ext6` INT NULL DEFAULT NULL,
  `ext7` DATETIME NULL DEFAULT NULL,
  `ext8` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
COMMENT = '进项票和订单明细关联表';

-- ----------------------------
-- Describe: 进项票主表增加区位码字段和修改状态备注
-- Author: kongbinheng
-- Date: 2015-09-07 13:45:36
-- ----------------------------
ALTER TABLE inv_invoice_in
ADD COLUMN area_code VARCHAR(45) NULL COMMENT '区位码（10位）' AFTER id,
MODIFY COLUMN status VARCHAR(45) COMMENT '发票状态（保存，提交，已确认，已确认但有异常，已作废，已认证）';

-- ----------------------------
-- Describe: 进项池明细表增加品名材质组合、规格类型、状态、实收金额（偏差范围内金额)四个字段
-- Author: kongbinheng
-- Date: 2015-09-07 13:50:18
-- ----------------------------
ALTER TABLE inv_invoice_in_detail
ADD COLUMN nsort_name_comb VARCHAR(200) NULL COMMENT '品名与材质组合（组合中间会有空格,不能出现“详见清单”字样)' AFTER amount,
ADD COLUMN type_of_spec VARCHAR(45) NULL COMMENT '参考值： 直径，折扣' AFTER nsort_name_comb,
ADD COLUMN actual_amount DECIMAL(18,6) NULL COMMENT '实收金额（关联订单子项会有偏差）' AFTER type_of_spec,
ADD COLUMN status VARCHAR(45) NULL COMMENT '待关联toberelation，已关联hasrelation' AFTER actual_amount;

-- ----------------------------
-- Describe: 进项票主表增加关联状态字段和修改主状态备注
-- Author: lichaowei
-- Date: 2015-09-10 10:50:18
-- ----------------------------
ALTER TABLE inv_invoice_in
ADD COLUMN relation_status VARCHAR(45) NULL COMMENT '待关联toberelation，已关联hasrelation' AFTER status,
MODIFY COLUMN status VARCHAR(45) COMMENT '发票状态：全部ALL，待收票AWAITS，待寄出RECEIVED，待确认SENT，待认证(已确认)WAIT，已认证ALREADY，已作废CANCEL';
