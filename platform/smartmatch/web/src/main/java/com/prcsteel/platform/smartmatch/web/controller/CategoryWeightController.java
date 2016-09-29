package com.prcsteel.platform.smartmatch.web.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.core.model.model.Materials;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.core.service.MaterialsService;
import com.prcsteel.platform.smartmatch.model.dto.CategoryWeightDto;
import com.prcsteel.platform.smartmatch.model.model.CategoryWeight;
import com.prcsteel.platform.smartmatch.model.query.CatagoryWeightQuery;
import com.prcsteel.platform.smartmatch.model.query.SingleWeightQuery;
import com.prcsteel.platform.smartmatch.service.CategoryWeightService;
import com.prcsteel.platform.smartmatch.service.FactoryService;
import com.prcsteel.platform.smartmatch.service.impl.ResourceServiceImpl;

@Controller
@RequestMapping("/smartmatch/categoryweight")
public class CategoryWeightController extends BaseController {
	
	// 增加日志
	private Logger logger = LoggerFactory.getLogger(CategoryWeightController.class);

	@Resource
	CategoryWeightService categoryWeightService;

	@Resource
	CategoryService categoryService;

	@Resource
	FactoryService factoryService;

	@Resource
	MaterialsService materialsService;

	// 物资单件重量表首页
	@RequestMapping("/list")
	public String categoryWeightList(ModelMap out) {
		out.put("categoryTypes", categoryService.getAllCategory());
		return "smartmatch/categoryWeight/list";
	}

	// 加载物资单件质量表数据
	@ResponseBody
	@RequestMapping("/search")
	public PageResult search(@RequestParam("factory") String factory,
			@RequestParam("category") String category,
			@RequestParam("norms") String norms, @RequestParam("start") Integer start,
			@RequestParam("length") Integer length) {

		// 请求参数封装
		CatagoryWeightQuery catagoryWeightQuery = new CatagoryWeightQuery();
		catagoryWeightQuery.setFactory(factory);
		catagoryWeightQuery.setCategory(category);
		catagoryWeightQuery.setNorms(norms);
		catagoryWeightQuery.setStart(start);
		catagoryWeightQuery.setLength(length);

		Integer total = categoryWeightService.totalCategoryWeight(catagoryWeightQuery);

		List<Map<String, Object>> list = categoryWeightService.selectByNameAndBusinessAndMaterialAndNorms(catagoryWeightQuery);

		PageResult result = new PageResult();
		result.setData(list);
		result.setRecordsFiltered(total);
		result.setRecordsTotal(list.size());

		return result;
	}

	// 编辑
	@ResponseBody
	@RequestMapping(value = { "/{id}/edit" }, method = { RequestMethod.POST })
	public Result edit(@PathVariable Long id) {
		Result result = new Result();
		CategoryWeightDto categoryWeightDto = categoryWeightService.selectByPrimaryKey(id);
		if (categoryWeightDto != null) {
			result.setSuccess(true);
			result.setData(categoryWeightDto);
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	// 删除
	@ResponseBody
	@RequestMapping(value = { "/{id}/delete" }, method = { RequestMethod.POST })
	public Result delete(@PathVariable Long id) {
		Result result = new Result();
		try {
			categoryWeightService.deleteByPrimaryKey(id);
			result.setSuccess(true);
			result.setData("删除成功！");
		} catch (BusinessException e) {
			logger.error("删除失败：" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	// 根据品名获取材质
	@ResponseBody
	@RequestMapping(value = "/getMaterials", method = RequestMethod.POST)
	public List<Materials> getMaterials(String categoryUuid) {
		return materialsService.queryMaterials(categoryUuid);
	}

	// 保存
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Result save(CategoryWeight categoryWeight) {
		Result result = new Result();
		User user = getLoginUser();
		try {
			if (categoryWeight.getId() == null) {
				categoryWeightService.addCategoryWeight(categoryWeight,user);
			} else {
				categoryWeightService.updateCategoryWeight(categoryWeight,user);
			}
			result.setSuccess(true);
			result.setData("保存成功！");
		} catch (BusinessException e) {
			logger.error("保存失败：" + e.getMsg());
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("/selectSingleWeightByParamIds")
	public Result selectSingleWeightByParamIds(SingleWeightQuery query){
		BigDecimal result = categoryWeightService.selectSingleWeightByParamIds(query);
		return new Result(result, result == null ? false : true);
	}
}
