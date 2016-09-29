package com.prcsteel.platform.account.model.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dengxiyan on 2016/3/3.
 * 原AccountTag中的枚举组合后
 * 修改用于查询条件的数据源
 */
public enum  AccountTagForSearch {
    Buyer(1L,"买家"),
    SellerConsign(10L,"卖家(代)"), // 卖家|代 即2|8=10
    SellerTemp(6L,"卖家(临)"),     // 卖家|临 即2|4=6
    Warehouse(16L,"仓库"),
    Transport(32L,"运输"),
    Machining(64L,"加工");

    private Long code;
    private String name;

    AccountTagForSearch(Long code, String name) {
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

    public static List<AccountTagForSearch> getList(){
        return Arrays.asList(AccountTagForSearch.values());
    }
}
