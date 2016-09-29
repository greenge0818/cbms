package com.prcsteel.platform.order.model.query;

import java.util.Date;

/**
 * @author tuxianming
 */
public class ReportBuyerInvoiceOutQuery {
	private Long buyerId;
	private int ModificationNumber;
	
	private Integer length;
	private Integer start;
	
	private Date startTime;
	private Date endTime;
	
	private String buyName;
	private String invoiceNo;
	
	public int getModificationNumber() {
		return ModificationNumber;
	}

	public void setModificationNumber(int modificationNumber) {
		ModificationNumber = modificationNumber;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public ReportBuyerInvoiceOutQuery setInvoiceNo(String djh) {
		this.invoiceNo = djh;
		return this;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
}
