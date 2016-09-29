DELIMITER $$
DROP PROCEDURE IF EXISTS pro_cust_type_update $$
CREATE PROCEDURE pro_cust_type_update()
BEGIN
		DECLARE no_more1 						INT DEFAULT 0;							/*是否达到记录的末尾控制变量*/
		DECLARE p_account_id				BIGINT;    									/*客户id*/
		DECLARE p_order_count 			INT;  											/*客户在订单表数*/
		DECLARE p_items_count 			INT;  											/*客户在订单明细数*/
		DECLARE p_type							VARCHAR(45) DEFAULT 'both';	/*客户类型*/
		DECLARE p_account_type			VARCHAR(45);

		DECLARE cur_1 CURSOR FOR
				/*查询客户表*/
				SELECT id FROM steel_cbms.cust_account order by id asc;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more1 = 1;
    OPEN cur_1;
    FETCH cur_1 INTO p_account_id; /*获取第一条记录*/
    WHILE no_more1 <> 1 do
				/*查询客户在订单表数*/
				SELECT COUNT(1) INTO p_order_count FROM steel_cbms.busi_consign_order WHERE account_id = p_account_id;

				/*查询客户在订单明细数*/
				SELECT COUNT(1) INTO p_items_count FROM steel_cbms.busi_consign_order_items WHERE seller_id = p_account_id;

				/*若订单表和明细表都存在客户则为买卖家一体*/
				IF p_order_count > 0 AND p_items_count > 0 THEN
						/*客户表*/
						UPDATE steel_cbms.cust_account SET type = p_type WHERE id = p_account_id;
						/*客户划转记录表*/
						UPDATE steel_cbms.cust_account_assign_logs SET type = p_type WHERE account_id = p_account_id;
						/*客户联系人表*/
						UPDATE steel_cbms.cust_account_contact SET type = p_type WHERE account_id = p_account_id;
						/*客户联系人划转记录表*/
						UPDATE steel_cbms.cust_account_contact_assign_logs SET type = p_type WHERE account_id = p_account_id;
						/*客户合同模板表*/
						UPDATE steel_cbms.cust_account_contract_template SET type = p_type WHERE account_id = p_account_id;
						/*客户账户记录表*/
						UPDATE steel_cbms.cust_account_trans_log SET type = p_type WHERE account_id = p_account_id;
				END IF;

				SELECT type INTO p_account_type FROM steel_cbms.cust_account WHERE id = p_account_id;
				IF p_account_type = 'seller' THEN
						/*客户划转记录表*/
						UPDATE steel_cbms.cust_account_assign_logs SET type = p_account_type WHERE account_id = p_account_id;
						/*客户联系人表*/
						UPDATE steel_cbms.cust_account_contact SET type = p_account_type WHERE account_id = p_account_id;
						/*客户联系人划转记录表*/
						UPDATE steel_cbms.cust_account_contact_assign_logs SET type = p_account_type WHERE account_id = p_account_id;
						/*客户合同模板表*/
						UPDATE steel_cbms.cust_account_contract_template SET type = p_account_type WHERE account_id = p_account_id;
						/*客户账户记录表*/
						UPDATE steel_cbms.cust_account_trans_log SET type = p_account_type WHERE account_id = p_account_id;
				END IF;

    FETCH cur_1 INTO p_account_id; /*取下一条记录*/
    END WHILE;
    CLOSE cur_1;

END $$

DELIMITER ;