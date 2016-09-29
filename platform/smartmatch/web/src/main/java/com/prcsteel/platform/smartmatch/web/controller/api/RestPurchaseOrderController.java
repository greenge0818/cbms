package com.prcsteel.platform.smartmatch.web.controller.api;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.RequirementStatusDto;
import com.prcsteel.platform.smartmatch.model.enums.PurchaseOrderStatus;
import com.prcsteel.platform.smartmatch.service.PurchaseOrderService;
import com.prcsteel.platform.smartmatch.service.RestMarketToSmartMatchService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 询价单Rest接口
 *
 * @author prcsteel
 */
@RestController
@RequestMapping("/api/purchaseorder/")
public class RestPurchaseOrderController {
    //日志
    private static final Logger log = LoggerFactory.getLogger(RestPurchaseOrderController.class);

    @Resource
    private PurchaseOrderService purchaseOrderService;

    @Resource
    private RestMarketToSmartMatchService restMarketToSmartMatchService;

    @RequestMapping(value = "updateStatusById.html", method = RequestMethod.POST)
    @ApiOperation("根据询价单的ID更新询价单的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "询价单的ID,例：1",required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "statusStr", value = "需要更新的询价单状态,例：BILLED", required = true,dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginId", value = "更新操作的操作人,例：cbadmin",required = true,dataType = "String", paramType = "query")
    })
    public Result updateStatusById(String id, String statusStr, String loginId) {
        Long orderId=0l;
        try{
            orderId = Long.parseLong(id);
        }
        catch (Exception ex){
            return new Result("参数错误", false);
        }
        if (id == null || StringUtils.isBlank(statusStr)) {
            log.error("接口调用失败传递的参数为空:id:" + id + ",statusNum:" + statusStr + ",loginId:" + loginId);
            return new Result("参数错误", false);
        }
        if (loginId == null) {
            loginId = "";
        }
        String status = "";
        if (PurchaseOrderStatus.BILLED.getCode().equals(statusStr)) {
            status = PurchaseOrderStatus.BILLED.getCode();
        } else {
        	status = statusStr;
        }
        //传递过来的数据。
        if (log.isDebugEnabled()) {
            log.debug("--------------id:" + id + ",status:" + status + ",loginId:" + loginId);
        }
        try {
            purchaseOrderService.updateStatusById(orderId, status, loginId);
        } catch (Exception e) {
            log.error("接口调用失败", e);
            return new Result("接口调用失败:" + e.getMessage(), false);
        }
        return new Result();
    }

    /**
     * 更新需求单状态
     *
     * @return
     * @author peanut
     * @date 2016/6/22
     */
    @RequestMapping(value = "status/update.html", method = RequestMethod.POST)
    @ApiOperation("更新需求单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "需求单号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "statusTo", value = "设置成状态，例：QUOTED 报价，CLOSED 关闭", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "operateCode", value = "报价单号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "operated", value = "报价单生成时间或关闭时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "closeReason", value = "关闭理由", dataType = "String", paramType = "query")
    })
    public void updateRequirementStatus(RequirementStatusDto requirementStatusDto) {
        restMarketToSmartMatchService.updateRequirementStatus(requirementStatusDto);
    }

    @RequestMapping(value = "getCityByOrderId.html", method = RequestMethod.POST)
    @ApiOperation("获取询价单交货地")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderId", value = "询价单号", required = true, dataType = "Long", paramType = "query")})
    public City getCityByOrderId(Long orderId) {
        try {
            City city = purchaseOrderService.getCityByOrderId(orderId);
            return city;
        } catch (Exception ex) {
            log.error(ex.toString());
            return null;
        }
    }
}
