package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by caochao on 2016/6/22.
 */
public interface ResourceChangeForMaketService {

    void send(Object o,User operator);
}
