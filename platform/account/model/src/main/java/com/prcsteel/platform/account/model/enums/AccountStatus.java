package com.prcsteel.platform.account.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 账号、联系人状态
 * Created by lichaowei on 2015/11/4.
 */

public enum AccountStatus {
    LOCKED(0,"锁定"),         //锁定
    NORMAL(1,"正常");         //正常

    private Integer code;
    private String name;

    AccountStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static String getNameByCode(Integer code) {
		Optional<AccountStatus> res = Stream.of(AccountStatus.values()).filter(a -> a.getCode() == code).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}

}