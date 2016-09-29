package com.prcsteel.platform.order.model.enums;

import com.prcsteel.platform.account.model.enums.AccountType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Rabbit Mao on 2015/9/9.
 */
public enum ConsignOrderStatusForSearch {
    ALL("all", "全部"),
    NEW("1", "待审核"),   //1
    NEWAPPROVED("2", "待关联"),  //2
    TOBEPAYREQUEST("3", "待申请付款"),
    PAYREQUEST("4", "待审核付款"),
    COMFIRMEDPAY("5", "待确认已付款"), //已打印付款申请
    TOBEPICKUP("6", "待提货"),   //4
    TOBEDELINERY("7", "待放货"),  //4
    SECONDSETTLE("8", "待二次结算"),
    INVOICEREQUEST("9", "待开票"),  //7,8
    FINISH("10", "交易完成"),
    CLOSEREQUEST("11", "交易关闭待审核"),  //3,5,-5,-6
    CLOSED("12", "交易关闭"),  //-1,-2,-3,-4,-7
    TOBEPRINTPAYREQUEST("13", "待打印付款申请"),//已通过审核 待打印
	TOBEINVOICEREQUEST("14", "待开票申请");
	
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

    ConsignOrderStatusForSearch(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        Optional<ConsignOrderStatus> res = Stream.of(ConsignOrderStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    public static List<ConsignOrderStatusForSearch> getOrderStatusList(AccountType type){
        List<ConsignOrderStatusForSearch> list = new ArrayList<ConsignOrderStatusForSearch>();
        list.addAll(Arrays.asList(ALL, NEW, NEWAPPROVED, TOBEPICKUP, TOBEDELINERY, SECONDSETTLE, INVOICEREQUEST, FINISH, CLOSEREQUEST, CLOSED));
        if(type.equals(AccountType.seller)){
            list.add(3, TOBEPAYREQUEST);
            list.add(4, PAYREQUEST);
            list.add(5,TOBEPRINTPAYREQUEST);
            list.add(6, COMFIRMEDPAY);
        }
        return list;
    }
}
