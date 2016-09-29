package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import com.prcsteel.platform.account.model.model.AccountAttachment;

public interface AccountAttachmentDao {
    int deleteByPrimaryKey(Long id);

    int insert(AccountAttachment record);

    int insertSelective(AccountAttachment record);

    AccountAttachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountAttachment record);

    int updateByPrimaryKey(AccountAttachment record);
    
    List<AccountAttachment> listByAccountId(Long accountId);

    List<AccountAttachment> selectByAccountIdAndType(Long accountId,String type);
    
    void deleteByAccountIdAndType(Long accountId,String type);
}