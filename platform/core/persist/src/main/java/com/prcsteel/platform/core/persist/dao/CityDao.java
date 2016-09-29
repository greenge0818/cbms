package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.dto.ProvinceCityRelationDto;
import com.prcsteel.platform.core.model.model.City;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

public interface CityDao {
    @Cacheable(value = Constant.CACHE_NAME, key="'" + Constant.CACHE_CITY_LIST_BY_PROVINCE_ID +"' + #p0")
    List<City> listByProvinceId(Long provinceId);

    @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_CITY_SELECT_BY_PRIMARYKEY + "' + #p0")
    City selectByPrimaryKey(Long id);
    
    /**
	 * 根据城市id集查询出城市集
	 * @param ids  城市id集
	 * @return     城市集
	 */
	List<City> selectCityByIds(@Param("ids") List<Long> ids);  //add by peanut on 2015-11-16
	
	/**
     * 获取所有城市信息作页面缓存   
     * @return
     */
	 @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_CITY_ALL+"'")
	List<City> getAllCity();  //add by peanut on 2015-11-17
	
	/**
	 * 通过城市名查城市id
	 * @param cityName
	 * @return
	 */
	City queryByCityName(String cityName);

	/**
	 * @description获取所有城市-省关系
	 * @author：zhoucai
	 * @date ：2016-6-23
	 * @return
	 */
	List<ProvinceCityRelationDto> getProvinceCityRelation();
}