package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class FormInfo implements Serializable {

	private static final long serialVersionUID = 7913247051521919066L;

	private String formKey;
	
	private String deploymentId;
	
	private String processDefinitionId;
	
	private String processDefinitionUrl;
	
	private String taskId;
	
	private String taskUrl;
	
	private List<FormPropertiesDefinition> formProperties;

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public List<FormPropertiesDefinition> getFormProperties() {
		return formProperties;
	}

	public void setFormProperties(List<FormPropertiesDefinition> formProperties) {
		this.formProperties = formProperties;
	}
	
}
