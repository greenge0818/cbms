package org.prcsteel.rest.sdk.activiti.query;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class HistoricQuery implements Serializable {

	private static final long serialVersionUID = -2725182427625323109L;

	private String taskId;
	private String processInstanceId;
	private String processDefinitionKey;
	private String processDefinitionKeyLike;
	private String processDefinitionId;
	private String processDefinitionName;
	private String processDefinitionNameLike;
	private String processBusinessKey;
	private String processBusinessKeyLike;
	private String executionId;
	private String taskDefinitionKey;
	private String taskName;
	private String taskNameLike;
	private String taskDescription;
	private String taskDescriptionLike;
	private String taskDeleteReason;
	private String taskDeleteReasonLike;
	private String taskAssignee;
	private String taskAssigneeLike;
	private String taskOwner;
	private String taskOwnerLike;
	private String taskInvolvedUser;
	private String taskPriority;
	private Boolean finished;
	private Boolean processFinished;
	private String parentTaskId;
	private Date dueDate;
	private Date dueDateAfter;
	private Date dueDateBefore;
	private Boolean withoutDueDate;
	private Date taskCompletedOn;
	private Date taskCompletedAfter;
	private Date taskCompletedBefore;
	private Date taskCreatedOn;
	private Date taskCreatedBefore;
	private Date taskCreatedAfter;
	private Boolean includeTaskLocalVariables;
	private Boolean includeProcessVariables;
	private String tenantId;
	private String tenantIdLike;
	private Boolean withoutTenantId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}
	public String getProcessDefinitionKeyLike() {
		return processDefinitionKeyLike;
	}
	public void setProcessDefinitionKeyLike(String processDefinitionKeyLike) {
		this.processDefinitionKeyLike = processDefinitionKeyLike;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}
	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}
	public String getProcessDefinitionNameLike() {
		return processDefinitionNameLike;
	}
	public void setProcessDefinitionNameLike(String processDefinitionNameLike) {
		this.processDefinitionNameLike = processDefinitionNameLike;
	}
	public String getProcessBusinessKey() {
		return processBusinessKey;
	}
	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}
	public String getProcessBusinessKeyLike() {
		return processBusinessKeyLike;
	}
	public void setProcessBusinessKeyLike(String processBusinessKeyLike) {
		this.processBusinessKeyLike = processBusinessKeyLike;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskNameLike() {
		return taskNameLike;
	}
	public void setTaskNameLike(String taskNameLike) {
		this.taskNameLike = taskNameLike;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskDescriptionLike() {
		return taskDescriptionLike;
	}
	public void setTaskDescriptionLike(String taskDescriptionLike) {
		this.taskDescriptionLike = taskDescriptionLike;
	}
	public String getTaskDeleteReason() {
		return taskDeleteReason;
	}
	public void setTaskDeleteReason(String taskDeleteReason) {
		this.taskDeleteReason = taskDeleteReason;
	}
	public String getTaskDeleteReasonLike() {
		return taskDeleteReasonLike;
	}
	public void setTaskDeleteReasonLike(String taskDeleteReasonLike) {
		this.taskDeleteReasonLike = taskDeleteReasonLike;
	}
	public String getTaskAssignee() {
		return taskAssignee;
	}
	public void setTaskAssignee(String taskAssignee) {
		this.taskAssignee = taskAssignee;
	}
	public String getTaskAssigneeLike() {
		return taskAssigneeLike;
	}
	public void setTaskAssigneeLike(String taskAssigneeLike) {
		this.taskAssigneeLike = taskAssigneeLike;
	}
	public String getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}
	public String getTaskOwnerLike() {
		return taskOwnerLike;
	}
	public void setTaskOwnerLike(String taskOwnerLike) {
		this.taskOwnerLike = taskOwnerLike;
	}
	public String getTaskInvolvedUser() {
		return taskInvolvedUser;
	}
	public void setTaskInvolvedUser(String taskInvolvedUser) {
		this.taskInvolvedUser = taskInvolvedUser;
	}
	public String getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public Boolean getProcessFinished() {
		return processFinished;
	}
	public void setProcessFinished(Boolean processFinished) {
		this.processFinished = processFinished;
	}
	public String getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getDueDateAfter() {
		return dueDateAfter;
	}
	public void setDueDateAfter(Date dueDateAfter) {
		this.dueDateAfter = dueDateAfter;
	}
	public Date getDueDateBefore() {
		return dueDateBefore;
	}
	public void setDueDateBefore(Date dueDateBefore) {
		this.dueDateBefore = dueDateBefore;
	}
	public Boolean getWithoutDueDate() {
		return withoutDueDate;
	}
	public void setWithoutDueDate(Boolean withoutDueDate) {
		this.withoutDueDate = withoutDueDate;
	}
	public Date getTaskCompletedOn() {
		return taskCompletedOn;
	}
	public void setTaskCompletedOn(Date taskCompletedOn) {
		this.taskCompletedOn = taskCompletedOn;
	}
	public Date getTaskCompletedAfter() {
		return taskCompletedAfter;
	}
	public void setTaskCompletedAfter(Date taskCompletedAfter) {
		this.taskCompletedAfter = taskCompletedAfter;
	}
	public Date getTaskCompletedBefore() {
		return taskCompletedBefore;
	}
	public void setTaskCompletedBefore(Date taskCompletedBefore) {
		this.taskCompletedBefore = taskCompletedBefore;
	}
	public Date getTaskCreatedOn() {
		return taskCreatedOn;
	}
	public void setTaskCreatedOn(Date taskCreatedOn) {
		this.taskCreatedOn = taskCreatedOn;
	}
	public Date getTaskCreatedBefore() {
		return taskCreatedBefore;
	}
	public void setTaskCreatedBefore(Date taskCreatedBefore) {
		this.taskCreatedBefore = taskCreatedBefore;
	}
	public Date getTaskCreatedAfter() {
		return taskCreatedAfter;
	}
	public void setTaskCreatedAfter(Date taskCreatedAfter) {
		this.taskCreatedAfter = taskCreatedAfter;
	}
	public Boolean getIncludeTaskLocalVariables() {
		return includeTaskLocalVariables;
	}
	public void setIncludeTaskLocalVariables(Boolean includeTaskLocalVariables) {
		this.includeTaskLocalVariables = includeTaskLocalVariables;
	}
	public Boolean getIncludeProcessVariables() {
		return includeProcessVariables;
	}
	public void setIncludeProcessVariables(Boolean includeProcessVariables) {
		this.includeProcessVariables = includeProcessVariables;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getTenantIdLike() {
		return tenantIdLike;
	}
	public void setTenantIdLike(String tenantIdLike) {
		this.tenantIdLike = tenantIdLike;
	}
	public Boolean getWithoutTenantId() {
		return withoutTenantId;
	}
	public void setWithoutTenantId(Boolean withoutTenantId) {
		this.withoutTenantId = withoutTenantId;
	}
	
}
