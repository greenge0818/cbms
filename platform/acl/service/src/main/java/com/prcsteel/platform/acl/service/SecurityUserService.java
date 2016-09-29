package com.prcsteel.platform.acl.service;

import com.prcsteel.platform.acl.model.model.SecurityUser;

/**
 * Created by rolyer on 15-6-25.
 */
public interface SecurityUserService {
    SecurityUser queryByUserName(String username);
}
