package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.model.InquiryHistory;

public interface InquiryHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(InquiryHistory record);

    int insertSelective(InquiryHistory record);

    InquiryHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InquiryHistory record);

    int updateByPrimaryKey(InquiryHistory record);
}