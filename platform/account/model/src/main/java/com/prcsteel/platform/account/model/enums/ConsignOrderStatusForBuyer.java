package com.prcsteel.platform.account.model.enums;

import com.prcsteel.platform.account.model.enums.AccountType;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by caochao on 2016/04/29
 */
public enum ConsignOrderStatusForBuyer {
    NEW("1", "待审核"),   //1
    NEWAPPROVED("2", "待关联"),  //2
    TOBEPICKUP("6", "待提货"),   //4
    TOBEDELINERY("7", "待放货"),  //4
    SECONDSETTLE("8", "待二次结算"),
    TOBEINVOICEREQUEST("14", "待开票申请"),
    INVOICEREQUEST("9", "待开票"),  //7,8
    FINISH("10", "交易完成"),
    CLOSEREQUEST("11", "交易关闭待审核"),  //3,5,-5,-6
    CLOSED("12", "交易关闭"); //-1,-2,-3,-4,-7,-8

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ConsignOrderStatusForBuyer(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        Optional<ConsignOrderStatusForBuyer> res = Stream.of(ConsignOrderStatusForBuyer.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    /**
     * 获取状态和说明列表
     * @return
     */
    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList();
        Map<String, String> map;
        ConsignOrderStatusForBuyer[] array = ConsignOrderStatusForBuyer.values();
        for (ConsignOrderStatusForBuyer status : array) {
            map = new HashMap<>();
            map.put("code", status.getCode());
            map.put("name", status.getName());
            list.add(map);
        }
        return list;
    }

}
