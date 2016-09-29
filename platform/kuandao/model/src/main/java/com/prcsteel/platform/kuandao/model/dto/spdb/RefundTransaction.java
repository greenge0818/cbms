package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class RefundTransaction extends BaseTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4720881724794416645L;
	//商户日期
	private String mercDtTm;
	//汇入流水号
	private String impAcqSsn;
	//退货标识
	private String transType;
	//商户订单号、、退款订单号
	private String termSsn;
	//原网关流水
	private String oAcqSsn;
	//原入账日期
	private String oSttDate;
	//退货金额
	private String tranAmt;
	//终端号
	private String termCode;
	//备注1
	private String remark1;
	//备注2
	private String remark2;
	//现网关流水
	private String acqSsn;
	//响应码
	private String respCode;
	//入账日期
	private String settDate;
	public String getMercDtTm() {
		return mercDtTm;
	}
	public void setMercDtTm(String mercDtTm) {
		this.mercDtTm = mercDtTm;
	}
	public String getImpAcqSsn() {
		return impAcqSsn;
	}
	public void setImpAcqSsn(String impAcqSsn) {
		this.impAcqSsn = impAcqSsn;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTermSsn() {
		return termSsn;
	}
	public void setTermSsn(String termSsn) {
		this.termSsn = termSsn;
	}
	public String getoAcqSsn() {
		return oAcqSsn;
	}
	public void setoAcqSsn(String oAcqSsn) {
		this.oAcqSsn = oAcqSsn;
	}
	public String getoSttDate() {
		return oSttDate;
	}
	public void setoSttDate(String oSttDate) {
		this.oSttDate = oSttDate;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getAcqSsn() {
		return acqSsn;
	}
	public void setAcqSsn(String acqSsn) {
		this.acqSsn = acqSsn;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getSettDate() {
		return settDate;
	}
	public void setSettDate(String settDate) {
		this.settDate = settDate;
	}
	
	
	
}
