-- ----------------------------
-- Describe: 订正cust_account_trans_log(客户流水表)的关联订单号数据
-- Author: lixiang
-- Date: 2016-01-13 14:31:28
-- ----------------------------
DROP TABLE IF EXISTS tmp_table;

CREATE TABLE tmp_table (
	id  bigint(20) NOT NULL, 
	name1 VARCHAR(100) NOT NULL,
	name2 VARCHAR(100) NOT NULL
); 


DROP PROCEDURE IF EXISTS update_cust_account_trans_log;
DELIMITER && 
CREATE PROCEDURE update_cust_account_trans_log()

BEGIN

DECLARE consign_code VARCHAR(45); -- 付款申请单的关联单号

DECLARE cur_amount DECIMAL(19,6); -- 金额

DECLARE dateTime datetime;  -- 时间

DECLARE cust_id BIGINT; -- 客户id

DECLARE order_code VARCHAR(45); -- 流水表关联单号

DECLARE log_code VARCHAR(45);

DECLARE log_id BIGINT;

-- 遍历数据结束标志
DECLARE done INT DEFAULT FALSE;

-- 查询所有二次结算付款申请单数据得到关联单号，付款金额，时间，客户id
DECLARE cur CURSOR FOR( 
SELECT code,buyer_id,total_amount,last_updated FROM busi_pay_request WHERE status='CONFIRMEDPAY' AND type='2');

-- 将结束标志绑定到游标
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
-- 打开游标
OPEN cur;

-- 游标 开始循环
read_loop: LOOP

-- 提取游标里的数据，这里只有一个，多个的话也一样；
FETCH cur INTO consign_code,cust_id,cur_amount,dateTime;
		
-- 声明结束的时候
IF done THEN
LEAVE read_loop;
END IF;

-- 将付款申请单数据作为条件查询流水表的关联单号
SELECT id,consign_order_code INTO log_id,log_code FROM cust_account_trans_log 
WHERE amount = cur_amount AND association_type='1' AND account_id=cust_id AND created > dateTime LIMIT 1 ;

-- 将流水表的单号修正
UPDATE cust_account_trans_log SET consign_order_code = consign_code WHERE consign_order_code = log_code;

INSERT INTO tmp_table VALUES(log_id,consign_code,log_code);

SET done =  FALSE;
END LOOP;
SELECT * from tmp_table;
END &&
DELIMITER ;

CALL update_cust_account_trans_log();