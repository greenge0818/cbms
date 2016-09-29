package com.prcsteel.platform.account.model.enums;

import java.util.*;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 
 * @author Green.Ge
 * 开票资料状态
 */
public enum InvoiceDataStatus {
	Approved("1","通过"),//审核通过
	Requested("2","待审核"),
	Insufficient("3","未上传"),
	Declined("4","审核不通过"),
	Nosubmit("5","待提交");//资料不足就是待提交 ，在发票预警里需要把资料不足的状态展示为待提交

	// 成员变量
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

	private String code;

	// 构造方法
	InvoiceDataStatus(String code, String name) {
		this.name = name;
		this.code = code;
	}

	public static String getNameByCode(String code) {
		Optional<InvoiceDataStatus> res = Stream.of(InvoiceDataStatus.values()).filter(a -> a.getCode().equals(code)).findFirst();
		return res.isPresent() ? res.get().getName() : "";
	}

	public static List<Map<String,String>> getList(){
		List<Map<String,String>> list = new ArrayList();
		List<InvoiceDataStatus> sourceList = Arrays.asList(Insufficient, Requested, Declined, Approved);
		Map<String,String> map ;
		for(InvoiceDataStatus status:sourceList){
			map = new HashMap<>();
			map.put("code",status.getCode());
			map.put("name",status.getName());
			list.add(map);
		}
		return list;
	}
}
