package com.prcsteel.platform.smartmatch.model.model;

import java.util.List;

/**
 * 分检系统推送过来的询价单信息
 * @author tangwei
 *
 */
public class MqInquiryOrder {

	private String loginId;
	
	/**
	 * 需求单单号
	 */
	private String requirementCode;
	/**
	 * 询价单单号
	 */
	private String inquiryCode;
	/**
	 * 询价单id
	 */
	private int inquiryId;
	/**
	 * 需求单来源
	 */
	private String reqSource;
	/**
	 * 买家名称
	 */
	private String accountName;
	/**
	 * 联系人姓名
	 */
	private String contactName;
	/**
	 * 联系人手机号码
	 */
	private String contactMobile;
	/**
	 * 交货地id（城市）
	 */
	private Integer cityId;
	/**
	 * 交货地名称（城市）
	 */
	private String cityName;
	/**
	 * 创建时间（YYYY-MM-DD hh:mm:ss）
	 */
	private String createTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 明细
	 */
	private List<MqInquiryOrderItem> inquiryItems;

	public String getRequirementCode() {
		return requirementCode;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setRequirementCode(String requirementCode) {
		this.requirementCode = requirementCode;
	}

	public String getInquiryCode() {
		return inquiryCode;
	}

	public void setInquiryCode(String inquiryCode) {
		this.inquiryCode = inquiryCode;
	}

	public int getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}

	public String getReqSource() {
		return reqSource;
	}

	public void setReqSource(String reqSource) {
		this.reqSource = reqSource;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<MqInquiryOrderItem> getInquiryItems() {
		return inquiryItems;
	}

	public void setInquiryItems(List<MqInquiryOrderItem> inquiryItems) {
		this.inquiryItems = inquiryItems;
	}
}
