package org.prcsteel.rest.sdk.activiti.result;

import java.io.Serializable;

/**
 * 
 * @author zhoukun
 */
public class FormSubmitResult implements Serializable {

	private static final long serialVersionUID = -401386669963504958L;

	private String id;
	
	private String url;
	
	private String businessKey;
	
	private Boolean suspended;
	
	private String processDefinitionId;
	
	private String processDefinitionUrl;
	
	private String activityId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionUrl() {
		return processDefinitionUrl;
	}

	public void setProcessDefinitionUrl(String processDefinitionUrl) {
		this.processDefinitionUrl = processDefinitionUrl;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
}
