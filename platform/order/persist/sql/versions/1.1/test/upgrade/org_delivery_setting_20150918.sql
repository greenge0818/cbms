DELIMITER $$
DROP PROCEDURE IF EXISTS org_setting $$
CREATE PROCEDURE `org_setting`()
BEGIN
-- 需要定义接收游标数据的变量 
DECLARE a BIGINT(20);
-- 遍历数据结束标志
DECLARE done INT DEFAULT FALSE;
-- 游标
DECLARE org_list CURSOR FOR select id from base_organization;
-- 将结束标志绑定到游标
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  -- 打开游标
OPEN org_list;

-- 开始循环
  read_loop: LOOP
    -- 提取游标里的数据，这里只有一个，多个的话也一样；
    FETCH org_list INTO a;
    -- 声明结束的时候
    IF DONE THEN
      LEAVE read_loop;
    END IF;
    -- 这里做你想做的循环的事件
        IF(select count(id) from base_organization_delivery_setting where org_id=a and delivery_type='DELIVERYLETTER')<1 THEN
		     insert base_organization_delivery_setting(org_id, delivery_type, created, created_by, last_updated, last_updated_by, modification_number)
               values (a, 'DELIVERYLETTER', current_timestamp(), 'system', current_timestamp(), 'system', 0);
		END IF;
  END LOOP;
CLOSE org_list;
END $$

DELIMITER ;