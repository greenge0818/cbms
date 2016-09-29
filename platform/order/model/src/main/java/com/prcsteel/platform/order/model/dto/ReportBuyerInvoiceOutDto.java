package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;

import com.prcsteel.platform.order.model.model.ReportBuyerInvoiceOut;

public class ReportBuyerInvoiceOutDto extends ReportBuyerInvoiceOut{
	private BigDecimal startAmount;			//期初未开销项金额（元）
	private BigDecimal actualOcurAmount;	//销售实提金额（元）
	private BigDecimal makeOutAmount;		//已开销项金额（元）
	private BigDecimal unmakeOutAmount;		//未开销项金额（元）
	
	/**
	 * 用来显示详情里面的时间
	 */
	private String dateStr;
	
	private String startTime;
	private String endTime;
	
	public ReportBuyerInvoiceOutDto(){}

	public BigDecimal getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(BigDecimal startAmount) {
		this.startAmount = startAmount;
	}

	public BigDecimal getActualOcurAmount() {
		return actualOcurAmount;
	}

	public void setActualOcurAmount(BigDecimal actualOcurAmount) {
		this.actualOcurAmount = actualOcurAmount;
	}

	public BigDecimal getMakeOutAmount() {
		return makeOutAmount;
	}

	public void setMakeOutAmount(BigDecimal makeOutAmount) {
		this.makeOutAmount = makeOutAmount;
	}

	public BigDecimal getUnmakeOutAmount() {
		return unmakeOutAmount;
	}

	public void setUnmakeOutAmount(BigDecimal unmakeOutAmount) {
		this.unmakeOutAmount = unmakeOutAmount;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
}
