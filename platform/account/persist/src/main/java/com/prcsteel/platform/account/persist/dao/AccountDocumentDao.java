package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.query.CompanyQuery;

/**
 * 客户资料审核
 * @author tangwei
 *
 */
public interface AccountDocumentDao {

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
	
	int updateAccountExtStatus(AccountDocumentDto doc);
}
