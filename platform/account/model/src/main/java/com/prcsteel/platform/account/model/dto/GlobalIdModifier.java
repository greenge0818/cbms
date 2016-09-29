package com.prcsteel.platform.account.model.dto;
/**
 * 
 * @author zhoukun
 */
public class GlobalIdModifier {

	public GlobalIdModifier(String moduleName, Long currentId, Long newId) {
		super();
		this.moduleName = moduleName;
		this.currentId = currentId;
		this.newId = newId;
	}

	private String moduleName;
	
	private Long currentId;
	
	private Long newId;

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

	public Long getNewId() {
		return newId;
	}

	public void setNewId(Long newId) {
		this.newId = newId;
	}
	
}
