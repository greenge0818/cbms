DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_operator_order_process;
CREATE PROCEDURE modify_operator_order_process()

BEGIN
	-- 修改数据
		
	DECLARE prcessId bigint;
	
	
	
	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR SELECT distinct id from busi_consign_process where operator_id =193;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into prcessId;
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;	
			BEGIN
			update busi_consign_process set operator_id = 238,
			operator_name = '韩青',
			operator_mobile = '15658019116'
			where id = prcessId;

			END;

			 
	END LOOP;
	
		-- 遍历数据结束标志
		-- 关闭游标
	CLOSE cur;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_operator_order_process();