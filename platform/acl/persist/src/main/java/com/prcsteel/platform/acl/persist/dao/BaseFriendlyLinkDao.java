package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.BaseFriendlyLink;

import java.util.List;

public interface BaseFriendlyLinkDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseFriendlyLink record);

    int insertSelective(BaseFriendlyLink record);

    BaseFriendlyLink selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseFriendlyLink record);

    int updateByPrimaryKey(BaseFriendlyLink record);

    List<BaseFriendlyLink> selectAllLink();
}