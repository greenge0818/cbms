package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhoucai on 2016/6/24.
 */
public class ResourceDailyDto implements Serializable{

	private Long id;
	private String categoryUuid;
    private String categoryName;
    private String materialUuid;
    private String materialName;
    private String spec;
    private Long factoryId;
    private String factoryName;
    private Long warehouseId;
    private String warehouseName;

    private BigDecimal price;
	/****仓库所在城市*****/
	private String cityName;

	public void setId(Long id) {
		this.id = id;
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


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


	public Long getId() {
		return id;
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


    public BigDecimal getPrice() {
        return price;
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



	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public ResourceDailyDto() {
		super();
	}

	public ResourceDailyDto( Long id,String categoryUuid, String categoryName,
					   String materialUuid, String materialName, String spec,
					   Long factoryId, String factoryName, Long warehouseId,
					   String warehouseName, String cityName,
					    BigDecimal price) {
		super();
		this.id = id;
		this.categoryUuid = categoryUuid;
		this.categoryName = categoryName;
		this.materialUuid = materialUuid;
		this.materialName = materialName;
		this.spec = spec;
		this.factoryId = factoryId;
		this.factoryName = factoryName;
		this.warehouseId = warehouseId;
		this.warehouseName = warehouseName;
		this.cityName = cityName;
		this.price = price;

	}
}