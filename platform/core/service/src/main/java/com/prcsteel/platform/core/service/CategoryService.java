package com.prcsteel.platform.core.service;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.core.model.dto.CategoryAliasDto;
import com.prcsteel.platform.core.model.dto.CategoryDto;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.smartmatch.model.query.CategoryNameQuery;

/**
 * Created by chenchen on 2015/8/6.
 */
public interface CategoryService {
	
	/**
	 * 获取所有品类
	 * @return
	 */
	public List<CategoryDto> getAllCategory();

	/**
	 * 开单时调用
	 * 查询cbms里面所有大类以及大类下面的小类
	 * @return
	 */
	List<Map<String, Object>> queryAllCategoryData();
	
	/**
	 * Add by wangyixiao on 2015/11/19.
	 */
	public List<Map<String, Object>> selectByCategoryName(CategoryNameQuery query);
	
	int totalCategory(CategoryNameQuery query);
	
	Category selectByPrimaryKey(Integer id);
	
	public void deleteByPrimaryKey(Integer id);
	
	public void addCategory(Category category);
	
	public void updateCategory(Category category, User user);
	
	List<Category> getAllCategoryModel();

	Category queryByCategoryUuid(String uuid);
	
	List<CategoryAliasDto> queryCategoryAliasList();

	public List<String> saveCategoryAlias(CategoryAliasDto saveObj, List<CategoryAlias> updateObjs, User user);

	public void deleteCategoryAlias(Long id);

	public List<CategoryAlias> queryCategoryAliasLikeAlias(String aliasName);
}
