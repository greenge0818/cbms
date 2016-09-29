package com.prcsteel.platform.smartmatch.api;

import java.util.List;

import com.prcsteel.platform.smartmatch.model.model.RestUser;
import org.prcsteel.rest.annotation.RestApi;
import org.prcsteel.rest.annotation.RestMapping;
import org.prcsteel.rest.annotation.UrlParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prcsteel.platform.acl.model.model.User;

/**
 * 用户Rest接口
 * @author tangwei
 *
 */
@RestApi(value="restUserService", restServer="aclRestServer")
public interface RestUserService {
    @RestMapping(value="user/querybyroletype/{roleType}.html",method= RequestMethod.GET)
    List<User>queryByRoleType(@UrlParam("roleType") String roleType);
    
    @RestMapping(value = "user/queryByLoginId/{loginId}.html", method = RequestMethod.GET)
    User queryByLoginId(@UrlParam("loginId") String loginId);

    @RestMapping(value = "user/queryById/{id}.html", method = RequestMethod.GET)
    RestUser queryById(@UrlParam("id") Long id);
}
