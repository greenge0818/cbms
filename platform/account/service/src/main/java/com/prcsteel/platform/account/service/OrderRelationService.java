package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.OrderRelationDto;
import com.prcsteel.platform.account.model.query.OrderRelationQuery;

import java.util.List;
import java.util.Map;

/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 项目管理项目列表
 * @date 2016/3/30
 */
public interface OrderRelationService {


    /**
     * 查询项目管理项目列表
     *
     * @param
     * @return
     */
    List<OrderRelationDto> queryAllProjectList(OrderRelationQuery query);
    /**
     * 创建新项目项目
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
