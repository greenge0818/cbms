package com.prcsteel.platform.smartmatch.model.query;

import java.math.BigDecimal;

/**
 * 普通资源查询明细
 *
 * @author peanut
 * @date 2016/6/23 15:33
 */
public class RestNormalResourceQuery {

    /**
     * 客户端城市名称
     */
    private String specificCityName;

    /**
     * 查询类型，例:热门资源：HOT,普通列表：NORMAL
     */
    private String type;

    /**
     * 品名uuid
     */
    private String categoryUuid;

    /**
     * 品名名称
     */
    private String categoryName;

    /**
     * 材质uuids(多个材质)
     */
    private String materialUuids;

    /**
     * 材质名称
     */
    private String materialNames;

    /**
     * 厂家ids（多选）
     */
    private String factoryIds;

    /**
     * 厂家名称（多选）
     */
    private String factoryNames;

    /**
     * 厂家ids（多选）
     */
    private String warehouseIds;

    /**
     * 厂家名称（多选）
     */
    private String warehouseNames;

    /**
     * 品规1（多选）
     */
    private String spec1;

    /**
     * 品规2（多选，可选区间）
     */
    private String spec2;

    /**
     * 品规3（多选，可选区间）
     */
    private String spec3;

    /**
     * 规格
     */
    private String spec;

    /**
     * 采购城市ID列表
     */
    private String purchaseCityIds;

    /**
     * 采购城市名称列表
     */
    private String purchaseCityNames;

    /**
     * 按某个字段排序 ，默认价格price
     */
    private String orderBy;

    /**
     * 顺排还是倒排 默认ASC从小到大；DESC 从大到小
     */
    private String orderWay;

    /**
     * 分页起始下标
     */
    private Integer start;

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 一页条数
     */
    private Integer pageSize;

    /**
     * 开始时间
     */
    private String startDate;

    /***
     * 结果时间
     */
    private String endDate;

    private BigDecimal priceMin;

    private BigDecimal priceMax;


    /**
     * 查询之前调用，确定分页参数
     */
    public void preQuery() {

        if ((this.pageIndex != null && this.pageSize != null) && (this.pageIndex >= 1 && this.pageSize >= 1)) {
            this.start = this.pageSize * (this.pageIndex - 1);
        }

    }

    public RestNormalResourceQuery() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMaterialUuids() {
        return materialUuids;
    }

    public void setMaterialUuids(String materialUuids) {
        this.materialUuids = materialUuids;
    }

    public String getMaterialNames() {
        return materialNames;
    }

    public void setMaterialNames(String materialNames) {
        this.materialNames = materialNames;
    }

    public String getFactoryIds() {
        return factoryIds;
    }

    public void setFactoryIds(String factoryIds) {
        this.factoryIds = factoryIds;
    }

    public String getFactoryNames() {
        return factoryNames;
    }

    public void setFactoryNames(String factoryNames) {
        this.factoryNames = factoryNames;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
    }

    public String getSpec3() {
        return spec3;
    }

    public void setSpec3(String spec3) {
        this.spec3 = spec3;
    }

    public String getPurchaseCityIds() {
        return purchaseCityIds;
    }

    public void setPurchaseCityIds(String purchaseCityIds) {
        this.purchaseCityIds = purchaseCityIds;
    }

    public String getPurchaseCityNames() {
        return purchaseCityNames;
    }

    public void setPurchaseCityNames(String purchaseCityNames) {
        this.purchaseCityNames = purchaseCityNames;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSpecificCityName() {
        return specificCityName;
    }

    public void setSpecificCityName(String specificCityName) {
        this.specificCityName = specificCityName;
    }

    public String getWarehouseIds() {
        return warehouseIds;
    }

    public void setWarehouseIds(String warehouseIds) {
        this.warehouseIds = warehouseIds;
    }

    public String getWarehouseNames() {
        return warehouseNames;
    }

    public void setWarehouseNames(String warehouseNames) {
        this.warehouseNames = warehouseNames;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(BigDecimal priceMin) {
        this.priceMin = priceMin;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
