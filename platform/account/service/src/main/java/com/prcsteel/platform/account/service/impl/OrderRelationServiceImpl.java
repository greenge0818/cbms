package com.prcsteel.platform.account.service.impl;

import java.util.List;
import com.prcsteel.platform.account.model.dto.OrderRelationDto;

import com.prcsteel.platform.account.model.query.OrderRelationQuery;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.persist.dao.OrderRelationDao;
import com.prcsteel.platform.account.service.OrderRelationService;

import javax.annotation.Resource;
/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: 采购记录项目相关impl
 * @date 2016/3/27
 */
@Service("OrderRelationService")
public class OrderRelationServiceImpl implements OrderRelationService {

    @Resource
    OrderRelationDao orderRelationDao;
    @Override
    /**
     * 查询项目管理项目列表
     *
     * @param
     * @return
     */

    public List<OrderRelationDto> queryAllProjectList(OrderRelationQuery query){

        return orderRelationDao.queryAllProjectList(query);

    }
    /**
     * 创建新项目项目
     *
     * @param
     * @return
     */
    public int creatProject(OrderRelationQuery query){
        return  orderRelationDao.creatProject(query);

    }

    /**
     * 查询当前项目是否显示
     *
     * @param
     * @return
     */
    public int queryIsShow(OrderRelationQuery query){
        return  orderRelationDao.queryIsShow(query);

    }

    /**
     * 修改项目信息
     *
     * @param
     * @return
     */
    public int editProjectInfo(OrderRelationQuery query){
        return  orderRelationDao.editProjectInfo(query);

    }


    /**
     * 删除项目
     *
     * @param
     * @return
     */
    public int deleteProjectInfo(OrderRelationQuery query){
        return  orderRelationDao.deleteProjectInfo(query);

    }


}
