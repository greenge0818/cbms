package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.persist.dao.ErrAccountEditDao;
import com.prcsteel.platform.account.service.ErrAccountEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("errAccountEditService")
public class ErrAccountEditServiceImpl implements ErrAccountEditService{


    @Autowired
    private ErrAccountEditDao errAccountEditDao;
    /**
     * @param errName
     * @param correctName
     * @return
     */
    public Integer updateAccountName(String errName, String correctName) {
        return errAccountEditDao.updateAccountName(errName,correctName);
    }
}
