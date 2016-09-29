package com.prcsteel.platform.order.web.controller.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.prcsteel.platform.order.web.controller.BaseController;
import javax.annotation.Resource;
import com.prcsteel.platform.common.enums.OpType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.order.model.model.OrderAuditTrail;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.common.service.OpLog;
import com.prcsteel.platform.common.service.OpParam;
import com.prcsteel.platform.common.vo.Result;


/**
 * Created by kongbinheng on 2015/7/18.
 */
@Controller
@RequestMapping("/order/")
public class OrderStatusController extends BaseController {

    @Autowired
    OrderStatusService orderStatusService;

    /**
     * 更新订单状态并插入流程记录
     * @param id
     * @param code
     * @param type
     * @param statusBuyer
     * @param statusSeller
     * @param operatorId
     * @param operatorName
     * @return
     */
    @OpLog(OpType.ChangeOrderStatus)
    @OpParam(name="id",value="OrderId")
    @OpParam(name="code",value="OrderCode")
    @OpParam("type")
    @OpParam("statusBuyer")
    @OpParam("statusSeller")
    @ResponseBody
    @RequestMapping(value = { "/updateStatus" }, method = { RequestMethod.POST })
    public Result updateStatus(@RequestParam("id") Long id,
                               @RequestParam("code") String code,
                               @RequestParam("type") String type,
                               @RequestParam("statusBuyer") String statusBuyer,
                               @RequestParam("statusSeller") String statusSeller,
                               @RequestParam("operatorId") Long operatorId,
                               @RequestParam("operatorName") String operatorName) {

		//请求参数封装
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", id);
        paramMap.put("code", code);
        paramMap.put("statusBuyer", statusBuyer);
        paramMap.put("statusSeller", statusSeller);

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(id);
        if("buyer".equals(type)){
            orderAuditTrail.setSetToStatus(statusBuyer);
        }else{
            orderAuditTrail.setSetToStatus(statusSeller);
        }
        orderAuditTrail.setOperatorId(operatorId);
        orderAuditTrail.setOperatorName(operatorName);
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(getLoginUser().getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(getLoginUser().getLoginId());
        orderAuditTrail.setModificationNumber(0);

        Result result = new Result();
        if(orderStatusService.updateOrderStatus(paramMap, orderAuditTrail)){
            result.setSuccess(true);
            result.setData("更新" + code + "订单号状态成功！");
        } else{
            result.setSuccess(false);
            result.setData("更新" + code + "订单号状态失败！");
        }
        return result;
    }

    /**
     * 插入流程记录
     * @param OrderId
     * @param type
     * @param statusBuyer
     * @param statusSeller
     * @param operatorId
     * @param operatorName
     * @return
     */
    @OpLog(OpType.AddOrderAuditTrail)
    @OpParam(name="OrderId",value="OrderId")
    @OpParam("type")
    @OpParam("statusBuyer")
    @OpParam("statusSeller")
    @ResponseBody
    @RequestMapping(value = { "/addorderaudittrail" }, method = { RequestMethod.POST })
    public Result addOrderAuditTrail(@RequestParam("orderId") Long OrderId,
                               @RequestParam("type") String type,
                               @RequestParam("statusBuyer") String statusBuyer,
                               @RequestParam("statusSeller") String statusSeller,
                               @RequestParam("operatorId") Long operatorId,
                               @RequestParam("operatorName") String operatorName) {

        OrderAuditTrail orderAuditTrail = new OrderAuditTrail();
        orderAuditTrail.setOrderId(OrderId);
        if("buyer".equals(type)){
            orderAuditTrail.setSetToStatus(statusBuyer);
        }else{
            orderAuditTrail.setSetToStatus(statusSeller);
        }
        orderAuditTrail.setOperatorId(operatorId);
        orderAuditTrail.setOperatorName(operatorName);
        orderAuditTrail.setCreated(new Date());
        orderAuditTrail.setCreatedBy(getLoginUser().getLoginId());
        orderAuditTrail.setLastUpdated(new Date());
        orderAuditTrail.setLastUpdatedBy(getLoginUser().getLoginId());
        orderAuditTrail.setModificationNumber(0);

        Result result = new Result();
        if(orderStatusService.insertOrderAuditTrail(orderAuditTrail) > 0){
            result.setSuccess(true);
        } else{
            result.setSuccess(false);
        }
        return result;
    }
    
    @Resource
    ConsignOrderService consignOrderService;
    /**
     * 生成2016.02月订单积分数据
     * @author Green.Ge
     * 2016.02.29
     * 
     */
    @RequestMapping(value = { "/genFebPoint" })
    public void genFebPoint(){
    	consignOrderService.genFebPoint();
    }
}
