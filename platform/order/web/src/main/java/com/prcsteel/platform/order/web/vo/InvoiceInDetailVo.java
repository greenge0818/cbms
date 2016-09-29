package com.prcsteel.platform.order.web.vo;

import java.io.Serializable;
import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceInDetailAndOrdItemDto;
import com.prcsteel.platform.order.model.model.InvoiceIn;

/**
 * 
 * @author zhoukun
 */
public class InvoiceInDetailVo implements Serializable {

	private static final long serialVersionUID = 7516910077973233359L;

	private InvoiceIn invoiceIn;
	
	List<InvoiceInDetailAndOrdItemDto> details;

	public InvoiceIn getInvoiceIn() {
		return invoiceIn;
	}

	public void setInvoiceIn(InvoiceIn invoiceIn) {
		this.invoiceIn = invoiceIn;
	}

	public List<InvoiceInDetailAndOrdItemDto> getDetails() {
		return details;
	}

	public void setDetails(List<InvoiceInDetailAndOrdItemDto> details) {
		this.details = details;
	}
	
}
