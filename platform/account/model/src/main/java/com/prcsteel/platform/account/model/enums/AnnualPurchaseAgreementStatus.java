package com.prcsteel.platform.account.model.enums;



import java.util.*;
import java.util.stream.Stream;

/**
 * Created by dengxiyan on 2016/3/3.
 * 年度采购协议状态
 */
public enum AnnualPurchaseAgreementStatus {
    ToPrint("ToPrint","待打印"),
    Requested("Requested","待审核"),
    FirstApproved("FirstApproved","一审通过"),
    SecondApproved("SecondApproved","二审通过"),
    Uploaded("Uploaded","已上传待审核"),
    Approved("Approved","审核通过"),
    Declined("Declined","审核未通过"),//已上传待审核后的审核未通过
    FirstDeclined("FirstDeclined", "一审未通过"),
    SecondDeclined("SecondDeclined", "二审未通过");

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
    AnnualPurchaseAgreementStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        Optional<AnnualPurchaseAgreementStatus> res = Stream.of(AnnualPurchaseAgreementStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    public static List<Map<String,String>> getList(){
        List<Map<String,String>> list = new ArrayList();
        Map<String,String> map ;
        AnnualPurchaseAgreementStatus[] array = AnnualPurchaseAgreementStatus.values();
        for(AnnualPurchaseAgreementStatus status:array){
            map = new HashMap<>();
            map.put("code",status.getCode());
            map.put("name",status.getName());
            list.add(map);
        }
        return list;
    }
}
