
package com.prcsteel.platform.order.model.query;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lichaowei on 2015/11/19.
 */
public class AllowanceDetailItemQuery {
    private Long id;

    private Long allowanceId;  //这个目前用于reb_allowance_order_item里面的allowance_id
    private Long allowanceKey; //这个目前用于reb_allowance_order_item里面的id
    
    private List<String> allowanceIds;

	private String allowanceType;

	private List<String> listStatus;

	private Long accountId;

	private Boolean allowanceUnused; // 未使用折让
	
	private BigDecimal amount; //折让金额
	
	Long unInvoiceInId; // 进项票Id（查询进项折让关联需要排除的进项Id）

    public AllowanceDetailItemQuery() {
    }

    public AllowanceDetailItemQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAllowanceId() {
		return allowanceId;
	}

	public void setAllowanceId(Long allowanceId) {
		this.allowanceId = allowanceId;
	}

	public List<String> getAllowanceIds() {
		return allowanceIds;
	}

	public void setAllowanceIds(List<String> allowanceIds) {
		this.allowanceIds = allowanceIds;
	}

	public String getAllowanceType() {
		return allowanceType;
	}

	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}

	public List<String> getListStatus() {
		return listStatus;
	}

	public void setListStatus(List<String> listStatus) {
		this.listStatus = listStatus;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Boolean isAllowanceUnused() {
		return allowanceUnused;
	}

	public void setAllowanceUnused(Boolean allowanceUnused) {
		this.allowanceUnused = allowanceUnused;
	}

	public Long getUnInvoiceInId() {
		return unInvoiceInId;
	}

	public void setUnInvoiceInId(Long unInvoiceInId) {
		this.unInvoiceInId = unInvoiceInId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getAllowanceKey() {
		return allowanceKey;
	}

	public AllowanceDetailItemQuery setAllowanceKey(Long allowanceKey) {
		this.allowanceKey = allowanceKey;
		return this;
	}
}





