package com.prcsteel.platform.kuandao.service;

import java.util.List;

import com.prcsteel.platform.kuandao.model.dto.BasesetDto;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;

public interface SettingService {

	/**
	 * 钢为收款账号查询
	 * @param start
	 * @param length
	 * @return
	 */
	List<PrcsteelAccountInfo> queryPrcsteelAccount(Integer start, Integer length);

	/**
	 * 款道参数查询
	 * @return
	 */
	BasesetDto queryBaseset();

	/**
	 * 参数修改
	 * @param email
	 * @param phonenumber
	 * @param limitbankname
	 * @param limitbankID
	 * @return
	 */
	Integer modifyBaseset(String[] email, String[] phonenumber, String[] limitbankname, String[] limitbankID, String username);
	
	
}
