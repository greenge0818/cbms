package com.prcsteel.platform.smartmatch.model.query;

import java.util.Date;
import java.util.List;

/**
 * Created by wucong on 2015/12/8.
 */
public class InquiryOrderQuery {
    private String code;
    private String seller;
    private String city;
    private String categoryName;
    private String materialName;
    private String spec;
    private String warehouse;
    private String factory;
    private String startDate;
    private String endDate;
    private List<Long> userIds;
    
    public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public void setCode(String code) {
        this.code = code;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getCode() {
        return code;
    }

    public String getSeller() {
        return seller;
    }

    public String getCity() {
        return city;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getSpec() {
        return spec;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public String getFactory() {
        return factory;
    }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
