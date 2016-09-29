package org.prcsteel.rest.sdk.activiti.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;

/**
 * 
 * @author zhoukun
 */
public class StartProcessRequest implements Serializable {

	private static final long serialVersionUID = -4922922911922024838L;

	String processDefinitionId;
	
	String processDefinitionKey;
	
	String businessKey;
	
	String tenantId;
	
	String message;
	
	List<ActivitiVariable> variables = new ArrayList<ActivitiVariable>();
	
	public void addVariable(String name,String value){
		variables.add(new ActivitiVariable(name, value));
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public List<ActivitiVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<ActivitiVariable> variables) {
		this.variables = variables;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
