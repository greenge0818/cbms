package com.prcsteel.platform.order.web.vo;

import java.util.List;

import com.prcsteel.platform.order.model.dto.InvoiceInAllowanceItemDto;
import com.prcsteel.platform.order.model.dto.InvoiceInDetailFormDto;
import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.model.InvoiceInAllowance;

/**
 * 
 * @author zhoukun
 */
public class SaveInvoiceInVo {

	InvoiceIn invoiceIn;
	
	List<InvoiceInDetailFormDto> invoiceDetails;

	InvoiceInAllowance invoiceInAllowance;

	List<InvoiceInAllowanceItemDto> inAllowanceItems;
	
	boolean isCheck;

	public InvoiceIn getInvoiceIn() {
		return invoiceIn;
	}

	public void setInvoiceIn(InvoiceIn invoiceIn) {
		this.invoiceIn = invoiceIn;
	}

	public List<InvoiceInDetailFormDto> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<InvoiceInDetailFormDto> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public InvoiceInAllowance getInvoiceInAllowance() {
		return invoiceInAllowance;
	}

	public void setInvoiceInAllowance(InvoiceInAllowance invoiceInAllowance) {
		this.invoiceInAllowance = invoiceInAllowance;
	}

	public List<InvoiceInAllowanceItemDto> getInAllowanceItems() {
		return inAllowanceItems;
	}

	public void setInAllowanceItems(List<InvoiceInAllowanceItemDto> inAllowanceItems) {
		this.inAllowanceItems = inAllowanceItems;
	}
	
	public boolean isCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
}
