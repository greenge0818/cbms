package com.prcsteel.platform.smartmatch.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.ConsignOrderItemsDto;
import com.prcsteel.platform.smartmatch.model.dto.ReportResourceInventoryDto;
import com.prcsteel.platform.smartmatch.model.model.AbnormalTradingDetail;
import com.prcsteel.platform.smartmatch.model.model.ReportResourceInventory;
import com.prcsteel.platform.smartmatch.model.query.DailyResourceQuery;
import com.prcsteel.platform.order.persist.dao.ConsignOrderItemsDao;
import com.prcsteel.platform.smartmatch.model.dto.CategoryGroupDto;
import com.prcsteel.platform.smartmatch.model.dto.CategoryInResourceDto;
import com.prcsteel.platform.smartmatch.persist.dao.AbnormalTradingDetailDao;
import com.prcsteel.platform.smartmatch.persist.dao.ReportResourceInventoryDao;
import com.prcsteel.platform.smartmatch.persist.dao.ResourceDao;
import com.prcsteel.platform.smartmatch.service.AreaService;
import com.prcsteel.platform.smartmatch.service.ReportResourceInventoryService;

/**
 * Created by Rolyer on 2015/11/28.
 */
@Service("reportResourceInventoryService")
public class ReportResourceInventoryServiceImpl implements ReportResourceInventoryService {



    @Resource
    private ReportResourceInventoryDao reportResourceInventoryDao;
    @Resource
    private ResourceDao resourceDao;
    @Resource
    private AreaService areaService;
    @Resource
    private ConsignOrderItemsDao consignOrderItemsDao;
    @Resource
    private AbnormalTradingDetailDao abnormalTradingDetailDao;
    //日志
    private static final org.slf4j.Logger log =  LoggerFactory.getLogger(ReportResourceInventoryServiceImpl.class);

    public ReportResourceInventoryDto queryReportResourceInventory(Long areaId, String dt) {

        List<ReportResourceInventory> resources = reportResourceInventoryDao.queryReportResourceInventory(areaId, dt);
        if (resources == null || resources.size()==0) {
            return null;
        } else {
            return mapToReportResourceInventoryDto(resources);
        }
    }

    /**
     * 数据映射
     * @param resources
     * @return
     */
    private ReportResourceInventoryDto mapToReportResourceInventoryDto(List<ReportResourceInventory> resources){
        int totalAccount= 0;
    	int totalResource = 0;
        BigDecimal totalWeight = BigDecimal.ZERO;
        int totalSpec = 0;
        BigDecimal totalSpecCoverageRate;
        int totalTransaction = 0;
        BigDecimal totalTransactionCoverageRate;
        int totalDeviation = 0;
        BigDecimal totalDeviationRate;

        for (ReportResourceInventory res : resources) {
        	totalAccount += res.getTotalAccount()==null?0:res.getTotalAccount();
            totalResource += res.getTotalResource();
            totalWeight = totalWeight.add(res.getWeight());
            totalSpec += res.getTotalSpec();
            totalDeviation += res.getTotalDeviation();
            totalTransaction += res.getTotalTransaction();
        }


        totalSpecCoverageRate = totalResource!=0 ? getPercent(Integer.valueOf(totalResource),Integer.valueOf(totalSpec)) : BigDecimal.ZERO;
        totalTransactionCoverageRate = totalResource!=0 ? getPercent(Integer.valueOf(totalTransaction),Integer.valueOf(totalResource)) : BigDecimal.ZERO;
        totalDeviationRate = totalResource!=0 ? getPercent(Integer.valueOf(totalTransaction),Integer.valueOf(totalResource)): BigDecimal.ZERO;

        Map<String, List<ReportResourceInventory>> groups = resources.stream().collect(Collectors.groupingBy(ReportResourceInventory::getCategoryGroupUuid));
        List<CategoryGroupDto> categoryGroupDtoList = new ArrayList<>();

        for (String key : groups.keySet()) {

            CategoryGroupDto dto = new CategoryGroupDto();
            dto.setCategoryGroupUuid(key);

            List<ReportResourceInventory> res = groups.get(key);
            dto.setCategoryGroupName(res.get(0).getCategoryGroupName());
            dto.setResources(res);

            categoryGroupDtoList.add(dto);
        }

        ReportResourceInventoryDto dto = new ReportResourceInventoryDto(totalAccount,totalResource,totalWeight, totalSpecCoverageRate, totalTransactionCoverageRate, totalDeviation, totalDeviationRate, categoryGroupDtoList);

        return dto;
    }

    /**
     * 每日库存统计
     */
    public void dailyStatistics() {
        try {
            DailyResourceQuery query = new DailyResourceQuery(null, Tools.dateToStr(new Date(), "yyyy-MM-dd"));

            List<CategoryInResourceDto> resources = resourceDao.queryCategoryInResource(query);         //当天所有资源
            List<ConsignOrderItemsDto> orderItems = consignOrderItemsDao.queryOrderItemsOfToday();      //当天所有订单项
            Integer totalTransaction = orderItems != null ? orderItems.size() : 0;

            List<CategoryInResourceDto> groups = getCategoryGroup(resources);   //所有大类
            List<CategoryInResourceDto> categories = getCategory(resources);    //所有品名

            List<City> cities = areaService.queryAllCenterCity();
            if (cities != null) {
                // 循环所需监控城市
                for (City city : cities) {
                    ReportResourceInventory resourceInventory = new ReportResourceInventory();
                    resourceInventory.setAreaId(city.getId());

                    //地区过滤
                    List<CategoryInResourceDto> resourcesFiltedByCity =  resources.stream()
                            .filter(a -> (a.getCityId() != null && a.getCityId().equals(city.getId()))).collect(Collectors.toList());

                    //当前区域卖家条数
                    int totalAccount=resourcesFiltedByCity.stream().collect(Collectors.groupingBy(CategoryInResourceDto::getSellerName)).size();
                    resourceInventory.setTotalAccount(totalAccount);

                    List<ConsignOrderItemsDto> orderItemsFiltedByCity = orderItems.stream()
                            .filter(a -> (a.getCityId() != null && a.getCityId().equals(city.getId()))).collect(Collectors.toList());

                    //循环大类
                    for (CategoryInResourceDto group : groups){
                        resourceInventory.setCategoryGroupUuid(group.getCategoryGroupUuid());
                        resourceInventory.setCategoryGroupName(group.getCategoryGroupName());

                        //大类过滤
                        List<CategoryInResourceDto> resourcesFiltedByGroup = resourcesFiltedByCity.stream()
                                .filter(a -> a.getCategoryGroupUuid().equals(group.getCategoryGroupUuid())).collect(Collectors.toList());

                        //循环品名
                        for (CategoryInResourceDto category : categories) {
                            if(!category.getCategoryGroupUuid().equals(group.getCategoryGroupUuid())){
                                continue;
                            }

                            resourceInventory.setCategoryUuid(category.getCategoryUuid());
                            resourceInventory.setCategoryName(category.getCategoryName());

                            resourceInventory.setTotalSpec(category.getVarietyQuantity().intValue()); //品规总数

                            //品名过滤
                            List<CategoryInResourceDto> resourcesFiltedByCategory = resourcesFiltedByGroup.stream()
                                    .filter(a -> a.getCategoryUuid().equals(category.getCategoryUuid())).collect(Collectors.toList());

                            List<ConsignOrderItemsDto> orderItemsFiltedByCategory = orderItemsFiltedByCity.stream()
                                    .filter(a -> a.getCategoryUuid().equals(category.getCategoryUuid())).collect(Collectors.toList());

                            resourceInventory.setTotalResource(resourcesFiltedByCategory.size()); //资源条数
                            resourceInventory.setTotalStockSpec(sumSpec(resourcesFiltedByCategory)); //品规总数
                            resourceInventory.setSpecCoverageRate(getSpecCoverageRate(category.getVarietyQuantity(), resourceInventory.getTotalStockSpec())); //品规覆盖率
                            resourceInventory.setWeight(BigDecimal.valueOf(resourcesFiltedByCategory.stream().mapToDouble(a -> a.getWeight().doubleValue()).sum()));// 重量

                            resourceInventory.setTotalTransaction(orderItemsFiltedByCategory.size()); //成交条数
                            resourceInventory.setTransactionCoverageRate(getTransactionCoverageRate(totalTransaction, resourceInventory.getTotalTransaction())); //TODO:成交覆盖率

                            // 过滤出有偏差数据
                            List<CategoryInResourceDto> resourcesFiltedByDeviation = resourcesFiltedByCategory.stream()
                                    .filter(a -> isBiased(a.getPrice(), averageDealPrice(orderItemsFiltedByCategory, a.getCategoryUuid(), a.getSpec()), a.getPriceDeviation()))
                                    .collect(Collectors.toList());

                            int totalDeviation = resourcesFiltedByDeviation.size();
                            resourceInventory.setTotalDeviation(totalDeviation); //价格偏差条数
                            resourceInventory.setDeviationRate(getPercent(resourceInventory.getTotalDeviation(), totalDeviation));

                            //TODO:数据写入优化，批量处理。
                            Long resourceInventoryId = add(resourceInventory);
                            if (resourceInventoryId!=null) {
                                //保存有偏差的交易数据。
                                addAbnormalTradingData(resourcesFiltedByDeviation, resourceInventoryId);
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
          log.error("======================dailyStatistics，错误信息：="+e.getMessage(),e);
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
        }


    }

    /**
     * 统计（库存）品规数量
     * @param resourcs 资源列表
     * @return
     */
    private int sumSpec(List<CategoryInResourceDto> resourcs){
        //先提取所有品规，再去除重复品规。
        resourcs.stream().collect(Collectors.mapping(CategoryInResourceDto::getSpec, Collectors.toList())).stream().distinct();

        return resourcs.size();
    }

    /**
     * 统计（库存）品规覆盖率
     * 计算方式：品规覆盖率=库存品规数/所属品名品规数；
     * @param varietyQuantity 品规种数
     * @param totalStockSpec 库存品规数
     * @return
     */
    private BigDecimal getSpecCoverageRate(Integer varietyQuantity, Integer totalStockSpec){
        // 品规种数为null或0时，品规覆盖率置空。
        if (varietyQuantity==null || varietyQuantity.intValue()==0) {
            return null;
        }
        // 库存品规数为null或0时，品规覆盖率置空。
        if (totalStockSpec==null || totalStockSpec.intValue()==0) {
            return BigDecimal.ZERO;
        }

        return getPercent(totalStockSpec, varietyQuantity);
    }

    /**
     * 统计（库存）品规覆盖率
     * 计算方式：品规覆盖率=库存品规数/所属品名品规数；
     * @param totalTransaction 品规种数
     * @param totalStockTransaction 库存品规数
     * @return
     */
    private BigDecimal getTransactionCoverageRate(Integer totalTransaction, Integer totalStockTransaction){
        // 品规种数为null或0时，品规覆盖率置空。
        if (totalTransaction==null || totalTransaction.intValue()==0) {
            return null;
        }
        // 库存品规数为null或0时，品规覆盖率置空。
        if (totalStockTransaction==null || totalStockTransaction.intValue()==0) {
            return BigDecimal.ZERO;
        }

        return getPercent(totalStockTransaction, totalTransaction);
    }


    /**
     * 成交品规格平均单价
     * @param orderItems 成交项目列表
     * @param categoryUuid 品名
     * @param spec 规格
     * @return
     */
    private BigDecimal averageDealPrice(List<ConsignOrderItemsDto> orderItems, String categoryUuid, String spec){
        if (orderItems==null || orderItems.size()==0) {
            return BigDecimal.ZERO;
        }

        OptionalDouble od = orderItems.stream()
                .filter(a->a.getCategoryUuid().equals(categoryUuid) && a.getSpec().equals(spec))
                .mapToDouble(a -> a.getPrice()!=null ? a.getPrice().doubleValue(): BigDecimal.ZERO.doubleValue())
                .average();

        if (od!=null && od.isPresent()) {
            return BigDecimal.valueOf(od.getAsDouble());
        }

        return BigDecimal.ZERO;
    }

    /**
     * 获取指定品规存储价格
     * @param resource
     * @param categoryUuid
     * @param spec
     */
    private BigDecimal getStockPrice(List<CategoryInResourceDto> resource, String categoryUuid, String spec) {
        if (resource==null || resource.size()==0) {
            return null;
        }

        Optional o = resource.stream()
                .filter(a -> a.getCategoryUuid().equals(categoryUuid) && a.getSpec().equals(spec))
                .findFirst();

        if (o!=null) {
            CategoryInResourceDto dto = (CategoryInResourceDto)o.get();
            return dto.getPrice();
        }

        return null;
    }

    /**
     * 是否偏差
     * 判断方式：|(成交品规库存单价-成交品规格平均单价)/成交品规格平均单价| > 品名价格基础偏差率
     * @param stockPrice            库存价
     * @param averageDealPrice      平均单价
     * @param priceDeviation        基础偏差率
     * @return
     */
    private boolean isBiased(BigDecimal stockPrice, BigDecimal averageDealPrice, BigDecimal priceDeviation){
        if (averageDealPrice == null || averageDealPrice.compareTo(BigDecimal.ZERO)==0) {
            return false;
        }

        if(stockPrice.subtract(averageDealPrice).divide(averageDealPrice, 6, BigDecimal.ROUND_HALF_UP).abs().doubleValue() > priceDeviation.doubleValue()){
            return true;
        }

        return false;
    }

    /**
     * 提取大类
     * @param list
     * @return
     */
    private List<CategoryInResourceDto> getCategoryGroup(List<CategoryInResourceDto> list){
        List<CategoryInResourceDto> unique = new ArrayList<>();
        if (list!=null && list.size()!=0) {
            list.stream().collect(Collectors.groupingBy(CategoryInResourceDto::getCategoryGroupUuid, Collectors.toList()))
                    .entrySet().stream().forEach(e -> unique.add(e.getValue().get(0)));
        }
        return unique;
    }

    /**
     * 提取品名
     * @param list
     * @return
     */
    private List<CategoryInResourceDto> getCategory(List<CategoryInResourceDto> list) {
        List<CategoryInResourceDto> unique = new ArrayList<>();
        if (list!=null && list.size()!=0) {
            list.stream().collect(Collectors.groupingBy(CategoryInResourceDto::getCategoryUuid, Collectors.toList()))
                    .entrySet().stream().forEach(e -> unique.add(e.getValue().get(0)));
        }
        return unique;
    }

    /**
     * 写入库存统计信息
     * @param resourceInventory
     * @return 返回主键id的值。
     */
    private Long add(ReportResourceInventory resourceInventory){
        resourceInventory.setCreatedBy("system");
        if (reportResourceInventoryDao.insertSelective(resourceInventory)>0) {
            return resourceInventory.getId();
        } else {
            return null;
        }
    }

    /**
     * 写入异常交易数据数据
     * @param resources
     * @param resourceInventoryId
     */
    private void addAbnormalTradingData(List <CategoryInResourceDto> resources, Long resourceInventoryId){
        List<AbnormalTradingDetail> details = new ArrayList<>();
        for (CategoryInResourceDto dto : resources) {
            AbnormalTradingDetail detail = new AbnormalTradingDetail();
            detail.setReportResourceInventoryId(resourceInventoryId);
            detail.setCategoryName(detail.getCategoryName());
            detail.setMaterialName(dto.getMaterialName());
            detail.setSpec(dto.getSpec());
            detail.setFactory(dto.getFactoryName());
            detail.setWarehouse(dto.getWarehouseName());
            detail.setPrice(dto.getPrice());
            detail.setSellerName(dto.getSellerName());

            details.add(detail);
        }
        if (details.size()>0) {
            abnormalTradingDetailDao.batchInsert(details);
        }
    }

    /**
     * 获取百分数，保留两位小时
     *
     * @param divisor
     * @param dividend
     * @return
     */
    private BigDecimal getPercent(Integer divisor, Integer dividend/*, int scale*/) {
        if (divisor == null || divisor.intValue() == 0) {
            return null;
        }
        if (dividend == null || dividend.intValue() == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf((1.0f * divisor) / (1.0f * dividend)).setScale(2, RoundingMode.HALF_UP);
    }
}
