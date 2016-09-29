package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceIn;
import com.prcsteel.platform.order.model.model.InvoiceInAllowance;

/**
 * Created by lcw on 8/4/2015.
 */
public class InvoiceInDto extends InvoiceIn {
	
	private List<Long> invoiceOutIds;

    private List<InvoiceInDetailDto> details;

    private String company;

    private String expressName;

    private Date sendTime;

    private BigDecimal totalNoTaxAmount;

    private BigDecimal totalTaxAmount;
    
    private boolean invoiceOut = false;//是否已开出销项票
    
    private String invoiceIsSend;//是否可寄出状态

    private String memo;
    
    
    //折让信息
    private InvoiceInAllowance allowance;
    private  List<AllowanceOrderItemsDto> allaownceDetails;
    
    
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<Long> getInvoiceOutIds() {
		return invoiceOutIds;
	}

	public void setInvoiceOutIds(List<Long> invoiceOutIds) {
		this.invoiceOutIds = invoiceOutIds;
	}

	public String getInvoiceIsSend() {
		return invoiceIsSend;
	}

	public void setInvoiceIsSend(String invoiceIsSend) {
		this.invoiceIsSend = invoiceIsSend;
	}

	public List<InvoiceInDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<InvoiceInDetailDto> details) {
        this.details = details;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getTotalNoTaxAmount() {
        return totalNoTaxAmount;
    }

    public void setTotalNoTaxAmount(BigDecimal totalNoTaxAmount) {
        this.totalNoTaxAmount = totalNoTaxAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

	public boolean isInvoiceOut() {
		return invoiceOut;
	}

	public void setInvoiceOut(boolean invoiceOut) {
		this.invoiceOut = invoiceOut;
	}

	public List<AllowanceOrderItemsDto> getAllaownceDetails() {
		return allaownceDetails;
	}

	public void setAllaownceDetails( List<AllowanceOrderItemsDto> allaownceDetails) {
		this.allaownceDetails = allaownceDetails;
	}

	public InvoiceInAllowance getAllowance() {
		return allowance;
	}

	public void setAllowance(InvoiceInAllowance allowance) {
		this.allowance = allowance;
	}
	
}
