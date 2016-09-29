package com.prcsteel.platform.order.model.dto;

import java.util.List;

import com.prcsteel.platform.order.model.model.ReportNewUserReward;

/**
 * Created by dengxiyan on 2015/9/18.
 * 提成报表：新增客户提成信息和提成详细列表
 */
public class RewardNewAcccountWithDetailsDto extends ReportNewUserReward{
    private String calFormula;//提成合计计算公式
    private List<RewardDetailDto> details;

    public String getCalFormula() {
        return calFormula;
    }

    public void setCalFormula(String calFormula) {
        this.calFormula = calFormula;
    }

    public List<RewardDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<RewardDetailDto> details) {
        this.details = details;
    }
}
