package com.prcsteel.platform.order.model.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author zhoukun
 */
public class ActivitiTaskFormDto implements Serializable {

	private static final long serialVersionUID = 9094393984788981491L;

	private String taskId;
	
	private String comment;
	
	private Map<String, String> formProperties;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<String, String> getFormProperties() {
		return formProperties;
	}

	public void setFormProperties(Map<String, String> formProperties) {
		this.formProperties = formProperties;
	}
	
}
