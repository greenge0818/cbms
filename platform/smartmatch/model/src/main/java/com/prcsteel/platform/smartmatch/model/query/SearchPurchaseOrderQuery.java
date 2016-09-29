package com.prcsteel.platform.smartmatch.model.query;

import com.prcsteel.platform.common.query.PagedQuery;

import java.util.List;

/**
 * Created by Rolyer on 2015/11/24.
 */
public class SearchPurchaseOrderQuery extends PagedQuery {
    private String sellers; //卖家id列表如 1,2,3,4
	private String nsortNameUuid; //品名
    private String materialUuid; //材质
    private List<String> factory;//多个厂家使用","隔开。
    private List<String> orderStatus; //（可查）订单状态
    private String spec1;
    private String spec2;
    private String spec3;
    private String spec4;
    private String spec5;
    private String sqlOfspec; //规格查询语句

	public String getSellers() {
		return sellers;
	}

	public void setSellers(String sellers) {
		this.sellers = sellers;
	}

	public String getNsortNameUuid() {
        return nsortNameUuid;
    }

    public void setNsortNameUuid(String nsortNameUuid) {
        this.nsortNameUuid = nsortNameUuid;
    }

    public String getMaterialUuid() {
        return materialUuid;
    }

    public void setMaterialUuid(String materialUuid) {
        this.materialUuid = materialUuid;
    }

    public List<String> getFactory() {
        return factory;
    }

    public void setFactory(List<String> factory) {
        this.factory = factory;
    }

    public List<String> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<String> orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getSpec4() {
        return spec4;
    }

    public void setSpec4(String spec4) {
        this.spec4 = spec4;
    }

    public String getSpec5() {
        return spec5;
    }

    public void setSpec5(String spec5) {
        this.spec5 = spec5;
    }

    public String getSqlOfspec() {
        return sqlOfspec;
    }

    public void setSqlOfspec(String sqlOfspec) {
        this.sqlOfspec = sqlOfspec;
    }
}
