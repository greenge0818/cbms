package com.prcsteel.platform.order.model.dto;

import com.prcsteel.platform.order.model.model.ReportOrgDay;

/**
 * @author dengxiyan
 * @version V1.0
 * @Title: ReportOrgDayDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description: 服务中心日报dto
 * @date 2015/12/14
 */
public class ReportOrgDayDto extends ReportOrgDay {
    private String calculateStr;

    public String getCalculateStr() {
        return calculateStr;
    }

    public void setCalculateStr(String calculateStr) {
        this.calculateStr = calculateStr;
    }
}
