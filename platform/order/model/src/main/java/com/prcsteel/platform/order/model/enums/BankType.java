package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author kongbinheng
 */
public enum BankType {

    SPDB("SPDB", "浦发银行"),
    ICBC("ICBC", "工商银行");

    private String code;
    private String name;

    BankType(String code, String name) {
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
        Optional<BankType> res = Stream.of(BankType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

}
