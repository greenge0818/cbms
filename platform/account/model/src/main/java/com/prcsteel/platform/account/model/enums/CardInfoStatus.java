package com.prcsteel.platform.account.model.enums;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by dengxiyan on 2016/3/3.
 * 证件资料状态
 */
public enum CardInfoStatus {

    Insufficient("Insufficient","未上传"),
    Requested("Requested","待审核"),
    Declined("Declined","审核未通过"),
    Approved("Approved","通过");

    private String code;
    private String name;

    CardInfoStatus(String code, String name) {
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

    public static String getNameByCode(String code) {
        Optional<CardInfoStatus> res = Stream.of(CardInfoStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
        return res.isPresent() ? res.get().getName() : "";
    }

    public static List<Map<String,String>> getList(){
        List<Map<String,String>> list = new ArrayList();
        List<CardInfoStatus> sourceList = Arrays.asList(Insufficient, Requested, Declined, Approved);
        Map<String,String> map ;
        for(CardInfoStatus status:sourceList){
            map = new HashMap<>();
            map.put("code",status.getCode());
            map.put("name",status.getName());
            list.add(map);
        }
        return list;
    }
}
