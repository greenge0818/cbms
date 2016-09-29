package com.prcsteel.platform.common.model;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rolyer on 2016/3/22.
 */
public class AccountPointsInput {
    private String name;
    private String mobile;
    private String openId;


    public AccountPointsInput() {
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


    
    @Override
    public String toString() {
        return "EarnPointsInput{" +
            "name='" + name + '\'' +
            ", mobile='" + mobile + '\'' +
            ", openId='" + openId + '\'' +
            '}';
    }
}
