package com.prcsteel.platform.order.model.dto;

/**
 * 批量上传凭证号图片时，图片的相关信息
 * @author tuxianming
 * @date 20160525
 *
 */
public class CredentialImageDto {
	private Long id;  //附件id
	private String credentialCode; //凭证号
	private Integer pageNum; 	//页码
	
	
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
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
}
