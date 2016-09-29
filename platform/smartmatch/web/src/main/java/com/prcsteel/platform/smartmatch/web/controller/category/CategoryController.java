package com.prcsteel.platform.smartmatch.web.controller.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.prcsteel.platform.smartmatch.model.query.CategoryNameQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.dto.CategoryAliasDto;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.service.CategoryGroupService;
import com.prcsteel.platform.core.service.CategoryService;
import com.prcsteel.platform.core.service.NormsService;
import com.prcsteel.platform.smartmatch.web.controller.BaseController;
import com.prcsteel.platform.common.vo.PageResult;
import com.prcsteel.platform.common.vo.Result;

/**
 * Created by chenchen on 15-7-31.
 */
@Controller
@RequestMapping("/category/")
public class CategoryController extends BaseController {

	@Resource
	CategoryGroupService categoryGroupService;
	@Resource
	CategoryService categoryService;
	//增加日志
	private Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Resource
	NormsService normsService;
	//modify by zhoucai@prcsteel.com 获取acldomain 2016-5-18
	@Value("${acl.domain}")
	private String aclDomain;
	@RequestMapping("index.html")
	public void index(ModelMap out) {
		out.put("message", "类别");
	}

	@RequestMapping("showcategory.html")
	public String showCategory(ModelMap out) {
		out.put("result", categoryGroupService.queryAllCategoryGroupInner());
		return "sys/category";

	}

	@RequestMapping("addcategory.html")
	public String addCategory(ModelMap out) {
		out.put("category", categoryService.getAllCategory());
		return "sys/categoryAdd";

	}

	@RequestMapping("editcategory.html")
	public String editCategory(ModelMap out, @RequestParam("uuid") String uuid) {
		out.put("group", categoryGroupService.selectByUUID(uuid));
		out.put("category", categoryService.getAllCategory());
		return "sys/categoryEdit";

	}

	@RequestMapping("deleteCategory.html")
	public String deleteCategory(ModelMap out, @RequestParam("uuid") String group_uuid) {
		this.categoryGroupService.deleteCategoryGroup(group_uuid, getLoginUser().getName());
		return this.showCategory(out);
	}

	@RequestMapping(value = { "saveCategory.html" }, method = { RequestMethod.POST })
	public String saveReward(ModelMap out, @RequestParam("categoryName") String categoryName,
			@RequestParam("cateCheck") String[] cateCheck) {
		this.categoryGroupService.addCategoryGroup(categoryName, cateCheck, getLoginUser().getName());
		return this.showCategory(out);
	}

	@RequestMapping(value = { "saveCategoryEdit.html" }, method = { RequestMethod.POST })
	public String saveRewardEdit(ModelMap out, @RequestParam("categoryName") String categoryName,
			@RequestParam("group_uuid") String group_uuid, @RequestParam("cateCheck") String[] cateCheck) {
		this.categoryGroupService.editCategoryGroup(group_uuid, categoryName, cateCheck, getLoginUser().getName());
		return this.showCategory(out);
	}

	@RequestMapping("getAllCategory")
	@ResponseBody
	public Result getAllCategory(){
		Result result = new Result();
		List<Category> list = categoryService.getAllCategoryModel();
		result.setSuccess(true);
		result.setData(list);
		return result;
	}
	
	
	// 品名设置首页
	@RequestMapping("list")
	public String categoryList(ModelMap out) {

		//modify by zhoucai@prcsteel.com 获取acldomain 大类设置跳转acl系统 2016-5-18
		out.put("aclDomain",aclDomain);
		return "resource/category/list";
	}	
	
	//加载品名列表数据
	@RequestMapping("searchCategoryList")
    public @ResponseBody PageResult search(CategoryNameQuery query) {
        Integer total = categoryService.totalCategory(query);
        
        List<Map<String, Object>> list = categoryService
                .selectByCategoryName(query);

        PageResult result = new PageResult();
        result.setData(list);
        result.setRecordsFiltered(total);
        result.setRecordsTotal(list.size());

        return result;
    }
	
	// 编辑
	@ResponseBody
	@RequestMapping(value = { "{id}/edit" }, method = { RequestMethod.POST })
	public Result edit(@PathVariable Integer id) {
		Result result = new Result();
		Category category = categoryService.selectByPrimaryKey(id);
		if (category != null) {
			result.setSuccess(true);
			result.setData(category);
		} else {
			result.setSuccess(false);
		}
		return result;
	}
	
	// 删除
	@ResponseBody
	@RequestMapping(value = { "{id}/delete" }, method = { RequestMethod.POST })
	public Result delete(@PathVariable Integer id) {
		Result result = new Result();
		try {
			categoryService.deleteByPrimaryKey(id);
			result.setSuccess(true);
			result.setData("删除成功！");
		} catch (BusinessException e) {
			result.setSuccess(false);
			result.setData(e.getMsg());
		}
		return result;
	}
	
	// 保存
    @ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public Result saveUser(Category category){
		Result result = new Result();
		User user = getLoginUser();
        try {
        	 categoryService.updateCategory(category,user);
        	 result.setSuccess(true);
             result.setData("保存成功！");
        } catch (BusinessException e) {
            result.setSuccess(false);
            result.setData(e.getMsg());
        }
        return result;
	}
    
    /**
     * tuxianming： 进项票品名映射设置 mapping
     * @return
     */
    @RequestMapping("categoryalias.html")
	public String showCategoryAlias() {
		return "sys/categoryalias";

	}

	/**
	 * tuxianming
	 * 进项票品名映射设置：查找所有的品类名 
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "loadcategoryalias.html", method = RequestMethod.GET)
  	public Result loadCategoryAlias() {
      	List<CategoryAliasDto> ass =  categoryService.queryCategoryAliasList();
      	return new Result(ass);
  	}
    
    /**
     * tuxianming
     * 进项票品名映射设置：根据别名模糊查找所有的品类名 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loadcategoryaliaslikealias.html", method = RequestMethod.POST)
    public Result loadCategoryaliasLikeAlias(String aliasName) {
    	Result result = new Result();
		List<CategoryAlias> ass = categoryService.queryCategoryAliasLikeAlias(aliasName);
		result.setData(ass);
    	return result;
    }
    
    /**
	 * tuxianming
	 * 进项票品名映射设置：保存和更新
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "addcategoryalias.html", method = RequestMethod.POST)
    public Result addCategoryAlias(CategoryAliasDto query){
    	
    	Result result = null;
    	try {
    		List<String> ids = categoryService.saveCategoryAlias(query, query.getAlias(), getLoginUser());
    		result = new Result(ids);
		} catch (Exception e) {
			result = new Result("保存失败！", false);
			logger.error("addCategoryAlias，进项票品名映射设置，保存和更新报错：" + e.getMessage());
		}
    	
		return result;
    }
    
    /**
	 * tuxianming
	 * 进项票品名映射设置：删除一条别名
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value = "deletecategoryalias.html", method = RequestMethod.GET)
    public Result deleteCategoryAlias(Long id){
    	
    	Result result = null;
    	try {
    		categoryService.deleteCategoryAlias(id);
    		result = new Result();
		} catch (Exception e) {
			result = new Result("删除失败！", false);
			logger.error("deleteCategoryAlias，删除一条别名：" + e.getMessage());
		}
    	
		return result;
		
    }
    
}
