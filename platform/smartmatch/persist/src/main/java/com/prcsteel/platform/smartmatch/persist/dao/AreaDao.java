package com.prcsteel.platform.smartmatch.persist.dao;

import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.Area;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Create by peanut on 2015-11-13
 */
@Repository
public interface AreaDao {
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
    String getRefCityNames(@Param("ids") List<Long> ids);

    /**
     * 根据中心城市获得周边城市（区域id之外的数据）
     *
     * @param centerCityId 中心城市 id
     * @param id           区域 id
     * @return
     */
    AreaCityDto findByCenterCityIdBesidesId(@Param("centerCityId") Long centerCityId, @Param("id") Long id);


    /**
     * 根据周边城市获取中心城市
     *
     * @param refCityId
     * @return
     */
    List<Area> findCenterCityByRefCityId(Long refCityId);

    /**
     * 获取所有中心城市列表
     *
     * @return
     */
    List<City> queryAllCenterCity();

    /**
     * 根据中心城市id集获取城市数组集
     *
     * @param ids 中心城市id集
     * @return
     */
    List<AreaCityDto> findByCenterIds(@Param("ids") List<Long> ids);

    /**
     * 根据地区分组获取所有中心城市
     *
     * @return 例: zoneName:西南       citys:  225:成都市,190:广州市
     */
    List<AreaCityDto> selectAllCenterCityGroupByZone();

    /**
     * 根据城市名称获取该城市映射的中心城市集
     *
     * @param cityName 城市名称
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    List<Map<String,String>> selectCenterCitysByName(String cityName);
}