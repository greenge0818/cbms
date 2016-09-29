package com.prcsteel.platform.kuandao.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *   款道2.0支付单流程处理完成之后会对客户进行充值操作，
 *   修改客户账户余额，需要账户体系提供接口。
 * Created by wangxiao on 2016/6/28.
 */
public class PaymentAccountDto implements Serializable {


	private static final long serialVersionUID = 7356330767510543158L;

	/**客户编号*/
	private Long accountId;
	/**充值金额*/
	private BigDecimal amount;
	/**交易时间*/
	private  Long  transDate;
	/**操作人员*/
	private String operatorName;
	/**款道支付单号*/
	private	String kuandaoPayorderId;
	/**操作id*/ 
	private  Long operatorId;


	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTransDate() {
		return transDate;
	}

	public void setTransDate(Long transDate) {
		this.transDate = transDate;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getKuandaoPayorderId() {
		return kuandaoPayorderId;
	}

	public void setKuandaoPayorderId(String kuandaoPayorderId) {
		this.kuandaoPayorderId = kuandaoPayorderId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
}
 
