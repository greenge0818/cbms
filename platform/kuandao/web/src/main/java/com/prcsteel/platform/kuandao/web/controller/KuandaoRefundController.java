package com.prcsteel.platform.kuandao.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.kuandao.model.dto.KuandaoDepositJournalDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoPaymentOrderDto;
import com.prcsteel.platform.kuandao.model.dto.KuandaoRefundDto;
import com.prcsteel.platform.kuandao.model.enums.KuandaoResultEnum;
import com.prcsteel.platform.kuandao.model.enums.MCLSImpStatus;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderSupportService;
import com.prcsteel.platform.kuandao.service.KuandaoRefundService;
import com.prcsteel.platform.kuandao.web.vo.PageResult;
import com.prcsteel.platform.kuandao.web.vo.Result;


@Controller
@RequestMapping("/kuandao/refund/")
public class KuandaoRefundController extends BaseController{
	@Resource
	private KuandaoRefundService kuandaoRefundService;
	@Resource
	private KuandaoPaymentOrderSupportService kuandaoPaymentOrderSupportService;
	
	@RequestMapping("index")
	public String index(ModelMap out){
		out.put("tab", "index");
		return "kuandao/refund/index";
	}
	
	@RequestMapping("hadRefund")
	public String hadRefund(ModelMap out){
		out.put("tab", "hadRefund");
		return "/kuandao/refund/hadRefund";
	}
	
	@RequestMapping("queryDepositAndOrder.html")
	@ResponseBody
	public PageResult queryDepositAndOrder(KuandaoDepositJournalDto dto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		
		int total=kuandaoRefundService.queryToTalDeposit(dto);
		List<KuandaoDepositJournalDto> list=kuandaoRefundService.queryDepositAndOrder(dto, start, length);
		return extractPageResult(total, list);
	}
	
	@RequestMapping("queryRefund.html")
	@ResponseBody
	public PageResult queryRefund(KuandaoRefundDto dto,
			@RequestParam("start") Integer start,
			@RequestParam("length") Integer length){
		dto.setImpStatus(MCLSImpStatus.refund.getCode());
		
		int total=kuandaoRefundService.queryTotalRefund(dto);
		List<KuandaoRefundDto> list= kuandaoRefundService.queryRefundByCondition(dto,start,length);
		return extractPageResult(total, list);
	}
	
	@RequestMapping("applyRefund.html")
	@ResponseBody
	public Result applyRefund(KuandaoRefundDto kuandaoRefundDto){
		Result result= new Result();
		String username = getLoginUser().getName();
		kuandaoRefundDto.setCreatedBy(username);
		int resultCode = kuandaoRefundService.applyRefund(kuandaoRefundDto);
		if(resultCode == KuandaoResultEnum.success.getCode()){
			result.setData("退款申请成功");
		}else if(resultCode == KuandaoResultEnum.businesserror.getCode()){
			result.setSuccess(false);
			result.setData("数据异常");
		}else if(resultCode == KuandaoResultEnum.readonly.getCode()){
			result.setSuccess(false);
			result.setData("汇入流水状态异常，退款申请失败");
		}else if(resultCode == KuandaoResultEnum.systemerror.getCode()){
			result.setSuccess(false);
			result.setData("向浦发申请退款失败");
		}
		return result;
	}
	
	@RequestMapping("generateOrder.html")
	@ResponseBody
	public Result generateOrder(KuandaoPaymentOrderDto kuandaoPaymentOrderDto){
		Result result= new Result();
		String username = getLoginUser().getName();
		kuandaoPaymentOrderDto.setCreatedBy(username);
		int resultCode = kuandaoPaymentOrderSupportService.generateOrder(kuandaoPaymentOrderDto);
		if(resultCode == KuandaoResultEnum.success.getCode()){
			result.setData("生成支付单成功");
		}else if(resultCode == KuandaoResultEnum.businesserror.getCode()){
			result.setSuccess(false);
			result.setData("数据异常");
		}else if(resultCode == KuandaoResultEnum.systemerror.getCode()){
			result.setSuccess(false);
			result.setData("提交支付单失败");
		}else if(resultCode ==KuandaoResultEnum.dataoperateerror.getCode()){
			result.setSuccess(false);
			result.setData("保存支付单失败");
		}else if(resultCode ==KuandaoResultEnum.readonly.getCode()){
			result.setSuccess(false);
			result.setData("支付单状态异常，提交失败");
		}else if(resultCode == KuandaoResultEnum.limitBank.getCode()){
			result.setSuccess(false);
			result.setData("该客户开户行是受限银行，不能生成支付单");
		}else if(resultCode == KuandaoResultEnum.nodata.getCode()){
			result.setSuccess(false);
			result.setData("客户不存在");
		}
		return result;
	}
	
	@RequestMapping("finishOrder.html")
	@ResponseBody
	public Result finishOrder(KuandaoDepositJournalDto kuandaoDepositJournalDto){
		Result result= new Result();
		String username = getLoginUser().getName();
		kuandaoDepositJournalDto.setCreatedBy(username);
		KuandaoPaymentOrderDto kuandaoPaymentOrderDto = new KuandaoPaymentOrderDto();
		kuandaoPaymentOrderDto.setImpAcqSsn(kuandaoDepositJournalDto.getImpAcqSsn());
		Integer resultCode = kuandaoPaymentOrderSupportService.finishOrder(kuandaoPaymentOrderDto);
		if(resultCode == KuandaoResultEnum.success.getCode()){
			result.setData("到货确认成功");
		}else if(resultCode == KuandaoResultEnum.businesserror.getCode()){
			result.setSuccess(false);
			result.setData("数据异常");
		}else if(resultCode == KuandaoResultEnum.systemerror.getCode() || resultCode == KuandaoResultEnum.timeout.getCode()){
			result.setSuccess(false);
			result.setData("向浦发到货确认失败");
		}else if(resultCode ==KuandaoResultEnum.dataoperateerror.getCode()){
			result.setSuccess(false);
			result.setData("修改支付单状态失败");
		}
		return result;
	}
}
