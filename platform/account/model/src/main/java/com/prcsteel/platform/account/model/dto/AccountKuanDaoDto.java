package com.prcsteel.platform.account.model.dto;


import java.io.Serializable;
import java.math.BigDecimal;


/**
 *   款道2.0支付单流程处理完成之后会对客户进行充值操作，
 *   修改客户账户余额，需要账户体系提供接口。
 * Created by wangxiao on 2016/6/28.
 * 名称	                说明	         类型	    	  备注
 * accountId	        客户编号	    Long	    	主键，可以根据这个更新
 * amount	            充值金额	    BigDecimal		需要做必要校验
 * transDate	        交易时间	    Long	    	交易时间
 * operatorName	        操作人员	    String		    暂定为“款道”
 * kuandaoPayorderId	款道支付单号	 Long	    	系统记录一下，方便将来查找问题
 *operatorId            操作id           Long
 */
public class AccountKuanDaoDto  implements Serializable {
	private Long accountId;
	private BigDecimal amount;
	private  Long  transDate;
	private String operatorName;
	private	String kuandaoPayorderId;
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
 