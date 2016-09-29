package org.prcsteel.rest.sdk.activiti.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author zhoukun
 */
public class FormPropertiesDefinition implements Serializable {

	private static final long serialVersionUID = 8557339947905141692L;

	private String id;
	
	private String name;
	
	private String type;
	
	private String value;
	
	private Boolean readable;
	
	private Boolean writable;
	
	private Boolean required;
	
	private String datePattern;
	
	private List<EnumValue> enumValues;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getReadable() {
		return readable;
	}

	public void setReadable(Boolean readable) {
		this.readable = readable;
	}

	public Boolean getWritable() {
		return writable;
	}

	public void setWritable(Boolean writable) {
		this.writable = writable;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public List<EnumValue> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(List<EnumValue> enumValues) {
		this.enumValues = enumValues;
	}
	
}
