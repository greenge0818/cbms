package com.prcsteel.platform.core.service;

import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.SysFeedback;
import com.prcsteel.platform.core.model.dto.CityDto;
import com.prcsteel.platform.core.model.dto.DistrictDto;
import com.prcsteel.platform.core.model.dto.ProvinceCityRelationDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.District;
import com.prcsteel.platform.core.model.model.Province;

import java.util.List;

/**
 * @Title: CommonService.java
 * @Package com.prcsteel.platform.order.service.impl
 * @Description: 通用Service
 * @author Green.Ge
 * @date 2015年7月14日 下午1:26:07
 * @version V1.0
 */
public interface CommonService {
	List<Province> getPrivinceList();

	List<City> getCityListByProvinceId(Long provinceId);

	List<District> getDistrictListByCityId(Long cityId);

	List<Organization> getAllOrganization();

	DistrictDto findDistrictById(Long id);

	CityDto findCityById(Long id);

	/**
	 * 发送短信
	 *
	 * @param phone   手机号码
	 * @param content 发送内容
	 * @return
	 */
	Boolean sendSMS(String phone, String content);

	//Green.Ge保存用户反馈
	boolean saveFeedBack(SysFeedback sysFeedback);

	/**
	 * 获取所有城市
	 *
	 * @return
	 */
	List<City> allCity();

	/**
	 * @description获取所有城市-省关系
	 * @author：zhoucai
	 * @date ：2016-6-23
	 * @return
	 */
	List<ProvinceCityRelationDto> getProvinceCityRelation();
}
 