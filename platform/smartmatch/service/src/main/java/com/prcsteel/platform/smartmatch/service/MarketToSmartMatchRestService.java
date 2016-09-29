package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.smartmatch.model.dto.ResourceDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.model.query.ResourceQuery;

import java.util.List;

/**
 * 超市调用找货 Rest 服务接口 service
 *
 * @author peanut
 * @date 2016/6/17 17:33
 */
public interface MarketToSmartMatchRestService {

    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIds 报价单id集
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<QuotationOrderItems> selectQuotationItemsByQuotationIds(List<Long> quotationIds);


    /**
     * 根据查询条件查询钢为产品资源库
     *
     * @param resourceQuery 查询对象
     * @return
     * @author peanut
     * @date 2016/6/17
     * @see ResourceQuery
     */
    List<ResourceDto> selectResourceByQuery(ResourceQuery resourceQuery);


    /**
     * 查询所有大类和品名及规格 ----可考虑用memcache缓存 + 数据库检索
     *
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<Object> selectAllNsortAndCategoryAndSpec();


    /**
     * 根据品名uuid 获取对应品名下的钢厂
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<Object> selectFactoryByCategoryUuid(String categoryUuid);

    /**
     * 根据品名uuid 获取对应品名下的材质
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<Object> selectMaterialsByCategoryUuid(String categoryUuid);

    /**
     * 根据品名uuid和材质uuid 获取对应的规格
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<Object> selectSpecByCategoryUuidAndMaterialsUuid(String categoryUuid, String materialsUuid);

    /**
     * 根据地区分组获取所有中心城市
     *
     * @param
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestCitysDto selectAllCenterCityGroupByZone();

    /**
     * 根据城市名称获取该城市映射的中心城市集
     *
     * @param cityName 城市名称
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    List<Object> selectCenterCitysByCityName(String cityName);


    /**
     *  获取指定城市的指定条件之外的N条随机资源
     *
     * @param
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    //List<ResourceDto> selectRandomResourceByCityNameAnd
}
