package com.prcsteel.platform.smartmatch.api;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.RestOrganization;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prcsteel.platform.acl.model.model.Organization;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 组织接口api
 * @date 2016/2/18
 */
@RestApi(value="restOrganizationService", restServer="aclRestServer")
public interface RestOrganizationService {

    /**
     * 根据组织id查询组织
     * @param id
     * @return
     */
    @RestMapping(value = "org/queryById/{id}.html", method = RequestMethod.GET)
    public RestOrganization queryById(@UrlParam("id") Long id);

    /**
     * 查找服务中心子集
     * @param parentId
     * @return
     */
    @RestMapping(value = "org/queryByParentId/{parentId}.html", method = RequestMethod.GET)
    public List<Organization> queryByParentId(@UrlParam("parentId") Long parentId);

    /**
     * 获取所有第二级中心
     * @return
     */
    @RestMapping(value = "org/queryAllSecendOrg.html", method = RequestMethod.GET)
    public List<Organization> getAllSecendOrg();

    /**
     * 查询所有组织
     * @return
     */
    @RestMapping(value = "org/queryAllBusinessOrg.html", method = RequestMethod.GET)
    public List<Organization> queryAllBusinessOrg();

    /**
     * 根据组织id查询设置
     * @return
     */
    @RestMapping(value = "org/queryDeliverySettingByOrgId/{id}.html", method = RequestMethod.GET)
    public List<String> queryDeliverySettingByOrgId(@UrlParam("id") Long id);

}
