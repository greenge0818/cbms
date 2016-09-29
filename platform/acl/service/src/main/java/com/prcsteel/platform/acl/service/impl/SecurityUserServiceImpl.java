package com.prcsteel.platform.acl.service.impl;

import com.prcsteel.platform.acl.model.model.SecurityUser;
import com.prcsteel.platform.acl.persist.dao.SecurityUserDao;
import com.prcsteel.platform.acl.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rolyer on 15-6-25.
 */
@Service("securityUserService")
public class SecurityUserServiceImpl implements SecurityUserService {
    @Autowired
    private SecurityUserDao securityUserDao;

    public SecurityUser queryByUserName(String username) {
        return securityUserDao.queryByUserName(username);
    }
}
