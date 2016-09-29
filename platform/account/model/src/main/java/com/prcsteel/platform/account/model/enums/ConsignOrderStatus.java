package com.prcsteel.platform.account.model.enums;

import java.util.*;
import java.util.stream.Stream;

/**
 * author: zhoucai@prcsteel.com
 * date:2016-3-23
 * vision :1.0
 * description:寄售订单，订单状态
 */
public enum ConsignOrderStatus {
    NEW("1", "新建（待审核)"),
    NEWDECLINED("-1", "订单关闭-审核不通过"),
    NEWAPPROVED("2", "审核通过(待关联)"),
    CLOSEREQUEST1("3", "请求关闭1"),
    CLOSEAPPROVED("-2", "订单关闭-请求通过"), //订单关闭-请求1通过、订单关闭-请求2通过
    RELATED("4", "已关联"),
    CLOSEREQUEST2("5", "请求关闭2"),
    SECONDSETTLE("6", "待二次结算"),
    INVOICEREQUEST("7", "待开票申请"),
    INVOICE("8", "待开票"),
    FINISH("10", "交易完成"),
    SYSCLOSED("-3", "订单关闭-5点半未关联（待关联）订单"),
	CLOSEPAY("-4", "订单关闭-确认付款关闭"),
    CLOSESECONDARYREQUEST("-5", "申请关闭订单"),
    CLOSESECONDARYAPPROVED("-6", "总经理审核通过关闭订单"),
    CLOSESECONDARY("-7", "订单关闭-财务审核通过关闭订单"),
    CLOSEPAYED("-8", "订单关闭-财务审核通过付款后关闭订单");

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

    ConsignOrderStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
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
        ConsignOrderStatus[] array = ConsignOrderStatus.values();
        for (ConsignOrderStatus status : array) {
            map = new HashMap<>();
            map.put("code", status.getCode());
            map.put("name", status.getName());
            list.add(map);
        }
        return list;
    }
}
