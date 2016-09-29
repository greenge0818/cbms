package com.prcsteel.platform.smartmatch.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.model.dto.PurchaseOrderTableDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationInfoDto;
import com.prcsteel.platform.smartmatch.model.dto.QuotationOrderItemsDto;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderItemsService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderService;

@Controller
@RequestMapping("/smartmatch/quotation/")
public class QuotationOrderItemsController extends BaseController {
	//增加日志
	private Logger logger = LoggerFactory.getLogger(QuotationOrderItemsController.class);

    @Resource
    private QuotationOrderItemsService quotationOrderItemsService;
    @Resource
    private QuotationOrderService quotationOrderService;
    @Resource
    private PurchaseOrderService purchaseOrderService;

    @RequestMapping("/index/{id}")
    public String index(ModelMap out, @PathVariable Integer id) {
        PurchaseOrderTableDto dto = quotationOrderItemsService.selectByQuotationId(id);
        out.put("id", id);
        out.put("dto", dto);
        out.put("historyPurchaseOrderDto", quotationOrderItemsService.selectHistoryQuotationOrder(Long.parseLong(id.toString())));
        return "/smartmatch/quotation/index";
    }
    
    @RequestMapping("/info/{id}")
    public String quotationInfo(ModelMap out, @PathVariable Integer id) {
    	//根据询价单获取最新的报价单号
    	Integer qid = quotationOrderItemsService.selectQuotationLastUpdateByPurchaseId(id);
    	//获取当前的询价单信息
        PurchaseOrderTableDto dto = quotationOrderItemsService.selectQuotationByPurchaseId(id);
        List<List<QuotationOrderItemsDto>>  historyPurchaseOrderDto = quotationOrderItemsService
				.selectHistoryQuotationOrderByPurchaseId(id,qid);
        if(historyPurchaseOrderDto == null){
        	historyPurchaseOrderDto = new ArrayList<List<QuotationOrderItemsDto>>();
        }
        out.put("id", qid);
        out.put("dto", dto);
		out.put("historyPurchaseOrderDto", historyPurchaseOrderDto);
      return "/smartmatch/quotation/quotationInfo";
    }
    
    @ResponseBody
    @RequestMapping("search.html")
    public PageResult search(Long id) {
    	//获取报价单详情页面DTO
        List<QuotationOrderItemsDto> list = quotationOrderItemsService.getDtoByOrderId(id);
        PageResult result = new PageResult();
        result.setData(list);
        return result;
    }
    
	/**
	 * 开单前保存开单数据
	 * 
	 * @return
	 */
	@RequestMapping("preOpenBill.html")
	@ResponseBody
	public Result preOpenBill(@RequestBody List<QuotationInfoDto> etDtoList) {
		Result result = new Result();
		try {
			User user = this.getLoginUser();
			quotationOrderItemsService.saveQuotationOrderItems(etDtoList);
			result.setData("操作成功");
		} catch (BusinessException e) {
			logger.error("操作失败"+e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	  
    @ResponseBody
    @RequestMapping("getQuotationInfo.html")
    public PageResult getQuotationInfo(Long id) {
    	//获取报价单详情页面DTO
		List<QuotationInfoDto> list = quotationOrderItemsService
				.getQuotationInfoOrderItems(id, getUserIds());
		PageResult result = new PageResult();
        result.setData(list);
        return result;
    }

    @RequestMapping("confirm.html")
    @ResponseBody
    public Result confirm(Long id) {
        try {
            quotationOrderService.confirm(id, getLoginUser());
            return new Result();
        } catch (BusinessException e) {
        	logger.error("===" + e.getMsg());
            return new Result(e.getMsg(), false);
        }
    }

    /**
     * 查看采购单对应的报价详情
     *
     * @param out
     * @param purchaseOrderId 采购单id
     * @return
     * @author peanut
     * @date 2016/06/15
     */
    @RequestMapping("{purchaseOrderId}/detail.html")
    public String quotationDetail(ModelMap out, @PathVariable("purchaseOrderId") String purchaseOrderId) {

        if (StringUtils.isNumeric(purchaseOrderId)) {
            List list = quotationOrderItemsService.selectQuotationByPurchaseOrderId(Long.parseLong(purchaseOrderId));
            out.put("quotation", list);
            out.put("quotaCount", list == null ? 0 : list.size());
            out.put("detailCount",purchaseOrderService.getInquiryHistoryByPurchaseId(Long.parseLong(purchaseOrderId)).size());
        }
        return "/smartmatch/quotation/detail";
    }
}
