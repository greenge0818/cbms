package com.prcsteel.platform.smartmatch.web.controller.api;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.model.dto.QuotationInfoDto;
import com.prcsteel.platform.smartmatch.model.dto.RestBaseDto;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.model.model.QuotationOrder;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderItemsService;
import com.prcsteel.platform.smartmatch.service.QuotationOrderService;
import com.prcsteel.platform.smartmatch.service.RestMarketToSmartMatchService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用Rest获取报价单信息
 *
 * @author peanut
 * @date 2016/6/17 17:29
 */
@RestController
@RequestMapping("/api")
public class RestQuotationController {

    private static final Logger log = LoggerFactory.getLogger(RestQuotationController.class);

    @Resource
    private RestMarketToSmartMatchService restMarketToSmartMatchService;

    @Resource
    private QuotationOrderItemsService quotationOrderItemsService;
    @Resource
    private QuotationOrderService quotationOrderService;
    
    @Resource
    private PurchaseOrderService purchaseOrderService;

    /**
     * 根据地区分组获取所有中心城市
     *
     * @return
     * @author peanut
     * @date 2016/6/20
     */
    @RequestMapping(value = "/quotationitems/query.html", method = RequestMethod.GET)
    @ApiOperation("根据报价单id获取报价单明细")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "quotationIds", value = "报价单id集,例：1,2,3,4", dataType = "String", paramType = "query")
    )
    public RestBaseDto selectQuotationItemsByQuotationIds(String quotationIds) {
        return restMarketToSmartMatchService.selectQuotationItemsByQuotationIds(quotationIds);
    }
    
    /**
     * 根据ID更新报价单详情的状态
     * @param ids  当前需要修改的数据的id集合，多个用下划线隔开
     * @param statusStr 状态字符
     * @param loginId
     * @return
     */
    @RequestMapping(value = "/quotationitems/updateStatusById.html", method = RequestMethod.POST)
    @ApiOperation("根据ID更新报价单详情的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "报价单详情单的ID,例：101_102_113",required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "statusStr", value = "是否已开单，1：已开单，0：未开单,例：1", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginId", value = "更新操作的操作人,例：cbadmin",required = true,dataType = "String", paramType = "query")
    })
    public Result updateStatusById(String ids, String statusStr, String loginId) {
    	Result result = new Result();
    	String [] idArray = null;
    	if(!StringUtils.isBlank(ids)){
    		idArray = ids.split("_");
    	}
        if (ids == null || StringUtils.isBlank(statusStr)) {
            return new Result("参数statusStr错误", false);
        }
        if (loginId == null) {
            loginId = "";
        }
        //传递过来的数据。
        try {
        	if(idArray != null){
        		List<Long> orderItemIds = new ArrayList<Long>();
				for (String orderItemId : idArray) {
					Long id = 0l;
					try {
						id = Long.parseLong(orderItemId);
						orderItemIds.add(id);
					} catch (Exception ex) {
						return new Result("参数id错误:"+ids, false);
					}
				}
        		quotationOrderItemsService.updateStatusById(orderItemIds, statusStr, loginId);
        		//获取还剩下的未开单的数量
        		Integer count = 0;
        		if(!orderItemIds.isEmpty()){
        			count = quotationOrderItemsService.getQuotationItemsUnbilledCount(orderItemIds.get(0));	
        			//如果当前的询价单所有的报价单详情都开单成功则，修改当前的询价单状态为已开单
					if (count == 0) {
						purchaseOrderService
								.updatePurchaseOrderStatusByQuotataionItemId(
										orderItemIds.get(0), PurchaseOrderStatus.BILLED.getCode(), loginId);
					}
        		}
        		
        		result.setData(count);
        		
        	}
        } catch (Exception e) {
            return new Result("接口调用失败:" + e.getMessage(), false);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/quotationitems/getQuotationOrderItems.html", method = RequestMethod.POST)
    public List<QuotationInfoDto> getQuotationOrderItemsByItemIds(String idsStr, String userIdsStr) {
        try {

            String[] idArray = idsStr.split("_");
            List<Long> ids = new ArrayList<Long>();
            for (String orderItemId : idArray) {
                Long id = 0l;
                id = Long.parseLong(orderItemId);
                ids.add(id);
            }

            List<Long> userIds = new ArrayList<Long>();
            if(null != userIdsStr){
                String[] userIdArray = userIdsStr.split("_");
                for (String userId : userIdArray) {
                    Long id = 0l;
                    id = Long.parseLong(userId);
                    userIds.add(id);
                }
            }else{
                userIds = null;
            }

            return quotationOrderItemsService.getQuotationInfoOrderItemsByItemIds(ids, userIds);
        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/quotation/getQuotationOrderById.html", method = RequestMethod.POST)
    public QuotationOrder getQuotationOrderItemsByItemIds(Long id) {
        return quotationOrderService.selectByPrimaryKey(id);
    }

}
