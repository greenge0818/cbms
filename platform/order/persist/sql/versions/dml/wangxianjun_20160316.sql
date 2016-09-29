-- 根据买家提单id,把原图片路径保存到另一张表
DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_order_attachment_from_pickup;
CREATE PROCEDURE modify_order_attachment_from_pickup()

BEGIN
	-- 修改数据
		-- 按买家分组并按时候排序
	DECLARE pickupId int;
	DECLARE billPath VARCHAR(100);
	
	
	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR SELECT id, buyer_pickupbill_path FROM busi_pickup_bill   WHERE status=1  AND buyer_pickupbill_path IS NOT NULL ;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into pickupId,billPath; 
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;	
			BEGIN
				insert into busi_consign_order_attachment(consign_order_id,type,file_url,created_by,last_updated_by)  values(pickupId,'ladbill',billPath,'system','system');

			END;

			 
	END LOOP;
	
		-- 遍历数据结束标志
		-- 关闭游标
	CLOSE cur;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_order_attachment_from_pickup();