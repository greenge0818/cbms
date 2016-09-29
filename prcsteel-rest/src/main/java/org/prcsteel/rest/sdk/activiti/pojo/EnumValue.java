package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;

/**
 * 
 * @author zhoukun
 */
public class EnumValue implements Serializable {

	private static final long serialVersionUID = -8828405689633761007L;

	private String id;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
