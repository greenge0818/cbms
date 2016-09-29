package com.prcsteel.platform.common.dto;

/**
 * Created by caochao on 2015/8/1.
 */
public class ResultDto {
    private boolean success=false;
    private String message="";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
