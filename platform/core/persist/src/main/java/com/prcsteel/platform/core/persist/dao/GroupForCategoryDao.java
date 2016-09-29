package com.prcsteel.platform.core.persist.dao;

import java.util.List;
import java.util.Map;

import com.prcsteel.platform.core.model.dto.CategoryGroupRecordDto;
import com.prcsteel.platform.core.model.model.GroupForCategory;

public interface GroupForCategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupForCategory record);

    int insertSelective(GroupForCategory record);

    GroupForCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupForCategory record);

    int updateByPrimaryKey(GroupForCategory record);
    /**
     * 获取所有超市的记录
     * @return
     */
    List<CategoryGroupRecordDto> queryAllReord();
    
    List<CategoryGroupRecordDto>  queryAllReordBySite(String site_uuid);
    
    List<CategoryGroupRecordDto> queryRecordByCateGroupUUID(String category_group_uuid);
    
    int  deleteRecordByGroupUUID(Map<String, Object> paramMap);
}