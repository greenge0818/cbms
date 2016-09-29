package com.prcsteel.platform.acl.model.dto;

import com.prcsteel.platform.acl.model.model.Organization;

import java.util.List;

/**
 * @author wangxianjun
 * @version V2.0
 * @Title: BaseOrganizationDeliverDto
 * @Package com.prcsteel.platform.order.model.dto
 * @Description:
 * @date 2016/01/27
 */
public class BaseOrganizationDto extends Organization {
    private List<BaseOrganizationDeliverDto> orgDeliverList;

    public List<BaseOrganizationDeliverDto> getOrgDeliverList() {
        return orgDeliverList;
    }

    public void setOrgDeliverList(List<BaseOrganizationDeliverDto> orgDeliverList) {
        this.orgDeliverList = orgDeliverList;
    }

    public BaseOrganizationDto() {
    }
}