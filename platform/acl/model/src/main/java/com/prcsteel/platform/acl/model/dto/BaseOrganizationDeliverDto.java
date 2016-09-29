package com.prcsteel.platform.acl.model.dto;


import com.prcsteel.platform.acl.model.model.BaseOrganizationDeliver;

/**
 * @author wangxianjun
 * @version V2.0
 * @Title: BaseOrganizationDeliverDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description:
 * @date 2016/01/27
 */
public class BaseOrganizationDeliverDto extends BaseOrganizationDeliver {
    private String name;

    public BaseOrganizationDeliverDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}