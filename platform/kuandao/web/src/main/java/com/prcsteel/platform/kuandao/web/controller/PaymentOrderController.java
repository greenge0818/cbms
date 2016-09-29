package com.prcsteel.platform.kuandao.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.prcsteel.platform.kuandao.common.util.SpdbHttpsPost;
import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.PaymentOrderSynchLogDto;
import com.prcsteel.platform.kuandao.model.enums.KuandaoChargStatus;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.OrderSubmitStatusEnum;
import com.prcsteel.platform.kuandao.model.model.PrcsteelAccountInfo;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderSupportService;
import com.prcsteel.platform.kuandao.web.vo.PageResult;
import com.prcsteel.platform.kuandao.web.vo.Result;

@Controller
@RequestMapping("/kuandao/payment/")
public class PaymentOrderController extends BaseController{
	@Resource
	private KuandaoPaymentOrderSupportService kuandaoPaymentOrderSupportService;

	@Resource
	private SpdbHttpsPost spdbHttpsPost;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentOrderController.class);

	
	@RequestMapping("index")
	public String index(ModelMap out){
		out.put("tab", "index");
		return "kuandao/payment/index";
	}
	@RequestMapping("abnormalpaymentorder")
	public String unOpenAccount(ModelMap out){
		out.put("tab", "abnormalpaymentorder");
		return "/kuandao/payment/abnormalpaymentorder";
	}
	
	@RequestMapping("paymentordersynchronizelog")
	public String synchronizeLog(ModelMap out){
		out.put("tab", "paymentordersynchronizelog");
		return "/kuandao/payment/paymentordersynchronizelog";
	}
	
	@RequestMapping("finishedpaymentorder")
	public String finishedpaymentorder(ModelMap out){
		out.put("tab", "finishedpaymentorder");
		return "/kuandao/payment/finishedpaymentorder";
	}
	
	//1、分页查询所有订单信息 待提交、待匹配、已匹配、支付完成
	@RequestMapping("querypayment.html")
	@ResponseBody
	public PageResult queryPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total=kuandaoPaymentOrderSupportService.totalPaymentOrders(paymentOrderDto);
		List<KuandaoPaymentOrderDto> list=kuandaoPaymentOrderSupportService.queryPaymentOrders(paymentOrderDto,start,length);
		return extractPageResult(total, list);
	}
	
	//1、分页查询所有订单信息 待提交、待匹配、已匹配、支付完成
	@RequestMapping("queryfinishedpayment.html")
	@ResponseBody
	public PageResult queryfinishedpayment(KuandaoPaymentOrderDto paymentOrderDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		Integer submitStatus = paymentOrderDto.getSubmitStatus();
		//前台页面传入参数，2 代表已充值
		if(submitStatus != null){
			if(submitStatus.compareTo(1) == 0){
				paymentOrderDto.setChargStatus(KuandaoChargStatus.init.getCode());
			}else if(submitStatus.compareTo(2) == 0){
				paymentOrderDto.setChargStatus(KuandaoChargStatus.finish.getCode());
			}
		}
		paymentOrderDto.setSubmitStatus(OrderSubmitStatusEnum.finish.getCode());
		int total=kuandaoPaymentOrderSupportService.totalPaymentOrders(paymentOrderDto);
		List<KuandaoPaymentOrderDto> list=kuandaoPaymentOrderSupportService.queryPaymentOrders(paymentOrderDto,start,length);
		return extractPageResult(total, list);
	}

	//2、分页查询所有异常支付订单 提交失败，可以再次提交或更改提交
	@RequestMapping("queryabnormalpayment.html")
	@ResponseBody
	public PageResult queryAbnormalPaymentOrders(KuandaoPaymentOrderDto paymentOrderDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total=kuandaoPaymentOrderSupportService.totalAbnormalPaymentOrders(paymentOrderDto);
		List<KuandaoPaymentOrderDto> list=kuandaoPaymentOrderSupportService.queryAbnormalPaymentOrders(paymentOrderDto,start,length);
		return extractPageResult(total, list);
	}
	//3、支付单日志
	@RequestMapping("querySynchronizeLog.html")
	@ResponseBody
	public PageResult querySynchronizeLog(PaymentOrderSynchLogDto synLogDto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		int total = kuandaoPaymentOrderSupportService.totalSynchronizeLog(synLogDto);
		List<PaymentOrderSynchLogDto> list = kuandaoPaymentOrderSupportService.querySynchronizeLog(synLogDto,start,length);
		return extractPageResult(total, list);
	}
	
	
	//2、更改支付单、同步到数据库
	@RequestMapping("modifyOrder.html")
	@ResponseBody
	public Result modifyOrder(KuandaoPaymentOrderDto dto){
		Result result=new Result();
		
		String userName=getLoginUser().getName();
		dto.setCreatedBy(userName);
		
		int count=kuandaoPaymentOrderSupportService.modifyOrder(dto);
		if(count==1){
			result.setSuccess(true);
			result.setData("更新成功");
		}else{
			result.setData("支付单信息更改失败");
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("submitAgain.html")
	@ResponseBody
	public Result orderSubmitAgain(@RequestParam("id") int id){
		Result result = new Result();
		String userName = getLoginUser().getName();
		
		int resultCode = kuandaoPaymentOrderSupportService.orderSubmitAgain(id,userName);
		if(resultCode == 1){
			result.setSuccess(true);
		}else if(resultCode ==KuandaoResultEnum.readonly.getCode()){
			result.setSuccess(false);
			result.setData("支付单状态异常，提交失败");
		}else{
			result.setSuccess(false);
			result.setData("提交失败，请查看同步日志");
		}
		return result;
	}
	
	@RequestMapping("kuandaoTakeNumber.html")
	public void kuandaoTakeNumber(HttpServletRequest request,HttpServletResponse response){
		PrcsteelAccountInfo info=kuandaoPaymentOrderSupportService.kuandaoTakeNumber();
		Gson gson=new Gson();
		String json=gson.toJson(info);
	 	String jsoncallback=request.getParameter("jsoncallback");
		String result=jsoncallback+"("+json+")";
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(result.getBytes());
			out.flush();
		} catch (IOException e) {
			logger.error("kuandaoTakeNumber failed",e);
		}finally{
			if(out != null){
				IOUtils.closeQuietly(out);
			}
		}
		
 	}
	
	
	@RequestMapping("chargResultNotify.html")
	@ResponseBody
	public ChargResultResponse chargResultNotify(ChargResultNotifyDto chargResultNotifyDto){
		return kuandaoPaymentOrderSupportService.chargResultNotify(chargResultNotifyDto);
	}
	
	@RequestMapping("charg.html")
	@ResponseBody
	public Result charg(@RequestParam("id") Integer id){
		Result result = new Result();
		Integer resultCode = kuandaoPaymentOrderSupportService.charg(id);
		if(resultCode.compareTo(KuandaoResultEnum.success.getCode()) == 0){
			result.setSuccess(true);
		}else if(resultCode.compareTo(KuandaoResultEnum.nodata.getCode()) == 0){
			result.setSuccess(false);
			result.setData("支付单不存在，请刷新重试");
		}else if(resultCode.compareTo(KuandaoResultEnum.businesserror.getCode()) == 0){
			result.setSuccess(false);
			result.setData("充值失败，请稍后再试");
		}else if(resultCode.compareTo(KuandaoResultEnum.readonly.getCode()) == 0){
			result.setSuccess(false);
			result.setData("正在充值，请稍后再试");
		}else if(resultCode.compareTo(KuandaoResultEnum.duplicate.getCode()) == 0){
			result.setSuccess(false);
			result.setData("存在多条数据，请联系管理员");
		}
		return result;
	}
}
