-- 根据放货单id,把原图片路径保存到另一张表
DELIMITER &&  
DROP PROCEDURE IF EXISTS add_new_order_process;
CREATE PROCEDURE add_new_order_process()

BEGIN
	-- 修改数据
		
	DECLARE userId bigint;
	DECLARE userName VARCHAR(100);
	DECLARE orgId bigint;
	DECLARE orgName VARCHAR(100);
  DECLARE num int(3);
	
	
	
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
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有' and operator_mobile = '15372059211';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',144,'杨秀','15372059211','所有','system','system',0);
				END IF;
			  SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_name = '管理员';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',1,'管理员','','所有','system','system',0);
				END IF;
        SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13958099011';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',117,'张峻桢','13958099011','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18969109787';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',201,'阚培培','18969109787','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '13957102076';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',237,'顾旭泽','13957102076','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '13666673285';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',7,'余凌','13666673285','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13777849329';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',9,'郑佩筠','13777849329','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18815293372';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',16,'楼清荷','18815293372','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13819119270';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',17,'舒楚楠','13819119270','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '15810386847';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',216,'赵舒霍','15810386847','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13777474745';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',161,'卢晶镜','13777474745','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13588053735';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',146,'翁晓媛','13588053735','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13456807615';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',113,'陈虎','13456807615','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18758861529';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',181,'胡丹','18758861529','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18258807856';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',182,'郑波','18258807856','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '15558010292';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',183,'朱佳炜','15558010292','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13957149559';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',131,'吴冬霆','13957149559','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '15307480153';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',124,'钟文忠','15307480153','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18657119112';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',125,'邹伟超','18657119112','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13634163103';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',192,'沈伊宁','13634163103','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '15658019116';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',238,'韩青','15658019116','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '13588312459';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',200,'张月','13588312459','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13588108790';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',204,'陈辉蓉','13588108790','所有','system','system',0);
				END IF;
					SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '15158158868';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',118,'周琳','15158158868','所有','system','system',0);
				END IF;
					SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '13758133938';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',209,'施燕','13758133938','所有','system','system',0);
				END IF;
					SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '15973135022';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',210,'刘融','15973135022','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '13958026421';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',69,'张雪仙','13958026421','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId and operator_role_name ='所有'  and operator_mobile = '18627376303';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',97,'刘芬','18627376303','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '15105815282';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',244,'梅璐','15105815282','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18267221599';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',240,'张宇帆','18267221599','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '18801588856';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',241,'韩世林','18801588856','所有','system','system',0);
				END IF;
				SELECT count(1) into num  from busi_consign_process where org_id = orgId and user_id = userId  and operator_role_name ='所有' and operator_mobile = '15084706705';
        IF num = 0 THEN
					insert into busi_consign_process(org_id,org_name,user_id,user_name,type,order_status_code,order_status_name,operator_id,operator_name,operator_mobile,
													 operator_role_name,created_by,last_updated_by,modification_number)
					values(orgId,orgName,userId,userName,0,0,'所有',92,'罗吒','15084706705','所有','system','system',0);
				END IF;

			END;

			 
	END LOOP;
	
		-- 遍历数据结束标志
		-- 关闭游标
	CLOSE cur;
END; -- &&
-- DELIMITER ;  


-- 执行存储过程
CALL add_new_order_process();