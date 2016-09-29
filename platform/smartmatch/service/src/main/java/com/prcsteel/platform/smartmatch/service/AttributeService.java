package com.prcsteel.platform.smartmatch.service;


import java.util.List;
import com.prcsteel.platform.smartmatch.model.model.Attribute;
import com.prcsteel.platform.smartmatch.model.query.AttributeEditQuery;



/**
 * Created by peanut on 2015/11/23.
 */
public interface AttributeService {
	
	/**
	 * 查询资源表（common_attribute）记录总数
	 * @param attributeEditQuery      属性集
	 * @return              总记录数
	 */
	Integer totalAttribute(AttributeEditQuery attributeEditQuery);
	
	/**
	 * 根据名称和类型查询资源表（common_attribute）记录
	 * @param attributeEditQuery          属性集
	 * @return                  记录结果集
	 */
	List<Attribute> selectByNameAndType(AttributeEditQuery attributeEditQuery);
	
	/**
	 * 根据主健id查找记录
	 * @param id     主健id
	 * @return
	 */
	Attribute findById(Long id);
	
	/**
	 * 新增记录
	 * @param attr
	 * @return
	 * @throws Exception 
	 */
	void addAttributeEdit(Attribute attr);
	
	/**
	 * 根据主健删除记录
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	void deleteByPrimaryKey(Long id);
	
	/**
	 * 修改记录
	 * @param attr
	 * @return
	 * @throws Exception 
	 */
	void updateAttribute(Attribute attr);
	
	/**
	 * 根据属性名称查找除id以外的记录（无模糊）
	 * @param name    name
	 * @return
	 */
	Attribute findByNameBesidesId(Long id, String name);
	
	/**
	 * 取得所有属性
	 * @return
	 */
	List<Attribute> getAllAttr();
	
}
