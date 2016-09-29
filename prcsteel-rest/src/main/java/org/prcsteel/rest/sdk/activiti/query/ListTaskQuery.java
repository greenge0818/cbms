package org.prcsteel.rest.sdk.activiti.query;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author zhoukun
 */
public class ListTaskQuery extends BaseQuery implements Serializable {

	private static final long serialVersionUID = -3846875290460024545L;

	private String name;

	private String nameLike;

	private String description;

	private Integer priority;

	private Integer minimumPriority;

	private Integer maximumPriority;

	private String assignee;

	private String assigneeLike;

	private String owner;

	private String ownerLike;

	private Boolean unassigned;

	private String delegationState;

	private String candidateUser;

	private String candidateGroup;

	private String candidateGroups;

	private String involvedUser;

	private String taskDefinitionKey;

	private String taskDefinitionKeyLike;

	private String processInstanceId;
	
	private String processInstanceBusinessKey;

	private String processInstanceBusinessKeyLike;

	private String processDefinitionKey;

	private String processDefinitionKeyLike;

	private String processDefinitionName;

	private String processDefinitionNameLike;

	private String executionId;

	private Date createdOn;

	private Date createdBefore;

	private Date createdAfter;

	private Date dueOn;

	private Date dueBefore;

	private Date dueAfter;

	private Boolean withoutDueDate;

	private Boolean excludeSubTasks;

	private Boolean active;

	private Boolean includeTaskLocalVariables;

	private Boolean includeProcessVariables;

	private String tenantId;

	private String tenantIdLike;

	private Boolean withoutTenantId;

	private String candidateOrAssigned;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getMinimumPriority() {
		return minimumPriority;
	}

	public void setMinimumPriority(Integer minimumPriority) {
		this.minimumPriority = minimumPriority;
	}

	public Integer getMaximumPriority() {
		return maximumPriority;
	}

	public void setMaximumPriority(Integer maximumPriority) {
		this.maximumPriority = maximumPriority;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAssigneeLike() {
		return assigneeLike;
	}

	public void setAssigneeLike(String assigneeLike) {
		this.assigneeLike = assigneeLike;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerLike() {
		return ownerLike;
	}

	public void setOwnerLike(String ownerLike) {
		this.ownerLike = ownerLike;
	}

	public Boolean getUnassigned() {
		return unassigned;
	}

	public void setUnassigned(Boolean unassigned) {
		this.unassigned = unassigned;
	}

	public String getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}

	public String getCandidateUser() {
		return candidateUser;
	}

	public void setCandidateUser(String candidateUser) {
		this.candidateUser = candidateUser;
	}

	public String getCandidateGroup() {
		return candidateGroup;
	}

	public void setCandidateGroup(String candidateGroup) {
		this.candidateGroup = candidateGroup;
	}

	public String getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(String candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public String getInvolvedUser() {
		return involvedUser;
	}

	public void setInvolvedUser(String involvedUser) {
		this.involvedUser = involvedUser;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getTaskDefinitionKeyLike() {
		return taskDefinitionKeyLike;
	}

	public void setTaskDefinitionKeyLike(String taskDefinitionKeyLike) {
		this.taskDefinitionKeyLike = taskDefinitionKeyLike;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessInstanceBusinessKey() {
		return processInstanceBusinessKey;
	}

	public void setProcessInstanceBusinessKey(String processInstanceBusinessKey) {
		this.processInstanceBusinessKey = processInstanceBusinessKey;
	}

	public String getProcessInstanceBusinessKeyLike() {
		return processInstanceBusinessKeyLike;
	}

	public void setProcessInstanceBusinessKeyLike(String processInstanceBusinessKeyLike) {
		this.processInstanceBusinessKeyLike = processInstanceBusinessKeyLike;
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

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Date createdBefore) {
		this.createdBefore = createdBefore;
	}

	public Date getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Date createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Date getDueOn() {
		return dueOn;
	}

	public void setDueOn(Date dueOn) {
		this.dueOn = dueOn;
	}

	public Date getDueBefore() {
		return dueBefore;
	}

	public void setDueBefore(Date dueBefore) {
		this.dueBefore = dueBefore;
	}

	public Date getDueAfter() {
		return dueAfter;
	}

	public void setDueAfter(Date dueAfter) {
		this.dueAfter = dueAfter;
	}

	public Boolean getWithoutDueDate() {
		return withoutDueDate;
	}

	public void setWithoutDueDate(Boolean withoutDueDate) {
		this.withoutDueDate = withoutDueDate;
	}

	public Boolean getExcludeSubTasks() {
		return excludeSubTasks;
	}

	public void setExcludeSubTasks(Boolean excludeSubTasks) {
		this.excludeSubTasks = excludeSubTasks;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public String getCandidateOrAssigned() {
		return candidateOrAssigned;
	}

	public void setCandidateOrAssigned(String candidateOrAssigned) {
		this.candidateOrAssigned = candidateOrAssigned;
	}
}
