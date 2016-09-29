package com.prcsteel.platform.smartmatch.model.query;

import java.util.List;

/**
 * 超市 Rest
 *
 * @author peanut
 * @date 2016/6/22 16:28
 */
public class RestHotResourceQuery {

    /**
     * 客户端所在城市名称
     */
    private String specificCityName;

    /**
     * 资源条数
     */
    private Integer length;

    /**
     * 普通资源查询明细
     */
    private List<RestNormalResourceQuery> items;


    public static class RestResourItems {

    }

    public RestHotResourceQuery() {
    }


    public String getSpecificCityName() {
        return specificCityName;
    }

    public void setSpecificCityName(String specificCityName) {
        this.specificCityName = specificCityName;
    }

    public List<RestNormalResourceQuery> getItems() {
        return items;
    }

    public void setItems(List<RestNormalResourceQuery> items) {
        this.items = items;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
