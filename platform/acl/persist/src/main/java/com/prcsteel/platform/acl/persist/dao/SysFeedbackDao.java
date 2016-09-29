package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.SysFeedback;

public interface SysFeedbackDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysFeedback record);

    int insertSelective(SysFeedback record);

    SysFeedback selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysFeedback record);

    int updateByPrimaryKey(SysFeedback record);
}