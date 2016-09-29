package com.prcsteel.platform.smartmatch.persist.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.smartmatch.model.model.Attribute;
import com.prcsteel.platform.smartmatch.model.query.AttributeEditQuery;

public interface AttributeDao {
    int deleteByPrimaryKey(Long id);

    int insert(Attribute record);

    int insertSelective(Attribute record);

    Attribute selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attribute record);

    int updateByPrimaryKey(Attribute record);

    /**
	 * 查询资源表（common_attribute）记录总数       
	 * @param attributeEditQuery      属性集
	 * @return              总记录数
	 */
	Integer totalAttribute(AttributeEditQuery attributeEditQuery);   // add by peanut on 15-11-23
	
	/**
	 * 根据名称和类型查询资源表（common_attribute）记录
	 * @param attributeEditQuery          属性集
	 * @return                  记录结果集
	 */
	List<Attribute> selectByNameAndType(AttributeEditQuery attributeEditQuery);  // add by peanut on 15-11-23
	
	/**
	 * 根据属性名称查找除id以外的记录（无模糊）
	 * @param name    name
	 * @return
	 */
	Attribute findByNameBesidesId(@Param("id")Long id,@Param("name") String name); // add by peanut on 15-11-23
	
	/**
	 * 取得所有属性
	 * @return
	 */
	List<Attribute> getAllAttr(); // add by peanut on 15-11-24
}