package com.prcsteel.platform.smartmatch.model.dto;

import java.util.Date;

/**
 * 分拣推送询价单消息保存实体
 * @author tangwei
 *
 */
public class InquiryOrderMessageQueue {
	private String code;//需求单号
	private String pushBy;//推送人
	private Date created;//创建时间
	private String reqSource;//记录来源
	private String ext1;//预留字段1
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPushBy() {
		return pushBy;
	}
	public void setPushBy(String pushBy) {
		this.pushBy = pushBy;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getReqSource() {
		return reqSource;
	}
	public void setReqSource(String reqSource) {
		this.reqSource = reqSource;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt5() {
		return ext5;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	public String getExt6() {
		return ext6;
	}
	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}
	
}
