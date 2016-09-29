package com.prcsteel.platform.core.persist.dao;

import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.core.model.dto.CategoryDto;
import com.prcsteel.platform.core.model.dto.CategoryInfoDto;
import com.prcsteel.platform.core.model.model.Category;
import com.prcsteel.platform.core.model.model.CategoryMaterials;
import com.prcsteel.platform.smartmatch.model.query.CategoryNameQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface CategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 获取所有小类别
     *
     * @return
     */
    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_All_CATEGORY + "'")
    List<CategoryDto> getAllCategory();

    /**
     * 根据所有小类和大类uuid
     *
     * @return
     */
    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_All_CATEGORY_GROUP + "'")
    List<CategoryInfoDto> queryAllCategoryAndGroup();

    List<Map<String, Object>> selectByCategoryName(CategoryNameQuery query);

    //green add on 2016.02.29 for earning point根据品名获取品名对象。
    Category selectByName(String name);

    int totalCategory(CategoryNameQuery query);

    @Cacheable(value = Constant.CACHE_NAME, key = "'" + Constant.CACHE_ALL_CATEGORY_MODEL + "'")
    List<Category> queryAllCategoryModel();

    /**
     * 获取指定Category
     *
     * @param uuid
     * @return
     */
    Category queryByCategoryUuid(String uuid);
    //通过大类获取品类
    List<com.prcsteel.platform.order.model.wechat.dto.Category> queryCategoryByGroupUuid(String uuid);
    
    /**
	 * 根据品名查品名uuid
	 * @param materialName
	 * @return
	 */
    Category selectByCategory(@Param("category")String category);

    /**
     * 根据别名列表查找存在的品名列表
     * @param aliasNameList
     * @return
     */
    List<Category> queryCategoryByAliasNameList(List<String> aliasNameList);


    /**
     * 查询所有品名对应的材质信息
     *
     * @return
     * @author peanut
     * @date 2016/8/18 10:36
     */
    List<CategoryMaterials> selectAllCategoryMaterials();
}