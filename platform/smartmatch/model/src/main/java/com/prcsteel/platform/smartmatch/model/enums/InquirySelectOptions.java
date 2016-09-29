package com.prcsteel.platform.smartmatch.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Rabbit
 */
public enum InquirySelectOptions {
    LEASTAMOUNT("LEASTAMOUNT", "总价最低"),          // 总价最低
    LEASTSELLER("LEASTSELLER", "卖家数最少"),        // 卖家数最少
    LEASTWAREHOUSE("LEASTWAREHOUSE", "仓库最少");    // 仓库最少
    private String code;
    private String name;

    InquirySelectOptions(String code, String name) {
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
        Optional<InquirySelectOptions> res = Stream.of(InquirySelectOptions.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }
}
