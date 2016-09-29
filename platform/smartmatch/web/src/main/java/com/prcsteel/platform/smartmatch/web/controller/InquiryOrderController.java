package com.prcsteel.platform.smartmatch.web.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderDto;
import com.prcsteel.platform.smartmatch.model.dto.InquiryOrderSellersDto;
import com.prcsteel.platform.smartmatch.model.enums.InquirySelectOptions;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrderItems;
import com.prcsteel.platform.smartmatch.model.query.InquiryOrderQuery;

import com.prcsteel.platform.smartmatch.service.InquiryOrderService;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.prcsteel.platform.acl.model.enums.SysSettingType;
import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.SysSettingService;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

import net.sf.json.JSONArray;

/**
 * Created by Rabbit on 2015/11/17.
 */
@Controller
@RequestMapping("/smartmatch/inquiryorder/")
public class InquiryOrderController extends BaseController {
	//增加日志
	private Logger logger = LoggerFactory.getLogger(InquiryOrderController.class);
	
    @Resource
    PurchaseOrderService purchaseOrderService;
    @Resource
    InquiryOrderService inquiryOrderService;
    @Resource
    SysSettingService sysSettingService;
    @Resource
    QuotationOrderService quotationOrderService;

    /**
     * 新增采购
     *
     * @param out
     * @param id  采购单编号，用于初始化采购单信息。
     */
    @RequestMapping("create")
    public void created(ModelMap out, Long id, String blockInquiryOrderSellerIds, String quotationOrderId) {
        out.put("options", InquirySelectOptions.values());
        out.put("purchaseOrder", purchaseOrderService.queryByIdForShow(id));
        out.put("blockInquiryOrderSellerIds", blockInquiryOrderSellerIds);
        if(StringUtils.isNotBlank(quotationOrderId)){
            out.put("quotationOrder", quotationOrderService.selectByPrimaryKey(Long.valueOf(quotationOrderId)));
        }
        SysSetting sysSetting = sysSettingService.queryBySettingType(SysSettingType.SMART_WEIGHT_WARNING.getCode());
        if (sysSetting != null) {
            String[] settingValues = sysSetting.getSettingValue().split("-");
            out.put("leastPercent", Double.valueOf(settingValues[0]) / 100);
            out.put("highestPercent", Double.valueOf(settingValues[1]) / 100);
        }
    }

    /**
     * 通过三种选择条件异步获取系统推荐报价方式
     *
     * @param purchaseOrderId
     * @param option
     * @return
     */
    @RequestMapping("getData")
    @ResponseBody
    public Result getData(Long purchaseOrderId, String option, String blockInquiryOrderSellerIds, String quotationOrderId) {
        Result result = new Result();
        try {
            result.setData(inquiryOrderService.selectByPurchaseOrderId(purchaseOrderId, option, blockInquiryOrderSellerIds, quotationOrderId));
            result.setSuccess(true);
        } catch (BusinessException e) {
        	logger.error("====" + e.getMsg());
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 保存报价单
     *
     * @param order
     * @param quotationOrderItems
     * @return
     */
    @RequestMapping("saveQuotationOrder")
    @ResponseBody
    public Result saveQuotationOrder(QuotationOrder order, String quotationOrderItems) {
        Result result = new Result();
        List<QuotationOrderItems> list = new Gson().fromJson(quotationOrderItems, new TypeToken<List<QuotationOrderItems>>() {
        }.getType());
        try {
            Long id = inquiryOrderService.saveQuotationOrder(order, list, getLoginUser());
            result.setSuccess(true);
            result.setData(id);
        } catch (BusinessException e) {
        	logger.error("====" + e.getMsg());
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("/getSellerNum")
    @ResponseBody
    public Map<String, Object> getSellerNum(Long purchaseOrderId) {
        return inquiryOrderService.getSellerNum(purchaseOrderId, getLoginUser());
    }

    @RequestMapping("/list")
    public String sortList(ModelMap out) {
        return "smartmatch/inquiryorder/list";
    }

    @RequestMapping("search.html")
    @ResponseBody
    public PageResult search(InquiryOrderQuery inquiryOrderQuery) {
    	inquiryOrderQuery.setUserIds(getUserIds());
        List<InquiryOrderDto> list = inquiryOrderService.getInquiryOrderList(inquiryOrderQuery);
        PageResult result = new PageResult();
        result.setData(list);
        return result;
    }

    @RequestMapping("updateInquiryOrderItems.html")
    @ResponseBody
    public Result updateInquiryOrderItems(String inquiryOrderItems) {
        Result result = new Result();
        JSONArray object = JSONArray.fromObject(inquiryOrderItems);
        try {
            inquiryOrderService.updateInquiryItems(object, getLoginUser());
        } catch (BusinessException e) {
        	logger.error("====" + e.getMsg());
            result.setData(e.getMsg());
            result.setSuccess(false);
        }
        return result;
    }
    
    @RequestMapping("getSellersInquiryRecord")
    @ResponseBody
    public Result getSellersInquiryRecord(String sellerIds){
    	Result result = new Result();
        try {
        	List<InquiryOrderSellersDto> sellerList = inquiryOrderService.getSellersInquiryRecord(sellerIds);
        	result.setData(sellerList);
        	
        } catch (BusinessException e) {
        	logger.error("====" + e.getMsg());
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
       
    @RequestMapping("getCityByWarehouseId")
    @ResponseBody
    public Result getCityByWarehouseId(Long warehouseId){
    	Result result = new Result();
        try {
        	String cityName = inquiryOrderService.getCityByWarehouseId(warehouseId);
        	result.setData(cityName);
        	
        } catch (BusinessException e) {
        	logger.error("====" + e.getMsg());
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
    }
}
