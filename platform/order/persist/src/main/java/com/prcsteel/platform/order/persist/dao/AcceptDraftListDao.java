package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.AcceptDraftList;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * 银票清单
 */
public interface AcceptDraftListDao {
    int deleteByPrimaryKey(Long id);

    int insert(AcceptDraftList record);

    int insertSelective(AcceptDraftList record);

    AcceptDraftList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcceptDraftList record);

    int updateByPrimaryKey(AcceptDraftList record);

    List<AcceptDraftList> queryByParam(AcceptDraftQuery query);

    Integer countByParam(AcceptDraftQuery query);

    List<AcceptDraftList> queryByAccountStatus(@Param("accountId") Long accountId, @Param("status") String status);

    List<AcceptDraftList> findByParam(@Param("code") String code, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("ids") List<Long> ids);

    Integer countById(@Param("code") String code);

    int batchInsert(List<AcceptDraftList> insertList);

    int batchUpdateAcceptDrafList(List<AcceptDraftList> updateList);

}