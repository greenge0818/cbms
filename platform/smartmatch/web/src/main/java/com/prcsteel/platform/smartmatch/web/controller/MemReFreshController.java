package com.prcsteel.platform.smartmatch.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.service.MemReFreshService;

/**
 * 刷新缓存控制层Action类
 * @author caosulin@prcsteel.com
 *
 */
@Controller
@RequestMapping("/smartmatch/memReFresh")
public class MemReFreshController extends BaseController {
	
	@Resource
	private MemReFreshService memReFreshService = null;
	//增加日志
	private Logger logger = LoggerFactory.getLogger(MemReFreshController.class);
	
	/**
	 * 初始化页面
	 * @param out
	 */
	@RequestMapping("index")
	public void created(ModelMap out) {
		
	}
	
	
	/**
	 * 
	 */
	@RequestMapping("/refresh")
	@ResponseBody
	public Result refreshCache(@RequestParam("memKey") String memKey){
		Result result = new Result();
		try {
			memReFreshService.refresh(memKey);
		}catch(BusinessException bz){
			result.setSuccess(false);
			result.setData(bz.getMsg());
		} catch (Exception e) {
			logger.error("刷新缓存发生异常",e);
			result.setData("刷新缓存发生异常");
			result.setSuccess(false);
		}
		return result;
	}
	
}
