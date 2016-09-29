package com.prcsteel.platform.acl.persist.dao;

import com.prcsteel.platform.acl.model.model.SecurityUser;

/**
 * Created by rolyer on 15-6-25.
 */
public interface SecurityUserDao {
    SecurityUser queryByUserName(String username);
}
