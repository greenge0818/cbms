package com.prcsteel.platform.acl.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.acl.model.model.PrintLog;

public interface PrintLogDao {

    public int insert(PrintLog printLog);

    public List<PrintLog> query(@Param("billCode") String billCode, @Param("billType") String billType, @Param("userId") Long userId);

}