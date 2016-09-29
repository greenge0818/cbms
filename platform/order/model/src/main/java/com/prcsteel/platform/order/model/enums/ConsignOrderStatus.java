package com.prcsteel.platform.order.model.enums;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author zhoukun
 */
public enum ConsignOrderStatus {
    NEW("1", "新建（待审核） 待审核"),
    NEWDECLINED("-1", "订单关闭-审核不通过"),
    NEWAPPROVED("2", "审核通过(待关联) 待关联"),
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

    public static List<String> getStatusByCode(String code) {
        List<String> statusList = new LinkedList<>();
        if (code.equals(ConsignOrderStatusForSearch.NEW.getCode())) {    //待审核
            statusList.add(NEW.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.NEWAPPROVED.getCode())) {
            statusList.add(NEWAPPROVED.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.TOBEPAYREQUEST.getCode())
                || code.equals(ConsignOrderStatusForSearch.PAYREQUEST.getCode())
                || code.equals(ConsignOrderStatusForSearch.COMFIRMEDPAY.getCode())
                || code.equals(ConsignOrderStatusForSearch.TOBEPICKUP.getCode())) {
            statusList.add(RELATED.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.TOBEDELINERY.getCode())) {
            statusList.add(RELATED.getCode());
            statusList.add(SECONDSETTLE.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.SECONDSETTLE.getCode())) {
            statusList.add(SECONDSETTLE.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.INVOICEREQUEST.getCode())) {
            statusList.add(INVOICE.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.FINISH.getCode())) {
            statusList.add(FINISH.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.CLOSEREQUEST.getCode())) {
            statusList.add(CLOSEREQUEST1.getCode());
            statusList.add(CLOSEREQUEST2.getCode());
            statusList.add(CLOSESECONDARYREQUEST.getCode());
            statusList.add(CLOSESECONDARYAPPROVED.getCode());
        } else if (code.equals(ConsignOrderStatusForSearch.CLOSED.getCode())) {
            statusList.add(SYSCLOSED.getCode());
            statusList.add(NEWDECLINED.getCode());
            statusList.add(CLOSEAPPROVED.getCode());
            statusList.add(CLOSEPAY.getCode());
            statusList.add(CLOSESECONDARY.getCode());
            statusList.add(CLOSEPAYED.getCode());
        } else {
            statusList = null;
        }
        return statusList;
    }

    /**
     * 获取关联订单后的状态（不包括订单关闭状态）
     * @return
     */
    public static List<String> getReletedOrderStatus(){
        //4、5、6、7、8、10、-5、-6
        return Arrays.asList(RELATED.getCode(),CLOSEREQUEST2.getCode(),SECONDSETTLE.getCode(),INVOICEREQUEST.getCode(),
                        INVOICE.getCode(),FINISH.getCode(),CLOSESECONDARYREQUEST.getCode(),CLOSESECONDARYAPPROVED.getCode());


    }

    /**
     * 获取二次结算后的状态（不包括订单关闭状态）
     * @return
     */
    public static List<String> getSecondOrderStatus(){
        //7、8、10、-5、-6
        return Arrays.asList(INVOICEREQUEST.getCode(),
                INVOICE.getCode(),FINISH.getCode(),CLOSESECONDARYREQUEST.getCode(),CLOSESECONDARYAPPROVED.getCode());
    }
}
