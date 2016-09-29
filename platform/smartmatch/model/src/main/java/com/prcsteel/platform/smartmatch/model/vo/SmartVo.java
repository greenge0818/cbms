package com.prcsteel.platform.smartmatch.model.vo;
/**
 * Created by myh on 2015/11/16.
 */

public class SmartVo {
    private Object data;
    private Boolean isLogin;
    private String message;
    private Integer statusCode;
    private Integer total;

    public SmartVo() {
        this.isLogin = false;
        this.message = "请求成功";
        this.statusCode = 0;
        this.total = 0;
    }

    public SmartVo(Object data) {
        this.data = data;
        this.isLogin = false;
        this.message = "请求成功";
        this.statusCode = 0;
        this.total = 0;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    public Boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
