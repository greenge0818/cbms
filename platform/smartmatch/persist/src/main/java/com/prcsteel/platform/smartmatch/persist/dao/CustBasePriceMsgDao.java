package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.CustBasePriceMsg;
import com.prcsteel.platform.smartmatch.model.query.MsgQuery;

public interface CustBasePriceMsgDao {
    int deleteByPrimaryKey(Long id);

    int insert(CustBasePriceMsg record);

    int insertSelective(CustBasePriceMsg record);

    CustBasePriceMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustBasePriceMsg record);

    int updateByPrimaryKey(CustBasePriceMsg record);
    
    /**
     * 历史短信查询
     * @param msgQuery
     * @return
     */
    List<CustBasePriceMsg> getHistoricalMsg (MsgQuery msgQuery);
    
    /**
     * 历史短信记录数
     * @param msgQuery
     * @return
     */
    int getHistoricalMsgCount (MsgQuery msgQuery);
}