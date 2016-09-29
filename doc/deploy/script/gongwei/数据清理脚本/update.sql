UPDATE cust_account SET bank_data_status='Approved' WHERE bank_data_status='Requested';
UPDATE base_organization SET credit_limit='0.000000';
DELETE FROM cust_resource WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_resource_history WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account_contract_template WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account_contact_assign_logs WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account_attachment WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account_bank WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account_contact WHERE account_id IN (SELECT id FROM cust_account WHERE type='buyer');
DELETE FROM cust_account WHERE type='buyer';

