package com.prcsteel.platform.smartmatch.service;

import com.prcsteel.platform.smartmatch.model.dto.RequirementStatusDto;
import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.dto.RestCitysDto;
import com.prcsteel.platform.smartmatch.model.query.RestHotResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestNormalResourceQuery;

/**
 * 超市调用找货 Rest 服务接口 service
 *
 * @author peanut
 * @date 2016/6/17 17:33
 */
public interface RestMarketToSmartMatchService {

    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIds 报价单id集
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestBaseDto selectQuotationItemsByQuotationIds(String quotationIds);


    /**
     * 根据查询条件查询钢为产品资源库
     *
     * @param restNormalResourceQuery 查询对象
     * @return
     * @author peanut
     * @date 2016/6/17
     * @see RestNormalResourceQuery
     */
    RestBaseDto selectNormalResource(RestNormalResourceQuery restNormalResourceQuery);


    /**
     * 查询所有大类和品名及规格
     *
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestBaseDto selectAllNsortAndCategoryAndSpec();


    /**
     * 根据品名uuid 获取对应品名下的钢厂
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestBaseDto selectFactoryByCategoryUuid(String categoryUuid);

    /**
     * 根据品名uuid 获取对应品名下的材质
     *
     * @param categoryUuid 品名uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestBaseDto selectMaterialsByCategoryUuid(String categoryUuid);

    /**
     * 根据品名uuid和材质uuid 获取对应的规格
     *
     * @param categoryUuid  品名uuid
     * @param materialsUuid 材质uuid
     * @return
     * @author peanut
     * @date 2016/6/17
     */
    RestBaseDto selectSpecByCategoryUuidAndMaterialsUuid(String categoryUuid, String materialsUuid);

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
    RestBaseDto selectCenterCitysByCityName(String cityName);


    /**
     * 获取指定城市的指定条件之外的N条随机资源
     *
     * @param restHotResourceQuery 查询对象
     * @return
     * @author peanut
     * @date 2016/6/17
     * @see RestHotResourceQuery
     */
    RestBaseDto selectRandomHostResource(RestHotResourceQuery restHotResourceQuery);


    /**
     * 根据需求单状态Dto更新超市系统内的需求单号状态
     *
     * @param requirementStatusDto 需求单状态Dto
     * @return
     * @author peanut
     * @date 2016/6/21
     * @see RequirementStatusDto
     */
    void updateRequirementStatus(RequirementStatusDto requirementStatusDto);


    /**
     * 查询所有品名对应的材质信息
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    RestBaseDto selectAllCategoryMaterials();
}
