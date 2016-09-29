package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.smartmatch.model.model.RestOrganization;

import java.util.List;

/**
 * Created by caochao on 2016/9/8.
 */
public interface AdvanceOrganizationService {
    RestOrganization queryById(Long id);

    List<Organization> queryAllBusinessOrg();
}
