package com.prcsteel.platform.smartmatch.model.dto;
/** 
 * 缺失资源Dto
 * @author peanut <p>2016年2月25日 下午1:25:47</p>  
 */
public class LostResourceDto {
	/**
	 * 序号
	 */
	private String no;
	/**
	 * 采购单时间
	 */
	private String purchaseDate;
	/**
	 * 采购单采购区Id集,如：73,78,3,64
	 */
	private String purchaseCityIds;
	/**
	 * 采购单采购区域名称集，如：常州市,杭州市,上海市,无锡市
	 */
	private String purchaseCityNames;
	/**
	 * 品名uuid
	 */
	private String categoryUuid;
	/**
	 * 品名名称
	 */
	private String categoryName;
	/**
	 * 材质uuid
	 */
	private String materialUuid;
	/**
	 * 采购单采购材质名称
	 */
	private String purchaseMaterialNames;
	/**
	 * 采购单采购规格
	 */
	private String purchaseSpec;
	/**
	 * 采购单钢厂id集，如：747,48
	 */
	private String factoryIds;
	/**
	 * 采购单钢厂名称集，如：沙钢,中天
	 */
	private String purchaseFactoryNames;
	/**
	 * 来源
	 */
	private String sourceType;
	/**
	 * 分页总数
	 */
	private int total;
	
	public LostResourceDto() {
	}
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseCityIds() {
		return purchaseCityIds;
	}

	public void setPurchaseCityIds(String purchaseCityIds) {
		this.purchaseCityIds = purchaseCityIds;
	}

	public String getPurchaseCityNames() {
		return purchaseCityNames;
	}

	public void setPurchaseCityNames(String purchaseCityNames) {
		this.purchaseCityNames = purchaseCityNames;
	}

	public String getCategoryUuid() {
		return categoryUuid;
	}

	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialUuid() {
		return materialUuid;
	}

	public void setMaterialUuid(String materialUuid) {
		this.materialUuid = materialUuid;
	}

	public String getPurchaseMaterialNames() {
		return purchaseMaterialNames;
	}

	public void setPurchaseMaterialNames(String purchaseMaterialNames) {
		this.purchaseMaterialNames = purchaseMaterialNames;
	}

	public String getPurchaseSpec() {
		return purchaseSpec;
	}

	public void setPurchaseSpec(String purchaseSpec) {
		this.purchaseSpec = purchaseSpec;
	}

	public String getFactoryIds() {
		return factoryIds;
	}

	public void setFactoryIds(String factoryIds) {
		this.factoryIds = factoryIds;
	}

	public String getPurchaseFactoryNames() {
		return purchaseFactoryNames;
	}

	public void setPurchaseFactoryNames(String purchaseFactoryNames) {
		this.purchaseFactoryNames = purchaseFactoryNames;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
}
