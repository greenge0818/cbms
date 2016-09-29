-- 当订单主表的consign_type 为空时，根据卖家consign_type修改成temp或consign
DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_busi_consign_order_data;
CREATE PROCEDURE modify_busi_consign_order_data()

BEGIN
	-- 修改数据
		-- 按买家分组并按时候排序
	DECLARE orderId int;
	DECLARE sellId int;
	DECLARE itemsNum int;
	DECLARE consighType varchar(45);
	

	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR select b.id from busi_consign_order b where  b.consign_type = 'null' or b.consign_type is null or b.consign_type = '';
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into orderId; 
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;
		
		SET itemsNum = 0;
		
			BEGIN
				select count(1) into itemsNum from (select distinct i.seller_id from busi_consign_order_items i where i.order_id = orderId) a;
			END;
			IF itemsNum > 1 THEN 
			  UPDATE busi_consign_order b SET b.consign_type = 'consign' WHERE b.id = orderId;
			ELSE
			BEGIN
			 select ifnull(c.consign_type,'temp') into consighType from cust_account c where c.id =(select distinct i.seller_id from busi_consign_order_items i where i.order_id = orderId) ;
			END;
			UPDATE busi_consign_order b SET b.consign_type = consighType WHERE b.id = orderId;
			END IF;
		
	
	END LOOP;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_busi_consign_order_data();