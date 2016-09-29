package com.prcsteel.platform.account.model.dto;

import java.util.List;

import com.prcsteel.platform.account.model.model.AccountBank;

/**
 * 客户资料审核
 * 
 * @author tangwei
 *
 */
public class AccountDocumentDto extends CompanyDto{

    private Long extId;//cust_account_ext 主键
    private String lastUpdateBy;//最新修改人
    private String disagreeDesc;//不同意原因描叙
    private String[] bankIds;//银行id集合
    private String[] disagreeDescs;//不同意原因描叙集合
    private List<AccountBank> bankList;//批量审核银行信息
    private Boolean isApproved =false;//银行信息是否全部审核通过
    
	public Long getExtId() {
		return extId;
	}
	public void setExtId(Long extId) {
		this.extId = extId;
	}
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	public String getDisagreeDesc() {
		return disagreeDesc;
	}
	public void setDisagreeDesc(String disagreeDesc) {
		this.disagreeDesc = disagreeDesc;
	}
	public String[] getBankIds() {
		return bankIds;
	}
	public void setBankIds(String[] bankIds) {
		this.bankIds = bankIds;
	}
	public String[] getDisagreeDescs() {
		return disagreeDescs;
	}
	public void setDisagreeDescs(String[] disagreeDescs) {
		this.disagreeDescs = disagreeDescs;
	}
	public List<AccountBank> getBankList() {
		return bankList;
	}
	public void setBankList(List<AccountBank> bankList) {
		this.bankList = bankList;
	}
	public Boolean getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}
}
