package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.smartmatch.model.dto.QuotationItemsPO;
import com.prcsteel.platform.smartmatch.model.dto.RequirementStatusDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(QuotationOrder record);

    int insertSelective(QuotationOrder record);

    QuotationOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuotationOrder record);

    int updateByPrimaryKey(QuotationOrder record);
	/**
	 * 获取当前询价单ID对应的所有的报价id
	 * @param purchaseId
	 * @return
	 */
	List<Long> selectAllQuotationIdByPurchaseId(Long purchaseId);
	/**
	 * 获取最新的报价单ID
	 * @param purchaseId
	 * @return
	 */
	Integer selectQuotationLastUpdateByPurchaseId(Integer purchaseId);

    /**
     * 获取指定采购单最新报价单ID
     *
     * @param purchaseOrderId 采购单ID
     * @return
     */
    Long queryTheLatestQuotationOrderIdByPurchaseOrderId(Long purchaseOrderId);

    /**
     * 根据采购单Id获取报价单
     *
     * @param purchaseOrderId
     * @return
     */
    List<QuotationOrder> selectByPurchaseOrderId(Long purchaseOrderId);


    /**
     * 根据报价单id找到对应采购单所对应所有报价单id
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    List<Long> selectAllQuotationIdByQuotationOrderId(Long quotationOrderId);

    /**
     * 根据采购单id找到对应所有报价单id
     *
     * @param purchaseOrderId 采购单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    List<Long> selectAllQuotationIdByPurchaseOrderId(Long purchaseOrderId);

    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIdList 报价单id集,例：1,2,3,4
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    List<QuotationItemsPO> selectQuotationItemsByQuotationIds(@Param("quotationIdList") List<Long> quotationIdList);

    /**
     *根据报价单id查询出推送报价单数据
     *
     * @param quotaionOrderId  报价单id
     * @return
     * @author peanut
     * @date 2016/6/27
     */
    RequirementStatusDto selectPushQuotationOrder(Long quotaionOrderId);
    
    /**
     * 根据询价单获取最新的报价单
     * @param purchaseOrderId
     * @return
     */
	QuotationOrder selectByPurchaseOderId(Long purchaseOrderId);
}