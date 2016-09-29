package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class HistoricTaskInstance implements Serializable {

	private static final long serialVersionUID = -6946826478497793030L;

	private String id;
	private String processDefinitionId;
	private String processDefinitionUrl;
	private String processInstanceId;
	private String processInstanceUrl;
	private String executionId;
	private String name;
	private String description;
	private String deleteReason;
	private String owner;
	private String assignee;
	private String startTime;
	private String endTime;
	private Integer durationInMillis;
	private Integer workTimeInMillis;
	private String claimTime;
	private String taskDefinitionKey;
	private String formKey;
	private Integer priority;
	private String dueDate;
	private String parentTaskId;
	private String url;
	private List<ActivitiVariable> variables;
	private String tenantId;
	private List<ActivitiVariable> processVariables;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessInstanceUrl() {
		return processInstanceUrl;
	}
	public void setProcessInstanceUrl(String processInstanceUrl) {
		this.processInstanceUrl = processInstanceUrl;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getDurationInMillis() {
		return durationInMillis;
	}
	public void setDurationInMillis(Integer durationInMillis) {
		this.durationInMillis = durationInMillis;
	}
	public Integer getWorkTimeInMillis() {
		return workTimeInMillis;
	}
	public void setWorkTimeInMillis(Integer workTimeInMillis) {
		this.workTimeInMillis = workTimeInMillis;
	}
	public String getClaimTime() {
		return claimTime;
	}
	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ActivitiVariable> getVariables() {
		return variables;
	}
	public void setVariables(List<ActivitiVariable> variables) {
		this.variables = variables;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<ActivitiVariable> getProcessVariables() {
		return processVariables;
	}
	public void setProcessVariables(List<ActivitiVariable> processVariables) {
		this.processVariables = processVariables;
	}
}
