package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class KuandaoAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -587837619313936247L;
	
	
	private Integer id;
	/**客户编号*/
	private Long acctId;
	/**客户类型  1：买家、6：卖家(临)  10:卖家(代) */
	private Long custType;
	/**服务中心Id*/
	private Long orgId;
	/**服务中心*/
	private String orgName;
	/**客户状态*/
	private Integer custStatus;
	/**浦发会员编号*/
	private String memeberCode;
	/**浦发会员名*/
	private String memeberName;
	/**浦发虚拟号*/
	private String virAcctNo;
	/**电话*/
	private String mobile;
	/**浦发会员账户类型 0：本行 1：他行*/
	private Integer acctType;
	/**浦发会员开户行*/
	private String branchId;
	/**浦发会员开户机构*/
	private String departmentId;
	/**浦发会员绑定账号*/
	private String acctNo;
	/**浦发会员待提取金额*/
	private BigDecimal drawAmt;
	/**浦发会员状态 0 – 正常（已绑定）1 – 已销户 2 – 账户待绑定 3 – 待开立虚账户 4 – 失败*/
	private String status;
	/**身份类型 0 企业 1 个人 */
	private String type;
	/**证件类型 0=企业组织机构代号 1=身份证*/
	private String idType;
	/**证件号*/
	private String idNo;
	/**修改状态 0 未修改 1 会员名、手机号被修改 2 组织机构代码被修改*/
	private Integer modificationStatus;
	/**同步结果*/
	private Integer result;
	/**同步失败次数*/
	private Integer failureCount;
	/**开户时间*/
	private Date applyTime;
	/**绑定时间*/
	private Date boundTime;
	/**申请时间*/
	private Date created;
	/**开户申请人*/
    private String createdBy;
    /**信息修改次数*/
    private Integer modificationNumber;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getAcctId() {
		return acctId;
	}
	public void setAcctId(Long acctId) {
		this.acctId = acctId;
	}
	public Long getCustType() {
		return custType;
	}
	public void setCustType(Long custType) {
		this.custType = custType;
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
	public Integer getCustStatus() {
		return custStatus;
	}
	public void setCustStatus(Integer custStatus) {
		this.custStatus = custStatus;
	}
	public String getMemeberCode() {
		return memeberCode;
	}
	public void setMemeberCode(String memeberCode) {
		this.memeberCode = memeberCode;
	}
	public String getMemeberName() {
		return memeberName;
	}
	public void setMemeberName(String memeberName) {
		this.memeberName = memeberName;
	}
	public String getVirAcctNo() {
		return virAcctNo;
	}
	public void setVirAcctNo(String virAcctNo) {
		this.virAcctNo = virAcctNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getAcctType() {
		return acctType;
	}
	public void setAcctType(Integer acctType) {
		this.acctType = acctType;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public BigDecimal getDrawAmt() {
		return drawAmt;
	}
	public void setDrawAmt(BigDecimal drawAmt) {
		this.drawAmt = drawAmt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public Integer getModificationStatus() {
		return modificationStatus;
	}
	public void setModificationStatus(Integer modificationStatus) {
		this.modificationStatus = modificationStatus;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getFailureCount() {
		return failureCount;
	}
	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}
	public Date getBoundTime() {
		return boundTime;
	}
	public void setBoundTime(Date boundTime) {
		this.boundTime = boundTime;
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
	public Integer getModificationNumber() {
		return modificationNumber;
	}
	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	

}
