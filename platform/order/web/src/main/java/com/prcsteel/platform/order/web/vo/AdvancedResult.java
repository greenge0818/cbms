package com.prcsteel.platform.order.web.vo;

import com.prcsteel.platform.common.vo.Result;

/**
 * created by caochao
 * 2015-10-20
 */
public class AdvancedResult extends Result {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AdvancedResult() {
        this.setSuccess(true);
    }

    public AdvancedResult(boolean success, String message, Object data) {
        this.message = message;
        this.setData(data);
        this.setSuccess(success);
    }
}
