package com.prcsteel.platform.order.web.controller.api;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.order.model.AppOrder;
import com.prcsteel.platform.order.model.CountOrder;
import com.prcsteel.platform.order.model.EcOrder;
import com.prcsteel.platform.order.model.query.OrderEcAppQuery;
import com.prcsteel.platform.order.service.order.ConsignOrderService;
import com.prcsteel.platform.order.web.vo.EcOrderResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class EcOrderController {


	@Resource
	private ConsignOrderService consignOrderService;


	private static final String SUCCESSCODE = "0";
	private static final String FAILCODE = "-1";



	private static Logger logger = LoggerFactory.getLogger(EcOrderController.class);


	// APP或超市根据超市用户ID查询对应的订单信息
	@ApiOperation("APP或超市根据超市用户ID查询对应的订单信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "超市用户ID", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "status", value = "订单状态：RELATED、SECONDSETTLE、FINISH、CLOSED", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "时间点，格式： YYYY-MM-DD", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "endTime", value = "时间点，格式： YYYY-MM-DD", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "keyword", value = "订单号或品类", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "index", value = "页码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "length", value = "每页展示条数", dataType = "string", paramType = "query")
	})

	@RequestMapping(value="/order/getOrders.html",method=RequestMethod.GET)
	@ResponseBody
	public EcOrderResult getOrdersByEcId(String userId,String status,String startTime,String endTime,String keyword,
									   @RequestParam(value = "pageIndex", required = false) Integer index,
									   @RequestParam(value = "pageSize", required = false)  Integer length
									  ) {
		EcOrderResult result = new EcOrderResult();
		    OrderEcAppQuery query  = new OrderEcAppQuery();
			if (StringUtils.isEmpty(userId)) {
				result.setStatus(FAILCODE);
				result.setMessage("请传递超市用户ID");
				return result;
			}
			Integer ecUserId = 0;
			try {
				ecUserId = Integer.parseInt(userId);
			} catch (NumberFormatException e) {
				logger.error("转换超市用户ID出错:" + userId);
				result.setStatus(FAILCODE);
				result.setMessage("请传递有效的超市用户ID");
				return result;
			}
		    query.setUserId(ecUserId);
		    query.setStatus(status);
		    query.setStartTime(startTime);
			query.setEndTime(endTime);
		    query.setKeyword(keyword);
			if(index != null && length!=null) {
				query.setStart((index - 1) * length);
				query.setLength(length);
			}
			try {
				List<AppOrder> orders = consignOrderService.selectOrderByecUserId(query);
				/*if (orders.size() == 0) {
					result.setStatus(FAILCODE);
					result.setMessage("未找到指定的订单信息");
					return result;
				}*/
				Map orderMap = new HashMap();
				orderMap.put("list",orders);
				orderMap.put("total",consignOrderService.countOrderByecUserId(query));

				result.setStatus(SUCCESSCODE);
				result.setMessage("success");
				result.setData(orderMap);
			}catch (BusinessException e) {
				result.setStatus(e.getCode());
				result.setMessage(e.getMsg());
				logger.info(e.getMsg());
			}

		return result;
	}
	// 超市根据订单id查询对应的订单信息
	@ApiOperation("超市根据订单id清单查询对应的订单信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Ids", value = "订单id清单，以逗号隔开", dataType = "string", paramType = "query"),
	})
	@RequestMapping(value="/order/getOrdersByIds.html",method=RequestMethod.GET)
	@ResponseBody
	public EcOrderResult getOrdersByOrderIds(String Ids) {
		EcOrderResult result = new EcOrderResult();
		if (StringUtils.isEmpty(Ids)) {
			result.setStatus(FAILCODE);
			result.setMessage("请传递订单ID");
			return result;
		}
		List<Long> idList = new ArrayList<Long>();
		List<EcOrder> orders = null;
		try {
			for (String id : Ids.split(",")) {
				idList.add(Long.parseLong(id));
			}
		}catch(Exception ex){
			logger.error(ex.getMessage());
			result.setStatus(SUCCESSCODE);
			result.setMessage("success");
			result.setData(orders);
			return result;
		}

		try {
			orders = consignOrderService.selectByOrderIds(idList);
			/*if (orders.size() == 0) {
				result.setStatus(FAILCODE);
				result.setMessage("未找到指定的订单信息");
				return result;
			}*/
			result.setStatus(SUCCESSCODE);
			result.setMessage("success");
			result.setData(orders);
		}catch (BusinessException e) {
			result.setStatus(e.getCode());
			result.setMessage(e.getMsg());
			logger.error(e.getMsg());
		}

		return result;
	}


	// 超市根据超市用户ID查询对应的订单条数
	@ApiOperation("超市根据超市用户ID查询对应状态的订单条数")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "超市用户id", dataType = "string", paramType = "query"),
	})
	@RequestMapping(value="/order/getOrderCount.html",method=RequestMethod.GET)
	@ResponseBody
	public EcOrderResult countOrderByEcId(String userId) {
		EcOrderResult result = new EcOrderResult();
		if (StringUtils.isEmpty(userId)) {
			result.setStatus(FAILCODE);
			result.setMessage("请传递超市用户ID");
			return result;
		}
		Integer ecUserId = 0;
		try {
			ecUserId = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			logger.error("转换超市用户ID出错:" + userId);
			result.setStatus(FAILCODE);
			result.setMessage("请传递有效的超市用户ID");
			return result;
		}

		try {
			CountOrder order = consignOrderService.countEcOrder(ecUserId);
			/*if (order == null) {
				result.setStatus(FAILCODE);
				result.setMessage("未找到指定的订单信息");
				return result;
			}*/
			Map<String,Integer> orderMap = new HashMap<String,Integer>();
			if(order != null) {
				orderMap.put("ALL", order.getAllSum());
				orderMap.put("RELATED", order.getRelatedSum());
				orderMap.put("SECONDSETTLE", order.getSecondSettleSum());
				orderMap.put("FINISH", order.getFinishSum());
				orderMap.put("CLOSED", order.getClosedSum());
			}else{
				orderMap.put("ALL", 0);
				orderMap.put("RELATED", 0);
				orderMap.put("SECONDSETTLE", 0);
				orderMap.put("FINISH", 0);
				orderMap.put("CLOSED", 0);
			}
			result.setStatus(SUCCESSCODE);
			result.setMessage("success");
			result.setData(orderMap);
		}catch (BusinessException e) {
			result.setStatus(e.getCode());
			result.setMessage(e.getMsg());
			logger.info(e.getMsg());
		}

		return result;
	}
}
