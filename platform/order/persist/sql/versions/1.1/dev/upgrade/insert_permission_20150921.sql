UPDATE acl_permission SET code = 'invoice:menu', url = 'invoice#menu' WHERE id = 171;
UPDATE acl_permission SET name = '打印清单' WHERE id = 134;

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (186, '核对进项发票', 'invoice:in:inputinvoice:page', '/invoice/in/*/inputinvoice.html', 126, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (187, '修改', 'invoice:in:inputinvoice:edit', '/invoice/in/*/inputinvoice.html#edit', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (188, '复制', 'invoice:in:inputinvoice:copy', '/invoice/in/*/inputinvoice.html#copy', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (189, '关联', 'invoice:in:inputinvoice:relate', '/invoice/in/*/inputinvoice.html#relate', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (190, '删除', 'invoice:in:inputinvoice:delete', '/invoice/in/*/inputinvoice.html#delete', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (191, '查看关联', 'invoice:in:inputinvoice:viewRelate', '/invoice/in/*/inputinvoice.html#viewRelate', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (192, '取消关联', 'invoice:in:inputinvoice:cancelelation', '/invoice/in/*/inputinvoice.html#cancelelation', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (193, '取消选择', 'invoice:in:inputinvoice:assign:cancelSelect', '/invoice/in/*/inputinvoice.html#assigncancelSelect', 189, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (194, '系统推荐', 'invoice:in:inputinvoice:assign:sysSuggestion', '/invoice/in/*/inputinvoice.html#assignsysSuggestion', 189, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (195, '确定', 'invoice:in:inputinvoice:assign:enter', '/invoice/in/*/inputinvoice.html#assignenter', 189, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (196, '提交', 'invoice:in:inputinvoice:submitInvoice', '/invoice/in/*/inputinvoice.html#submitInvoice', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (197, '打印清单', 'invoice:in:send:print', '/invoice/in/send.html#print', 125, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (198, '全部', 'invoice:in:page:index', '/invoice/in/index.html', 121, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (200, '查看详情', 'invoice:in:detailbynsort:page', '/invoice/in/detailbynsort.html', 166, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (201, '已打回待关联', 'invoice:in:returntoberelation:page', '/invoice/in/returntoberelation.html', 124, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe:待收票
-- Author: Rolyer Luo
-- Date: 09/21/2015 16:49
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (202, '核对进项发票', 'invoice:in:inputinvoice:2page', '/invoice/in/*/inputinvoice.html', 124, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (203, '修改', 'invoice:in:inputinvoice:2edit', '/invoice/in/*/inputinvoice.html#edit', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (204, '复制', 'invoice:in:inputinvoice:2copy', '/invoice/in/*/inputinvoice.html#copy', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (205, '关联', 'invoice:in:inputinvoice:2relate', '/invoice/in/*/inputinvoice.html#relate', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (206, '删除', 'invoice:in:inputinvoice:2delete', '/invoice/in/*/inputinvoice.html#delete', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (207, '查看关联', 'invoice:in:inputinvoice:2viewRelate', '/invoice/in/*/inputinvoice.html#viewRelate', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (208, '取消关联', 'invoice:in:inputinvoice:2cancelelation', '/invoice/in/*/inputinvoice.html#cancelelation', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (209, '取消选择', 'invoice:in:inputinvoice:assign:2cancelSelect', '/invoice/in/*/inputinvoice.html#assigncancelSelect', 205, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (210, '系统推荐', 'invoice:in:inputinvoice:assign:2sysSuggestion', '/invoice/in/*/inputinvoice.html#assignsysSuggestion', 205, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (211, '确定', 'invoice:in:inputinvoice:assign:2enter', '/invoice/in/*/inputinvoice.html#assignenter', 205, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (212, '提交', 'invoice:in:inputinvoice:2submitInvoice', '/invoice/in/*/inputinvoice.html#submitInvoice', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe: 修正权限
-- Author: Rolyer Luo
-- Date: 09/21/2015 16:49
-- ----------------------------
UPDATE acl_permission SET url = '/order/query/detail.html#closeaudit' WHERE id = 60;

-- ----------------------------
-- Describe: 销项票权限配置
-- Author: Rolyer Luo
-- Date: 09/21/2015 18:18
-- ----------------------------
UPDATE acl_permission SET code = 'invoice:out:unchecklist:checked', url = '/invoice/out/invoicedata/unchecklist.html#checked' WHERE id = 145;

UPDATE acl_permission SET name = '查看', code = 'invoice:out:apply:show', url = '/invoice/apply/index.html#show' WHERE id = 146;

UPDATE acl_permission SET name = '新增开票申请', code = 'invoice:out:apply:add', url = '/invoice/apply/index.html#add' WHERE id = 147;

UPDATE acl_permission SET code = 'invoice:out:audit:checked', url = '/invoice/out/audit.html#checked' WHERE id = 150;

UPDATE acl_permission SET code = 'invoice:out:inputinvoiceinfo:page', url = '/invoice/out/inputinvoiceinfo.html' WHERE id = 155;

UPDATE acl_permission SET code = 'invoice:out:express:add', url = '/invoice/express/index.html#add' WHERE id = 157;

UPDATE acl_permission SET code = 'invoice:out:confirm:print', url = '/invoice/out/confirm.html#print' WHERE id = 160;

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (213, '删除', 'invoice:out:apply:delete', '/invoice/apply/index.html#delete', 138, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (214, '撤回', 'invoice:out:apply:revoke', '/invoice/apply/index.html#revoke', 138, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (215, '打印清单', 'invoice:out:confirmed:print', '/invoice/out/confirmed.html#print', 144, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);


-- ----------------------------
-- Describe:待开票
-- Author: Rolyer Luo
-- Date: 09/22/2015 11:49
-- ----------------------------
UPDATE acl_permission SET name = '生成开票清单', code = 'invoice:out:waitinginvoice:generate', url = '/invoice/out/waitinginvoice.html#generate' WHERE id = 151;
UPDATE acl_permission SET name = '已生成的开票清单', code = 'invoice:out:generatedinvoice:page', url = '/invoice/out/generatedinvoice.html' WHERE id = 152;
UPDATE acl_permission SET name = '操作', code = 'invoice:out:generate:page', url = '/invoice/out/generate.html' WHERE id = 153;

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (216, '查看开票清单', 'invoice:out:checklist:page', '/invoice/out/checklist/*/index.html', 152, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (217, '导出全部到Excel', 'invoice:out:checklist:export', '/invoice/out/checklist/*/index.html#export', 216, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);


-- ----------------------------
-- Describe:报表权限
-- Author: Rolyer Luo
-- Date: 09/22/2015 14:20
-- ----------------------------
UPDATE acl_permission SET code = 'report:menu', url = 'report#menu' WHERE id = 123;
UPDATE acl_permission SET name = '业务报表', code = 'report:business:menu', url = 'report/business#menu', parent_id=123 WHERE id = 148;
UPDATE acl_permission SET name = '财务报表', code = 'report:financial:menu', url = 'report/financial#menu', parent_id=123 WHERE id = 154;

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (218, '交易单明细', 'report:business:orderdetails:page', '/report/business/orderdetails.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (219, '交易单明细', 'report:business:buyerstatistics:page', '/report/business/buyerstatistics.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (220, '服务中心交易统计', 'report:business:tradestatistics:page', '/report/business/tradestatistics.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (221, '品类交易统计', 'report:business:nsort:page', '/report/business/nsort.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (222, '卖家成交统计报表', 'report:business:sellerStatistics:page', '/report/business/sellerStatistics.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (223, '服务中心提成统计报表', 'report:reward:commission:page', '/report/reward/commission.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (224, '买家返利报表', 'report:business:buyerRebate:page', '/report/business/buyerRebate.html', 148, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (225, '货币资金报表', 'report:account:fundsinandout:page', '/report/account/fundsinandout.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (226, '未收进项发票清单', 'report:finance:uninvoicedIn:page', '/report/finance/uninvoicedIn.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (227, '未开销项发票清单', 'report:finance:uninvoiced:page', '/report/finance/uninvoiced.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (228, '买家账户报表', 'report:accoutcust:buyerstate:page', '/report/accoutcust/buyerstate.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (229, '卖家账户报表', 'report:accoutcust:sellerstate:page', '/report/accoutcust/sellerstate.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (230, '服务中心二次结算储备金日报', 'report:organization:secondsettlementorg:page', '/report/organization/secondsettlementorg.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (231, '进项发票清单', 'report:finance:invoiceinlist:page', '/report/finance/invoiceinlist.html', 154, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);


-- ----------------------------
-- Describe: 更新进项权限
-- Author: Rolyer Luo
-- Date: 09/23/2015 9:22
-- ----------------------------
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:confirm:page', url = '/invoice/in/*/inputinvoice.html' WHERE id = 133;
UPDATE acl_permission SET name='录入发票信息', code = 'invoice:in:inputinvoice:awaits:page', url = '/invoice/in/*/inputinvoice.html' WHERE id = 186;
UPDATE acl_permission SET name='关联', code = 'invoice:in:inputinvoice:returntoberelation:page', url = '/invoice/in/*/inputinvoice.html' WHERE id = 202;

UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:edit' WHERE id = 187;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:copy' WHERE id = 188;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:relate' WHERE id = 189;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:delete' WHERE id = 190;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:viewRelate' WHERE id = 191;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:cancelelation' WHERE id = 192;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:assign:cancelSelect' WHERE id = 193;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:assign:sysSuggestion' WHERE id = 194;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:assign:enter' WHERE id = 195;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:awaits:submitInvoice' WHERE id = 196;

UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:edit' WHERE id = 203;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:copy' WHERE id = 204;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:relate' WHERE id = 205;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:delete' WHERE id = 206;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:viewRelate' WHERE id = 207;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:cancelelation' WHERE id = 208;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:assign:cancelSelect' WHERE id = 209;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:assign:sysSuggestion' WHERE id = 210;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:assign:enter' WHERE id = 211;
UPDATE acl_permission SET code = 'invoice:in:inputinvoice:returntoberelation:submitInvoice' WHERE id = 212;

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (232, '修改', 'invoice:in:inputinvoice:confirm:edit', '/invoice/in/*/inputinvoice.html#edit', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (233, '复制', 'invoice:in:inputinvoice:confirm:copy', '/invoice/in/*/inputinvoice.html#copy', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (234, '关联', 'invoice:in:inputinvoice:confirm:relate', '/invoice/in/*/inputinvoice.html#relate', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (235, '删除', 'invoice:in:inputinvoice:confirm:delete', '/invoice/in/*/inputinvoice.html#delete', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (236, '查看关联', 'invoice:in:inputinvoice:confirm:viewRelate', '/invoice/in/*/inputinvoice.html#viewRelate', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (237, '取消关联', 'invoice:in:inputinvoice:confirm:cancelelation', '/invoice/in/*/inputinvoice.html#cancelelation', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (238, '取消选择', 'invoice:in:inputinvoice:confirm:assign:cancelSelect', '/invoice/in/*/inputinvoice.html#assigncancelSelect', 234, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (239, '系统推荐', 'invoice:in:inputinvoice:confirm:assign:sysSuggestion', '/invoice/in/*/inputinvoice.html#assignsysSuggestion', 234, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (240, '确定', 'invoice:in:inputinvoice:confirm:assign:enter', '/invoice/in/*/inputinvoice.html#assignenter', 234, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (241, '提交', 'invoice:in:inputinvoice:confirm:submitInvoice', '/invoice/in/*/inputinvoice.html#submitInvoice', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe: 待申请开票-提交权限
-- Author: Rolyer Luo
-- Date: 09/23/2015 13:28
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (242, '提交', 'invoice:out:apply:submit', '/invoice/apply/index.html#submit', 138, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

-- ----------------------------
-- Describe: 增加"添加权限"
-- Author: Rolyer Luo
-- Date: 09/23/2015 13:28
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (243, '添加', 'invoice:in:inputinvoice:awaits:add', '/invoice/in/*/inputinvoice.html#add', 186, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (244, '添加', 'invoice:in:inputinvoice:returntoberelation:add', '/invoice/in/*/inputinvoice.html#add', 202, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);

INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (245, '添加', 'invoice:in:inputinvoice:confirm:add', '/invoice/in/*/inputinvoice.html#add', 133, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);


-- ----------------------------
-- Describe: 销项票-审核按钮权限
-- Author: Rolyer Luo
-- Date: 09/24/2015 9:24
-- ----------------------------
INSERT INTO steel_cbms.acl_permission (id, name, code, url, parent_id, created, created_by, last_updated, last_updated_by, modification_number, row_id, parent_row_id)
VALUES (246, '审核', 'invoice:out:apply:approve', '/invoice/apply/index.html#approve', 138, '2015-09-21 11:00:35', 'cbadmin', '2015-09-21 11:00:35', 'cbadmin', 0, null, null);
