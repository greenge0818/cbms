package com.prcsteel.platform.smartmatch.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.Area;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.smartmatch.persist.dao.AreaDao;
import com.prcsteel.platform.smartmatch.service.AreaService;
/**
 *  Create by peanut
 *
 */
@Service("areaService")
public class AreaServiceImpl implements AreaService {
	@Resource
	private AreaDao areaDao;
	
	@Resource 
	private CityDao cityDao;


	//日志
	private static final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

	@Override
	public int deleteByPrimaryKey(Long id) {
		return areaDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Area record) {
		return areaDao.insert(record);
	}

	@Override
	public int insertSelective(Area record) {
		return areaDao.insertSelective(record);
	}

	@Override
	public Area selectByPrimaryKey(Long id) {
		return areaDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Area record) {
		return areaDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Area record) {
		return areaDao.updateByPrimaryKey(record);
	}

	@Override
	public List<AreaCityDto> query(AreaQuery areaQuery) {
		return areaDao.query(areaQuery);
	}
	
	@Override
	public int queryTotal(AreaQuery areaQuery) {
		return areaDao.queryTotal(areaQuery);
	}

	@Override
	public String getRefCityNames(List<Long> ids) {
		return areaDao.getRefCityNames(ids);
	}

	@Override
	public Integer add(Area Area) {
		return areaDao.insertSelective(Area);
	}

	@Override
	public List<City> selectCityByIds(List<Long> ids) {
		return cityDao.selectCityByIds(ids);
	}
	
	@Override
	public AreaCityDto findByCenterCityIdBesidesId(Long centerCityId,Long id) {
		return areaDao.findByCenterCityIdBesidesId(centerCityId,id);
	}

	@Override
	public List<Area> findCenterCityByRefCityId(Long cityId) {
		return areaDao.findCenterCityByRefCityId(cityId);
	}
    
	/**
	 * 根据城市id获取城市 （周边获取中心，中心获取周边）
	 * 
	 * <p> 根据cityId判断该城市是否是中心城市 
	 *<blockquote>如果是中心城市，查询出其周边城市中是中心城市的城市数据</blockquote>
	 *<blockquote>如果不是中心城市(即周边城市)，查询出其隶属的所有中心城市的城市数据</blockquote>
	 *<p>其他城市数据：所有中心城市中排除上述所列的中心城市</p>
	 * @param cityId   城市id
	 * @return
	 */
	@Override
	public Map<String,Object> getCitysById(Long cityId) {
		if(cityId ==null ){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "城市未正确选择!");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		List<AreaCityDto> list=null;
		AreaCityDto areaCity=areaDao.findByCenterCityIdBesidesId(cityId,null);
		//中心城市
		if(areaCity!=null){
			map.put("isCenter", true);
			//周边城市id集
			String refsCityIds=areaCity.getRefCityIds();
			if(!StringUtils.isEmpty(refsCityIds)){
				 list=areaDao.findByCenterIds(Arrays.asList(refsCityIds.split(",")).stream().map(e->Long.parseLong(e)).collect(Collectors.toList()));
			}
		}else{//周边城市
			map.put("isCenter", false);
			List<Area> areaList=areaDao.findCenterCityByRefCityId(cityId);
			if(areaList!=null && !areaList.isEmpty()){
				list=areaDao.findByCenterIds(areaList.stream().map(e->e.getCenterCityId()).collect(Collectors.toList()));
			}
		}
		/**
		 * 其他城市数据处理
		 */
		
		//所有中心城市
		List<City> allCenterCityList=areaDao.queryAllCenterCity();
		if(list!=null){
			//已询的周边城市中是中心城市id集
			List<Long> centerIds=list.stream().map(t->t.getCenterCityId()).collect(Collectors.toList());
			centerIds.add(cityId);
			//过滤
 			allCenterCityList=allCenterCityList.stream().filter(e->!centerIds.contains(e.getId())).collect(Collectors.toList());
		}
		
		map.put("city", list);
		map.put("restCity", allCenterCityList);
		
		return map;
	}
	//把周边城市以逗号分隔的数字转化为汉字
	@Override
	public List<AreaCityDto> getCnCityNames(List<AreaCityDto> list) {
		List<AreaCityDto> resultList = new ArrayList<AreaCityDto>();
	     if(list !=null && list.size()>0){
	    	 for(AreaCityDto area :list){
	    		//周边城市id集
	    		 String ids=area.getRefCityIds();
	    		 if(StringUtils.isNotEmpty(ids)){
	    			List<Long> longIds=Arrays.asList(ids.split(",")).stream().map(s->Long.parseLong(s)).collect(Collectors.toList());
	    			String refCityNames= getRefCityNames(longIds);
	    			area.setRefCityNames(refCityNames);
	    		 }else{
	    			area.setRefCityNames("");
	    		 }
	    		 resultList.add(area);
	    	 }
	     }
		return resultList;
	}

	@Override
	public List<City> queryAllCenterCity() {
		return areaDao.queryAllCenterCity();
	}


	/**
	 * 根据地区分组获取所有中心城市
	 *
	 * @return
	 * @author peanut
	 * @date 2016/6/20
	 */
	@Override
	public RestCitysDto selectAllCenterCityGroupByZone() {

		RestCitysDto citysDto = new RestCitysDto();

		List<AreaCityDto> list = areaDao.selectAllCenterCityGroupByZone();
		if (list != null && !list.isEmpty()) {
			//地区列表
			List<RestCitysDto.Area> alist = new ArrayList<>();
			for (AreaCityDto acDto : list) {
				//城市
				String citys = acDto.getCitys();

				//*citys的数据结构：1:长沙地区,2:武汉地区,3:郑州地区*//*
				if (StringUtils.isNotBlank(citys)) {
					RestCitysDto.Area area = new RestCitysDto.Area();
					String[] arrayCitys = citys.split(",");
					if (arrayCitys != null && arrayCitys.length > 0) {
						List<RestCitysDto.Area.City> clist = new ArrayList<>();
						for (String c : arrayCitys) {
							String[] arrayC = c.split(":");
							if (arrayC != null && arrayC.length > 0) {
								RestCitysDto.Area.City city = new RestCitysDto.Area.City();
								city.setId(NumberUtils.isNumber(arrayC[0]) ? Long.parseLong(arrayC[0]) : null);
								city.setName(arrayC[1]);
								clist.add(city);
							}
						}
						area.setCitys(clist);
					}
					area.setAreaName(acDto.getZoneName());
					alist.add(area);
				}
			}
			citysDto.setData(alist);
			//正确的返回码
			citysDto.setStatus("0");
		} else {
			log.error("城市信息为空!");
			citysDto.setStatus("-1");
		}

		return citysDto;
	}

	/**
	 * 根据城市名称获取该城市映射的中心城市集
	 *
	 * @param cityName 城市名称
	 * @return
	 * @author peanut
	 * @date 2016/6/21
	 */
	@Override
	public RestBaseDto selectCenterCitysByCityName(String cityName) {
		if(StringUtils.isBlank(cityName)){
			log.error("城市名称为空!");
			return new RestBaseDto("-1");
		}
		List<Map<String,String>> list=areaDao.selectCenterCitysByName(cityName);
		if(list ==null || list.isEmpty()){
			log.error("城市名称为空!");
			return new RestBaseDto("-1");
		}
		RestBaseDto restBaseDto=new RestBaseDto();
		restBaseDto.setStatus("0");
		restBaseDto.setData(list);
		return restBaseDto;
	}
}
