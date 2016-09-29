package com.prcsteel.platform.account.model.model;

/**
 * @author zhoukun
 */
public class GlobalId {

	private String moduleName;
	
	private Long currentId;
	
	private String remark;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Long currentId) {
		this.currentId = currentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
