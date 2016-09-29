package com.prcsteel.platform.smartmatch.model.model;

import java.util.Date;

/**
 * 买家订阅与联系人关联表对象
 * @author prcsteel
 *
 */
public class CustBasePriceContactRelation{
	//主键
	private Long id;
	//创建时间
    private Date created;
    //订阅id
    private Long subscriberId;
    //联系人ID
    private Long contactId;
    //联系人名称
    private String contactName;
    //联系人电话
    private String tel;
    //创建人名称
    private String createdBy;
    //修改时间
    private Date lastUpdated;
    //修改人名称
    private String lastUpdatedBy;

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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