package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wucong on 2015/11/24.
 */
public class ResourceDto implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7390417048886541555L;
	
	private Long id;
    private Long accountId;
    private String accountName;
    private String categoryUuid;
    private String categoryName;
    private String materialUuid;
    private String materialName;
    private String spec;
    private String spec1;
    private String spec2;
    private String spec3;
    private Long factoryId;
    private String factoryName;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal weight;
    private String weightConcept;
    private Integer quantity;
    private BigDecimal price;
    private String remark;
    private String status;
    private String statusNum;
    private String sourceType;
    private String lastUpdatedBy;
    private String userIds;
    private Integer total;
    /**** 日常询价更新时间  *****/
	private Date lastUpdated;
	
	/**** 日常正常更新时间  *****/
	private Date mgtLastUpdated;
	
	//add by caosulin@prcsteel.com 2016.6.2增加异常和城市
	/****异常状态，exception 异常，normal 正常*****/
	private String isException;
	
	/****仓库所在城市*****/
	private String cityName;


	/****仓库所在城市id*****/
	private Long cityId;


	/****价格的涨跌****/
	private String priceChange;
    
	public void setId(Long id) {
		this.id = id;
	}

	public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setWeightConcept(String weightConcept) {
        this.weightConcept = weightConcept;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
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

    public String getFactoryName() {
        return factoryName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getWeightConcept() {
        return weightConcept;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    public String getStatus() {
        return status;
    }

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getMaterialUuid() {
		return materialUuid;
	}

	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(String statusNum) {
		this.statusNum = statusNum;
	}
	
	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}


	public Date getMgtLastUpdated() {
		return mgtLastUpdated;
	}

	public void setMgtLastUpdated(Date mgtLastUpdated) {
		this.mgtLastUpdated = mgtLastUpdated;
	}
	
	public String getIsException() {
		return isException;
	}

	public void setIsException(String isException) {
		this.isException = isException;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getPriceChange() {
		return priceChange;
	}

	public void setPriceChange(String priceChange) {
		this.priceChange = priceChange;
	}

	public String getSpec1() {
		return spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	public String getSpec2() {
		return spec2;
	}

	public void setSpec2(String spec2) {
		this.spec2 = spec2;
	}

	public String getSpec3() {
		return spec3;
	}

	public void setSpec3(String spec3) {
		this.spec3 = spec3;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public ResourceDto() {
		super();
	}

	public ResourceDto(Long id, Long accountId, String accountName,
			String categoryUuid, String categoryName,
			String materialUuid, String materialName, String spec,
			Long factoryId, String factoryName, Long warehouseId,
			String warehouseName, BigDecimal weight, String weightConcept,
			Integer quantity, BigDecimal price, String remark, String status,
			String sourceType,String lastUpdatedBy,String userIds) {
		super();
		this.id = id;
		this.accountId = accountId;
		this.accountName = accountName;
		this.categoryUuid = categoryUuid;
		this.categoryName = categoryName;
		this.materialUuid = materialUuid;
		this.materialName = materialName;
		this.spec = spec;
		this.factoryId = factoryId;
		this.factoryName = factoryName;
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.weight = weight;
		this.weightConcept = weightConcept;
		this.quantity = quantity;
		this.price = price;
		this.remark = remark;
		this.status = status;
		this.sourceType=sourceType;
		this.lastUpdatedBy = lastUpdatedBy;
		this.userIds=userIds;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ResourceDto that = (ResourceDto) o;

		if (categoryUuid != null ? !categoryUuid.equals(that.categoryUuid) : that.categoryUuid != null) return false;
		if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null) return false;
		if (materialUuid != null ? !materialUuid.equals(that.materialUuid) : that.materialUuid != null) return false;
		if (materialName != null ? !materialName.equals(that.materialName) : that.materialName != null) return false;
		if (spec1 != null ? !spec1.equals(that.spec1) : that.spec1 != null) return false;
		if (spec2 != null ? !spec2.equals(that.spec2) : that.spec2 != null) return false;
		if (spec3 != null ? !spec3.equals(that.spec3) : that.spec3 != null) return false;
		if (factoryId != null ? !factoryId.equals(that.factoryId) : that.factoryId != null) return false;
		if (factoryName != null ? !factoryName.equals(that.factoryName) : that.factoryName != null) return false;
		if (cityId != null ? !cityId.equals(that.cityId) : that.cityId != null) return false;
		return cityName != null ? cityName.equals(that.cityName) : that.cityName == null;

	}

	@Override
	public int hashCode() {
		int result = categoryUuid != null ? categoryUuid.hashCode() : 0;
		result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
		result = 31 * result + (materialUuid != null ? materialUuid.hashCode() : 0);
		result = 31 * result + (materialName != null ? materialName.hashCode() : 0);
		result = 31 * result + (spec1 != null ? spec1.hashCode() : 0);
		result = 31 * result + (spec2 != null ? spec2.hashCode() : 0);
		result = 31 * result + (spec3 != null ? spec3.hashCode() : 0);
		result = 31 * result + (factoryId != null ? factoryId.hashCode() : 0);
		result = 31 * result + (factoryName != null ? factoryName.hashCode() : 0);
		result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
		result = 31 * result + (cityId != null ? cityId.hashCode() : 0);
		return result;
	}
}