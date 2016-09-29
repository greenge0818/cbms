package com.prcsteel.platform.account.model.dto;

/**
 * 客户合同模板审核
 * 
 * @author chengui
 *
 */
public class ContractTemplateAuditDto{

    private Long id;
    private String status;
    private String disagreeDesc;//不同意原因描叙


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDisagreeDesc() {
		return disagreeDesc;
	}

	public void setDisagreeDesc(String disagreeDesc) {
		this.disagreeDesc = disagreeDesc;
	}
}
