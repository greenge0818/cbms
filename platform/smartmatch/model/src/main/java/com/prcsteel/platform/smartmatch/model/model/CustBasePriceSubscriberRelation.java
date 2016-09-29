package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

/**
 * 买家订阅与基价关联表
 * @author prcsteel
 *
 */
public class CustBasePriceSubscriberRelation   {
	//创建时间
    private Date created;
    //创建人名称
    private String createdBy;
    //修改时间
    private Date lastUpdated;
    //修改人名称
    private String lastUpdatedBy;
    
    //订阅id
    private Long subscriberId;
    //基价id
    private Long basePriceId;

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getBasePriceId() {
        return basePriceId;
    }

    public void setBasePriceId(Long basePriceId) {
        this.basePriceId = basePriceId;
    }
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}