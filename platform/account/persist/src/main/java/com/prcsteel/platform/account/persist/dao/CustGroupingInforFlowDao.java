package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.dto.CustGroupingInforDto;
import com.prcsteel.platform.account.model.model.CustGroupingInforFlow;
import com.prcsteel.platform.account.model.query.CustGroupingInforQuery;

import java.util.List;

public interface CustGroupingInforFlowDao {
    int deleteByPrimaryKey(Long id);

    int insert(CustGroupingInforFlow record);

    int insertSelective(CustGroupingInforFlow record);

    CustGroupingInforFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustGroupingInforFlow record);

    int updateByPrimaryKey(CustGroupingInforFlow record);

    List<CustGroupingInforDto> queryGroupingInfor(CustGroupingInforQuery custGroupingInforQuery);

    /**
     * @description :删除新增分组操作流水通过流水号
     * @author zhoucai@prcsteel.com
     * @version V1.0
     * @param flowNo
     * @return int
     */

    int deleteLimitFlowByFlowNo( String serial);


    
}