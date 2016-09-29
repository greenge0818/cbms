package com.prcsteel.platform.smartmatch.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.common.exception.BusinessException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.smartmatch.model.model.Factory;
import com.prcsteel.platform.smartmatch.service.FactoryService;

import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by Wangyx on 2015/11/12.
 */

@Controller
@RequestMapping("/smartmatch/factory")
public class FactoryController extends BaseController {
	
	//增加日志
	private Logger logger = LoggerFactory.getLogger(FactoryController.class);
	
	@Resource
 	FactoryService factoryService;

     //钢厂管理首页
	@RequestMapping("/list")
	public String factoryList(){
		return "smartmatch/factory/list";
	} 
	
     //加载钢厂数据
	@ResponseBody
	@RequestMapping("/search")
    public PageResult search(
    		@RequestParam("factoryName") String factoryName,
			@RequestParam("factoryCity") String factoryCity,
			@RequestParam("factoryBusiness") String factoryBusiness,
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length) {

        // 请求参数封装
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("factoryName", factoryName);
		paramMap.put("factoryCity", factoryCity);
		paramMap.put("factoryBusiness", factoryBusiness);
        paramMap.put("start", start);
        paramMap.put("length", length);
 
        Integer total = factoryService.totalFactory(paramMap);
        
        List<Map<String, Object>> list = factoryService
                .selectByFactoryNameAndCityAndBusiness(paramMap);

        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());

        return result;
    }
	
	//保存
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result saveFactory(Factory factory){
		Result result = new Result();
		User user = getLoginUser();
        try {
        	if(factory.getId() == null){
        		factoryService.addFactory(factory,user);
        	}
        	else{
        		factoryService.updateFactory(factory,user);
        	}
        	 result.setSuccess(true);
             result.setData("保存成功！");
        } catch (BusinessException e) {
        	logger.error("保存失败" + e.getMsg());
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
	}
	
	//编辑
	@ResponseBody
	@RequestMapping(value = { "/{id}/edit" }, method = { RequestMethod.POST })
	public Result factoryEdit(@PathVariable("id") Long id){
		Result result = new Result();
		Factory factory = factoryService.selectByPrimaryKey(id);
		if(factory != null) {
			result.setSuccess(true);
			result.setData(factory);
		} else{
			result.setSuccess(false);
		}
		return result;	
	}
	
	//删除
	@ResponseBody
	@RequestMapping(value = { "/{id}/delete" }, method = { RequestMethod.POST })
	public Result factoryDelete(@PathVariable("id") Long id){
		Result result = new Result();
        try {
        	factoryService.deleteByPrimaryKey(id);
        	 result.setSuccess(true);
             result.setData("删除成功！");
        } catch (BusinessException e) {
        	logger.error("删除失败" + e.getMsg());
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
	}
	
	/**
	 * 获取所有钢厂
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllFactory", method = RequestMethod.POST)
	public Result getFactory() {
		Result result = new Result();
		result.setData(factoryService.getAllFactory());
		return result;
	}
	
}
