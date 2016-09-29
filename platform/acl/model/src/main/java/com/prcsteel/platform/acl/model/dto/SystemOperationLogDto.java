package com.prcsteel.platform.acl.model.dto;

import com.prcsteel.platform.common.enums.OpLevel;
import com.prcsteel.platform.common.enums.OpType;

import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class SystemOperationLogDto {

	private Long id;
	
	private Long operatorId;
	
	private String operatorName;
	
	private OpType operationKey;
	
	private String operationName;
	
	private OpLevel operationLevel;
	
	private Integer operationLevelValue;
	
	private String parameters;
	
	private Date created;
	
	private String createdBy;
	
	private Date lastUpdated;
	
	private String lastUpdatedBy;
	
	private Integer modificationNumber;

	public String getOperationLevelDesc(){
		return operationLevel != null ? operationLevel.getDescription() : "";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public OpType getOperationKey() {
		return operationKey;
	}

	public void setOperationKey(OpType operationKey) {
		this.operationKey = operationKey;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public OpLevel getOperationLevel() {
		return operationLevel;
	}

	public void setOperationLevel(OpLevel operationLevel) {
		this.operationLevel = operationLevel;
	}

	public Integer getOperationLevelValue() {
		return operationLevelValue;
	}

	public void setOperationLevelValue(Integer operationLevelValue) {
		this.operationLevelValue = operationLevelValue;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
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

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(Integer modificationNumber) {
		this.modificationNumber = modificationNumber;
	}
	
	
}
