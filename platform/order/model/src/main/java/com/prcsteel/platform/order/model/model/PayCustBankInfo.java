package com.prcsteel.platform.order.model.model;

/**
 * 新增付款 卖家银行信息
 * Created by chengui on 2016/3/8.
 */
public class PayCustBankInfo {

    private Long custCode; //卖家公司代码
    
    private String custTel; //联系电话
    
    private String custType; //客户类型（买家，卖家或两者都是）
    
    private String custRegAddr; //注册地址

    private String bankCode; //银行行号

    private String bankCity; //银行所在城市

    private String branchBankName; //支行名称

	public Long getCustCode() {
		return custCode;
	}

	public void setCustCode(Long custCode) {
		this.custCode = custCode;
	}

	public String getCustTel() {
		return custTel;
	}

	public void setCustTel(String custTel) {
		this.custTel = custTel;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getCustRegAddr() {
		return custRegAddr;
	}

	public void setCustRegAddr(String custRegAddr) {
		this.custRegAddr = custRegAddr;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}
    
}