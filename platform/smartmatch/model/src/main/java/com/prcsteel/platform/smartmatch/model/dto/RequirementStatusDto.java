package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;

/**
 * 需求单状态Dto--走MQ
 *
 * @author peanut
 * @date 2016/6/22 9:56
 */
public class RequirementStatusDto implements Serializable {
    /**
     * 需求单号
     */
    private String code;

    /**
     * 设置成状态，例：QUOTED 报价，CLOSED 关闭
     */
    private String statusTo;

    /**
     * 报价单号
     */
    private String operateCode;

    /**
     * 报价单生成时间或关闭时间
     */
    private String operated;

    /**
     * 关闭理由
     */
    private String closeReason;

    public RequirementStatusDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatusTo() {
        return statusTo;
    }

    public void setStatusTo(String statusTo) {
        this.statusTo = statusTo;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getOperated() {
        return operated;
    }

    public void setOperated(String operated) {
        this.operated = operated;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }
}
