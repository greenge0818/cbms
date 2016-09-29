package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.model.AccountManager;

public interface AccountManagerDao {
    int deleteByPrimaryKey(Long id);

    int insert(AccountManager record);

    int insertSelective(AccountManager record);

    AccountManager selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountManager record);

    int updateByPrimaryKey(AccountManager record);

	List<AccountManager> queryManagerList(Long id);

	int updateIsDeletedForIds(@Param("ids")List<Long> ids, @Param("isDeleted")int isDeleted);
}