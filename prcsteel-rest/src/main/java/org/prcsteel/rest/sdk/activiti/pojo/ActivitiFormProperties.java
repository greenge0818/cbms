package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;

/**
 * 
 * @author zhoukun
 */
public class ActivitiFormProperties implements Serializable {
	
	public ActivitiFormProperties(String id, String value) {
		this.id = id;
		this.value = value;
	}

	private static final long serialVersionUID = -8115078679027684695L;

	private String id;

	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
