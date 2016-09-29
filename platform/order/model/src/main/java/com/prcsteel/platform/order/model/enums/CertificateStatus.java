package com.prcsteel.platform.order.model.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author peanut
 * @version 1.0
 * @description  凭证状态
 * @date 2016/4/12 14:06
 */
public enum CertificateStatus {
    PENDING_PRINT("PENDING_PRINT","待打印"), //待打印
    PENDING_UPLOAD("PENDING_UPLOAD","待上传"),//待上传
    PENDING_SUBMIT("PENDING_SUBMIT","待提交"),// 待提交
    PENDING_APPROVAL("PENDING_APPROVAL","待审核"),// 待审核
    APPROVED("APPROVED","审核通过"),   // 审核通过
    DISAPPROVE ("DISAPPROVE","审核不通过"); // 审核不通过

    private String code;
    private String name;
    CertificateStatus(String code,String name){
        this.code=code;
        this.name=name;
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

    /**
     * 根据code,获取名称
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        Optional<CertificateStatus> optional=Arrays.asList(CertificateStatus.values()).stream().filter(e->code.equals(e.getCode())).findFirst();
        return optional==null?"":optional.get().getName();
    }
}
