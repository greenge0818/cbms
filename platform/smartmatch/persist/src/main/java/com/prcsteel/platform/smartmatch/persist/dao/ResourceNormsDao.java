package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.model.ResourceNorms;

public interface ResourceNormsDao {
    int deleteByPrimaryKey(Long id);

    int insert(ResourceNorms record);

    int insertSelective(ResourceNorms record);

    ResourceNorms selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ResourceNorms record);

    int updateByPrimaryKey(ResourceNorms record);
    
    /**
     * 根据资源id集删除规格记录
     * @param resourceIds         资源id集
     */
	void deleteByResourceIds(@Param("resourceIds") List<Long> resourceIds); // add by peanut on 2015-12-9
	
	/**
	 * 复制规格表中的数据至规格历史表中
	 */
	void doCopyResourceNorms();
	
	/**
	 * 通过资源id查询
	 * @param resourceId 资源id
	 * @param value 规格值
	 * @return
	 */
	List<ResourceNorms> selectByresourceId (@Param("resourceId") Long resourceId);
	
	/**
	 * 通过资源id查询
	 * @param resourceId 资源id
	 * @param value 规格值
	 * @return
	 */
	String queryByResourceId(@Param("resourceId") Long resourceId);
	
}