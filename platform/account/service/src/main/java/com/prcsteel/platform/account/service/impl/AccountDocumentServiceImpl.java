package com.prcsteel.platform.account.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.model.dto.AccountDocumentDto;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountDocumentDao;
import com.prcsteel.platform.account.service.AccountDocumentService;

@Service("accountDocumentService")
public class AccountDocumentServiceImpl implements
		AccountDocumentService {

    @Resource
    AccountDocumentDao accountAuditDocumentDao;
    
    @Resource
    AccountDao accountDao;
    
	@Override
	public List<CompanyDto> queryCompanyByStatus(CompanyQuery query) {
		return accountAuditDocumentDao.queryCompanyByStatus(query);
	}

	@Override
	public int queryTotalCompanyByStatus(CompanyQuery query) {
		return accountAuditDocumentDao.queryTotalCompanyByStatus(query);
	}

	@Override
	public int updateAccountExtStatus(AccountDocumentDto doc) {
		return accountAuditDocumentDao.updateAccountExtStatus(doc);
	}

	@Override
	public int updateAccountTag(Account account,String userName) {
        account.setLastUpdatedBy(userName);
        account.setLastUpdated(new Date());
        account.setModificationNumber(account.getModificationNumber() + 1);
        return accountDao.updateByPrimaryKeySelective(account);
	}

}
