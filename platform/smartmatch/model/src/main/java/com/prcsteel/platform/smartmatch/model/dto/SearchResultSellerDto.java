package com.prcsteel.platform.smartmatch.model.dto;

import java.util.List;

import com.prcsteel.platform.account.model.model.AccountContact;

/**
 * 卖家详情
 */
public class SearchResultSellerDto {
	/**
	 * 卖家类型
	 */
	private String consignType;
	/**
	 * 卖家ID
	 */
	private Long sellerId;
	/**
	 * 卖家名称
	 */
	private String sellerName;
	/**
	 * 联系人
	 */
	List<AccountContact> contactList;
	/**
	 * 交易员ID
	 */
	private Long traderId;
	/**
	 * 交易员
	 */
	private String traderName;
	/**
	 * 资源信息
	 */
	List<SearchResultItemsDto> itemList;

	private String inquiryDateShow;
	
	private String lastUserShow;
	

	public String getConsignType() {
		return consignType;
	}
	public void setConsignType(String consignType) {
		this.consignType = consignType;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<AccountContact> getContactList() {
		return contactList;
	}
	public void setContactList(List<AccountContact> contactList) {
		this.contactList = contactList;
	}
	public Long getTraderId() {
		return traderId;
	}
	public void setTraderId(Long traderId) {
		this.traderId = traderId;
	}
	public String getTraderName() {
		return traderName;
	}
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	public List<SearchResultItemsDto> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchResultItemsDto> itemList) {
		this.itemList = itemList;
	}
	public String getInquiryDateShow() {
		return inquiryDateShow;
	}
	public void setInquiryDateShow(String inquiryDateShow) {
		this.inquiryDateShow = inquiryDateShow;
	}
	public String getLastUserShow() {
		return lastUserShow;
	}
	public void setLastUserShow(String lastUserShow) {
		this.lastUserShow = lastUserShow;
	}
	public SearchResultSellerDto(String consignType,Long sellerId, String sellerName, List<AccountContact> contactList, Long traderId,
			String traderName, List<SearchResultItemsDto> itemList, String inquiryDateShow, String lastUserShow) {
		super();
		this.consignType=consignType;
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.contactList = contactList;
		this.traderId = traderId;
		this.traderName = traderName;
		this.itemList = itemList;
		this.inquiryDateShow = inquiryDateShow;
		this.lastUserShow = lastUserShow;
	}
	
}
