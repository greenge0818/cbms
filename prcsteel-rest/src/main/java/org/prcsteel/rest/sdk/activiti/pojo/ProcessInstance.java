package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhoukun
 */
public class ProcessInstance implements Serializable {

	private static final long serialVersionUID = -8161366704290491273L;

	private Integer id;
	
	private String url;
	
	private String businessKey;
	
	private Boolean suspended;
	
	private Boolean ended;
	
	private String processDefinitionId;
	
	private String processDefinitionUrl;
	
	private String activityId;
	
	private List<Map<String, Object>> variables;
	
	private String tenantId;
	
	private Boolean completed;
	
	public Object getVariable(String key){
		if(variables != null){
			for (Map<String, Object> v : variables) {
				if(v.containsKey(key)){
					return v.get(key);
				}
			}
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Boolean getEnded() {
		return ended;
	}

	public void setEnded(Boolean ended) {
		this.ended = ended;
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

	public List<Map<String, Object>> getVariables() {
		return variables;
	}

	public void setVariables(List<Map<String, Object>> variables) {
		this.variables = variables;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	
	
}
