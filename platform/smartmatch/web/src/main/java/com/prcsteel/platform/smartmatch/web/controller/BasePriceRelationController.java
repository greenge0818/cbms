package com.prcsteel.platform.smartmatch.web.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.smartmatch.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prcsteel.platform.acl.model.model.Organization;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDetailDto;
import com.prcsteel.platform.smartmatch.model.dto.CustBasePriceRelationDto;
import com.prcsteel.platform.smartmatch.model.model.CustBasePriceRelation;
import com.prcsteel.platform.smartmatch.model.query.CustBasePriceRelationQuery;
import com.prcsteel.platform.smartmatch.service.BasePriceRelationService;
import com.prcsteel.platform.smartmatch.service.QuotationService;

/**
 * 基价关联设置
 * Created by caosulin on 2016/9/13.
 */
@Controller
@RequestMapping("/smartmatch/basePriceRelation/")
public class BasePriceRelationController extends BaseController {
	
	//增加日志
	private Logger logger = LoggerFactory.getLogger(BasePriceRelationController.class);
	
	@Resource
	private BasePriceRelationService basePriceRelationService = null;
	
	@Resource
	private QuotationService quotationService = null;
	
	@Resource
	AreaService areaService;

	/**
	 * 基价管理
	 * @param out
	 * @author caochao
	 */
	@RequestMapping("index.html")
	public void baseprice(ModelMap out){
		//地区集合
		AreaQuery query = new AreaQuery();
		query.setIsEnable("1");
		query.setStart(0);
		query.setLength(100);
		List<AreaCityDto> cityList = areaService.query(query);
		User user = getLoginUser();
		Organization org = getOrganization();
		City city = quotationService.selectCity(org.getCityId());

		out.put("user", user);
		out.put("city", city);
		out.put("cityList", cityList);
	}
	
	/**
	 * 基价关联设置
	 * @param out
	 * @author caosulin
	 */
	@RequestMapping("basepricerelation.html")
	public void basePriceRelation(ModelMap out){
		List<AreaCityDto> cityList = basePriceRelationService.getAllCity();
		User user = getLoginUser();
		Organization org = organizationService.queryById(user.getOrgId());
		City city = basePriceRelationService.selectCity(org.getCityId());		
		out.put("cityList", cityList);
		out.put("city", city);
	}
	
	/**
	 * 基价格关联设置列表查询
	 * @param CustBasePriceRelationQuery
	 * @return
	 */
	@RequestMapping("/searchbasepricerelation.html")
	@ResponseBody
	public PageResult searchBasePriceRelation(CustBasePriceRelationQuery query) {
		Integer total = basePriceRelationService.getBasePriceRelationListTotal(query);
		List<CustBasePriceRelation> list = new ArrayList<CustBasePriceRelation>();
		if(total > 0){
			list = basePriceRelationService.getBasePriceRelationList(query);
		}
        PageResult result=new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(total);
        return result;
	}
	
	/**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * 
	 * @return
	 */
	@RequestMapping(value="getCommonData")
	@ResponseBody
	public Result getCommonData() {
		return new Result(basePriceRelationService.getCommonData());
	}
	
	/**
	 * 获取材质
	 * @return
	 */
	@RequestMapping(value="queryMaterials")
	@ResponseBody
	public Result queryMaterials(String categoryUuid) {
		return new Result(basePriceRelationService.queryMaterials(categoryUuid));
	}
	
	/**
	 * 获取所有卖家、钢厂、仓库、品名的数据
	 * 
	 * @return
	 */
	@RequestMapping(value="checkBasePrice")
	@ResponseBody
	public Result checkBasePrice(Long accountId,Long cityId, Long basePriceId) {
		Result result =  new Result();
		Boolean flag = basePriceRelationService.checkBasePrice( accountId, cityId,  basePriceId);
		result.setSuccess(flag);
		return result;
	}
	
	
	/**
	 * 基价关联设置获取所有的基价
	 * @param cityId
	 * @author afeng
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryBasePrice.html", method = RequestMethod.POST)
	public Result queryBasePrice(Long cityId) {
		Result result = new Result();
		List<CustBasePriceDto> list = null;
		try {
			list = basePriceRelationService.queryBasePriceByCityId(cityId);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setData("获取所有基价失败");
			logger.error("获取所有基价失败",e);
		}
		result.setData(list);
		return result;
	}
	

	/**
	 * 获取基价关联设置失败
	 * @param id
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "getBasePriceRelation.html", method = RequestMethod.POST)
	public Result getBasePriceRelationById(Long id) {
		Result result = new Result();
		CustBasePriceRelationDto dto = null;
		try {
			dto = basePriceRelationService.queryBasePriceRelationById(id);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setData("获取基价关联设置失败");
			logger.error("获取基价关联设置失败",e);
		}
		result.setData(dto);
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "deleteBasePriceRelation.html", method = RequestMethod.POST)
	public Result deleteBasePriceRelation(Long id) {	
		Result result = new Result();
		result.setData("保存成功");
		try {
			basePriceRelationService.deleteBasePriceRelation(id);
		} catch (BusinessException e1){
			result.setSuccess(false);
			result.setData(e1.getMsg());
		}catch (Exception e) {
			result.setSuccess(false);
			result.setData("删除失败！");
			logger.error("删除失败",e);
		}
		return result;
	
	}
	/**
	 * 保存基价关联设置
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveBasePriceRelation.html", method = RequestMethod.POST)
	public Result saveBasePriceRelation(CustBasePriceRelationDto dto,String items) {
		List<CustBasePriceRelationDetailDto> list = new Gson().fromJson(items,
				new TypeToken<List<CustBasePriceRelationDetailDto>>() {
				}.getType());
		dto.setDetails(list);
		User user = this.getLoginUser();
		
		Result result = new Result();
		result.setData("保存成功");

		try {
			basePriceRelationService.saveBasePriceRelation(dto,user);
		} catch (BusinessException e1){
			result.setSuccess(false);
			result.setData(e1.getMsg());
		}catch (Exception e) {
			result.setSuccess(false);
			result.setData("保存失败！");
			logger.error("保存失败",e);
		}
		return result;
	}
	

	/**
	 * 更新基价信息
	 * @param releaseDateList
	 * @author caochao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "refreshquote.html", method = RequestMethod.POST)
	public Result releasequote(String releaseDateList) {
		Result result = new Result();
		try {
			if (quotationService.releaseQuote(releaseDateList, false, getLoginUser())) {
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setData("更新报价失败！");
			}
		} catch (BusinessException ex) {
			logger.error("更新报价失败！" + ex.getMsg());
			result.setSuccess(false);
			result.setData(ex.getMsg());
		}
		return result;
	}

}
