package com.prcsteel.platform.account.model.dto;

import com.prcsteel.platform.account.model.model.Account;

/**
 * Created by lcw on 2015/7/16.
 */
public class SellerAccountDto extends Account{

    private String manager;

    private String managerTel;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManagerTel() {
        return managerTel;
    }

    public void setManagerTel(String managerTel) {
        this.managerTel = managerTel;
    }

}
