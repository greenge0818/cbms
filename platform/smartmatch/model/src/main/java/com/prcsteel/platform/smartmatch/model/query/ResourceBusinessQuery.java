package com.prcsteel.platform.smartmatch.model.query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.BusinessQueryData;


/**
 * 业务找货资源查询条件
 * @author tangwei
 *
 */
public class ResourceBusinessQuery extends ResourceQuery{

	private String cityName;//交货地
	
	private List<BusinessQueryData> queryDataList = new LinkedList<BusinessQueryData>();
	
	private String conditionName;//搜索条件名称(查品名,材质,等)
	
	private String []specs;//前台规格列表
	
	private String []categorys;//前台品名列表
	
	private String []materials;//前台材质列表
	
	private String []factorys;//前台钢厂列表
	
	private String []citys;//前台交货地列表
	
	private Boolean isTodayPrice = false;//是否查看今日报价
	
	private String beginTime;
	
	private String endTime;

	private List<Long> accountIdList = new ArrayList<Long>();//客户Id列表集合
	
	private String prefixSpec1;//规格区间前缀
	
	private String suffixSpec1;//规格区间后缀
	
	private String prefixSpec2;//规格区间前缀
	
	private String suffixSpec2;//规格区间后缀
	
	private String prefixSpec3;//规格区间前缀
	
	private String suffixSpec3;//规格区间后缀
	
	private int spec1Flag = 0;//规格1区间查询标记
	
	private int spec2Flag = 0;//规格2区间查询标记
	
	private int spec3Flag = 0;//规格3区间查询标记
	
	private int specQueryFlag = 0;//区间查询标记
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<BusinessQueryData> getQueryDataList() {
		return queryDataList;
	}

	public void setQueryDataList(List<BusinessQueryData> queryDataList) {
		this.queryDataList = queryDataList;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String[] getSpecs() {
		return specs;
	}

	public void setSpecs(String[] specs) {
		this.specs = specs;
	}

	public String[] getCategorys() {
		return categorys;
	}

	public void setCategorys(String[] categorys) {
		this.categorys = categorys;
	}

	public String[] getMaterials() {
		return materials;
	}

	public void setMaterials(String[] materials) {
		this.materials = materials;
	}

	public String[] getFactorys() {
		return factorys;
	}

	public void setFactorys(String[] factorys) {
		this.factorys = factorys;
	}

	public String[] getCitys() {
		return citys;
	}

	public void setCitys(String[] citys) {
		this.citys = citys;
	}

	public Boolean getIsTodayPrice() {
		return isTodayPrice;
	}

	public void setIsTodayPrice(Boolean isTodayPrice) {
		this.isTodayPrice = isTodayPrice;
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

	public List<Long> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(List<Long> accountIdList) {
		this.accountIdList = accountIdList;
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

	public int getSpecQueryFlag() {
		return specQueryFlag;
	}

	public void setSpecQueryFlag(int specQueryFlag) {
		this.specQueryFlag = specQueryFlag;
	}

}
