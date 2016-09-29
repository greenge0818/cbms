package com.prcsteel.platform.order.web.controller.order;

import com.prcsteel.platform.common.aspect.OpAction;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.order.model.dto.SecondaryDto;
import com.prcsteel.platform.order.model.enums.ConsignOrderStatus;
import com.prcsteel.platform.order.model.enums.PayStatus;
import com.prcsteel.platform.order.model.enums.UserType;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.model.PayRequest;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.service.order.OrderStatusService;
import com.prcsteel.platform.order.service.order.PickupBillService;
import com.prcsteel.platform.order.service.payment.PayRequestService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Rabbit Mao on 2015/7/22.
 */
@Controller
@RequestMapping("/order/secondarysettlement/")
public class SecondarySettlementController extends BaseController {
    ShiroVelocity permissionLimit = new ShiroVelocity();
    
	private final static Logger logger = LoggerFactory.getLogger(SecondarySettlementController.class);
    private static final String PERMISSION_BUTTON_SECONADRYSETTLEMENT_ACCEPTDRAFT = "order:secondarysettlement:acceptdraft";  //银票支付订单二结

    @Autowired
    PickupBillService pickupBillService;
    @Autowired
    ConsignOrderItemsService orderItemsService;
    @Autowired
    ConsignOrderService orderService;
    @Autowired
    OrderStatusService orderStatusService;
    @Resource
    PayRequestService payRequestService;

    @RequestMapping("show.html")
    public void show(Long orderId, ModelMap out) {
        String right = orderService.checkUserRight(orderId, getLoginUser(), getUserIds(), ConsignOrderStatus.SECONDSETTLE.getCode());
        if (!right.equals(UserType.NOPERM.toString())) {
            out.put("order", orderService.queryById(orderId));
            out.put("sellerItems", orderItemsService.querySellersItemsByOrderId(orderId, right));
            out.put("usertype", right);

            // 只查看融资订单权限（页面只能查看，不能操作，隐藏所有按钮）
            if(permissionLimit.hasPermission(Constant.PERMISSION_FINANCE_ORDER)){
                out.put("financeorder", true);
            }

            if (orderService.isAllMatch(orderId)) {
                PayRequest payRequest = payRequestService.selectAvailablePaymentByOrderId(orderId);
                if (payRequest == null) {
                    out.put("mes", "还未申请付款，无法二次结算。");
                } else {
                    if (PayStatus.REQUESTED.toString().equals(payRequest.getStatus())) {
                        out.put("mes", "付款申请还未通过审核，无法二次结算。");
                    } else if (PayStatus.APPLYPRINTED.toString().equals(payRequest.getStatus()) ||
                            PayStatus.APPROVED.toString().equals(payRequest.getStatus())) {
                        out.put("mes", "财务还未确认付款，无法二次结算。");
                    } else if (PayStatus.CONFIRMEDPAY.toString().equals(payRequest.getStatus())) {
                        if(!orderService.checkIsPayByAcceptDraft(orderId) ||
                                (orderService.checkIsPayByAcceptDraft(orderId) && permissionLimit.hasPermission(PERMISSION_BUTTON_SECONADRYSETTLEMENT_ACCEPTDRAFT))) {
                            out.put("canSecondarySettlement", true);
                        }else{
                            out.put("mes", "此单涉及银票支付，须管理员审核，请联系陈虎（13456807615）");
                        }
                    }
                    if (PayStatus.APPROVED.toString().equals(payRequest.getStatus())
                            || PayStatus.APPLYPRINTED.toString().equals(payRequest.getStatus())
                            || PayStatus.CONFIRMEDPAY.toString().equals(payRequest.getStatus())) {
                        List<SecondaryDto> data = orderService.secondarySum(orderId);
                        out.put("secodaryAmount", data);
                    }
                }
            }
        }
    }

    @RequestMapping("loadDataForEdit.html")
    @ResponseBody
    public Result loadDataForEdit(Long orderId, Long sellerId) {
        Result result = new Result();
        String right = orderService.checkUserRight(orderId, getLoginUser(), getUserIds(), ConsignOrderStatus.SECONDSETTLE.getCode());
        if (right.equals(UserType.SALESMAN.toString()) || right.equals(UserType.SERVER.toString())) {
            List<ConsignOrderItems> itemsList = orderItemsService.getItemsIdForEdit(orderId, sellerId, right);
            if (itemsList.size() > 0) {
                result.setData(itemsList);
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setData("该卖家订单已全部匹配完成");
            }
        } else {
            result.setSuccess(false);
            result.setData("你没有权限访问该订单");
        }
        return result;
    }


    @RequestMapping("inputInfos.html")
    @ResponseBody
    public Result inputInfo(Long orderId, String itemList) {
        Result result = new Result();
        String right = orderService.checkUserRight(orderId, getLoginUser(), getUserIds(), ConsignOrderStatus.SECONDSETTLE.getCode());
        Map<String, Object> map;

        if (right.equals(UserType.SALESMAN.toString()) || right.equals(UserType.SERVER.toString())) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode itemNodes = null;
            try {
                itemNodes = mapper.readTree(itemList);
            } catch (IOException e) {
            }
            try{
                map = orderService.inputInfo(itemNodes, orderId, right, getLoginUser());
                if (map.get("success").equals(true)) {
                    result.setSuccess(true);
                    result.setData(map.get("message"));
                }
            }catch (BusinessException e){
                result.setSuccess(false);
                result.setData(e.getMsg());
            }
        } else {
            result.setSuccess(false);
            result.setData("没有权限");
        }
        return result;
    }

    @OpAction(key="orderId")
    @RequestMapping("secondarySettlement.html")
    @ResponseBody
    public Result secondarySettlement(Long orderId,int actualWeight) {
        Result result = new Result();

        String right = orderService.checkUserRight(orderId, getLoginUser(), getUserIds(), ConsignOrderStatus.SECONDSETTLE.getCode());
        if (!UserType.NOPERM.toString().equals(right)) {
        	try{
        		result.setSuccess(true);
                result.setData(orderService.secondary(orderId, getLoginUser(), right,actualWeight));
        	}catch(BusinessException be){
        		result.setSuccess(false);
                result.setData(be.getMsg());
                logger.error("二次结算失败",be);
        	}
        } else {
            result.setSuccess(false);
            result.setData("该订单已完成二次结算");
        }
        return result;
    }

//    @RequestMapping("secondaryAccomplish.html")
//    @ResponseBody
//    public Result secondaryAccomplish(Long orderId) {
//        Result result = new Result();
//
//        String right = orderService.checkUserRight(orderId, getLoginUser(), getUserIds(), ConsignOrderStatus.SECONDSETTLE.getCode());
//        if (!UserType.NOPERM.toString().equals(right)) {
//            try{
//                result.setSuccess(true);
//                result.setData(orderService.secondaryAccomplish(orderId, getLoginUser(), right));
//            }catch(BusinessException be){
//                result.setSuccess(false);
//                result.setData(be.getMsg());
//                logger.error("二次结算失败",be);
//            }
//        } else {
//            result.setSuccess(false);
//            result.setData("该订单已完成二次结算");
//        }
//        return result;
//    }
}
