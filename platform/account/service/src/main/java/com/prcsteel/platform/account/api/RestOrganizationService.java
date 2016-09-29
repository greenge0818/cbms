package com.prcsteel.platform.account.api;

import com.prcsteel.platform.acl.model.model.Organization;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
    @RequestMapping(value = "org/queryById/{id}.html", method = RequestMethod.GET)
    public Organization queryById(@UrlParam("id") Long id);

    /**
     * 查找服务中心子集
     * @param parentId
     * @return
     */
    @RequestMapping(value = "org/queryByParentId/{parentId}.html", method = RequestMethod.GET)
    public List<Organization> queryByParentId(@UrlParam("parentId") Long parentId);

    /**
     * 获取所有第二级中心
     * @return
     */
    @RequestMapping(value = "org/queryAllSecendOrg.html", method = RequestMethod.GET)
    public List<Organization> getAllSecendOrg();

    /**
     * 查询所有组织
     * @return
     */
    @RequestMapping(value = "org/queryAllBusinessOrg.html", method = RequestMethod.GET)
    public List<Organization> queryAllBusinessOrg();

    /**
     * 根据组织id查询设置
     * @return
     */
    @RequestMapping(value = "org/queryDeliverySettingByOrgId/{id}.html", method = RequestMethod.GET)
    public List<String> queryDeliverySettingByOrgId(@UrlParam("id") Long id);

}
