package com.prcsteel.platform.smartmatch.model.dto;

/**
 * 资源集
 */
public class SearchResultItemsDto {
	/**
	 * 采购单ID
	 */
	private Long purchaseOrderId;
	/**
	 * 询价单明细ID
	 */
	private Long inquiryOrderItemId;
	/**
	 * 资源ID
	 */
	private Long resourceId;
	/**
	 * 单件重量
	 */
	private Double singleWeight;
	
	/**
	 * 品名
	 */
	private String categoryName;
	/**
	 * 品名UUID
	 */
	private String categoryUuid;
	/**
	 * 厂家ID
	 */
	private Long factoryId;
	/**
	 * 厂家名称
	 */
	private String factoryName;
	/**
	 * 材质
	 */
	private String materialName;
	/**
	 * 材质UUID
	 */
	private String materialUuid;
	/**
	 * 价格
	 */
	private Float price;
	/**
	 * 求购重量
	 */
	private Double purchaseWeight;
	/**
	 * 求购数量
	 */
	private Integer purchaseQuantity;
	/**
	 * 可供件数
	 */
	private Integer resultQuantity;
	/**
	 * 可供重量
	 */
	private Double resultWeight;
	/**
	 * 规格
	 */
	private String spec;
	/**
	 * 仓库ID
	 */
	private Long warehouseId;
	/**
	 * 仓库名
	 */
	private String warehouseName;
	/**
	 * 异常仓库
	 */
	private String abnormalWarehouse;
	/**
	 * 仓库所在城市ID
	 */
	private Long warehouseCityId;
	/**
	 * 仓库所在城市名
	 */
	private String warehouseCityName;
	/**
	 * 计重方式
	 */
	private String weightConcept;
	/**
	 * 采购资源明细ID
	 */
	private Long purchaseOrderItemId;
	/**
	 * 重量是否满足85%范围要求
	 */
	private Integer isOK;
	/**
	 * 采购资源规格
	 */
	private String purchaseSpec;
	/**
	 * 采购资源指定钢厂ID列表
	 */
	private String purchaseFactoryIds;
	/**
	 * 采购资源指定钢厂名称列表
	 */
	private String purchaseFactoryNames;
	/**
	 * 采购资源指定规格个数
	 */
	private Integer purchaseSpecLength;
	/**
	 * 采购品规基表规格个数
	 */
	private Integer commonSpecLength;
	/**
	 * 资源类型
	 */
	private String status;
	/**
	 * 采购材质UUID列表 如 aaaaaa，bbbbbb，ccccc  a，b，c均为uuid
	 */
	private String purchaseMaterialUuids;
	/**
	 * 采购材质名称列表 如 1,2,3
	 */
	private String purchaseMaterialNames;
	
	
	public Long getInquiryOrderItemId() {
		return inquiryOrderItemId;
	}
	public void setInquiryOrderItemId(Long inquiryOrderItemId) {
		this.inquiryOrderItemId = inquiryOrderItemId;
	}
	public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}
	
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public String getCategoryUuid() {
		return categoryUuid;
	}
	public Long getFactoryId() {
		return factoryId;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public String getMaterialName() {
		return materialName;
	}
	public String getMaterialUuid() {
		return materialUuid;
	}
	public Float getPrice() {
		return price==null?0:price;
	}
	public Double getPurchaseWeight() {
		return purchaseWeight;
	}
	public Integer getResultQuantity() {
		return resultQuantity==null?0:resultQuantity;
	}
	public Double getResultWeight() {
		return resultWeight==null?0:resultWeight;
	}
	public String getSpec() {
		return spec==null?"":spec;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public String getWarehouseName() {
		return warehouseName==null?"":warehouseName;
	}
	public String getWeightConcept() {
		return weightConcept;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}
	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public void setPurchaseWeight(Double purchaseWeight) {
		this.purchaseWeight = purchaseWeight;
	}
	public void setResultQuantity(Integer resultQuantity) {
		this.resultQuantity = resultQuantity;
	}
	public void setResultWeight(Double resultWeight) {
		this.resultWeight = resultWeight;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public void setWeightConcept(String weightConcept) {
		this.weightConcept = weightConcept;
	}
	public Double getSingleWeight() {
		return singleWeight;
	}
	public void setSingleWeight(Double singleWeight) {
		this.singleWeight = singleWeight;
	}
	public Integer getIsOK() {
		return isOK;
	}
	public void setIsOK(Integer isOK) {
		this.isOK = isOK;
	}
	public String getPurchaseSpec() {
		return purchaseSpec;
	}
	public void setPurchaseSpec(String purchaseSpec) {
		this.purchaseSpec = purchaseSpec;
	}
	public String getPurchaseFactoryIds() {
		return purchaseFactoryIds;
	}
	public void setPurchaseFactoryIds(String purchaseFactoryIds) {
		this.purchaseFactoryIds = purchaseFactoryIds;
	}
	public String getPurchaseFactoryNames() {
		return purchaseFactoryNames;
	}
	public void setPurchaseFactoryNames(String purchaseFactoryNames) {
		this.purchaseFactoryNames = purchaseFactoryNames;
	}
	public Integer getPurchaseSpecLength() {
		return purchaseSpecLength;
	}
	public void setPurchaseSpecLength(Integer purchaseSpecLength) {
		this.purchaseSpecLength = purchaseSpecLength;
	}
	public Integer getCommonSpecLength() {
		return commonSpecLength;
	}
	public void setCommonSpecLength(Integer commonSpecLength) {
		this.commonSpecLength = commonSpecLength;
	}
	public Integer getPurchaseQuantity() {
		return purchaseQuantity;
	}
	public void setPurchaseQuantity(Integer purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}
	public Long getWarehouseCityId() {
		return warehouseCityId;
	}
	public void setWarehouseCityId(Long warehouseCityId) {
		this.warehouseCityId = warehouseCityId;
	}
	public String getWarehouseCityName() {
		return warehouseCityName;
	}
	public void setWarehouseCityName(String warehouseCityName) {
		this.warehouseCityName = warehouseCityName;
	}
	public String getAbnormalWarehouse() {
		return abnormalWarehouse;
	}
	public void setAbnormalWarehouse(String abnormalWarehouse) {
		this.abnormalWarehouse = abnormalWarehouse;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPurchaseMaterialUuids() {
		return purchaseMaterialUuids;
	}
	public void setPurchaseMaterialUuids(String purchaseMaterialUuids) {
		this.purchaseMaterialUuids = purchaseMaterialUuids;
	}
	public String getPurchaseMaterialNames() {
		return purchaseMaterialNames;
	}
	public void setPurchaseMaterialNames(String purchaseMaterialNames) {
		this.purchaseMaterialNames = purchaseMaterialNames;
	}
	
	
}
