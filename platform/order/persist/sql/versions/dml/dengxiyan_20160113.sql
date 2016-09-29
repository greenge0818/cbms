-- ----------------------------------------------------	
-- dengxiyan
-- 往来单位报表2016年期初余额固化
-- 当前往来单位账户流水表中的2015最后一条流水的余额,固化为2016的期初余额
-- 2016/01/13
-- ----------------------------------------------------

DROP PROCEDURE IF EXISTS init_report_account_financial_data;
DELIMITER &&
CREATE PROCEDURE init_report_account_financial_data()

BEGIN
	DECLARE v_operate_type VARCHAR(100) DEFAULT 'INITIAL_BALANCE';
	DECLARE v_remark VARCHAR(500) DEFAULT '当前往来单位账户流水表中的2015最后一条流水的余额,固化为2016的期初余额';
	DECLARE cdate VARCHAR(45) DEFAULT '2015-12-31 23:59:59';
  DECLARE start_time VARCHAR(50) DEFAULT '2016-01-01';

	DECLARE v_account_id BIGINT;
	DECLARE v_account_name VARCHAR(45);
	DECLARE v_current_balance DECIMAL(19,6);

	-- 遍历数据结束标志
	DECLARE done INT DEFAULT FALSE;

	-- 游标 当前往来单位账户流水表中的2015最后一条流水的余额,固化为2016的期初余额
	DECLARE cur CURSOR FOR
	(SELECT t.account_id,t.account_name,-t.current_balance
	FROM
	(
	SELECT atl.account_id,ca.name AS account_name,
	SUBSTRING_INDEX(GROUP_CONCAT(IFNULL(atl.cash_current_balance,0) + IFNULL(atl.current_balance,0) ORDER BY atl.id DESC),',',1) AS current_balance
	FROM cust_account_trans_log atl,cust_account ca
	WHERE
	DATE_FORMAT(atl.created,'%Y-%m-%d') < start_time
	AND ca.id = atl.account_id
	GROUP BY atl.account_id,ca.name
	)AS t
	WHERE t.current_balance <> 0);

	-- 将结束标志绑定到游标
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
	-- 打开游标
	OPEN cur;

	-- 游标 开始循环
	read_loop: LOOP

		-- 提取游标里的数据，这里只有一个，多个的话也一样；
		FETCH cur INTO v_account_id, v_account_name,v_current_balance;

		-- 声明结束的时候
		IF done THEN
			LEAVE read_loop;
		END IF;


	-- 插入数据到踩点表
	INSERT INTO report_account_financial(happen_time,account_id,account_name,current_balance,operate_type,remark
	,created,created_by,last_updated,last_updated_by,modification_number)
	VALUES(cdate,v_account_id,v_account_name,v_current_balance,v_operate_type,v_remark
	,CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system',0);


	END LOOP;
END; -- &&
-- DELIMITER ;

-- 执行存储过程
CALL init_report_account_financial_data();
