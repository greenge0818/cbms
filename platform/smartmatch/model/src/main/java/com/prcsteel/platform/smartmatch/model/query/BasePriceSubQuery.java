package com.prcsteel.platform.smartmatch.model.query;

import java.util.List;

import com.prcsteel.platform.common.query.PagedQuery;

/**
 * 买家订阅列表查询
 * @author prcsteel
 *
 */
public class BasePriceSubQuery extends PagedQuery{

	//id
    private Long id;
    //买家名称
    private String accountName;
    //买家ID
    private String accountId;
    //地区名称
    private String cityName;
    //城市ID
    private Long cityId;
    //订阅基价的名称
    private String subBasePriceName;
    //电话号码。
    private String tel;
    //交易员
    private String ownerName;

    //可以查看当前记录的用户Id集合
    private List<Long> userIdList;
    
    //当前登录人
    private String loginId;
    
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

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
   

}
