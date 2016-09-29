package com.prcsteel.platform.smartmatch.model.model;

public class BusinessQueryData {
	private String spec;//前台规格列表
	
	private String categoryName;//前台品名
	
	private String materialName;//前台材质
	
	private String factoryName;//前台钢厂
	
	private String cityName;//前台交货地
	
	private String prefixSpec1;//规格区间前缀
	
	private String suffixSpec1;//规格区间后缀
	
	private String prefixSpec2;//规格区间前缀
	
	private String suffixSpec2;//规格区间后缀
	
	private String prefixSpec3;//规格区间前缀
	
	private String suffixSpec3;//规格区间后缀
	
	private int spec1Flag = 0;//规格1区间查询标记
	
	private int spec2Flag = 0;//规格2区间查询标记
	
	private int spec3Flag = 0;//规格3区间查询标记

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPrefixSpec1() {
		return prefixSpec1;
	}

	public void setPrefixSpec1(String prefixSpec1) {
		this.prefixSpec1 = prefixSpec1;
	}

	public String getSuffixSpec1() {
		return suffixSpec1;
	}

	public void setSuffixSpec1(String suffixSpec1) {
		this.suffixSpec1 = suffixSpec1;
	}

	public String getPrefixSpec2() {
		return prefixSpec2;
	}

	public void setPrefixSpec2(String prefixSpec2) {
		this.prefixSpec2 = prefixSpec2;
	}

	public String getSuffixSpec2() {
		return suffixSpec2;
	}

	public void setSuffixSpec2(String suffixSpec2) {
		this.suffixSpec2 = suffixSpec2;
	}

	public String getPrefixSpec3() {
		return prefixSpec3;
	}

	public void setPrefixSpec3(String prefixSpec3) {
		this.prefixSpec3 = prefixSpec3;
	}

	public String getSuffixSpec3() {
		return suffixSpec3;
	}

	public void setSuffixSpec3(String suffixSpec3) {
		this.suffixSpec3 = suffixSpec3;
	}

	public int getSpec1Flag() {
		return spec1Flag;
	}

	public void setSpec1Flag(int spec1Flag) {
		this.spec1Flag = spec1Flag;
	}

	public int getSpec2Flag() {
		return spec2Flag;
	}

	public void setSpec2Flag(int spec2Flag) {
		this.spec2Flag = spec2Flag;
	}

	public int getSpec3Flag() {
		return spec3Flag;
	}

	public void setSpec3Flag(int spec3Flag) {
		this.spec3Flag = spec3Flag;
	}
}
