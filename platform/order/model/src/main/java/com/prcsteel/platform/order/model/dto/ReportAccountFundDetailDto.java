package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.ReportAccountFinancial;

import java.math.BigDecimal;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportAccountFundDetail
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 往来单位账务明细报表dto
 * @date 2015/12/17
 */
public class ReportAccountFundDetailDto extends ReportAccountFinancial {
    private String dateStr;
    private BigDecimal bankHappenAmount;//银行存款发生额

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getBankHappenAmount() {
        return bankHappenAmount;
    }

    public void setBankHappenAmount(BigDecimal bankHappenAmount) {
        this.bankHappenAmount = bankHappenAmount;
    }
}
