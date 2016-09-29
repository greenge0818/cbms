package com.prcsteel.platform.core.service;

import java.util.List;

import com.prcsteel.platform.core.model.dto.CategoryGroupDto;
import com.prcsteel.platform.core.model.model.CategoryGroup;

/**
 * Created by lcw on 2015/8/3.
 */
public interface CategoryGroupService {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryGroup record);

    int insertSelective(CategoryGroup record);

    CategoryGroup selectByPrimaryKey(Integer id);
    
    CategoryGroup selectByUUID(String uuid);
    
    int updateByPrimaryKeySelective(CategoryGroup record);

    int updateByPrimaryKey(CategoryGroup record);

    /**
     * 根据父级UUID查找子集
     *
     * @param parentUuid 父级UUID
     * @return
     */
    List<CategoryGroup> queryByParentUuid(String parentUuid);
    
    List<CategoryGroupDto> queryAllCategoryGroupInner();
    /**
     * 添加分组以及分组对应的类别
     * @param categoryGroupName
     * @param cate_ids
     * @param opt
     * @return
     */
    boolean addCategoryGroup(String categoryGroupName,String[] cate_ids,String opt);
    /**
     * 修改大类
     * @param group_uuid
     * @param categoryGroupName
     * @param cate_ids
     * @param opt
     * @return
     */
    boolean editCategoryGroup(String group_uuid,String categoryGroupName,String[] cate_ids,String opt);
    
    boolean deleteCategoryGroup(String group_uuid,String opt);
    
    List<CategoryGroup> selectNoSelectForRebate();
    
    List<CategoryGroup> selectNoSelectForReward();

	List<CategoryGroupDto> queryAllParentCategoryGroupInner();

    //查找所有CBMS大类给积分系统
    List<com.prcsteel.platform.order.model.wechat.dto.CategoryGroup>  queryAllCategoryGroupToWechat();
}
