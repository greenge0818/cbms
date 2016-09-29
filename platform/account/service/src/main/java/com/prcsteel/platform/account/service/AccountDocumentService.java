package com.prcsteel.platform.account.service;

import java.util.List;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.query.CompanyQuery;

/**
 * 资料审核服务类
 * @author tangwei
 *
 */
public interface AccountDocumentService {

	/**
	 * 查询公司带审核列表
	 * @param query
	 * @return
	 */
    List<CompanyDto> queryCompanyByStatus(CompanyQuery query);

	
    /**
     * 统计公司带审核列表
     * @param query
     * @return
     */
	int queryTotalCompanyByStatus(CompanyQuery query);
	
	/**
	 * 审核公司证件,资料
	 * @param doc
	 * @return
	 */
	int updateAccountExtStatus(AccountDocumentDto doc);
	
	/**
	 * 更新客户性质
	 * @param record
	 * @return
	 */
	int updateAccountTag(Account account,String userName);
}
