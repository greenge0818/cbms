package com.prcsteel.platform.acl.web.controller;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 组织api
 * @date 2016/2/18
 */
@RestController
@RequestMapping("/api/org/")
public class RestOrganizationController extends BaseController {

    @Resource
    private OrganizationService organizationService;

    /**
     * 根据组织id查询组织
     * @param id
     * @return
     */
    @RequestMapping(value = "queryById/{id}.html", method = RequestMethod.GET)
    public Organization queryById(@PathVariable Long id){
        return organizationService.queryById(id);
    }

    /**
     * 查找服务中心子集
     * @param parentId
     * @return
     */
    @RequestMapping(value = "queryByParentId/{parentId}.html", method = RequestMethod.GET)
    public List<Organization> queryByParentId(@PathVariable Long parentId){
        return organizationService.queryByParentId(parentId);
    }

    /**
     * 获取所有第二级中心
     * @return
     */
    @RequestMapping(value = "queryAllSecendOrg.html", method = RequestMethod.GET)
    public List<Organization> queryAllSecendOrg(){
        return organizationService.getAllSecendOrg();
    }

    /**
     * 查询所有组织
     * @return
     */
    @RequestMapping(value = "queryAllBusinessOrg.html", method = RequestMethod.GET)
    public List<Organization> queryAllBusinessOrg(){
        return organizationService.queryAllBusinessOrg();
    }

    /**
     * 根据组织id查询设置
     * @return
     */
    @RequestMapping(value = "queryDeliverySettingByOrgId/{id}.html", method = RequestMethod.GET)
    public List<String> queryDeliverySettingByOrgId(@PathVariable Long id){
        return organizationService.selectDeliverySettingByOrgId(id);
    }

}
