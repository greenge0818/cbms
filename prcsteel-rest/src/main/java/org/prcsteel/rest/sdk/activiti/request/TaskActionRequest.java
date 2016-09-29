package org.prcsteel.rest.sdk.activiti.request;

import java.io.Serializable;
import java.util.List;

import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;

/**
 * 
 * @author zhoukun
 */
public class TaskActionRequest implements Serializable {

	private static final long serialVersionUID = 8251356102559682105L;

	private String action;
	
	private List<ActivitiVariable> variables;
	
	private String assignee;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<ActivitiVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<ActivitiVariable> variables) {
		this.variables = variables;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	
}
