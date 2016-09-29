package com.prcsteel.platform.smartmatch.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.PurchaseOrderItems;

/**
 * Created by myh on 2015/11/24.
 */
public class PurchaseOrderItemsDto extends PurchaseOrderItems {
    private List<PurchaseOrderItemsAttributeDto> attributeList;
    private String option;
    private Long originId;
    private String sortUuid;
   // private String factoryNames;
    private String remark;//分拣推送过来的备注信息

    public PurchaseOrderItemsDto(){
    	
    }
    
    public PurchaseOrderItemsDto(String categoryUuid,String materialUuid,
    		String factoryIds,BigDecimal weight,Integer quantity,String spec1,String spec2,String spec3,Date time, String userName){
    	setCategoryUuid(categoryUuid);
    	setMaterialUuid(materialUuid);
    	setFactoryIds(factoryIds);
    	setWeight(weight);
    	setQuantity(quantity);
    	setSpec1(spec1);
    	setSpec2(spec2);
    	setSpec3(spec3);
    	setCreatedBy(userName);
    	setLastUpdatedBy(userName);
    	setCreated(time);
    	setLastUpdated(time);
    }
    
    public List<PurchaseOrderItemsAttributeDto> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<PurchaseOrderItemsAttributeDto> attributeList) {
        this.attributeList = attributeList;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getSortUuid() {
        return sortUuid;
    }

    public void setSortUuid(String sortUuid) {
        this.sortUuid = sortUuid;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
