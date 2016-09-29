package com.prcsteel.rest.bdl.payment.spdb.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by ericlee on 7/18/15.
 */
@XmlRootElement(name="List")
@XmlAccessorType(XmlAccessType.FIELD)
public class List implements Serializable {
    /*
    <voucherNo></voucherNo>
    <seqNo></seqNo>
    <txAmount>1.00</txAmount>
    <balance>35445507.88</balance>
    <tranFlag>0</tranFlag>
    <transDate>20040906</transDate>
    <transTime>091223<transTime/>
    <note>0</note>
    <remark>测试桥北路营业部</remark>
    <payeeBankNo></payeeBankNo>
    <payeeBankName></payeeBankName>
    <payeeAcctNo></payeeAcctNo>
    <payeeName></payeeName>
    */
    private String voucherNo;  //凭证号
    private String seqNo;  //交易流水号
    private String txAmount; //交易金额
    private String balance;  //账户余额
    private String tranFlag;  //借贷标志
    private String transDate;  //交易日期
    private String transTime;  //交易时间
    private String note;  //备注
    private String remark;  //摘要代码
    private String payeeBankNo; //对方行号
    private String payeeBankName;//对方行名
    private String payeeAcctNo; //对方账号
    private String payeeName; //对方户名
    private String transCode;  //交易代码
    private String branchId;  //分行ID
    private String customerAcctNo;  //客户账号
    private String payeeAcctType;  //对方账号类型
    private String transCounter;  //交易柜员
    private String authCounter;  //授权柜员
    private String otherChar10;  //备用域10位
    private String otherChar40;  //备用域40位
    private String seqNum;  //传票序号
    private String revFlag;  //冲补标志


    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(String txAmount) {
        this.txAmount = txAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTranFlag() {
        return tranFlag;
    }

    public void setTranFlag(String tranFlag) {
        this.tranFlag = tranFlag;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayeeBankNo() {
        return payeeBankNo;
    }

    public void setPayeeBankNo(String payeeBankNo) {
        this.payeeBankNo = payeeBankNo;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeAcctNo() {
        return payeeAcctNo;
    }

    public void setPayeeAcctNo(String payeeAcctNo) {
        this.payeeAcctNo = payeeAcctNo;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCustomerAcctNo() {
        return customerAcctNo;
    }

    public void setCustomerAcctNo(String customerAcctNo) {
        this.customerAcctNo = customerAcctNo;
    }

    public String getPayeeAcctType() {
        return payeeAcctType;
    }

    public void setPayeeAcctType(String payeeAcctType) {
        this.payeeAcctType = payeeAcctType;
    }

    public String getTransCounter() {
        return transCounter;
    }

    public void setTransCounter(String transCounter) {
        this.transCounter = transCounter;
    }

    public String getAuthCounter() {
        return authCounter;
    }

    public void setAuthCounter(String authCounter) {
        this.authCounter = authCounter;
    }

    public String getOtherChar10() {
        return otherChar10;
    }

    public void setOtherChar10(String otherChar10) {
        this.otherChar10 = otherChar10;
    }

    public String getOtherChar40() {
        return otherChar40;
    }

    public void setOtherChar40(String otherChar40) {
        this.otherChar40 = otherChar40;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public String getRevFlag() {
        return revFlag;
    }

    public void setRevFlag(String revFlag) {
        this.revFlag = revFlag;
    }

    public List(String voucherNo, String seqNo, String txAmount, String balance, String tranFlag, String transDate, String transTime, String note, String remark, String payeeBankNo, String payeeBankName, String payeeAcctNo, String payeeName, String transCode, String branchId, String customerAcctNo, String payeeAcctType, String transCounter, String authCounter, String otherChar10, String otherChar40, String seqNum, String revFlag) {
        this.voucherNo = voucherNo;
        this.seqNo = seqNo;
        this.txAmount = txAmount;
        this.balance = balance;
        this.tranFlag = tranFlag;
        this.transDate = transDate;
        this.transTime = transTime;
        this.note = note;
        this.remark = remark;
        this.payeeBankNo = payeeBankNo;
        this.payeeBankName = payeeBankName;
        this.payeeAcctNo = payeeAcctNo;
        this.payeeName = payeeName;
        this.transCode = transCode;
        this.branchId = branchId;
        this.customerAcctNo = customerAcctNo;
        this.payeeAcctType = payeeAcctType;
        this.transCounter = transCounter;
        this.authCounter = authCounter;
        this.otherChar10 = otherChar10;
        this.otherChar40 = otherChar40;
        this.seqNum = seqNum;
        this.revFlag = revFlag;
    }

    public List() {
    }
}
