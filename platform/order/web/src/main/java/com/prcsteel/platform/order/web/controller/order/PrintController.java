package com.prcsteel.platform.order.web.controller.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.prcsteel.platform.acl.model.model.SysSetting;
import com.prcsteel.platform.acl.service.SysSettingService;

import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.order.model.dto.OrderItemDetailDto;
import com.prcsteel.platform.order.model.model.ConsignOrder;
import com.prcsteel.platform.order.model.model.ConsignOrderItems;
import com.prcsteel.platform.order.model.query.OrderTradeCertificateQuery;
import com.prcsteel.platform.order.service.order.ConsignOrderItemsService;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.controller.BaseController;
import com.prcsteel.platform.order.web.support.ShiroVelocity;

/**
 * 
 * @author tuxianming
 * @desc 主要是用于各种打印
 */
@Controller
@RequestMapping("/order/print/")
public class PrintController extends BaseController {

	private static Logger logger = LogManager.getLogger(PrintController.class);

	@Resource
	ConsignOrderItemsService consignOrderItemsService;

	@Resource
	ConsignOrderService consignOrderService;
	@Resource
	SysSettingService sysSettingService;

	ShiroVelocity permissionLimit = new ShiroVelocity();

	/**
	 * tuxianming 寄售交易单管理 > 交易凭证 > 打印卖家凭证 > 打印销售单
	 * 
	 * @param orderIds
	 * @return
	 */
	@RequestMapping("printsaleslip.html")
	public void printSaleSlip(@RequestParam(value = "orderIds") List<Long> orderIds,
			@RequestParam(value = "sellerId") Long sellerId,
			@RequestParam(value = "needPage", defaultValue = "true") Boolean needPage,
			@RequestParam(value = "type") String type, ModelMap out) {
		out.put("type", type);
		if (orderIds != null && orderIds.size() > 0) {

			OrderTradeCertificateQuery query = new OrderTradeCertificateQuery().setOrderIds(orderIds);
			query.setSellerId(sellerId);
			query.setIsActualWeightZero("1");//打印时排除二结实提重量为0的明细
			query.setCloseStatus(true);
			// 查询，根据传过来的id查询订单详情
			List<OrderItemDetailDto> items = consignOrderService.selectOrdersByParams(query);

			String saleDate = "";
			if (items.size() > 0) {
				//有订单明细时才展示
				if (orderIds.size() > 1) {
					OrderItemDetailDto max = items.stream().max((a, a1) -> a.getCreated().compareTo(a1.getCreated())).get();
					OrderItemDetailDto min = items.stream().min((a, a1) -> a.getCreated().compareTo(a1.getCreated())).get();
					String maxStr = new SimpleDateFormat("yyyyMMdd").format(max.getCreated());
					String minStr = new SimpleDateFormat("yyyyMMdd").format(min.getCreated());
					if (maxStr.equals(minStr)) {
						OrderItemDetailDto item = items.get(0);
						saleDate = Tools.dateToStr(item.getCreated(), "yyyy年MM月dd日");
					} else {
						saleDate = Tools.dateToStr(min.getCreated(), "yyyy年MM月dd日") + "至"
								+ Tools.dateToStr(max.getCreated(), "yyyy年MM月dd日");
					}

				} else {
					OrderItemDetailDto item = items.get(0);
					saleDate = Tools.dateToStr(item.getCreated(), "yyyy年MM月dd日");
				}

				// out.put("accountName", items.get(0).getAccountName());

				List<ConsignOrderItems> orderItems = new ArrayList<ConsignOrderItems>();
				items.forEach(i -> {
					orderItems.addAll(i.getItems());
				});
				out.put("accountName", orderItems.get(0).getSellerName());
				BigDecimal total = BigDecimal.ZERO;
				for (ConsignOrderItems item : orderItems) {
				/*
				 * BigDecimal amount =
				 * (item.getCostPrice()!=null?item.getCostPrice():BigDecimal.
				 * ZERO) .multiply(item.getActualPickWeightServer())
				 * .add(item.getAllowanceBuyerAmount()!=null?item.
				 * getAllowanceBuyerAmount():BigDecimal.ZERO);
				 * item.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
				 * 
				 * item.setActualPickWeightServer(
				 * (item.getActualPickWeightServer()!=null?item.
				 * getActualPickWeightServer():BigDecimal.ZERO)
				 * .add(item.getAllowanceWeight()!=null?item.getAllowanceWeight(
				 * ):BigDecimal.ZERO) );
				 * 
				 * if(item.getActualPickWeightServer().doubleValue()==0)
				 * item.setActualPickWeightServer(BigDecimal.ONE);
				 * 
				 * item.setDealPrice(amount.divide(item.
				 * getActualPickWeightServer(), 2, BigDecimal.ROUND_HALF_UP));
				 * 
				 * total = total.add(item.getAmount());
				 */
					// 折让后的 实提重量
					BigDecimal actualWeight = item.getActualPickWeightServer();
					// 用allowanceBuyerAmount 代替 实提销售金额 allowanceAmount 代替 实提采购金额
					if (actualWeight.compareTo(BigDecimal.ZERO) != 0) {
						// 实提采购单价
						item.setCostPrice(item.getAllowanceAmount().divide(actualWeight, 2, BigDecimal.ROUND_HALF_UP));
						// 实提销售单价
						item.setDealPrice(item.getAllowanceBuyerAmount().divide(actualWeight, 2, BigDecimal.ROUND_HALF_UP));
					}

				}
				total = new BigDecimal(orderItems.stream().mapToDouble(a -> a.getAllowanceAmount().doubleValue()).sum());

				out.put("sellerId", sellerId); // 卖家用来打印时与订单id一起更新凭证号
				out.put("needPage", needPage); // 卖家用来打印时与订单id一起更新凭证号
				out.put("total", total);
				out.put("items", orderItems);
				out.put("saleDate", saleDate);
				out.put("currDate", Tools.dateToStr(new Date(), "yyyy年MM月dd日"));
				out.put("orderIds", orderIds);
			}
		}
	}

	/**
	 * tuxianming 在已经完成二结的订单， 可以打印收货确认涵
	 * 
	 * @param out
	 * @return
	 */
	@RequestMapping("printdeliveryletter.html")
	public String printDeliveryeLetter(ModelMap out, @RequestParam("orderIds") List<Long> orderIds,
			@RequestParam(value = "needPage", defaultValue = "true") Boolean needPage,
			@RequestParam(value = "type") String type) {
		out.put("type", type);
		if (orderIds != null && orderIds.size() > 0) {

			OrderTradeCertificateQuery query = new OrderTradeCertificateQuery().setOrderIds(orderIds);
			query.setIsActualWeightZero("1");//打印时排除二结实提重量为0的明细
			query.setCloseStatus(true);		
			List<OrderItemDetailDto> items = consignOrderService.selectOrdersByParams(query);
			if (items.size() > 0) {
				ConsignOrder dto = items.get(0);
				dto.setTotalAmount(BigDecimal.ZERO);

				String saleDate = "";
				if (orderIds.size() > 1) {
					OrderItemDetailDto max = items.stream().max((a, a1) -> a.getCreated().compareTo(a1.getSecondaryTime())).get();
					OrderItemDetailDto min = items.stream().min((a, a1) -> a.getCreated().compareTo(a1.getSecondaryTime())).get();
					String maxStr = new SimpleDateFormat("yyyyMMdd").format(max.getCreated());
					String minStr = new SimpleDateFormat("yyyyMMdd").format(min.getCreated());
					if (maxStr.equals(minStr)) {
						OrderItemDetailDto item = items.get(0);
						saleDate = Tools.dateToStr(item.getCreated(), "yyyy年MM月dd日");
					} else {
						saleDate = Tools.dateToStr(min.getCreated(), "yyyy年MM月dd日") + "至"
								+ Tools.dateToStr(max.getCreated(), "yyyy年MM月dd日");
					}

				} else {
					OrderItemDetailDto item = items.get(0);
					saleDate = Tools.dateToStr(item.getCreated(), "yyyy年MM月dd日");
				}
				dto.setSecondaryTimeStr(saleDate);
				
				List<ConsignOrderItems> orderItems = new ArrayList<ConsignOrderItems>();
				items.forEach(i -> {
					orderItems.addAll(i.getItems());
				});

				orderItems.forEach(item -> {
					// 折让后的 实提重量
					BigDecimal actualWeight = item.getActualPickWeightServer();
					// 用allowanceBuyerAmount 代替 实提销售金额 allowanceAmount 代替 实提采购金额
					if (actualWeight.compareTo(BigDecimal.ZERO) != 0) {
						// 实提采购单价
						item.setCostPrice(item.getAllowanceAmount().divide(actualWeight, 2, BigDecimal.ROUND_HALF_UP));
						// 实提销售单价
						item.setDealPrice(
								item.getAllowanceBuyerAmount().divide(actualWeight, 2, BigDecimal.ROUND_HALF_UP));
					}

					/*
					 * BigDecimal amount =
					 * (item.getDealPrice()!=null?item.getDealPrice():BigDecimal
					 * .ZERO) .multiply(item.getActualPickWeightServer())
					 * .add(item.getAllowanceBuyerAmount()!=null?item.
					 * getAllowanceBuyerAmount():BigDecimal.ZERO);
					 * item.setAmount(amount.setScale(2,
					 * BigDecimal.ROUND_HALF_UP));
					 * 
					 * item.setActualPickWeightServer(
					 * (item.getActualPickWeightServer()!=null?item.
					 * getActualPickWeightServer():BigDecimal.ZERO)
					 * .add(item.getAllowanceWeight()!=null?item.
					 * getAllowanceWeight():BigDecimal.ZERO) );
					 * 
					 * if(item.getActualPickWeightServer().doubleValue()==0)
					 * item.setActualPickWeightServer(BigDecimal.ONE);
					 * 
					 * item.setDealPrice(amount.divide(item.
					 * getActualPickWeightServer(), 2,
					 * BigDecimal.ROUND_HALF_UP));
					 * 
					 * dto.setTotalAmount(dto.getTotalAmount().add(item.
					 * getAmount()));
					 */
				});
				SysSetting attachment = sysSettingService.queryBySettingType("template_print_pic");
				dto.setTotalAmount(new BigDecimal(
						orderItems.stream().mapToDouble(a -> a.getAllowanceBuyerAmount().doubleValue()).sum()));
				out.put("needPage", needPage);
				out.put("items", orderItems);
				out.put("redPacketImg", attachment == null ? null : attachment.getSettingValue());
				out.put("order", dto);
				out.put("orderIds", orderIds);
				out.put("currDate", Tools.dateToStr(new Date(), "yyyy年MM月dd日"));
			}
		}

		return "order/print/printdeliveryletter";
	}

}
