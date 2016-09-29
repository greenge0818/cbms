package com.prcsteel.platform.smartmatch.model.query;

/**
 * Created by Rolyer on 2015/11/30.
 */
public class DailyResourceQuery {
    /**
     * 地区ID
     */
    private Long areaId;
    /**
     * 时间
     */
    private String dt;

    public DailyResourceQuery() {
    }

    public DailyResourceQuery(Long areaId, String dt) {
        this.areaId = areaId;
        this.dt = dt;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
