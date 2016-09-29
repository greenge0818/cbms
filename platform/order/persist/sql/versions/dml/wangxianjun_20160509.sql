-- 根据放货单id,把原图片路径保存到另一张表
DELIMITER &&  
DROP PROCEDURE IF EXISTS modify_order_process;
CREATE PROCEDURE modify_order_process()

BEGIN
	-- 修改数据
		
	DECLARE userId bigint;
	DECLARE userName VARCHAR(100);
	DECLARE orgId bigint;
	DECLARE orgName VARCHAR(100);
	
	
	
	-- 遍历数据结束标志
	DECLARE done int default false;
	
	-- 游标
	DECLARE cur CURSOR FOR SELECT DISTINCT user_id ,user_name,org_id,org_name from busi_consign_process;
	
	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP
	
		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur into userId,userName,orgId,orgName; 
		
		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;	
			BEGIN
				insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
												 operator_role_name,created_by,last_updated_by,modification_number)
				values(orgId,orgName,userId,userName,0,4,'待关联银票',192,'沈伊宁','13634163103','所有','system','system',0);
				insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
												 operator_role_name,created_by,last_updated_by,modification_number)
				values(orgId,orgName,userId,userName,0,4,'待关联银票',193,'郑静','18968042878','所有','system','system',0);
				insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
												 operator_role_name,created_by,last_updated_by,modification_number)
				values(orgId,orgName,userId,userName,0,4,'待关联银票',204,'陈辉蓉','13588108790','所有','system','system',0);
			END;

			 
	END LOOP;
	
		-- 遍历数据结束标志
		-- 关闭游标
	CLOSE cur;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL modify_order_process();