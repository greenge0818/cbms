package com.prcsteel.platform.core.persist.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.model.CategoryGroup;

public interface CategoryGroupDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryGroup record);

    int insertSelective(CategoryGroup record);

    CategoryGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryGroup record);

    int updateByPrimaryKey(CategoryGroup record);

    /**
     * 根据父级UUID查找子集
     *
     * @param parentUuid 父级UUID
     * @return
     */
    List<CategoryGroup> queryByParentUuid(String parentUuid);
    
    List<CategoryGroup>  queryAllBaseCategoryGroup();

    @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_All_CATEGORY_GROUP_INNER +"'")
    List<CategoryGroupDto> queryAllCategoryGroupInner();

    @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_All_PARENT_CATEGORY_GROUP_OUTER +"'")
    List<CategoryGroupDto> queryAllParentCategoryGroupOuter();

    @Cacheable(value = Constant.CACHE_NAME, key="'"+ Constant.CACHE_All_CATEGORY_GROUP_OUTER +"'")
    List<CategoryGroupDto> queryAllCategoryGroupOuter();
    
    CategoryGroup selectByUUID(String uuid);
    
    List<CategoryGroup> selectNoSelectForRebate();
    
    List<CategoryGroup> selectNoSelectForReward();

    CategoryGroupDto selectByNsortName(String nsortName);
    //Green add 2016.02.26 跟上面的方法接口定义其实是一样的，但是取数有所差别，此接口为积分系统而建。
    //然而后来产品说还是按以前的那个来，所以这个并没有什么卵用
    CategoryGroupDto selectByCategoryName(String categoryName);
    
	List<CategoryGroupDto> queryAllParentCategoryGroupInner();
    //查找所有CBMS大类给积分系统
    List<com.prcsteel.platform.order.model.wechat.dto.CategoryGroup>  queryAllCategoryGroupToWechat();


}