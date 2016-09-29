package com.prcsteel.platform.order.model.enums;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by lichaowei on 2015/11/11.
 */
public enum AcceptDraftAttachmentType {
    MAIN("MAIN", "正面"),
    TRANSFERLOG("TRANSFERLOG", "粘单");

    // 成员变量
    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // 构造方法
    AcceptDraftAttachmentType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<AcceptDraftAttachmentType> res = Stream.of(AcceptDraftAttachmentType.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

}
