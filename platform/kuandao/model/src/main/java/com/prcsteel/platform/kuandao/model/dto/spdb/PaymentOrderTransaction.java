package com.prcsteel.platform.kuandao.model.dto.spdb;

import java.io.Serializable;

public class PaymentOrderTransaction extends BaseTransaction implements Serializable{
	private static final long serialVersionUID = -7912267439134448990L;
	private String mercDtTm;
	private String termSsn;
	private String tranAmt;
	private String rcvMerId;
	private String rcvMerName;
	private String payMerId;
	private String payMerName;
	private String mercUrl;
	private String subMercGoodsName;
	private String subMercGoodsCount;
	private String remark;
	private String acqSsn;
	private String respCode;
	private String settDate;
	public String getMercDtTm() {
		return mercDtTm;
	}
	public void setMercDtTm(String mercDtTm) {
		this.mercDtTm = mercDtTm;
	}
	public String getTermSsn() {
		return termSsn;
	}
	public void setTermSsn(String termSsn) {
		this.termSsn = termSsn;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getRcvMerId() {
		return rcvMerId;
	}
	public void setRcvMerId(String rcvMerId) {
		this.rcvMerId = rcvMerId;
	}
	public String getRcvMerName() {
		return rcvMerName;
	}
	public void setRcvMerName(String rcvMerName) {
		this.rcvMerName = rcvMerName;
	}
	public String getPayMerId() {
		return payMerId;
	}
	public void setPayMerId(String payMerId) {
		this.payMerId = payMerId;
	}
	public String getPayMerName() {
		return payMerName;
	}
	public void setPayMerName(String payMerName) {
		this.payMerName = payMerName;
	}
	public String getMercUrl() {
		return mercUrl;
	}
	public void setMercUrl(String mercUrl) {
		this.mercUrl = mercUrl;
	}
	public String getSubMercGoodsName() {
		return subMercGoodsName;
	}
	public void setSubMercGoodsName(String subMercGoodsName) {
		this.subMercGoodsName = subMercGoodsName;
	}
	public String getSubMercGoodsCount() {
		return subMercGoodsCount;
	}
	public void setSubMercGoodsCount(String subMercGoodsCount) {
		this.subMercGoodsCount = subMercGoodsCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
