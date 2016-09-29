package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.dto.OrganizationDto;
import com.prcsteel.platform.acl.model.model.Organization;

import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit Mao on 2015/7/14.
 */
public interface OrganizationService {
    Organization queryById(Long id);

    List<String> selectDeliverySettingByOrgId(Long id);

    List<Organization> queryByParentId(Long parentId);

    OrganizationDto selectOrgInfoByParam(Map<String, Object> param);

    void addOrganization(Organization organization, List<String> deliveryTypes, String curName);

    void addDepartment(Organization organization, String curName);

    void updateOrganization(Organization organization, List<String> deliveryTypes, String curUser);
    
    void  updateDepartment(Organization organization, String curUser);

    /**
     * 获取所有子服务中心
     * @param parentOrgId 上级服务中心id
     * @param mode        0: 包含上级服务中心自己   1: 不包含上级服务中心自己
     * @return
     */
    List<Long> getAllChildOrgId(Long parentOrgId, Integer mode);

    /**
     * 查询所有业务服务中心：如长沙服务中心
     * @return
     */
    List<Organization>  queryAllBusinessOrg();

    /**
     * 获取所有第二级中心
     * @return
     */
    List<Organization>  getAllSecendOrg();

    List<Organization> getAllOrganization();

     /* 查询所有业务服务中心所在城市：如长沙
     * @return
     */
    List<com.prcsteel.platform.acl.model.dto.Organization>  queryBusinessOrgToWeChat();

    List<com.prcsteel.platform.acl.model.dto.Organization> queryDraftedOrg();
}
