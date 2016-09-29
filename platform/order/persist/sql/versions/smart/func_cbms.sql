

-- ----------------------------
-- Procedure structure for org_setting
-- ----------------------------
DROP PROCEDURE IF EXISTS `org_setting`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `org_setting`()
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
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for pro_account_template
-- ----------------------------
DROP PROCEDURE IF EXISTS `pro_account_template`;
DELIMITER ;;
CREATE DEFINER=`root`@`192.168.0.%` PROCEDURE `pro_account_template`()
BEGIN
		DECLARE no_more INT DEFAULT 0;				/*是否达到记录的末尾控制变量*/
		DECLARE p_account_id BIGINT;    			/*公司id*/
		DECLARE p_account_name VARCHAR(100);	/*公司名称*/
		DECLARE p_account_type VARCHAR(45);  	/*公司类型*/
		DECLARE p_content TEXT;  							/*合同内容*/
		DECLARE p_content_name VARCHAR(45);   /*合同模板名称*/
		DECLARE p_count INT;  								/*合同模板数*/
		
    DECLARE cur_1 CURSOR FOR 
				SELECT id, name, type FROM cust_account;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more = 1;
    OPEN cur_1;
    FETCH cur_1 INTO p_account_id, p_account_name, p_account_type; /*获取第一条记录*/
    WHILE no_more<>1 do
				/*买家设置默认销售合同模板*/
				IF p_account_type = 'buyer' THEN
						SET p_content = '<table class=\"contable print-table border0\">\r\n	<tbody class=\"border0\">\r\n		<tr>\r\n			<td colspan=\"5\">\r\n				甲方（需方）：#companyName#\r\n			</td>\r\n			<td colspan=\"5\">\r\n				合同编号：#contractNo#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"5\">\r\n				乙方（供方）：杭州钢为网络科技有限公司\r\n			</td>\r\n			<td colspan=\"5\">\r\n				签订时间：#year#年#month#月#date#日\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"5\">\r\n			</td>\r\n			<td colspan=\"5\">\r\n				签订地点：\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				甲乙双方就现货销售事宜，经平等、自愿协商，达成如下一致意见：\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				一、产品信息\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td rowspan=\"2\" width=\"90\">\r\n				货物名称\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				规格\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				材质\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				厂家\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				仓库\r\n			</td>\r\n			<td class=\"text-center\">\r\n				件数\r\n			</td>\r\n			<td class=\"text-right\">\r\n				重量\r\n			</td>\r\n			<td>\r\n				记重\r\n			</td>\r\n			<td class=\"text-right\">\r\n				单价（含税）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				金额\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td class=\"text-center\">\r\n				（件）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（吨）\r\n			</td>\r\n			<td>\r\n				方式\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（元/吨）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（元）\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n	<tbody id=\"items\">\r\n	</tbody>\r\n	<tbody>\r\n		<tr>\r\n			<td colspan=\"5\" align=\"center\">\r\n				合计\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td class=\"text-right\">\r\n				#totalWeight#\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td class=\"text-right\">\r\n				#totalAmount#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"2\">\r\n				金额大写\r\n			</td>\r\n			<td colspan=\"8\">\r\n				人民币：#totalCapital#（￥#total#元）\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table border0\">\r\n	<tbody class=\"border0\">\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				二、结算信息：\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td width=\"90\">\r\n				乙方（供方）\r\n			</td>\r\n			<td colspan=\"5\">\r\n				杭州钢为网络科技有限公司\r\n			</td>\r\n			<td width=\"90\">\r\n				行号\r\n			</td>\r\n			<td colspan=\"3\">\r\n				310331000068\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				开户行\r\n			</td>\r\n			<td colspan=\"5\">\r\n				浦发银行杭州市钱江支行\r\n			</td>\r\n			<td>\r\n				税号\r\n			</td>\r\n			<td colspan=\"3\">\r\n				91330103MA27X0F7XW\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				帐号\r\n			</td>\r\n			<td colspan=\"5\">\r\n				95090154800004792\r\n			</td>\r\n			<td>\r\n				电话\r\n			</td>\r\n			<td colspan=\"3\">\r\n				400-705-9515\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				地址\r\n			</td>\r\n			<td colspan=\"9\">\r\n				杭州市西湖区西溪街道文二路188号16号楼559室\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<div>\r\n	<ul>\r\n		<li>\r\n			三、交（提）货信息\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			1.付款方式：签订本合同后<em class=\"line-under\"> 1 </em>日内通过<em class=\"line-under\">网银</em>支付货款，款到放货；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			2.交（提）货地点： <em class=\"line-under\">货物所在仓库</em>；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			3.运输方式： <em class=\"line-under\">#deliveryType#</em>；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			4.相关费用承担：出库费由<em class=\"line-under\">&nbsp;&nbsp;&nbsp;&nbsp;</em>担，运费由<em class=\"line-under\">&nbsp;&nbsp;&nbsp;&nbsp;</em>方承担。\r\n		</li>\r\n		<li>\r\n			四、质量要求及技术标准：供货方所提供的产品须符合国家行业的标准。\r\n		</li>\r\n		<li>\r\n			五、乙方对质量负责的条件和期限：如发生质量异议，在甲方收货后 7 个工作日内通知乙方，双方协商解决，对原货物不得变动或使用,非乙方过错造成损失的乙方不承担责任。质量争议最终以钢厂的处理意见为准。若在该期限内若甲方未提出异议，则认为验收合格。\r\n		</li>\r\n		<li>\r\n			六、违约责任： <em class=\"line-under\">依据《中华人民共和国合同法》相关规定处理。</em> \r\n		</li>\r\n		<li>\r\n			七、合同争议的解决方式：本合同在履行过程中发生的争议由双方当事人协商解决，协商或调节不成的在合同签定所在地法院诉讼解决。\r\n		</li>\r\n		<li>\r\n			八、本合同自双方盖章之日起生效，本合同一式二份，双方各执一份。传真件与原件具有同等法律效力。\r\n		</li>\r\n	</ul>\r\n</div>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td width=\"90\">\r\n				甲方：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#companyName#\r\n			</td>\r\n			<td width=\"90\">\r\n				乙方：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				杭州钢为网络科技有限公司\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				地址：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#addr#\r\n			</td>\r\n			<td>\r\n				地址：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				杭州市西湖区西溪街道文二路188号16号楼559室\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				传真号码：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#fax#\r\n			</td>\r\n			<td>\r\n				传真号码：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				0571-89718790\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				法定代表人<br />\r\n(授权代表)：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#legalPersonName#\r\n			</td>\r\n			<td>\r\n				法定代表人<br />\r\n(授权代表)：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				400-705-9515\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				日期：\r\n			</td>\r\n			<td colspan=\"5\">\r\n			</td>\r\n			<td>\r\n				日期：\r\n			</td>\r\n			<td colspan=\"3\">\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>';
						SET p_content_name = '默认销售合同模板';
				/*卖家设置默认采购合同模板*/
				ELSE
						SET p_content = '<table class=\"contable print-table border0\">\r\n	<tbody class=\"border0\">\r\n		<tr>\r\n			<td colspan=\"5\">\r\n				甲方（需方）：杭州钢为网络科技有限公司\r\n			</td>\r\n			<td colspan=\"5\">\r\n				合同编号：#contractNo#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"5\">\r\n				乙方（供方）：#companyName#\r\n			</td>\r\n			<td colspan=\"5\">\r\n				签订时间：#year#年#month#月#date#日\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"5\">\r\n			</td>\r\n			<td colspan=\"5\">\r\n				签订地点：\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				甲乙双方就现货销售事宜，经平等、自愿协商，达成如下一致意见：\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				一、产品信息\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td rowspan=\"2\" width=\"90\">\r\n				货物名称\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				规格\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				材质\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				厂家\r\n			</td>\r\n			<td rowspan=\"2\">\r\n				仓库\r\n			</td>\r\n			<td class=\"text-center\">\r\n				件数\r\n			</td>\r\n			<td class=\"text-right\">\r\n				重量\r\n			</td>\r\n			<td>\r\n				记重\r\n			</td>\r\n			<td class=\"text-right\">\r\n				单价（含税）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				金额\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td class=\"text-center\">\r\n				（件）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（吨）\r\n			</td>\r\n			<td>\r\n				方式\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（元/吨）\r\n			</td>\r\n			<td class=\"text-right\">\r\n				（元）\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n	<tbody id=\"items\">\r\n	</tbody>\r\n	<tbody>\r\n		<tr>\r\n			<td colspan=\"5\" align=\"center\">\r\n				合计\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td class=\"text-right\">\r\n				#totalWeight#\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td>\r\n			</td>\r\n			<td class=\"text-right\">\r\n				#totalAmount#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td colspan=\"2\">\r\n				金额大写\r\n			</td>\r\n			<td colspan=\"8\">\r\n				人民币：#totalCapital#（￥#total#元）\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table border0\">\r\n	<tbody class=\"border0\">\r\n		<tr>\r\n			<td colspan=\"10\">\r\n				二、结算信息：\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td width=\"90\">\r\n				乙方（供方）\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#companyName#\r\n			</td>\r\n			<td width=\"90\">\r\n				行号\r\n			</td>\r\n			<td colspan=\"3\" width=\"150\">\r\n				#bankCode#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				开户行\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#bankName#\r\n			</td>\r\n			<td>\r\n				税号\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#taxCode#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				帐号\r\n			</td>\r\n			<td colspan=\"5\">\r\n				#bankAccount#\r\n			</td>\r\n			<td>\r\n				电话\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#tel#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				地址\r\n			</td>\r\n			<td colspan=\"9\">\r\n				#addr#\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n<div>\r\n	<ul>\r\n		<li>\r\n			三、交（提）货信息\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			1.付款方式：签订本合同后<em class=\"line-under\"> 1 </em>日内通过<em class=\"line-under\">网银</em>支付货款，款到放货；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			2.交（提）货地点： <em class=\"line-under\">货物所在仓库</em>；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			3.运输方式： <em class=\"line-under\">#deliveryType#</em>；\r\n		</li>\r\n		<li class=\"print-indent\">\r\n			4.相关费用承担：出库费由<em class=\"line-under\">&nbsp;&nbsp;&nbsp;&nbsp;</em>担，运费由<em class=\"line-under\">&nbsp;&nbsp;&nbsp;&nbsp;</em>方承担。\r\n		</li>\r\n		<li>\r\n			四、质量要求及技术标准：供货方所提供的产品须符合国家行业的标准。\r\n		</li>\r\n		<li>\r\n			五、乙方对质量负责的条件和期限：如发生质量异议，在甲方收货后 7 个工作日内通知乙方，双方协商解决，对原货物不得变动或使用,非乙方过错造成损失的乙方不承担责任。质量争议最终以钢厂的处理意见为准。若在该期限内若甲方未提出异议，则认为验收合格。\r\n		</li>\r\n		<li>\r\n			六、违约责任： <em class=\"line-under\">依据《中华人民共和国合同法》相关规定处理。</em> \r\n		</li>\r\n		<li>\r\n			七、合同争议的解决方式：本合同在履行过程中发生的争议由双方当事人协商解决，协商或调节不成的在合同签定所在地法院诉讼解决。\r\n		</li>\r\n		<li>\r\n			八、本合同自双方盖章之日起生效，本合同一式二份，双方各执一份。传真件与原件具有同等法律效力。\r\n		</li>\r\n	</ul>\r\n</div>\r\n<table class=\"contable print-table\">\r\n	<tbody>\r\n		<tr>\r\n			<td width=\"90\">\r\n				甲方：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				杭州钢为网络科技有限公司\r\n			</td>\r\n			<td width=\"90\">\r\n				乙方：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#companyName#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				地址：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				杭州市西湖区西溪街道文二路188号16号楼559室\r\n			</td>\r\n			<td>\r\n				地址：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#addr#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				传真号码：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				0571-89718790\r\n			</td>\r\n			<td>\r\n				传真号码：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#fax#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				法定代表人<br />\r\n(授权代表)：\r\n			</td>\r\n			<td colspan=\"5\">\r\n				\r\n			</td>\r\n			<td>\r\n				法定代表人<br />\r\n(授权代表)：\r\n			</td>\r\n			<td colspan=\"3\">\r\n				#legalPersonName#\r\n			</td>\r\n		</tr>\r\n		<tr>\r\n			<td>\r\n				日期：\r\n			</td>\r\n			<td colspan=\"5\">\r\n			</td>\r\n			<td>\r\n				日期：\r\n			</td>\r\n			<td colspan=\"3\">\r\n			</td>\r\n		</tr>\r\n	</tbody>\r\n</table>';
						SET p_content_name = '默认采购合同模板';
				END IF;

				/*查询该客户下的合同模板数量*/
				SELECT count(1) INTO p_count FROM cust_account_contract_template WHERE account_id = p_account_id;
				/*不存在合同模板*/
				IF p_count = 0 THEN
						INSERT INTO cust_account_contract_template(account_id, name, thumbnail_url, content, type, enabled, created, created_by, last_updated, last_updated_by, modification_number) 
						VALUES (p_account_id, p_content_name, '', p_content, p_account_type, 1, SYSDATE(), 'system', SYSDATE(), 'system', 0);
				END IF;
    FETCH cur_1 INTO p_account_id, p_account_name, p_account_type; /*取下一条记录*/
    END WHILE;
    CLOSE cur_1;     
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for splitString
-- ----------------------------
DROP PROCEDURE IF EXISTS `splitString`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `splitString`()
BEGIN 
DECLARE a BIGINT(20);
DECLARE b varchar(100);
DECLARE c varchar(100);
DECLARE d varchar(100);
declare cnt int default 0; 
declare i int default 0; 
-- 遍历数据结束标志
DECLARE done INT DEFAULT FALSE;

DECLARE specs_list CURSOR FOR select id,category_uuid,material_uuid,ext3 from cust_resource;
-- 将结束标志绑定到游标
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
DROP TABLE IF EXISTS `tmp_split`; 
create table `tmp_split` (`resource_id` bigint(20) not null,`category_uuid` varchar(128) not null,`material_uuid` varchar(128) not null,`priority` int(10) not null,`val` varchar(128) not null) DEFAULT CHARSET=utf8; 
OPEN specs_list;

-- 开始循环
  read_loop: LOOP
    -- 提取游标里的数据，这里只有一个，多个的话也一样；
    FETCH specs_list INTO a,b,c,d;
    -- 声明结束的时候
    IF DONE THEN
      LEAVE read_loop;
    END IF;
    -- 这里做你想做的循环的事件
				set i=0;
        set cnt = func_split_TotalLength(d,'*'); 
				while i < cnt 
				do 
				set i = i + 1; 
				insert into tmp_split(`resource_id`,`category_uuid`,`material_uuid`,`priority`,`val`) values (a,b,c,i, func_split(d,'*',i)); 
				end while; 
				
  END LOOP;
CLOSE specs_list;

END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_get_splitStringTotal
-- ----------------------------
DROP FUNCTION IF EXISTS `func_get_splitStringTotal`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `func_get_splitStringTotal`( 
f_string varchar(10000),f_delimiter varchar(50) 
) RETURNS int(11)
BEGIN 
return 1+(length(f_string) - length(replace(f_string,f_delimiter,''))); 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_split
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split`;
DELIMITER ;;
CREATE FUNCTION `func_split`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN 
    # 拆分传入的字符串，返回拆分后的新字符串 
        declare result varchar(255) default ''; 
        set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1)); 
        return result; 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_splitString
-- ----------------------------
DROP FUNCTION IF EXISTS `func_splitString`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `func_splitString`( f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN 

declare result varchar(255) default ''; 
set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
return result; 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_split_TotalLength
-- ----------------------------
DROP FUNCTION IF EXISTS `func_split_TotalLength`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `func_split_TotalLength`(f_string varchar(1000),f_delimiter varchar(5)) RETURNS int(11)
BEGIN 
    # 计算传入字符串的总length 
    return 1+(length(f_string) - length(replace(f_string,f_delimiter,''))); 
END
;;
DELIMITER ;
