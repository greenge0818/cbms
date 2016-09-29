-- ----------------------------------------------------	
-- tuxianming
-- 初始客户销项报表
-- 2016/01/13 10:35:00
-- ----------------------------------------------------	
DROP PROCEDURE IF EXISTS init_report_buyer_invoice_out_data;
DELIMITER &&  
CREATE PROCEDURE init_report_buyer_invoice_out_data()
BEGIN
	-- 修改数据
		-- 按买家分组并按时候排序
	DECLARE balance decimal(18,6);
	DECLARE buyerId int;
	DECLARE accountName varchar(500);
	DECLARE cdate varchar(45) default '2015-12-31 23:00:00';
	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR select account_id, account_name from busi_consign_order where account_id is not null and  account_name is not null group by account_id;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;
	
	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into buyerId, accountName;
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;
		
		SET balance = 0;
		
		IF buyerId IS NOT NULL THEN
			-- -----计算余额-----------
			-- 如果是第一条数据，或者是同一个买家的第一条数据，则计算初始值
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
				
			END;
		
			-- 更新这条数据
			IF balance IS NOT NULL AND balance <> 0 THEN
				insert into `report_buyer_invoice_out`(buyer_id, buy_name, `invoice_out_balance`, `operation_type`, remark,
					created, created_by, last_updated, last_updated_by, modification_number
				) values(buyerId, accountName, balance, '7', '', cdate, 'system', cdate, 'system', 0);
			END IF;
			
		END IF;
	END LOOP;
	
	-- 关闭游标
	CLOSE cur;
	
END; -- &&
-- DELIMITER ;  

-- 执行存储过程
CALL init_report_buyer_invoice_out_data();
DROP PROCEDURE IF EXISTS init_report_buyer_invoice_out_data;
