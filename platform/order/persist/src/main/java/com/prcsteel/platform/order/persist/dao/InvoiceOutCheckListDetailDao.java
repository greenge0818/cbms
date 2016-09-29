package com.prcsteel.platform.order.persist.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.dto.ChecklistDetailDto;
import com.prcsteel.platform.order.model.dto.InvoiceOutCheckListDetailDto;
import com.prcsteel.platform.order.model.dto.UnSendInvoiceOutDto;
import com.prcsteel.platform.order.model.model.InvoiceOutCheckListDetail;
import com.prcsteel.platform.order.model.query.ChecklistDetailQuery;

public interface InvoiceOutCheckListDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(InvoiceOutCheckListDetail record);

    int insertSelective(InvoiceOutCheckListDetail record);

    InvoiceOutCheckListDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceOutCheckListDetail record);

    int updateByPrimaryKey(InvoiceOutCheckListDetail record);

    /**
     * 查询开票清单
     * @param query
     * @return
     */
    List<ChecklistDetailDto> queryByCondition(ChecklistDetailQuery query);

    /**
     * 统计开票清单数
     * @param query
     * @return
     */
    Integer countByCondition(ChecklistDetailQuery query);
    
    /**
     * 根据买家得到所有的已开销项金额之和
     * @param buyerId
     * @return
     */
    BigDecimal querySumAmountByBuyerId(Long buyerId);

	ChecklistDetailDto queryByConditionTotal(ChecklistDetailQuery query);
    
	  /**
     * 客户服留存销项报表
     * @author tuxianming
     * @date 20160621
     * @param query
     * @return
     */
    List<UnSendInvoiceOutDto> queryUnsendInvoiceOut(ChecklistDetailQuery query);
    
    /**
     * 客户留存销项报表统计
     * @author tuxianming
     * @date 20160621
     * @param query
     * @return
     */
    int totalUnsendInvoiceOut(ChecklistDetailQuery query);
    
    /**
     * 查询checklistDetail， 所有不可寄出的销项详情
     * @author tuxianming
     * @date 20160627
     * @param query
     * @return
     */
    List<InvoiceOutCheckListDetailDto> queryDetail(ChecklistDetailQuery query);
    
    /**
     * 更行checklistDetail是不是寄出状态
     * @author tuxianming
     * @date 20160720
     * @param query
     * @return
     */
   void updateSend(@Param("isSend") Boolean isSend, @Param("list") List<Long> ids);
	
}