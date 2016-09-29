package com.prcsteel.platform.account.model.enums;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by caochao on 2016/04/29
 */
public enum ConsignOrderStatusForSeller {
    NEW("1", "待审核"),   //1
    NEWAPPROVED("2", "待关联"),  //2
    TOBEPAYREQUEST("3", "待申请付款"),
    PAYREQUEST("4", "待审核付款"),
    TOBEPRINTPAYREQUEST("13", "待打印付款申请"),//已通过审核 待打印
    COMFIRMEDPAY("5", "待确认已付款"), //已打印付款申请
    TOBEPICKUP("6", "待提货"),   //4
    TOBEDELINERY("7", "待放货"),  //4
    SECONDSETTLE("8", "待二次结算"),
    INVOICEREQUEST("9", "待开票"),  //7,8
    FINISH("10", "交易完成"),
    CLOSEREQUEST("11", "交易关闭待审核"),  //3,5,-5,-6
    CLOSED("12", "交易关闭");  //-1,-2,-3,-4,-7,-8

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

    ConsignOrderStatusForSeller(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        Optional<ConsignOrderStatus> res = Stream.of(ConsignOrderStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    /**
     * 获取状态和说明列表
     * @return
     */
    public static List<Map<String, String>> getList() {
        List<Map<String, String>> list = new ArrayList();
        Map<String, String> map;
        ConsignOrderStatusForSeller[] array = ConsignOrderStatusForSeller.values();
        for (ConsignOrderStatusForSeller status : array) {
            map = new HashMap<>();
            map.put("code", status.getCode());
            map.put("name", status.getName());
            list.add(map);
        }
        return list;
    }

}
