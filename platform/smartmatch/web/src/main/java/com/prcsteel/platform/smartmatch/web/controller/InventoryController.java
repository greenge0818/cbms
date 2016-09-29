package com.prcsteel.platform.smartmatch.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.order.model.enums.ResourceStatusType;
import com.prcsteel.platform.smartmatch.model.dto.HistoryResourceDto;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.smartmatch.service.ResourceService;
import com.prcsteel.platform.smartmatch.model.dto.ReportResourceInventoryDto;
import com.prcsteel.platform.smartmatch.service.AreaService;
import com.prcsteel.platform.smartmatch.service.ReportResourceInventoryService;

/**
 * Created by Rolyer on 2015/11/30.
 */
@Controller
@RequestMapping("/smartmatch/inventory/")
public class InventoryController extends BaseController {
    private final static String format = "yyyy-MM-dd HH:mm";
    private final static String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    private final static Date DIVIDE_TIME = Tools.strToDate(Tools.dateToStr(new Date(), FORMAT_YYYYMMDD) + " 17:30", format);//分界时间点

    @Resource
    private ReportResourceInventoryService reportResourceInventoryService;

    @Resource
    private AreaService areaService;

    @Resource
    private ResourceService resourceService;

    /**
     * 库存监控
     *
     * @param out
     * @param areaId 地区
     * @param dt     时间
     * @return
     */
    @RequestMapping("info")
    public String info(ModelMap out, String areaId, String dt,String tabIndex) {
        //挂牌资源总数
        Map<String, Integer> map = resourceService.selecCountResourceByStatus(getUserIds());
        out.put("listedCount", map.get("listedCount"));
        out.put("unListedCount", map.get("unListedCount"));
        out.put("allCount", map.get("allCount"));
        out.put("inqueryCount", map.get("inqueryCount"));// 询价资源
        out.put("exceptionCount", map.get("exceptionCount"));
        out.put("historyTransactionCount", map.get("historyTransactionCount"));
        out.put("tabIndex", tabIndex);
        
        List<City> cities = areaService.queryAllCenterCity();
        out.put("cities", cities);
        if (StringUtils.isBlank(dt)) {
            dt = getLimitDateTime(new Date());
        }
//        } else {
//            dt = getLimitDateTime(Tools.strToDate(dt, FORMAT_YYYYMMDD));
//        }

        Long id = (StringUtils.isNotBlank(areaId) && StringUtils.isNumeric(areaId)) ? Long.parseLong(areaId) : null;
        ReportResourceInventoryDto info = reportResourceInventoryService.queryReportResourceInventory(id, dt);

        out.put("info", info);
        out.put("areaId", id!=null?id.toString():"");

        out.put("dt", dt);

        return "smartmatch/inventory/info";
    }

    /**
     * 获取时间限制
     * @return
     */
    private String getLimitDateTime(Date date) {
        Date currentTime = new Date();
        String dt = Tools.dateToStr(date, FORMAT_YYYYMMDD);
        if (date!=null && Tools.dateToStr(date,FORMAT_YYYYMMDD).equals(Tools.dateToStr(currentTime,FORMAT_YYYYMMDD))) {
            if (currentTime.before(DIVIDE_TIME)) { //当天17：30之前
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);

                dt = Tools.dateToStr(calendar.getTime(), FORMAT_YYYYMMDD);
            }
        }

        return dt;
    }
    
    /********************************卖家资源统计 add by peanut on 2016-02-17*********/
    /**
     * 卖家资源统计首页跳转
     * @param out
     * @return
     */
    @RequestMapping("seller/statistic/index")
    private String indexForSellerStatistic(ModelMap out){
    	//地区信息放入请求域
        out.put("cities", areaService.queryAllCenterCity());
        
        out.put("dt", getLimitDateTime(new Date()));
        
        List<ResourceStatusType> list=new ArrayList<ResourceStatusType>();
        list.add(ResourceStatusType.ALL);
        list.add(ResourceStatusType.NORMAL);
        list.add(ResourceStatusType.HISTORYTRANSACTION);
        out.put("rTypes",list);
    	return "smartmatch/inventory/sellerIndex";
    }
    
    /**
     * 查询卖家资源统计数据
     * @param dt           日期
     * @param areaId       区域
     * @return
     */
    @RequestMapping("seller/statistic/search")
    @ResponseBody
    private PageResult search(String dt,String areaId,String rType){
    	List<HistoryResourceDto>list=resourceService.getHistorySellerStatisticData(dt,areaId,rType);
		PageResult pr=new PageResult();
		pr.setData(list);
		pr.setRecordsTotal(list.size());
		pr.setRecordsFiltered(list.size());
    	return pr;
    }
    /********************************卖家资源统计 end*********/
}
