-- ----------------------------------------------------	
-- dq
-- 订正供应商进项票 金额小数点后2两位之后的无效数字 
-- 2016/01/07 17:16:00
-- ----------------------------------------------------	
UPDATE report_seller_invoice_in
SET order_amount = ROUND(order_amount,2),
invoice_in_amount = ROUND(invoice_in_amount,2),
invoice_in_balance = ROUND(invoice_in_balance,2);

-- ----------------------------------------------------	
-- dq
-- 初始供应商进项报表
-- 2016/01/12 17:16:00
-- ----------------------------------------------------	
DROP PROCEDURE IF EXISTS init_report_seller_invoice_in_data;

DELIMITER $$
CREATE
    PROCEDURE init_report_seller_invoice_in_data()
    BEGIN
	-- 修改数据
	-- 按卖家分组并按时候排序
	DECLARE balance DECIMAL(18,6);
	DECLARE sellerId BIGINT;
	DECLARE sellerName VARCHAR(500);
	DECLARE cdate VARCHAR(45) DEFAULT '2015-12-31 23:00:00';
	
	-- 遍历数据结束标志
	DECLARE done INT DEFAULT FALSE;
	-- 游标
	DECLARE cur CURSOR FOR SELECT seller_id, seller_name FROM busi_consign_order_items GROUP BY seller_id;
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;
	
	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur INTO sellerId, sellerName;
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;
		
		IF sellerId IS NOT NULL THEN
			SET balance = 0;
			
			SELECT 
				(invoice_in_balance - order_amount + invoice_in_amount) INTO balance
			FROM report_seller_invoice_in
			WHERE id = (SELECT MIN(id) FROM report_seller_invoice_in WHERE seller_id = sellerId);
		    
			IF balance IS NOT NULL AND balance <> 0 THEN
				INSERT INTO report_seller_invoice_in (happen_time, seller_id, seller_name, order_id, order_code, contract_code, order_amount, invoice_in_amount, invoice_in_balance, operation_type, remark, created, created_by, last_updated, last_updated_by, modification_number) 
				VALUES(cdate,sellerId,sellerName,NULL,NULL,NULL,'0.000000','0.000000',balance,'system','system',cdate,'system',cdate,'system','0');
			ELSE
				SET balance = 0;
				SELECT
					(total_amount - total_received_amount) INTO balance
				FROM inv_pool_in
				WHERE id = (SELECT MAX(id) FROM inv_pool_in WHERE seller_id = sellerId);
				
				SELECT
					(balance + SUM(
						CASE WHEN bco.status >= 7 
						THEN ROUND(bcoi.actual_pick_quantity_server*bcoi.cost_price,2)
						ELSE ROUND(bcoi.weight*bcoi.cost_price,2) END
					)) INTO balance 
				FROM busi_consign_order bco
				LEFT JOIN busi_consign_order_items bcoi ON bcoi.order_id = bco.id
				WHERE  bcoi.seller_id = sellerId
				AND bco.status IN (4,5,6)
				GROUP BY bcoi.seller_id;
				
				SELECT
					(balance + SUM(i.total_amount)) INTO balance 
				FROM inv_invoice_in i
				WHERE i.seller_id = sellerId
				AND i.status IN ('AWAITS','RECEIVED','SENT')
				GROUP BY i.seller_id;

				IF balance IS NOT NULL AND balance <> 0 THEN
					INSERT INTO report_seller_invoice_in (id, happen_time, seller_id, seller_name, order_id, order_code, contract_code, order_amount, invoice_in_amount, invoice_in_balance, operation_type, remark, created, created_by, last_updated, last_updated_by, modification_number) 
					VALUES(NULL, cdate,sellerId,sellerName,NULL,NULL,NULL,'0.000000','0.000000',balance,'system','system',cdate,'system',cdate,'system','0');
				END IF;
			END IF;
		END IF;
		SET done = FALSE;
	END LOOP;
	-- 关闭游标
	CLOSE cur;

    END; -- $$
-- DELIMITER;


-- 执行存储过程
CALL init_report_seller_invoice_in_data();

DROP PROCEDURE IF EXISTS init_report_seller_invoice_in_data;