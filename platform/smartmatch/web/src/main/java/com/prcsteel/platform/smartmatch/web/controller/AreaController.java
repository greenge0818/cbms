package com.prcsteel.platform.smartmatch.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.common.enums.OperateStatus;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.dto.AreaCityDto;
import com.prcsteel.platform.core.model.model.Area;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.query.AreaQuery;
import com.prcsteel.platform.smartmatch.service.AreaService;

/**
 * 基础数据之区域管理
 * 
 * @author peanut 
 *
 */

@Controller
@RequestMapping("smartmatch/area/")
public class AreaController extends BaseController {
	@Autowired
	private AreaService areaService;
	
	/**
	 * 区域管理首页
	 * @param out
	 */
	@RequestMapping("index")
	public void index(ModelMap out){
//		System.out.println("I am in base area. ");
	}
	
	/**
	 * 区域新增跳转
	 */
	@RequestMapping(value="create")
	public void create(){}

	/**
	 * 区域管理首页ajax加载列表数据
	 * @param areaQuery
	 * @return
	 */
	@RequestMapping(value="searchArea", method = RequestMethod.POST)
	@ResponseBody
	public PageResult ajaxSearchArea(AreaQuery areaQuery) {
		
     List<AreaCityDto> list = areaService.query(areaQuery);
     Integer total = areaService.queryTotal(areaQuery);
     List<AreaCityDto> resultList = areaService.getCnCityNames(list);
     PageResult result = new PageResult();
     result.setData(resultList);
     result.setRecordsFiltered(total);
     result.setRecordsTotal(resultList.size());
     return result;
	}
	/**
	 * 检查中心城市是否已被选择
	 * @param cityId    中心城市id
	 * @return
	 */
	@RequestMapping("checkCenterCity")
	@ResponseBody
	public Result checkCenterCity(@RequestParam("cityId") Long cityId,@RequestParam("id") Long id){
		
		Result result=new Result();
		AreaCityDto areaCity=areaService.findByCenterCityIdBesidesId(cityId,id);
		if(areaCity!=null){
			result.setData("中心城市已被选择,请另选其他城市");
		}else{
			result.setSuccess(Boolean.FALSE);
		}
		return result;
	}

	/**
	 * 添加基础区域
	 * @param zoneName       区域类别
	 * @param name           区域名称
	 * @param centerCityId   区域中心城市id
	 * @param refCityIds     区域周边城市 id集
	 * @param enable         是否启用
	 * @return
	 */
	@RequestMapping(value="add" ,method = RequestMethod.POST)
	@ResponseBody
	public Result add(
			@RequestParam(value="zoneName" ,required=true)  String zoneName,
			@RequestParam(value="name" ,required=true)  String name,
			@RequestParam(value="centerCityId",required=true)  Long centerCityId,
			@RequestParam("refCityIds")  String refCityIds,
			@RequestParam("isEnable")  String enable){
		
		Result result=new Result();
		if (StringUtils.isEmpty(centerCityId.toString()) || StringUtils.isEmpty(name)) {
            result.setData(OperateStatus.INVALID_PARAMETER.ordinal());
            return result;
        }
        String loginId = this.getLoginUser().getLoginId();
        Area area=new Area();
        area.setZoneName(zoneName);
        area.setName(name);
        area.setCenterCityId(centerCityId);
        area.setRefCityIds(refCityIds);
        area.setIsEnable(enable);
        area.setLastUpdatedBy(loginId);
        area.setCreatedBy(loginId);
        area.setCreated(new Date());
        area.setLastUpdated(new Date());
        
        Integer effect = areaService.add(area);
        
        if(effect !=null && effect >=1){
			result.setSuccess(Boolean.TRUE);
			result.setData("删除成功!");
		}else{
			result.setSuccess(Boolean.FALSE);
			result.setData("删除失败!");
		}

        return result;
	}
	/**
	 * 删除区域
	 * @param id   区域表主健id
	 * @return
	 */
	@RequestMapping(value="{id}/del", method=RequestMethod.POST)
	@ResponseBody
	public Result del(@PathVariable(value="id" ) Long id ){
		
		Result result=new Result();
		if(id == null){
			 result.setSuccess(Boolean.FALSE);
			 result.setData("参数不合法");
	         return result;
		}
		Integer effect=	areaService.deleteByPrimaryKey(id);
		if(effect !=null && effect >=1){
			result.setSuccess(Boolean.TRUE);
			result.setData("删除成功!");
		}else{
			result.setSuccess(Boolean.FALSE);
			result.setData("删除失败!");
		}
		return result;
	} 
	/**
	 * 根据id查询记录
	 * @param id   区域表主健id
	 * @return
	 */
	@RequestMapping(value="{id}/detail")
	public 	String findById( ModelMap out, @PathVariable(value="id" ) Long id ){
		if(id == null){
			 return "smartmatch/area/index";
		}
		Area area=areaService.selectByPrimaryKey(id);
		//中心城市id
		Long centerCityId=area.getCenterCityId();
		//周边城市ids
		String refCityIds=area.getRefCityIds();
		//周边城市
		List<City> refCitys=null;
		if(!StringUtils.isEmpty(refCityIds)){
			List<Long> refIds=Arrays.asList(refCityIds.split(",")).stream().map(s->Long.parseLong(s)).collect(Collectors.toList());
			refCitys=areaService.selectCityByIds(refIds);
		}
		
		List<Long> centerId=new ArrayList<Long>();
		centerId.add(centerCityId);
		//中心城市
		List<City> centerCity=areaService.selectCityByIds(centerId);
		
		out.put("area", area);
		out.put("refCitys", refCitys);
		out.put("centerCity", centerCity);
		
		return "smartmatch/area/create";
	}

	/**
	 * 编辑保存区域对象
	 * @param area
	 * @return
	 */
	@RequestMapping(value="edit",method=RequestMethod.POST )
	@ResponseBody
	public Result edit(Area area){
		
		String loginId = this.getLoginUser().getLoginId();
		area.setLastUpdated(new Date());
		area.setLastUpdatedBy(loginId);
		
		Integer effect=areaService.updateByPrimaryKeySelective(area);
		
		Result result=new Result();
		if(effect !=null && effect >=1){
			result.setSuccess(Boolean.TRUE);
			result.setData("保存成功!");
		}else{
			result.setSuccess(Boolean.FALSE);
			result.setData("保存失败!");
		}
		return result;
	}
}
