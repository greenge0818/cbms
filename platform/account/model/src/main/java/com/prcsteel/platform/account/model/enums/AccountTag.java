package com.prcsteel.platform.account.model.enums;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 客户tag，取代原type,consign_type（买家，卖家...）
 * Created by lichaowei on 2016/1/15.
 */
public enum AccountTag {
    buyer(1L,"买家"),
    seller(2L,"卖家"),
    temp(4L,"临采"),
    consign(8L,"代运营"),
    warehouse(16L,"仓库"),
    transport(32L,"运输"),
    machining(64L,"加工");

    private Long code;
    private String name;

    AccountTag(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<AccountTag> getNameByCode(Long code) {
        return Stream.of(AccountTag.values()).filter(a -> (a.getCode() & code) == a.getCode()).collect(Collectors.toList());
    }

    public static AccountTag getSingleTagByCode(Long code){
        Optional<AccountTag> res = Stream.of(AccountTag.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get() : null;
    }
}
