package com.prcsteel.platform.order.model.query;


/**
 * @author wangxianjun@prcsteel.com
 * @version V1.0
 * @Description: 项目查询
 * @date 2016/6/27
 */
public class OrderEcAppQuery {
	//超市用户ID
    private Integer userId;
    
  //订单状态
    private String status;
    //起始时间
    private String startTime;
    //结束时间
    private String endTime;
    
    //品名或单号
    private String keyword;

    private Integer start;

    private Integer length;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}


