package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.dto.OrderRelationDto;
import com.prcsteel.platform.account.model.query.OrderRelationQuery;

import java.util.List;

/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 项目管理项目DAo
 * @date 2016/3/30
 */
public interface OrderRelationDao {
	/**
	 * 查询项目管理项目列表
	 *
	 * @param
	 * @return
	 */
	List<OrderRelationDto> queryAllProjectList(OrderRelationQuery query);
	/**
	 * 创建新项目
	 *
	 * @param
	 * @return
	 */
	int creatProject(OrderRelationQuery query);

	/**
	 * 修改项目信息
	 *
	 * @param
	 * @return
	 */
	int editProjectInfo(OrderRelationQuery query);


	/**
	 * 删除项目
	 *
	 * @param
	 * @return
	 */
	int deleteProjectInfo(OrderRelationQuery query);

	/**
	 * 查询当前项目是否显示
	 *
	 * @param
	 * @return
	 */

	int queryIsShow(OrderRelationQuery query);


}