package com.prcsteel.platform.smartmatch.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.platform.core.model.model.CategoryMaterials;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.smartmatch.model.dto.*;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.query.RestHotResourceQuery;
import com.prcsteel.platform.smartmatch.model.query.RestNormalResourceQuery;
import com.prcsteel.platform.smartmatch.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 超市调用找货 Rest 服务接口 service
 *
 * @author peanut
 * @date 2016/6/17 17:34
 */
@Service("restMarketToSmartMatchService")
public class RestMarketToSmartMatchServiceImpl implements RestMarketToSmartMatchService {

    //日志
    private static final Logger log = LoggerFactory.getLogger(RestMarketToSmartMatchServiceImpl.class);

    @Resource
    private AreaService areaService;

    @Resource
    private QuotationOrderService quotationOrderService;

    @Resource
    private CreatePurchaseOrderService createPurchaseOrderService;

    @Resource
    private RequirementStatusService requirementStatusService;

    @Resource
    private ResourceService resourceService;

    @Resource
    private CategoryDao categoryDao;


    /**
     * 根据报价单id集，查询报价单明细
     *
     * @param quotationIds 报价单id集,例：1,2,3,4
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectQuotationItemsByQuotationIds(String quotationIds) {

        if (StringUtils.isBlank(quotationIds)) {

            log.error("报价单id集为空!");
            return new RestBaseDto("-1");
        }
        List<String> idList =Arrays.asList(quotationIds.split(",")) ;
        if (idList.size() <= 0) {
            log.error("报价单id集为空!");
            return new RestBaseDto("-1");
        }
        Optional optional = idList.stream().filter(e -> !StringUtils.isNumeric(e)).findAny();
        if (optional.isPresent()) {
            log.error("报价单id集中存在非数字!");
            return new RestBaseDto("-1");
        }

        List<Long> quotationIdList = idList.stream().map(n -> Long.parseLong(n)).collect(Collectors.toList());

        List<QuotationItemsPO> quotationItemsPOList = quotationOrderService.selectQuotationItemsByQuotationIds(quotationIdList);
        if (quotationItemsPOList == null || quotationItemsPOList.isEmpty()) {
            log.error("无报价单明细数据!");
            return new RestBaseDto("-1");
        }
        //报价单分组
        Map<String, List<QuotationItemsPO>> map = quotationItemsPOList.stream().collect(Collectors.groupingBy(e -> e.getRemoteCode()));

        List<RestQuotationItemsDto> result = new ArrayList<>();
        for (Map.Entry<String, List<QuotationItemsPO>> entry : map.entrySet()) {
            List<QuotationItemsPO> entryList = entry.getValue();
            if (entryList != null && !entryList.isEmpty()) {
                //一条报价记录
                RestQuotationItemsDto items = new RestQuotationItemsDto();
                //取第一条数据构造报价数据头
                QuotationItemsPO firstDto = entryList.get(0);
                items.setOperator(firstDto.getOperator());
                items.setMobile(firstDto.getMobile());
                items.setCity(firstDto.getCity());
                items.setRemoteCode(firstDto.getRemoteCode());

                //json转换
                List<RestQuotationItemsDto.RestQuotationItems> retList = new Gson().fromJson(new Gson().toJson(entryList),
                        new TypeToken<List<RestQuotationItemsDto.RestQuotationItems>>() {
                        }.getType());
                //报价明细
                items.setItems(retList);

                result.add(items);
            }

        }

        RestBaseDto baseDto = new RestBaseDto();
        baseDto.setData(result);
        baseDto.setStatus("0");
        return baseDto;
    }


    /**
     * 查询所有大类和品名及规格
     *
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectAllNsortAndCategoryAndSpec() {

        List<Map<String, Object>> list = createPurchaseOrderService.getSortAndNsort();
        if (list == null || list.isEmpty()) {
            log.error("大类信息为空!");
            return new RestBaseDto("-1");
        }
        RestBaseDto restBaseDto = new RestBaseDto("0");
        restBaseDto.setData(list);
        return restBaseDto;
    }

    /**
     * 根据品名uuid 获取对应品名下的钢厂
     *
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectFactoryByCategoryUuid(String categoryUuid) {
        if (StringUtils.isBlank(categoryUuid)) {
            log.error("品名uuid为空!");
            return new RestBaseDto("-1");
        }
        List<Map<String, Object>> list = createPurchaseOrderService.getFactory(categoryUuid);

        RestBaseDto restBaseDto = new RestBaseDto("0");
        restBaseDto.setData(list);
        return restBaseDto;
    }

    /**
     * 根据品名uuid 获取对应品名下的材质
     *
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectMaterialsByCategoryUuid(String categoryUuid) {
        if (StringUtils.isBlank(categoryUuid)) {
            log.error("品名uuid为空!");
            return new RestBaseDto("-1");
        }
        List<Map<String, Object>> list = createPurchaseOrderService.getMaterial(categoryUuid);

        RestBaseDto restBaseDto = new RestBaseDto("0");
        restBaseDto.setData(list);
        return restBaseDto;
    }

    /**
     * 根据品名uuid和材质uuid 获取对应的规格
     *
     * @param categoryUuid  品名uuid
     * @param materialsUuid 材质uuid
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectSpecByCategoryUuidAndMaterialsUuid(String categoryUuid, String materialsUuid) {
        if (StringUtils.isBlank(categoryUuid)) {
            log.error("品名uuid为空!");
            return new RestBaseDto("-1");
        }
        Map<String, Object> map = createPurchaseOrderService.getSpec(categoryUuid, materialsUuid);

        RestBaseDto restBaseDto = new RestBaseDto("0");
        restBaseDto.setData(map);
        return restBaseDto;
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
        return areaService.selectAllCenterCityGroupByZone();
    }

    /**
     * 根据城市名称获取所有中心城市
     *
     * @param cityName 城市名称
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public RestBaseDto selectCenterCitysByCityName(String cityName) {
        if (StringUtils.isBlank(cityName)) {
            log.error("城市名称为空!");
            return new RestBaseDto("-1");
        }
        return areaService.selectCenterCitysByCityName(cityName);
    }

    /**
     * 根据需求单状态Dto更新超市系统内的需求单号状态
     *
     * @param requirementStatusDto 需求单状态Dto
     * @return
     * @author peanut
     * @date 2016/6/21
     */
    @Override
    public void updateRequirementStatus(RequirementStatusDto requirementStatusDto) {
        if (StringUtils.isBlank(requirementStatusDto.getCode())) {
            log.error("update requirementStatus error: 需求单号为空!");
        } else if (StringUtils.isBlank(requirementStatusDto.getOperateCode())) {
            log.error("update requirementStatus error: 报价单号为空!");
        } else if (StringUtils.isBlank(requirementStatusDto.getStatusTo())) {
            log.error("update requirementStatus error: 设置状态为空!");
        } else if (requirementStatusDto.getOperated() == null) {
            log.error("update requirementStatus error: 操作时间为空!");
        } else if (PurchaseOrderStatus.CLOSED.getCode().equals(requirementStatusDto.getStatusTo())
                && StringUtils.isBlank(requirementStatusDto.getCloseReason())) {
            log.error("update requirementStatus error: 关闭理由为空!");
        } else {

            //发送消息
            requirementStatusService.send(requirementStatusDto);

        }
    }


    /**
     * 根据查询条件查询钢为产品资源库
     *
     * @param restNormalResourceQuery 查询对象
     * @return
     * @author peanut
     * @date 2016/6/22
     * @see RestNormalResourceQuery
     */
    @Override
    public RestBaseDto selectNormalResource(RestNormalResourceQuery restNormalResourceQuery) {

        if (restNormalResourceQuery == null) {
            log.error("select rest resource error: select query is null!");
            return new RestBaseDto("-1");
        }

        RestBaseDto restBaseDto = new RestBaseDto();
        restBaseDto.setStatus("0");
        restBaseDto.setData(resourceService.selectNormalResource(restNormalResourceQuery));
        return restBaseDto;
    }

    /**
     * 获取指定城市的指定条件之外的N条随机资源
     *
     * @param restHotResourceQuery 查询对象
     * @return
     * @author peanut
     * @date 2016/6/22
     * @see RestHotResourceQuery
     */
    @Override
    public RestBaseDto selectRandomHostResource(RestHotResourceQuery restHotResourceQuery) {

        if (restHotResourceQuery == null) {
            log.error("select hot resource error: select query is null!");
            return new RestBaseDto("-1");
        }

        RestBaseDto restBaseDto = new RestBaseDto();
        restBaseDto.setStatus("0");
        restBaseDto.setData(resourceService.selectRandomHostResource(restHotResourceQuery));
        return restBaseDto;
    }


    /**
     * 查询所有品名对应的材质信息
     *
     * @return
     * @author peanut
     * @date 2016/08/18
     */
    @Override
    public RestBaseDto selectAllCategoryMaterials() {
        List<CategoryMaterials> list = categoryDao.selectAllCategoryMaterials();
        if (list == null || list.isEmpty()) {
            log.error("获取品名材质信息结果为空!");
            return new RestBaseDto("-1");
        }
        RestBaseDto restBaseDto = new RestBaseDto("0");
        restBaseDto.setData(list);
        return restBaseDto;
    }
}
