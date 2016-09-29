package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.util.Date;

public class SynchronizeLogDto implements Serializable{
	

	private static final long serialVersionUID = -8712624151579633614L;
	/**日志编号*/
	private Integer id;
	/**客户id 即cust_kuandao_account.id*/
	private Integer acctId;
	/**同步方式  0:手动 1:自动*/
	private Integer type;
	/**同步内容*/
	private String description;
	/**同步结果 0：失败 1：成功*/
	private Integer result;
	/**错误信息*/
	private String errMsg;
	/**生成时间*/
	private Date created;
	/**创建者*/
	private String createdBy;
	/**客户名称*/
	private String memeberName;
	/**浦发会员代码*/
	private String memeberCode;
	/**服务中心Id*/
	private Long orgId;
	/**服务中心名*/
	private String orgName;
	
	private String createDateTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcctId() {
		return acctId;
	}

	public void setAcctId(Integer acctId) {
		this.acctId = acctId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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

	public String getMemeberName() {
		return memeberName;
	}

	public void setMemeberName(String memeberName) {
		this.memeberName = memeberName;
	}

	public String getMemeberCode() {
		return memeberCode;
	}

	public void setMemeberCode(String memeberCode) {
		this.memeberCode = memeberCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}
	
	
}
