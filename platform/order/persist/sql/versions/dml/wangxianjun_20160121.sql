-- 根据卖家更新提成表开单时间为订单明细表第一笔订单开单时间
DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_report_new_user_reward_data;
CREATE PROCEDURE modify_report_new_user_reward_data()

BEGIN
	-- 修改数据
		-- 按买家分组并按时候排序
	DECLARE openCreated datetime;
	DECLARE sellerId int;
	DECLARE buyerOrgId int;
	DECLARE buyerOrgName VARCHAR(45);
	DECLARE ownerId int;
  DECLARE ownerName VARCHAR(45);

	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	DECLARE cur CURSOR FOR select distinct s.seller_id from busi_consign_order_items s left join busi_consign_order b on s.order_id = b.id  where b.status > 6  ;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into sellerId; 
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;	
			BEGIN		

			select distinct o.created,o.buyer_org_id,o.buyer_org_name,o.owner_id,o.owner_name
			into openCreated,buyerOrgId,buyerOrgName,ownerId,ownerName
			from busi_consign_order o left join busi_consign_order_items i
			on o.id =i.order_id
			where o.created = (select min(s.created)  from busi_consign_order_items s left join busi_consign_order b on s.order_id = b.id  where b.status > 6  and s.seller_id = sellerId )
			and o.status > 6 and i.seller_id = sellerId;

			END;
			
			  UPDATE report_new_user_reward r
			  SET r.open_order_date = openCreated ,
			      r.manager_id = ownerId,
			  	  r.manager_name = ownerName,
			  	  r.org_id = buyerOrgId,
			  	  r.org_name = buyerOrgName
			  WHERE r.add_new_seller = sellerId and is_deleted = '0';
	END LOOP;
	
		-- 遍历数据结束标志

		-- 关闭游标
	CLOSE cur;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_report_new_user_reward_data();