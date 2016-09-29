package com.prcsteel.platform.account.web.controller;
//package com.prcsteel.cbms.web.controller;
//
//import java.io.IOException;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.google.gson.Gson;
//import com.prcsteel.platform.order.model.exception.BusinessException;
//import Consts;
//import Result;
//
///*
// *
// *@author zhoukun
// */
//@Component
//public class GlobalExceptionHandler implements HandlerExceptionResolver {
//
//	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//	
//	@Override
//	public ModelAndView resolveException(HttpServletRequest request,
//			HttpServletResponse response, Object handler, Exception ex) {
//		String errorMessage = null;
//		String errorCode = null;
//		// 业务类异常处理
//    	ModelAndView mv = new ModelAndView();
//    	if(ex instanceof BusinessException){
//    		BusinessException bus = (BusinessException)ex;
//    		errorCode = bus.getCode();
//    		errorMessage = bus.getMsg();
//    	}else{
//    		errorMessage = ex.getMessage();
//    	}
//    	// 已知其他异常处理
//		logger.error(ex.getMessage(),ex);
//		String requestBy = request.getHeader(Constant.HTTP_HEAD_REQUEST_WITH);
//		if(requestBy !=null && requestBy.toLowerCase().equals(Constant.AJAX_REQUEST_HEAD_VALUE)){
//			Result res = new Result();
//			res.setSuccess(false);
//			res.setData(String.format("code:%s,message:%s", errorCode,errorMessage));
//			Gson gson = new Gson();
//			byte[] byteRes = gson.toJson(res).getBytes();
//			try {
//				ServletOutputStream out = response.getOutputStream();
//				out.write(byteRes, 0, byteRes.length);
//				out.close();
//			} catch (IOException e) {
//				logger.error("write json result failed!",e);
//			}
//			return null;
//		}else{
//			mv.getModelMap().put("errorCode", errorCode);
//    		mv.getModelMap().put("errorMessage", errorMessage);
//    		mv.setViewName("error");
//		}
//    	return mv;
//	}
//
//}
