package com.prcsteel.platform.account.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.account.model.model.AssignLog;

/**
 * Created by caochao on 2015/7/14.
 */
public interface AssignLogDao {
    public List<AssignLog> query(@Param("accountId") Long accountId, @Param("start")int begin,@Param("length")int end);
    public int count(@Param("accountId") Long accountId);
}
