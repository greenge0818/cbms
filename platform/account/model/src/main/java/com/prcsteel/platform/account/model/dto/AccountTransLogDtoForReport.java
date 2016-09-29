package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.AccountTransLog;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: AccountTransLogForReport
 * @Package com.prcsteel.platform.order.model.dto
 * @Description:
 * @date 2015/12/28
 */
public class AccountTransLogDtoForReport extends AccountTransLog {
    private String applyTypeForReport;//申请类型用于报表

    public AccountTransLogDtoForReport() {
    }

    public AccountTransLogDtoForReport(String applyTypeForReport) {
        this.applyTypeForReport = applyTypeForReport;
    }

    public String getApplyTypeForReport() {
        return applyTypeForReport;
    }

    public void setApplyTypeForReport(String applyTypeForReport) {
        this.applyTypeForReport = applyTypeForReport;
    }

}
