package com.prcsteel.platform.account.api;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.model.model.UserOrg;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author kongbinheng
 * @version v2.0_account
 * @Description: 用户接口api
 * @date 2016/1/14
 */
@RestApi(value="restUserService", restServer="aclRestServer")
public interface RestUserService {

    /**
     * 根据登录用户login_id查询用户
     * @param loginId
     * @return
     */
    @RestMapping(value="user/queryByLoginId/{loginId}.html", method=RequestMethod.GET)
    public User queryByLoginId(@UrlParam("loginId") String loginId);

    /**
     * 根据登录用户主键id查询用户
     * @param id
     * @return
     */
    @RestMapping(value="user/queryById/{id}.html", method=RequestMethod.GET)
    public User queryById(@UrlParam("id") Long id);

    /**
     * 获取指定Role范围内的用户的编号集
     * @param roleIds
     * @return
     */
    @RequestMapping(value = "user/queryUserIdsByRoleIds.html", method = RequestMethod.POST)
    public List<Long> queryUserIdsByRoleIds(@UrlParam List<Long> roleIds);

    /**
     * 判断用户是否是经理
     * @param user
     * @return
     */
    @RequestMapping(value = "user/isManager.html", method = RequestMethod.POST)
    public boolean isManager(@UrlParam User user);

    /**
     * 根据userId查询配置
     */
    @RequestMapping(value = "user/getConfigByUserId/{userId}.html", method = RequestMethod.GET)
    public List<UserOrg> getConfigByUserId(@UrlParam("userId") Long userId);

}
