package com.prcsteel.platform.order.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.prcsteel.platform.order.model.model.ConsignOrderAttachment;

/**
 * 批量上传凭证号图片时，上传的对象与系统对象相同的时候，保存的两个对象 
 * @author tuxianming
 * @date 20160531
 *
 */
public class CredentialDuplicateImage {
	
	private String code;
	private Long certiId;
	List<Integer> pageNums = new ArrayList<>();
	List<Long> attachmentIds = new ArrayList<>();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getCertiId() {
		return certiId;
	}
	public void setCertiId(Long certiId) {
		this.certiId = certiId;
	}
	public List<Integer> getPageNums() {
		return pageNums;
	}
	public void setPageNums(List<Integer> pageNums) {
		this.pageNums = pageNums;
	}
	public List<Long> getAttachmentIds() {
		return attachmentIds;
	}
	public void setAttachmentIds(List<Long> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}
	 
}
