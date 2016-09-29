package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import com.prcsteel.platform.account.persist.dao.AccountAcceptDraftDao;
import com.prcsteel.platform.account.service.AccountAcceptDraftService;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by chengui on 2016/4/22
 * 客户银票分配记录
 */
@Service("accountAcceptDraftService")
public class AccountAcceptDraftServiceImpl implements AccountAcceptDraftService {

    @Resource
    AccountAcceptDraftDao accountAcceptDraftDao;

    @Override
    public void save(AccountAcceptDraft accountAcceptDraft, User user) {
        AccountAcceptDraft old = accountAcceptDraftDao.selectByAccountIdAndAcceptDraftId(accountAcceptDraft);
        Date current = new Date();
        accountAcceptDraft.setLastUpdated(current);
        accountAcceptDraft.setLastUpdatedBy(user.getLoginId());
        int result;
        if(null == old){
            accountAcceptDraft.setCreated(current);
            accountAcceptDraft.setCreatedBy(user.getLoginId());
            result = accountAcceptDraftDao.insertSelective(accountAcceptDraft);
        }else{
            accountAcceptDraft.setId(old.getId());
            accountAcceptDraft.setCreated(old.getCreated());
            accountAcceptDraft.setCreatedBy(old.getCreatedBy());
            accountAcceptDraft.setAmount(old.getAmount().add(accountAcceptDraft.getAmount()));
            accountAcceptDraft.setModificationNumber(null == old.getModificationNumber() ? 1 : old.getModificationNumber() + 1);
            result = accountAcceptDraftDao.updateByPrimaryKeySelective(accountAcceptDraft);
        }

        if(result <= 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "分配银票资金失败");
        }
    }

}
