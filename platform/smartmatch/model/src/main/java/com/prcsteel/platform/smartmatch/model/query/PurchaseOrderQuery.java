package com.prcsteel.platform.smartmatch.model.query;

import java.util.Date;
import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by wucong on 2015/11/26.
 */
public class PurchaseOrderQuery extends PagedQuery {
    private String code;
    private String buyerName;
    private String deliveryName;
    private String orgName;
    private String categoryName;
    private String status;
    private Date startTime;
    private Date endTime;
    private List<Long> userIds;
    private List<String> statusList;
    private User user;
    private String tabIndex;
    private String requirementCode;//需求单号
    
    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public String getCode() {
        return code;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getRequirementCode() {
		return requirementCode;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}

	
}

