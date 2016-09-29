package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.dto.AcceptDraftDto;
import com.prcsteel.platform.order.model.model.AcceptDraft;
import com.prcsteel.platform.order.model.query.AcceptDraftQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcceptDraftDao {
    int deleteByPrimaryKey(Long id);

    int insert(AcceptDraft record);

    int insertSelective(AcceptDraft record);

    AcceptDraft selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcceptDraft record);

    int updateByPrimaryKey(AcceptDraft record);

    int updateStatusByPrimaryKeyAndOldStatus(AcceptDraftDto record);

    List<AcceptDraft> queryByParam(AcceptDraftQuery query);

    Integer countByParam(AcceptDraftQuery query);

    List<AcceptDraft> queryByAccountStatus(@Param("accountId") Long accountId, @Param("status") String status, @Param("code") String code);

}