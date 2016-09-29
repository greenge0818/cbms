package com.prcsteel.platform.smartmatch.service.impl;

import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;
import com.prcsteel.platform.smartmatch.persist.dao.AreaDao;
import com.prcsteel.platform.smartmatch.service.MarketToSmartMatchRestService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 超市调用找货 Rest 服务接口 service
 *
 * @author peanut
 * @date 2016/6/17 17:34
 */
@Service
public class MarketToSmartMatchRestServiceImpl implements MarketToSmartMatchRestService {

    //日志
    private static final Logger log = LoggerFactory.getLogger(MarketToSmartMatchRestServiceImpl.class);

    @Resource
    private AreaDao areaDao;

    @Override
    public List<QuotationOrderItems> selectQuotationItemsByQuotationIds(List<Long> quotationIds) {
        return null;
    }

    @Override
    public List<ResourceDto> selectResourceByQuery(ResourceQuery resourceQuery) {
        return null;
    }

    @Override
    public List<Object> selectAllNsortAndCategoryAndSpec() {
        return null;
    }

    @Override
    public List<Object> selectFactoryByCategoryUuid(String categoryUuid) {
        return null;
    }

    @Override
    public List<Object> selectMaterialsByCategoryUuid(String categoryUuid) {
        return null;
    }

    @Override
    public List<Object> selectSpecByCategoryUuidAndMaterialsUuid(String categoryUuid, String materialsUuid) {
        return null;
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

    @Override
    public List<Object> selectCenterCitysByCityName(String cityName) {
        return null;
    }
}
