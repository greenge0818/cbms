package com.prcsteel.platform.order.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zhoukun
 */
public enum InvoiceInStatus {
    AWAITS("AWAITS", "待收票"),          // 待收票
    RECEIVED("RECEIVED", "待寄出"),        // 待寄出
    SENT("SENT", "待确认"),            // 待确认
    //CONFIRMED,       // 已确认
    WAIT("WAIT", "已确认"),             // 待认证
    ALREADY("ALREADY", "已认证"),         // 已认证
    CANCEL("CANCEL", "已作废") ,            // 已作废
    WARN("WARN", "发票预警"),             // 发票预警
    ROLLBACK("ROLLBACK", "打回待关联") //增加流水操作打回
    ;
    private String code;
    private String name;

    InvoiceInStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

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

    public static String getName(String code) {
        Optional<InvoiceInStatus> res = Stream.of(InvoiceInStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
    
    /**
     * 获取开销项票时，可以统计为已收进项金额的进项票状态
     * @return
     */
    public static List<InvoiceInStatus> getEffectiveStatus(){
    	return Arrays.asList(WAIT,ALREADY);
    }
    
    /**
     * 获取已收但未认证的进项票状态
     * @return
     */
    public static List<InvoiceInStatus> getReceivedStatus(){
    	return Arrays.asList(WAIT,RECEIVED,SENT);
    }
}
