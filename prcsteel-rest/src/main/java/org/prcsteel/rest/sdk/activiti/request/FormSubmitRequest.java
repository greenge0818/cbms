package org.prcsteel.rest.sdk.activiti.request;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.prcsteel.rest.sdk.activiti.pojo.ActivitiFormProperties;

/**
 * 
 * @author zhoukun
 */
public class FormSubmitRequest implements Serializable {

	private static final long serialVersionUID = -3444200428438892063L;

	private String taskId;
	
	private List<ActivitiFormProperties> properties = new LinkedList<>();
	
	public void addFormProperties(String id,String value){
		properties.add(new ActivitiFormProperties(id,value));
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<ActivitiFormProperties> getProperties() {
		return properties;
	}

	public void setProperties(List<ActivitiFormProperties> properties) {
		this.properties = properties;
	}
}
