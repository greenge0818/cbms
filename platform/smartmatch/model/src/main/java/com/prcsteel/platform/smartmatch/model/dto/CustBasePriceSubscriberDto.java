package com.prcsteel.platform.smartmatch.model.dto;
import java.util.Date;
import java.util.List;


/**
 * 买家订阅基价DTO
 * @author prcsteel
 *
 */
public class CustBasePriceSubscriberDto {
	//id
    private Long id;
    //买家名称
    private String accountName;
    //买家ID
    private String accountId;
    //交易员
    private String ownerName;
    //交易员ID
    private String ownerId;
    //地区名称
    private String cityName;
    //城市ID
    private Long cityId;
    //订阅基价的名称
    private String subBasePriceName;
    //订阅基价的名称
    private String subBasePriceIds;
    //电话号码。
    private String tel;
    //订阅基价的联系人
    private String subBasePriceContactIds;
    //订阅基价的联系人名称
    private String subBasePriceContactNames;
    //订阅基价的联系人电话
    private String subBasePriceContactTels;
	//订阅基价交易员信息
	private String subBasePriceTraderIds;
    //操作类型
    private String actionType;
    
    private Date created;

    private String createdBy;

    private Date lastUpdated;

    private String lastUpdatedBy;
    //对接业务员
    private String traderTel;
    
    //联系人id集合
    private List<String> contactIds ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public String getSubBasePriceName() {
		return subBasePriceName;
	}

	public void setSubBasePriceName(String subBasePriceName) {
		this.subBasePriceName = subBasePriceName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSubBasePriceIds() {
		return subBasePriceIds;
	}

	public void setSubBasePriceIds(String subBasePriceIds) {
		this.subBasePriceIds = subBasePriceIds;
	}

	public String getSubBasePriceContactIds() {
		return subBasePriceContactIds;
	}

	public void setSubBasePriceContactIds(String subBasePriceContactIds) {
		this.subBasePriceContactIds = subBasePriceContactIds;
	}

	public String getSubBasePriceContactNames() {
		return subBasePriceContactNames;
	}

	public void setSubBasePriceContactNames(String subBasePriceContactNames) {
		this.subBasePriceContactNames = subBasePriceContactNames;
	}

	public String getSubBasePriceContactTels() {
		return subBasePriceContactTels;
	}

	public void setSubBasePriceContactTels(String subBasePriceContactTels) {
		this.subBasePriceContactTels = subBasePriceContactTels;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getSubBasePriceTraderIds() {
		return subBasePriceTraderIds;
	}

	public void setSubBasePriceTraderIds(String subBasePriceTraderIds) {
		this.subBasePriceTraderIds = subBasePriceTraderIds;
	}

	public List<String> getContactIds() {
		return contactIds;
	}

	public void setContactIds(List<String> contactIds) {
		this.contactIds = contactIds;
	}

	public String getTraderTel() {
		return traderTel;
	}

	public void setTraderTel(String traderTel) {
		this.traderTel = traderTel;
	}
	
}