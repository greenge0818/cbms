package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.model.AccountAttachment;

import java.util.List;

/**
 * Created by lichaowei on 2016/3/19.
 */
public interface AccountAttachmentService {
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
