package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDto {

	private double totalAmountBig = 0;// 总金额

	private String totalAmountBoo;// 大写总金额

	private String buyerName;// 买家名称

	private String contractCode;// 合同编号

	private BigDecimal totalAmount;// 合同金额

	private Long requestId;// 申请人id

	private String requestName;// 申请人

	private String code;// 申请单编号

	private int printTimes;// 打印次数

	private Long orgId;// 申请部门ID

	private BigDecimal payAmount; // 金额

	private Date created; // 申请日期

	public double getTotalAmountBig() {
		return totalAmountBig;
	}

	public void setTotalAmountBig(double totalAmountBig) {
		this.totalAmountBig = totalAmountBig;
	}

	public String getTotalAmountBoo() {
		return totalAmountBoo;
	}

	public void setTotalAmountBoo(String totalAmountBoo) {
		this.totalAmountBoo = totalAmountBoo;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(int printTimes) {
		this.printTimes = printTimes;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
