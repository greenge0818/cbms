package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.dto.ContactDto;
import com.prcsteel.platform.smartmatch.model.dto.DepartmentDto;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderTableDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationInfoDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import org.apache.ibatis.annotations.Param;

public interface QuotationOrderItemsDao {
    int deleteByPrimaryKey(Long id);

    int insert(QuotationOrderItems record);

    int insertSelective(QuotationOrderItems record);

    QuotationOrderItems selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuotationOrderItems record);

    int updateByPrimaryKey(QuotationOrderItems record);

    List<QuotationOrderItemsDto> getDtoByOrderId(Long id);

    int batchInsert(List<QuotationOrderItems> items);

    PurchaseOrderTableDto selectByQuotationId(Integer id);
    /**
     * 获取当前卖家的联系人
     * @param companyId
     * @return
     */
    public List<DepartmentDto> queryDeptByCompanyId(Long companyId);
    /**
     * 根据询价单ID获取询价信息
     * @param companyId
     * @return
     */
    public List<ContactDto> queryContactsByCompanyId(Long companyId);
    /**
     * 根据询价单ID获取询价信息
     * @param id
     * @return
     */
    public PurchaseOrderTableDto selectQuotationByPurchaseId(Integer id);
    /**
     * 根据询价单获取当前的所有报价信息
     * @param purchaseId
     * @param quotationOrderId
     * @return
     */
	public List<QuotationOrderItemsDto> selectQuotationItemsByPurchaseId(
			Integer purchaseId);
	/**
	 * 根据报价单ID获取报价单详情
	 * @param id
	 * @return
	 */
	public List<QuotationInfoDto> buildOrderItemsByQuotationOrderId(Long id);


    List<QuotationInfoDto> buildOrderItemsByIds(@Param("ids") List<Long> ids);
    
    int deleteByQuotationOrderId(Long quotationOrderId);

	public List<QuotationInfoDto> getQuotationItemsUnbilledItems(Long id);
}