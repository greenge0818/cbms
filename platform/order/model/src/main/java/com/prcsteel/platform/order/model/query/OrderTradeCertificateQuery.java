package com.prcsteel.platform.order.model.query;

import java.util.List;

/**
 * 寄售交易单管理 --> 交易凭证查询条件 
 * @author tuxianming
 */
public class OrderTradeCertificateQuery {
	
	private Boolean blacklist;  //黑名单,   {false|null}:不须审核通过也能开票、{true}必须审核通过才能开票,  供应商标示: 白名单卖家/非白名单卖家/非供应商等
	private Boolean groupBySeller; //按卖家分组
	private Boolean needPage = true; //是不是需要分页，用于批量打印的时候, 也就是批量打印的时候，为false
	private Boolean moreInfo = false; //是不是返回订单详情
	private Boolean credentialNull;			//打印凭证号为空
	private Boolean credentialNullOfBatch;	//打印批量凭证号为空
	private Boolean closeStatus;	//关闭订单
	private Boolean printQRCode=false; //是不是打印凭证号二维码
	private Integer credentialNum; 	//凭证总页数

	private List<Long> orderIds;	
	private String ownerName;
	private String code;
	private Long sellerId;
	private String sellerName;
	private Long buyerId;
	private String buyerName;
	private String startTime;
	private String endTime;
	//Green.Ge 4/8 added 用于上传凭证查询界面参数
	private String accountType;//客户类型;
	private Long certificateId;//凭证号ID
	private String certificateNO;//凭证号
//	private Boolean approvalRequired;//是否必须审核通过才能开票
	private List<Long> userIds;
	private String excludeIds;
	private String includeIds;
	
	private String credentialType; 	//凭证类型： 买家凭证， 卖家凭证（buyer, seller）
	private String status;
	private Integer start;
	private Integer length;
	private String isActualWeightZero;//打印时排除二结实提重量为0的明细  1 排除，0不排除
	
	
	public String getIsActualWeightZero() {
		return isActualWeightZero;
	}
	public void setIsActualWeightZero(String isActualWeightZero) {
		this.isActualWeightZero = isActualWeightZero;
	}
	public List<Long> getOrderIds() {
		return orderIds;
	}
	public OrderTradeCertificateQuery setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
		return this;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public OrderTradeCertificateQuery setOwnerName(String ownerName) {
		this.ownerName = ownerName;
		return this;
	}
	public String getCode() {
		return code;
	}
	public OrderTradeCertificateQuery setCode(String code) {
		this.code = code;
		return this;
	}
	
	public String getSellerName() {
		return sellerName;
	}
	public OrderTradeCertificateQuery setSellerName(String sellerName) {
		this.sellerName = sellerName;
		return this;
	}
	public String getStartTime() {
		return startTime;
	}
	public OrderTradeCertificateQuery setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}
	public String getEndTime() {
		return endTime;
	}
	public OrderTradeCertificateQuery setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}
	public Integer getStart() {
		return start;
	}
	public OrderTradeCertificateQuery setStart(Integer start) {
		this.start = start;
		return this;
	}
	public Integer getLength() {
		return length;
	}
	public OrderTradeCertificateQuery setLength(Integer length) {
		this.length = length;
		return this;
	}
	public Boolean getGroupBySeller() {
		return groupBySeller;
	}
	public OrderTradeCertificateQuery setGroupBySeller(Boolean groupBySeller) {
		this.groupBySeller = groupBySeller;
		return this;
	}
	public Boolean getNeedPage() {
		return needPage;
	}
	public void setNeedPage(Boolean needPage) {
		this.needPage = needPage;
	}
	
	
	public Long getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(Long certificateId) {
		this.certificateId = certificateId;
	}
	public String getCertificateNO() {
		return certificateNO;
	}
	public void setCertificateNO(String certificateNO) {
		this.certificateNO = certificateNO;
	}
//	public Boolean getApprovalRequired() {
//		return approvalRequired;
//	}
//	public void setApprovalRequired(Boolean approvalRequired) {
//		this.approvalRequired = approvalRequired;
//	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public OrderTradeCertificateQuery setBuyerName(String buyerName) {
		this.buyerName = buyerName;
		return this;
	}
	public String getCredentialType() {
		return credentialType;
	}
	public OrderTradeCertificateQuery setCredentialType(String credentialType) {
		this.credentialType = credentialType;
		return this;
	}
	public Boolean getMoreInfo() {
		return moreInfo;
	}
	public OrderTradeCertificateQuery setMoreInfo(Boolean moreInfo) {
		this.moreInfo = moreInfo;
		return this;
	}
	public Boolean getCredentialNull() {
		return credentialNull;
	}
	public OrderTradeCertificateQuery setCredentialNull(Boolean credentialNull) {
		this.credentialNull = credentialNull;
		return this;
	}
	public Boolean getCredentialNullOfBatch() {
		return credentialNullOfBatch;
	}
	public OrderTradeCertificateQuery setCredentialNullOfBatch(Boolean credentialNullOfBatch) {
		this.credentialNullOfBatch = credentialNullOfBatch;
		return this;
	}
	public Boolean getCloseStatus() {
		return closeStatus;
	}
	public OrderTradeCertificateQuery setCloseStatus(Boolean closeStatus) {
		this.closeStatus = closeStatus;
		return this;
	}
	
	public Boolean getBlacklist() {
		return blacklist;
	}
	public OrderTradeCertificateQuery setBlacklist(Boolean blacklist) {
		this.blacklist = blacklist;
		return this;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public OrderTradeCertificateQuery setSellerId(Long sellerId) {
		this.sellerId = sellerId;
		return this;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public String getExcludeIds() {
		return excludeIds;
	}
	public void setExcludeIds(String excludeIds) {
		this.excludeIds = excludeIds;
	}
	public String getIncludeIds() {
		return includeIds;
	}
	public void setIncludeIds(String includeIds) {
		this.includeIds = includeIds;
	}
	public Boolean getPrintQRCode() {
		return printQRCode;
	}
	public void setPrintQRCode(Boolean printQRCode) {
		this.printQRCode = printQRCode;
	}
	public Integer getCredentialNum() {
		return credentialNum;
	}
	public void setCredentialNum(Integer credentialNum) {
		this.credentialNum = credentialNum;
	}
}
