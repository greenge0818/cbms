package com.prcsteel.payment.bdl.model;

import com.prcsteel.platform.common.utils.BeanXMLMapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name="GenericResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericResponse implements Serializable {
	private Object data;
	private String message;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toXML() {
		return BeanXMLMapping.toXML(this);
	}

	public static Object fromXML(String xml) {
		return BeanXMLMapping.fromXML(xml, GenericResponse.class);
	}

	@Override
	public String toString() {
		return "PayResponse [data=" + data + ", message="
				+ message + "]";
	}
}
