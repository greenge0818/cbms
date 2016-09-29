package com.prcsteel.platform.account.api;

import com.prcsteel.platform.acl.model.model.Role;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 角色接口api
 * @date 2016/2/18
 */
@RestApi(value="restRoleService", restServer="aclRestServer")
public interface RestRoleService {

    /**
     * 根据角色id查询角色
     * @param id
     * @return
     */
    @RequestMapping(value = "role/queryById/{id}.html", method = RequestMethod.GET)
    public Role queryById(@UrlParam("id") Long id);

    /**
     * 根据角色父id查询角色
     * @param parentId
     * @return
     */
    @RequestMapping(value = "role/queryRoleIds/{parentId}.html", method = RequestMethod.GET)
    public List<Long> queryRoleIds(@UrlParam("parentId") Long parentId);

}
