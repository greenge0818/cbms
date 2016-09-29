package com.prcsteel.platform.order.model.query;

import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * Created by rolyer on 15-9-16.
 */
public class ChecklistDetailQuery extends PagedQuery {
    private Long id;			//清单号id
    private String beginTime;
    private String endTime;
    private String buyerName;
    private List<String> orgIds;
    private String contractType;
    private String contractNo;
    
    private Long buyerId;
    private String orderCode;
    
    private Boolean isLimitDebet;
    private Boolean isSend;

    public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

	public List<String> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<String> orgIds) {
		this.orgIds = orgIds;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Boolean getIsLimitDebet() {
		return isLimitDebet;
	}

	public ChecklistDetailQuery setIsLimitDebet(Boolean isLimitDebet) {
		this.isLimitDebet = isLimitDebet;
		return this;
	}

	public Boolean getIsSend() {
		return isSend;
	}

	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}

}
