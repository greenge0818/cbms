package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.model.AccountExt;
import org.apache.ibatis.annotations.Param;

public interface AccountExtDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountExt record);

    int insertSelective(AccountExt record);

    AccountExt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AccountExt record);

    int updateByPrimaryKey(AccountExt record);

    AccountExt selectByAccountId(Long id);

    int updateByAccountIdSelective(AccountExt record);

    int updateAccountExtByType(@Param("type") String type);
}