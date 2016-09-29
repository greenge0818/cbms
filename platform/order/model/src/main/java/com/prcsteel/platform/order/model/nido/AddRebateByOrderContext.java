package com.prcsteel.platform.order.model.nido;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.framework.nido.context.NidoContext;

/**
 * 
 * @author zhoukun
 */
public class AddRebateByOrderContext extends NidoContext {

	public AddRebateByOrderContext(Long orderId,User user,Long sellerId){
		this.orderId = orderId;
		this.user = user;
		this.sellerId = sellerId;
	}
	
	private static final long serialVersionUID = 5830408319554185970L;

	Long orderId;
	
	User user;
	
	Long sellerId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	
}
