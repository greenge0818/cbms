-- ----------------------------------------------------	
-- tuxianming
-- 订正客户销项报表2015-12-31 23:59:59 - 2016-01-05 16:09:00 的数据	
-- 2016/01/06 17:16:00
-- ----------------------------------------------------	
	
-- 插入已有数据
-- 插入关联合同数据
insert into `report_buyer_invoice_out`(buyer_id, buy_name, order_id, order_code, contract_code, order_amount, `operation_type`, remark,
	created, created_by, last_updated, last_updated_by, modification_number
)
select * from (
	select 
		b.account_id, b.account_name, b.id, b.code, b.contract_code, b.total_amount, '1', '关联合同',
		b1.created, b1.created_by, b1.last_updated, b1.last_updated_by, 0
	from busi_consign_order as b
		left join busi_consign_order_audit_trail as b1 on b1.order_id=b.id
	where b1.set_to_status='4' and b.created BETWEEN '2015-12-31 23:59:59' and '2016-01-05 16:09:00'
)as f;

-- 插入二结发生额
insert into `report_buyer_invoice_out`(buyer_id, buy_name, order_id, order_code, contract_code, order_amount, `operation_type`, remark,
	created, created_by, last_updated, last_updated_by, modification_number
)
select * from (
	select 
		 b.account_id, b.account_name, b.id, b.code, contract_code, b.total_contract_releted_amount, '2', '二结',
		 b1.created, b1.created_by, b1.last_updated, b1.last_updated_by, 0
	from busi_consign_order as b
	left join busi_consign_order_audit_trail as b1 on b1.order_id=b.id
	where b1.set_to_status='7' and b.created BETWEEN '2015-12-31 23:59:59' and '2016-01-05 16:09:00'
)as ff;


-- 插入订单关闭
insert into `report_buyer_invoice_out`(buyer_id, buy_name, order_id, order_code, contract_code, order_amount, `operation_type`, remark,
	created, created_by, last_updated, last_updated_by, modification_number
)
select * from (
	select 
		b.account_id, b.account_name, b.id, b.code, b.contract_code, -(abs(b.total_contract_releted_amount)), '3', '订单关闭',
		b1.created, b1.created_by, b1.last_updated, b1.last_updated_by, 0
	from busi_consign_order as b
	left join busi_consign_order_audit_trail as b1 on b1.order_id=b.id
	where 
		b1.set_to_status in('-2','-4','-7', '-8') 
		and b.total_contract_releted_amount is not null
		and b.last_updated BETWEEN '2015-12-31 23:59:59' and '2016-01-05 16:09:00'
)as cc;

-- 插入折让
insert into `report_buyer_invoice_out`(buyer_id, buy_name, order_amount, `operation_type`, remark,
	created, created_by, last_updated, last_updated_by, modification_number
)
select * from (
	select k.buyer_id, k.buyer_name,k.allowance_amount, '4', CONCAT('销售调价(单号:', k2.allowance_code, ')'),
		k.last_updated as created, k.last_updated_by as created_by, k.last_updated, k.last_updated_by, 0
	from reb_allowance_order_detail_item as k
		left join reb_allowance_order_item as k1 on k1.id = k.allowance_order_id
		left join reb_allowance as k2 on k2.id = k1.allowance_id
	where k2.status = 'approved' and k.last_updated BETWEEN '2015-12-31 23:59:59' and '2016-01-05 16:09:00'
)as cc;
-- 插入销项： 1号到5号没有发生销项
-- 结束插入已有数据


-- 修改客户销项报表2016/01/01号后的的数据
DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_report_buyer_invoice_out_data;
CREATE PROCEDURE modify_report_buyer_invoice_out_data()

BEGIN
	-- 修改数据
		-- 按买家分组并按时候排序
	DECLARE report_id int;
	DECLARE buyerId int;
	DECLARE operationType varchar(45);
	DECLARE orderAmount decimal(18,6);
	DECLARE invoiceoutAmount decimal(18,6);
	DECLARE prev_buyer_id int;
	DECLARE prev_balance decimal(18,6);
	
	DECLARE balance decimal(18,6) default 0;	
	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR select id, buyer_id, operation_type, order_amount,invoice_out_amount from report_buyer_invoice_out order by buyer_id asc, last_updated asc;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;
	
	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into report_id, buyerId, operationType, orderAmount, invoiceoutAmount; 
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;
		
		SET balance = 0;
		
		-- 如果是第一条数据，或者是同一个买家的第一条数据，则计算初始值
		IF (prev_buyer_id is null OR prev_buyer_id!=buyerId) THEN
		-- select id into id from tbl_currentWeather where cityid = _cityid;
		-- 期初余额 = Σ期初未开销项金额+Σ交易发生额-Σ销项票发生额
			-- Σ（销售订单已关联金额）+Σ（±二结发生额） -订单关闭金额
			BEGIN
				select IFNULL(IFNULL(s.amount,0)-IFNULL(s1.amount,0), 0) into balance from (
					-- 已关联与二结完成之和
					select 
						b.account_id, sum(b.total_contract_releted_amount) as amount
					from busi_consign_order as b
					left join busi_consign_order_audit_trail as b1 on b1.order_id=b.id
					where b1.set_to_status in('4','7') and b.account_id = buyerId 
						and b1.last_updated < '2015-12-31 23:59:59'
				) as s
				left join (
					-- 订单关闭之和
					select 
						b.account_id, sum(abs(b.total_contract_releted_amount)) as amount
					from busi_consign_order as b
					left join busi_consign_order_audit_trail as b1 on b1.order_id=b.id
					where b1.set_to_status in('-2', '-4','-7','-8') and b.account_id = buyerId
						and b.total_contract_releted_amount is not null
						and b1.last_updated < '2015-12-31 23:59:59'
				) as s1 on s1.account_id = s.account_id;
				
				-- 折让
				select (IFNULL(sum(IFNULL(k.allowance_amount,0)),0)+balance) into balance
				from reb_allowance_order_detail_item as k
					left join reb_allowance_order_item as k1 on k1.id = k.allowance_order_id
					left join reb_allowance as k2 on k2.id = k1.allowance_id
				where k2.status = 'approved' and k.buyer_id=buyerId
					and k.last_updated < '2015-12-31 23:59:59';
				
				-- 已经开销项金额
				select (balance - IFNULL(sum(IFNULL(k.amount, 0)),0)) into balance
				from inv_invoice_out_checklist_detail as k
					left join inv_invoice_out_item_detail as k1 on k.item_detail_id =  k1.id
					left join inv_invoice_out_apply_detail as k2 on k2.id = k1.apply_detail_id
					left join inv_invoice_out_checklist as k3 on k3.id = k.checklist_id
				where k2.buyer_id = buyerId and k3.status='INVOICED'
					and k.last_updated < '2015-12-31 23:59:59';
				
				-- 把本条的数据计算进去
				IF (orderAmount is not null) THEN 
					SET balance = balance + orderAmount;
				END IF;
				
				IF (invoiceoutAmount is not null) THEN 
					SET balance = balance - invoiceoutAmount;
				END IF;
				
			END;
		ELSE
			BEGIN
				-- 如果不是同一个买家的的第一条
				IF (orderAmount is not null) THEN 
					SET balance = prev_balance + orderAmount;
				END IF;
				
				IF (invoiceoutAmount is not null) THEN 
					SET balance = prev_balance - invoiceoutAmount;
				END IF;
			END;
		END IF;
		
		-- 更新这条数据
		update report_buyer_invoice_out set invoice_out_balance=balance where id = report_id;
		
		-- 存储当前变量
		SET prev_buyer_id = buyerId;
		SET prev_balance  = balance;
		SET balance = 0;
	
	END LOOP;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_report_buyer_invoice_out_data();