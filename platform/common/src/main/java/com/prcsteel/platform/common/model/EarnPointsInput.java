package com.prcsteel.platform.common.model;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Rolyer on 2016/2/25.
 */
public class EarnPointsInput {
    private String name;
    private String mobile;
    private String openId;
    private String orderCode;
    private String genDate;
    private String account;
    private List<OrderItem> orderItems;

    public EarnPointsInput() {
    }

    public HashMap<String,String> toHashMap( ){
        HashMap<String,String> hm = new HashMap<>();
        if (StringUtils.isNotBlank(name)) {
            hm.put("name", name);
        }
        if (StringUtils.isNotBlank(mobile)) {
            hm.put("mobile", mobile);
        }
        if (StringUtils.isNotBlank(openId)) {
            hm.put("openId", openId);
        }
        if (StringUtils.isNotBlank(orderCode)) {
            hm.put("orderCode", orderCode);
        }
        if (this.getGenDate()!=null) {
            hm.put("genDate", genDate);
        }
        if (StringUtils.isNotBlank(account)) {
            hm.put("account", account);
        }
        return hm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getGenDate() {
		return genDate;
	}

	public void setGenDate(String genDate) {
		this.genDate = genDate;
	}

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

	public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    @Override
    public String toString() {
        return "EarnPointsInput{" +
            "name='" + name + '\'' +
            ", mobile='" + mobile + '\'' +
            ", openId='" + openId + '\'' +
            ", orderCode='" + orderCode + '\'' +
            ", genDate=" + genDate +
            ", orderItems=" + orderItems +
            '}';
    }
}
