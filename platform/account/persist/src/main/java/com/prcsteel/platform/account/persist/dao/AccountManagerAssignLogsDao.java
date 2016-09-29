package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import com.prcsteel.platform.account.model.model.AccountManagerAssignLogs;

public interface AccountManagerAssignLogsDao {
    int deleteByPrimaryKey(Long id);

    int insert(AccountManagerAssignLogs record);

    int insertSelective(AccountManagerAssignLogs record);

    AccountManagerAssignLogs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountManagerAssignLogs record);

    int updateByPrimaryKey(AccountManagerAssignLogs record);

	int batchInsert(List<AccountManagerAssignLogs> list);
}