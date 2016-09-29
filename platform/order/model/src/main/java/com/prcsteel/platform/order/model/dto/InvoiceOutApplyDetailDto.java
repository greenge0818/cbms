package com.prcsteel.platform.order.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceOutApplyDetail;

/**
 * Created by rolyer on 15-9-14.
 */
public class InvoiceOutApplyDetailDto extends InvoiceOutApplyDetail {
    private BigDecimal totalApplyAmount = BigDecimal.ZERO;
    private BigDecimal balanceSecondSettlement = BigDecimal.ZERO;
    private BigDecimal totalUnApplyAmount = BigDecimal.ZERO;
    private Date minCreated;
    private int countMonth;

	private List<InvoiceOutItemDetailsDto> itemDetailList = new ArrayList<InvoiceOutItemDetailsDto>();

	public void setItemDetailList(List<InvoiceOutItemDetailsDto> itemDetailList) {
		this.itemDetailList = itemDetailList;
	}

	public void addDetail(InvoiceOutItemDetailsDto itemDetail) {
		this.itemDetailList.add(itemDetail);
	}

	public List<InvoiceOutItemDetailsDto> getItemDetailList() {
		return itemDetailList;
	}
	

    public BigDecimal getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(BigDecimal totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public BigDecimal getBalanceSecondSettlement() {
        return balanceSecondSettlement;
    }

    public void setBalanceSecondSettlement(BigDecimal balanceSecondSettlement) {
        this.balanceSecondSettlement = balanceSecondSettlement;
    }

    public BigDecimal getTotalUnApplyAmount() {
        return totalUnApplyAmount;
    }

    public void setTotalUnApplyAmount(BigDecimal totalUnApplyAmount) {
        this.totalUnApplyAmount = totalUnApplyAmount;
    }

    public Date getMinCreated() {
        return minCreated;
    }

    public void setMinCreated(Date minCreated) {
        this.minCreated = minCreated;
    }

    public int getCountMonth() {
        return countMonth;
    }

    public void setCountMonth(int countMonth) {
        this.countMonth = countMonth;
    }
}
