package com.prcsteel.platform.order.model.dto;

/***
 * @author  wangxiao
 * @description 批量上传凭证图片 tab页 查询Dto
 * @date 2016/05/23
 */
public class BatchUploadingDto {
	  /*****主健ID*****/
    private Long id;

    /*****凭证号*****/
    private String credentialCode;
    
    /*****凭证类型，如 buyer 买家  seller 卖家*****/
    private String type;
    
    /*****凭证名称*****/
    private String name;

    /*****凭证状态 PENDING_SUBMIT 待提交 PENDING_APPROVAL 待审核 APPROVED 审核通过  DISAPPROVE 审核不通过*****/
    private String status;
    
    /*****提交日期*****/
    private String submitDate;

    /*****凭证总页数*****/
    private String credentialNum;
    
    /*****上传状态 PENDING_REVISION 待校对 PENDING_COLLECT 已校对待集齐 ALREADY_COLLECT 已集齐*****/
    private String uploadStatus;
   
    /*****单号*****/
    private String code;

      /*****买家名称*****/
    private String accountName;

    /*****卖家名称*****/
    private String sellerName;
    
    /*****卖家名称*****/
    private String batchCredentialCode;

    /*****数量*****/
    private String pages;

    /*****图片路径*****/
    private String fileUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCredentialCode() {
		return credentialCode;
	}

	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	public String getCredentialNum() {
		return credentialNum;
	}

	public void setCredentialNum(String credentialNum) {
		this.credentialNum = credentialNum;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getBatchCredentialCode() {
		return batchCredentialCode;
	}

	public void setBatchCredentialCode(String batchCredentialCode) {
		this.batchCredentialCode = batchCredentialCode;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
    
    
    

}
