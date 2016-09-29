package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSellersDto;
import com.prcsteel.platform.smartmatch.model.model.InquiryOrderSellers;

public interface InquiryOrderSellersDao {
	int deleteByPrimaryKey(Long id);

	int insert(InquiryOrderSellers record);

	int insertSelective(InquiryOrderSellers record);

	InquiryOrderSellers selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(InquiryOrderSellers record);

	int updateByPrimaryKey(InquiryOrderSellers record);

	int deleteByInquiryOrderId(Long inquiryOrderId);

	List<InquiryOrderSellers> getInquiryOrderSellers(Long inquiryOrderId);

	InquiryOrderSellersDto selectByAccountId(Long accountId);

	List<Long> selectInqueriredAccountIds(Long inquiryOrderId);
}