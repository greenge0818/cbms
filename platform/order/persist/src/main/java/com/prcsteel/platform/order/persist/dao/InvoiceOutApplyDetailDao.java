package com.prcsteel.platform.order.persist.dao;


import com.prcsteel.platform.order.model.dto.InvoiceOutApplyDetailDto;
import com.prcsteel.platform.order.model.model.InvoiceOutApplyDetail;
import com.prcsteel.platform.order.model.query.InvoiceOutApplyDetailQuery;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface InvoiceOutApplyDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutApplyDetail record);

    int insertSelective(InvoiceOutApplyDetail record);

    InvoiceOutApplyDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutApplyDetail record);

    int updateByPrimaryKey(InvoiceOutApplyDetail record);

    List<InvoiceOutApplyDetailDto> queryCondition(InvoiceOutApplyDetailQuery query);
    
    int batchInsert(List<InvoiceOutApplyDetail> records);

	int deleteForInvoiceOutApplyId(@Param("invoiceOutApplyId")Long invoiceOutApplyId,@Param("lastUpdatedBy")String lastUpdatedBy);

	List<Long> selectIdByPrimaryKey(@Param("invoiceOutApplyId")Long invoiceOutApplyId);

	List<InvoiceOutApplyDetail> queryInvoiceOutApplyDetails(@Param("detailIds")List<Long> detailIds,@Param("invoiceId")Long invoiceId);
	
	int batchDelete(@Param("detailIds")List<Long> detailIds,@Param("lastUpdatedBy")String lastUpdatedBy );

	void decreaseApplyAmount(@Param("amount")double amount,@Param("detailId")Long detailId,@Param("operatorName")String operatorName);

	InvoiceOutApplyDetailDto queryByDjh(@Param("djh") String djh);
}