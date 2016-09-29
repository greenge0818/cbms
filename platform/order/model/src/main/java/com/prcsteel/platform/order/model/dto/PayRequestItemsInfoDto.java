package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.PayRequestItems;

/**
 * Created by Administrator on 2015/8/11.
 */
public class PayRequestItemsInfoDto extends PayRequestItems {
    private Long contractId;
    
    private Integer changeOrderId;
    
    private String type;

    public Integer getChangeOrderId() {
		return changeOrderId;
	}

	public void setChangeOrderId(Integer changeOrderId) {
		this.changeOrderId = changeOrderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
