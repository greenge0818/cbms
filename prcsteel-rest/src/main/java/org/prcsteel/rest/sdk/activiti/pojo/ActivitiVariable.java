package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;

import org.prcsteel.rest.sdk.activiti.enums.VariableScope;

/**
 * 
 * @author zhoukun
 */
public class ActivitiVariable implements Serializable {

	private static final long serialVersionUID = -5375259384813543465L;

	public ActivitiVariable(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	String name;
	
	String value;
	
	VariableScope variableScope;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public VariableScope getVariableScope() {
		return variableScope;
	}

	public void setVariableScope(VariableScope variableScope) {
		this.variableScope = variableScope;
	}
	
}
