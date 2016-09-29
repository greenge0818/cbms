package com.prcsteel.cxfrs.service.bdl.model.history;

public class Rd implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4254742160736475677L;
	private String drcrf;
	private String vouhNo;
	private String amount; //allow negative value
	private String recipBkNo;
	private String recipAccNo;
	private String recipName;
	private String summary;
	private String useCN;
	private String postScript;
	private String ref;
	private String busCode;
	private String oref;
	private String enSummary;
	private String busType;
	private String cvouhType; //diff
	private String addInfo;
	private String timeStamp;
	private String repReserved3;
	private String repReserved4;
	private String upDtranf;
	private String valueDate;
	private String trxCode;
	private String sequenceNo;
	private String cashf;
	private String subAcctSeq;
	private String tHCurrency;

	public String getDrcrf() {
		return drcrf;
	}
	public void setDrcrf(String drcrf) {
		this.drcrf = drcrf;
	}
	public String getVouhNo() {
		return vouhNo;
	}
	public void setVouhNo(String vouhNo) {
		this.vouhNo = vouhNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRecipBkNo() {
		return recipBkNo;
	}
	public void setRecipBkNo(String recipBkNo) {
		this.recipBkNo = recipBkNo;
	}
	public String getRecipAccNo() {
		return recipAccNo;
	}
	public void setRecipAccNo(String recipAccNo) {
		this.recipAccNo = recipAccNo;
	}
	public String getRecipName() {
		return recipName;
	}
	public void setRecipName(String recipName) {
		this.recipName = recipName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUseCN() {
		return useCN;
	}
	public void setUseCN(String useCN) {
		this.useCN = useCN;
	}
	public String getPostScript() {
		return postScript;
	}
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getOref() {
		return oref;
	}
	public void setOref(String oref) {
		this.oref = oref;
	}
	public String getEnSummary() {
		return enSummary;
	}
	public void setEnSummary(String enSummary) {
		this.enSummary = enSummary;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getCvouhType() {
		return cvouhType;
	}
	public void setCvouhType(String cvouhType) {
		this.cvouhType = cvouhType;
	}
	public String getAddInfo() {
		return addInfo;
	}
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getRepReserved3() {
		return repReserved3;
	}
	public void setRepReserved3(String repReserved3) {
		this.repReserved3 = repReserved3;
	}
	public String getRepReserved4() {
		return repReserved4;
	}
	public void setRepReserved4(String repReserved4) {
		this.repReserved4 = repReserved4;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUpDtranf() {
		return upDtranf;
	}

	public void setUpDtranf(String upDtranf) {
		this.upDtranf = upDtranf;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getCashf() {
		return cashf;
	}

	public void setCashf(String cashf) {
		this.cashf = cashf;
	}

	public String getSubAcctSeq() {
		return subAcctSeq;
	}

	public void setSubAcctSeq(String subAcctSeq) {
		this.subAcctSeq = subAcctSeq;
	}

	public String gettHCurrency() {
		return tHCurrency;
	}

	public void settHCurrency(String tHCurrency) {
		this.tHCurrency = tHCurrency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Rd(String drcrf, String vouhNo, String amount, String recipBkNo,
			  String recipAccNo, String recipName, String summary, String useCN,
			  String postScript, String ref, String busCode, String oref,
			  String enSummary, String busType, String cvouhType, String addInfo,
			  String timeStamp, String repReserved3, String repReserved4,
			  String upDtranf, String valueDate, String trxCode,
			  String sequenceNo, String cashf, String subAcctSeq, String tHCurrency) {
		super();
		this.drcrf = drcrf;
		this.vouhNo = vouhNo;
		this.amount = amount;
		this.recipBkNo = recipBkNo;
		this.recipAccNo = recipAccNo;
		this.recipName = recipName;
		this.summary = summary;
		this.useCN = useCN;
		this.postScript = postScript;
		this.ref = ref;
		this.busCode = busCode;
		this.oref = oref;
		this.enSummary = enSummary;
		this.busType = busType;
		this.cvouhType = cvouhType;
		this.addInfo = addInfo;
		this.timeStamp = timeStamp;
		this.repReserved3 = repReserved3;
		this.repReserved4 = repReserved4;
		this.upDtranf = upDtranf;
		this.valueDate = valueDate;
		this.trxCode = trxCode;
		this.sequenceNo = sequenceNo;
		this.cashf = cashf;
		this.subAcctSeq = subAcctSeq;
		this.tHCurrency = tHCurrency;
	}

	public Rd() {
	}
}
