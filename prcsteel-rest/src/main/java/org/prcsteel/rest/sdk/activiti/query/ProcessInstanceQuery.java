package org.prcsteel.rest.sdk.activiti.query;

import java.io.Serializable;

/**
 * 
 * @author zhoukun
 */
public class ProcessInstanceQuery implements Serializable {

	private static final long serialVersionUID = -8320680938738086986L;

	String id;
	
	String processDefinitionKey;
	
	String processDefinitionId;
	
	String businessKey;
	
	String involvedUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getInvolvedUser() {
		return involvedUser;
	}

	public void setInvolvedUser(String involvedUser) {
		this.involvedUser = involvedUser;
	}
	
	
}
