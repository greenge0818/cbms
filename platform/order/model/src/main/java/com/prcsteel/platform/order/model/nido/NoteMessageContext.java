package com.prcsteel.platform.order.model.nido;

import com.prcsteel.framework.nido.context.NidoContext;

/**
 * 
 * @author zhoukun
 */
public class NoteMessageContext extends NidoContext {
	
	public NoteMessageContext(String phone, String content){
		this.phone = phone;
		this.content = content;
	}

	private static final long serialVersionUID = -7558910823180518217L;

	private String phone; 
	
	private String content;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
