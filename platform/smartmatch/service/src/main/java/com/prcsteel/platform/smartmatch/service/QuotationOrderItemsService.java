package com.prcsteel.platform.smartmatch.service;


import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderTableDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationInfoDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;

public interface QuotationOrderItemsService {

    List<QuotationOrderItemsDto> getDtoByOrderId(Long id);

    /**
     * 根据报价单获取报价单详情的DTO集合
     * @param id
     * @return
     */
	public List<QuotationInfoDto> getQuotationInfoOrderItems(Long id,List<Long> userIds);

	/**
	 * 根据报价单详情id获取报价单详情的DTO集合
	 * @param ids
	 * @param userIds
	 * @return
	 */
	public List<QuotationInfoDto> getQuotationInfoOrderItemsByItemIds(List<Long> ids, List<Long> userIds);

	/**
	 * 保存报价单详情信息
	 * @param etDtoList
	 */
	public void saveQuotationOrderItems(List<QuotationInfoDto> etDtoList);
	
    PurchaseOrderTableDto selectByQuotationId(Integer id);

    /**
     * 根据报价单id查询历史报价单数据
     *
     * @param quotationOrderId 报价单id
     * @return
     * @author peanut
     * @date 2016/06/14
     */
    List<List<QuotationOrderItemsDto>> selectHistoryQuotationOrder(Long quotationOrderId);
    /**
     * 根据询价单获取询价信息
     * @param purchaseId
     * @return
     */
	public PurchaseOrderTableDto selectQuotationByPurchaseId(Integer purchaseId);
	/**
	 * 根据询价单ID获取报价历史。
	 * @param parseLong
	 * @return
	 */
	public List<List<QuotationOrderItemsDto>> selectHistoryQuotationOrderByPurchaseId(Integer purchaseId,Integer quotationOrderId);
	/**
	 * 获取最新的报价单ID
	 * @param purchaseId
	 * @return
	 */
	public Integer selectQuotationLastUpdateByPurchaseId(Integer purchaseId);

    /**
     * 根据报价单id集查询报价单详情数据
     *
     * @param quotationOrderIds 报价单id集
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    List<List<QuotationOrderItemsDto>> selectQuotationItemsByQuotationOrderIds(List<Long> quotationOrderIds);

    /**
     * 根据采购单id查询报价单详情数据
     *
     * @param purchaseOrderId 采购单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    List<List<QuotationOrderItemsDto>> selectQuotationByPurchaseOrderId(Long purchaseOrderId);
    /**
     * 根据ID更新报价单详情的状态
     * @param orderItemsId 当前需要修改的数据的id
     * @param statusStr 1：已开单      0：未开单
     * @param loginId  操作人登录名
     */
	public void updateStatusById(List<Long> orderItemsId, String statusStr, String loginId);
	 /**
     * 获取报价单详情还未开单的数量
     * @param id  当前的询价单的ID
     * @return
     */
	public Integer getQuotationItemsUnbilledCount(Long id);

}
