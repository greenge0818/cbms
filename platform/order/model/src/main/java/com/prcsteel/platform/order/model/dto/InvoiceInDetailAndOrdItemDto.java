package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceInDetail;
import com.prcsteel.platform.order.model.model.InvoiceInDetailOrderItem;

/**
 * 
 * @author zhoukun
 *
 */
public class InvoiceInDetailAndOrdItemDto extends InvoiceInDetail {
    
	List<InvoiceInDetailOrderItem> detailOrderItems;
	
	public List<InvoiceInDetailOrderItem> getDetailOrderItems() {
		return detailOrderItems;
	}

	public void setDetailOrderItems(List<InvoiceInDetailOrderItem> detailOrderItems) {
		this.detailOrderItems = detailOrderItems;
	}
}
