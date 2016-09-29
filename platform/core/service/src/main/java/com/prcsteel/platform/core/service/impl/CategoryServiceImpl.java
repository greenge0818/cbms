package com.prcsteel.platform.core.service.impl;


import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.prcsteel.platform.core.model.dto.*;
import com.prcsteel.platform.core.model.model.CategoryAlias;
import com.prcsteel.platform.core.model.query.CategoryAliasQuery;
import com.prcsteel.platform.core.persist.dao.CategoryAliasDao;
import com.prcsteel.platform.smartmatch.model.query.CategoryNameQuery;
import groovyjarjarantlr.collections.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.CategoryGroup;
import com.prcsteel.platform.core.persist.dao.CategoryDao;
import com.prcsteel.platform.core.persist.dao.CategoryGroupDao;
import com.prcsteel.platform.core.persist.dao.GroupForCategoryDao;
import com.prcsteel.platform.core.service.CategoryService;

/**
 * Created by chenchen on 2015/8/6.
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	//private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	private GroupForCategoryDao groupForCategoryDao;
	@Autowired
	private CategoryDao categoryDao;
	@Resource
	private CategoryAliasDao categoryAliasDao;
	@Resource
	private CategoryGroupDao categoryGroupDao;
	@Override
	public List<CategoryDto> getAllCategory() {
		List<CategoryDto> cateList=categoryDao.getAllCategory();
		List<CategoryGroupRecordDto> recordList =groupForCategoryDao.queryAllReord();
		for (CategoryGroupRecordDto recordDto : recordList) {
			
			for (CategoryDto categoryDto : cateList) {
				/*
				 * 找到这条记录有对应的类别
				 */
				if (recordDto.getCategoryUuid().equals(categoryDto.getUuid())) {
					if (recordDto.getSiteUuid().equals("inner_cbms")) {						
						CategoryGroup cateGroup=new CategoryGroup();
						cateGroup.setName(recordDto.getCategoryGroupName());
						cateGroup.setUuid(recordDto.getCategoryGroupUuid());
						categoryDto.setInner_group(cateGroup);
					}else if(recordDto.getSiteUuid().equals("outer_market")){
						CategoryGroup cateGroup=new CategoryGroup();
						cateGroup.setName(recordDto.getCategoryGroupName());
						cateGroup.setUuid(recordDto.getCategoryGroupUuid());
						categoryDto.setOuter_group(cateGroup);
					}
				}
			}
			System.out.println("");
			//TODO 如果没有任何归属，此类目为未设置
		}
		return cateList;
	}

	/**
	 * 根据所有小类和大类uuid
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryAllCategoryData() {
		List<Map<String, Object>> data = new LinkedList<>();
		/**
		 *  Map<String, Object>
		 statusCode 0
		 Massage 请求成功
		 total count()
		 isLogin false
		 data        List<Map<String, Object>>
		             sortID
		             sortName
		             classInfo     List<Map<String, Object>>
		                           classID
		                           className
		                           nsort         List<Map<String, Object>>
		                                         nsortID
		                                         nsortName
		 */
		List<CategoryGroupDto> parentCategoryGroupList = categoryGroupDao.queryAllParentCategoryGroupOuter();
		List<CategoryGroupDto> categoryGroupList = categoryGroupDao.queryAllCategoryGroupOuter();
		List<CategoryInfoDto> categoryList = categoryDao.queryAllCategoryAndGroup();
		if (parentCategoryGroupList.size() >= 1 && categoryList.size() >= 1) {
			Map<String, List<CategoryGroupDto>> categoryGroup = categoryGroupList.stream().collect(Collectors.groupingBy(CategoryGroupDto::getParentUuid));
			Map<String, List<CategoryInfoDto>> category = categoryList.stream().collect(Collectors.groupingBy(CategoryInfoDto::getGroupUuid));
			for (CategoryGroupDto dto : parentCategoryGroupList) {
				Map<String, Object> tempData = new HashMap<>();
				List<Map<String, Object>> classInfo = new LinkedList<>();
				tempData.put("sortID", dto.getCategoryGroupUuid());
				tempData.put("sortName", dto.getCategoryGroupName());
				tempData.put("classInfo", classInfo);
				data.add(tempData);
				List<CategoryGroupDto> categoryGroupListGroupingByParentUuid = categoryGroup.get(dto.getCategoryGroupUuid());
				if(categoryGroupListGroupingByParentUuid==null) break;
				for(CategoryGroupDto group : categoryGroupListGroupingByParentUuid) {
					Map<String, Object> classInfoMap = new HashMap<>();
					classInfoMap.put("classID", group.getCategoryGroupUuid());
					classInfoMap.put("className", group.getCategoryGroupName());
					classInfo.add(classInfoMap);
					List<Map<String, Object>> nsortList = new LinkedList<>();
					classInfoMap.put("nsort", nsortList);
					List<CategoryInfoDto> categoryListGroupingByGroupUuid = category.get(group.getCategoryGroupUuid());
					for (CategoryInfoDto categoryInfoDto : categoryListGroupingByGroupUuid) {
						Map<String, Object> nsort = new HashMap<>();
						nsort.put("nsortID", categoryInfoDto.getUuid());
						nsort.put("nsortName", categoryInfoDto.getName());
						nsortList.add(nsort);
					}
				}
			}
		}
		return data;
	}
	
	@Override
	public List<Map<String, Object>> selectByCategoryName(CategoryNameQuery query) {
		return categoryDao.selectByCategoryName(query);
	}

	@Override
	public int totalCategory(CategoryNameQuery query) {
		return categoryDao.totalCategory(query);
	}

	@Override
	public Category selectByPrimaryKey(Integer id) {
		return categoryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<Category> getAllCategoryModel() {
		return categoryDao.queryAllCategoryModel();
	}

	@Override
	public Category queryByCategoryUuid(String uuid) {
		return categoryDao.queryByCategoryUuid(uuid);
	}

	@Override
	public void deleteByPrimaryKey(Integer id) {
		if (null != categoryDao.selectByPrimaryKey(id)){
			if(categoryDao.deleteByPrimaryKey(id) != 1){
	            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除品名信息失败");
	        }
		}else{
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "找不到该品名信息");
		}
	}

	@Override
	public void addCategory(Category category) {
//		category.setCreatedBy(WebAppContextUtil.getLoginUser().getLoginId());
//		category.setLastUpdatedBy(WebAppContextUtil.getLoginUser().getLoginId());
//		category.setCreated(new Date());
//		if (categoryDao.insertSelective(category) != 1) {
//			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "插入品名信息数据失败");
//		}
	}

	@Override
	public void updateCategory(Category category, User user) {
		category.setLastUpdatedBy(user.getLoginId());
		Category oldcategory = categoryDao.selectByPrimaryKey(category.getId());
		if(oldcategory==null){
			throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "未找到预更新的品名");
		}
		List<String> oldalias=new ArrayList<>();
		if(oldcategory.getAliasName()!=null){
			Collections.addAll(oldalias, oldcategory.getAliasName().split(","));
		}
		List<String> newalias=new ArrayList<>();
		if(category.getAliasName()!=null){
			Collections.addAll(newalias, category.getAliasName().split(","));
			newalias = new ArrayList<>(new HashSet<>(newalias));  //去重
			category.setAliasName(StringUtils.join(newalias.toArray(),",")); //去重后保存
			newalias.removeAll(oldalias); //新增的别名（用于判断是否存在）
		}
		if (newalias.size() > 0) {
			List<Category> existCategoryList = categoryDao.queryCategoryByAliasNameList(newalias);
			if (existCategoryList.stream().filter(a -> !a.getId().equals(category.getId())).count() > 0) {
				throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "别称与其它品名或别称重复！");
			}
		}
		if(categoryDao.updateByPrimaryKeySelective(category) != 1){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "更新品名数据失败");
        }
		
	}
	
	/**
	 * tuxianming
	 * 进项票品名映射设置：查找所有的品类名 
	 * @return
	 */
	@Override
	public List<CategoryAliasDto> queryCategoryAliasList() {
		
		List<CategoryAlias> alias = categoryAliasDao.selectAll();
		
		//categoryName, alias
		Map<String, CategoryAliasDto> aliasMap = new LinkedHashMap<String, CategoryAliasDto>();
		
		for (CategoryAlias categoryAlias : alias) {
			CategoryAliasDto tmp = aliasMap.get(categoryAlias.getCategoryName());
			if(tmp==null){
				tmp = new CategoryAliasDto();
				tmp.setCategoryId(categoryAlias.getCategoryId());
				tmp.setCategoryName(categoryAlias.getCategoryName());
				tmp.addAlias(categoryAlias);
				aliasMap.put(categoryAlias.getCategoryName(), tmp);
			}else{
				tmp.addAlias(categoryAlias);
			}
		}
		
		return aliasMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * tuxianming
	 * 进项票品名映射设置：保存和更新
	 * @return
	 */
	@Override
	public List<String> saveCategoryAlias(CategoryAliasDto saveObj, List<CategoryAlias> updateObjs, User user) {

		List<String> ids = new ArrayList<String>();
		Date date = new Date();
		if(saveObj.getAliasNames()!=null && saveObj.getAliasNames().size()>0){
    		for(String aliasName : saveObj.getAliasNames()){
    			
    			CategoryAlias ca = new CategoryAlias().setAliasName(aliasName)
    					.setCategoryId(saveObj.getCategoryId())
    					.setCategoryName(saveObj.getCategoryName())
    					.setCreated(date)
    					.setLastUpdated(date)
    					.setCreatedBy(user.getName())
    					.setLastUpdatedBy(user.getName())
    					.setModificationNumber(0);
    			
    			categoryAliasDao.insertSelective(ca);
    			ids.add(ca.getId()+"");
    		}
    	}
    	
    	if(updateObjs!=null){
    		for (CategoryAlias categoryAlias : updateObjs) {
				categoryAlias.setCategoryId(null).setCategoryName(null).setLastUpdated(date).setLastUpdatedBy(user.getName());
				categoryAliasDao.updateByPrimaryKeySelective(categoryAlias);
			}
    	}
    	return ids;
		
	}

	/**
	 * tuxianming
	 * 进项票品名映射设置：删除一条别名
	 * @return
	 */
	public void deleteCategoryAlias(Long id) {
		categoryAliasDao.deleteByPrimaryKey(id);
	}

	@Override
	public List<CategoryAlias> queryCategoryAliasLikeAlias(String aliasName) {
		return categoryAliasDao.seletcByParams(new CategoryAliasQuery().setAliasName(aliasName).setLike(true));
	}
	
}