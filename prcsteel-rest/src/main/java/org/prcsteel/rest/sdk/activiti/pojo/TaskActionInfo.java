package org.prcsteel.rest.sdk.activiti.pojo;

import org.prcsteel.rest.sdk.activiti.request.TaskActionRequest;

public class TaskActionInfo {
	
	private String taskId;
	
	private TaskActionRequest request;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskActionRequest getRequest() {
		return request;
	}

	public void setRequest(TaskActionRequest request) {
		this.request = request;
	}
}
