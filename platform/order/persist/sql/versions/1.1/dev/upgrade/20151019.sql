-- ----------------------------
-- Describe: inv_invoice_in 添加是否延迟字段
-- Author: dq
-- Date: 10/19/2015 16:23
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_in`   
  ADD COLUMN `is_defer` BIT DEFAULT 0  NOT NULL   COMMENT '是否延迟1(延迟)0(正常)' AFTER `status`;

-- ----------------------------
-- Describe: inv_invoice_out_item_detail 添加是否延迟字段
-- Author: dq
-- Date: 10/19/2015 16:23
-- ----------------------------
ALTER TABLE `steel_cbms`.`inv_invoice_out_item_detail`   
  ADD COLUMN `is_defer` BIT DEFAULT 0  NOT NULL   COMMENT '是否延迟1(延迟)0(正常)' AFTER `status`;

