package com.prcsteel.platform.smartmatch.model.dto;

import java.io.Serializable;

/**
 * 接口基础数据格式
 *
 * @author peanut
 * @date 2016/6/20 11:20
 */
public class RestBaseDto implements Serializable {

    /**
     * 状态码
     */
    private String status;

    /**
     * 数据列表
     */
    private Object data;


    public RestBaseDto() {
    }

    public RestBaseDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
