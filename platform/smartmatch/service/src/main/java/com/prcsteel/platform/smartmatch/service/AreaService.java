package com.prcsteel.platform.smartmatch.service;


import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.Area;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;

import java.util.List;
import java.util.Map;

/**
 * Create by  peanut  on 2015-11-13
 */
public interface AreaService {

    int deleteByPrimaryKey(Long id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    /**
     * 查询
     *
     * @param areaQuery 查询参数
     * @return
     */
    List<AreaCityDto> query(AreaQuery areaQuery);

    /**
     * 查询总数
     *
     * @param areaQuery 查询参数
     * @return
     */
    int queryTotal(AreaQuery areaQuery);

    /**
     * 根据周边城市数字id集取城市汉字集
     *
     * @param ids 周边城市数字id集 例: [1,2,3,4,5]
     * @return 城市汉字集 例：长沙,杭州,上海
     */
    String getRefCityNames(List<Long> ids);

    Integer add(Area area);

    /**
     * 根据城市id集查询城市
     *
     * @param ids 城市id集
     * @return 城市集
     */
    List<City> selectCityByIds(List<Long> ids);

    /**
     * 根据中心城市获得周边城市（区域id之外的数据）
     *
     * @param centerCityId 中心城市 id
     * @param id           区域 id
     * @return
     */
    AreaCityDto findByCenterCityIdBesidesId(Long centerCityId, Long id);

    /**
     * 根据周边城市获取中心城市
     *
     * @param cityId
     * @return
     */
    List<Area> findCenterCityByRefCityId(Long cityId);

    /**
     * 根据城市id获取城市 （周边获取中心，中心获取周边）
     *
     * @param cityId 城市id
     * @return
     */
    Map<String, Object> getCitysById(Long cityId);

    /**
     * 把区域城市列表数据中的周边城市(数字形式:1,2,3,4)转换成中文形式(北京,上海...)
     *
     * @param list
     * @return
     */
    List<AreaCityDto> getCnCityNames(List<AreaCityDto> list);

    /**
     * 获取所有中心城市列表
     *
     * @return
     */
    List<City> queryAllCenterCity();

    /**
     * 根据地区分组获取所有中心城市
     *
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    RestCitysDto selectAllCenterCityGroupByZone();

    /**
     * 根据城市名称获取所有中心城市
     *
     * @param cityName 城市名称
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    RestBaseDto selectCenterCitysByCityName(String cityName);
}
