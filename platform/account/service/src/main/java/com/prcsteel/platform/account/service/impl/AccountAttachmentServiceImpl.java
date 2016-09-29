package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.model.AccountAttachment;
import com.prcsteel.platform.account.persist.dao.AccountAttachmentDao;
import com.prcsteel.platform.account.service.AccountAttachmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lichaowei on 2016/3/19.
 */
@Service("accountAttachmentService")
public class AccountAttachmentServiceImpl implements AccountAttachmentService {

    @Resource
    AccountAttachmentDao aamDao;

    @Override
    public int deleteByPrimaryKey(Long id){
        return aamDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountAttachment record){
        return aamDao.insert(record);
    }

    @Override
    public int insertSelective(AccountAttachment record){
        return aamDao.insertSelective(record);
    }

    @Override
    public AccountAttachment selectByPrimaryKey(Long id){
        return aamDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountAttachment record){
        return aamDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountAttachment record){
        return aamDao.updateByPrimaryKey(record);
    }

    @Override
    public List<AccountAttachment> listByAccountId(Long accountId){
        return aamDao.listByAccountId(accountId);
    }

    @Override
    public List<AccountAttachment> selectByAccountIdAndType(Long accountId,String type){
        return aamDao.selectByAccountIdAndType(accountId, type);
    }

    @Override
    public void deleteByAccountIdAndType(Long accountId,String type){
        aamDao.deleteByAccountIdAndType(accountId, type);
    }

}
