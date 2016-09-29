package com.prcsteel.platform.kuandao.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.kuandao.model.dto.ChargResultNotifyDto;
import com.prcsteel.platform.kuandao.model.dto.ChargResultResponse;
import com.prcsteel.platform.kuandao.model.dto.spdb.SPDBNotifyRequstParam;
import com.prcsteel.platform.kuandao.service.KuandaoAccountService;
import com.prcsteel.platform.kuandao.service.KuandaoPaymentOrderSupportService;

@Controller
@RequestMapping("/notify/")
public class NotifyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);
	
	@Resource(name="kuandao.service.AccountService")
	private KuandaoAccountService kuandaoAccountService;
	
	@Resource
	private KuandaoPaymentOrderSupportService kuandaoPaymentOrderSupportService;
	
	
	@RequestMapping("boundNotify.html")
	public void boundNotify(HttpServletRequest request, HttpServletResponse response,SPDBNotifyRequstParam spdbRequstParam){
		OutputStream out = null;
		String membercode = request.getParameter("memberCode");
		String operType = request.getParameter("operType");
		try {
			String responseXml = kuandaoAccountService.boundNotify(spdbRequstParam, membercode, operType);
			if(StringUtils.isNotEmpty(responseXml)){
				out = response.getOutputStream();
				out.write(responseXml.getBytes());
				out.flush();
			}
		} catch (IOException e) {
			logger.error("process spdb SMBD failed",e);
		}finally{
			if(out != null){
				IOUtils.closeQuietly(out);
			}
		}
	}
	
	@RequestMapping("matchOrderNotify.html")
	public void matchOrderNotify(HttpServletRequest request, HttpServletResponse response,SPDBNotifyRequstParam spdbRequstParam){
	
		logger.debug("spdb matchOrderNotify ");
		OutputStream out = null;
		try {
			String paymentOrderCode = request.getParameter("paymentOrderCode");
			String transDate = request.getParameter("transDate");
			String transAmt = request.getParameter("transAmt");
			String responseXml = kuandaoPaymentOrderSupportService.matchOrderNotify(spdbRequstParam,paymentOrderCode,transDate,transAmt);
			if(StringUtils.isNotEmpty(responseXml)){
				out = response.getOutputStream();
				out.write(responseXml.getBytes());
				out.flush();
			}
		} catch (IOException e) {
			logger.error("process spdb SMBD failed",e);
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
	
	
}
