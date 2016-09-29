package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.ReportPrecipitationFunds;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportPrecipitationFundsDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 平台沉淀资金dto
 * @date 2015/12/14
 */
public class ReportPrecipitationFundsDto extends ReportPrecipitationFunds {
    private String calculateStr;

    public String getCalculateStr() {
        return calculateStr;
    }

    public void setCalculateStr(String calculateStr) {
        this.calculateStr = calculateStr;
    }
}
