package com.prcsteel.platform.order.service.invoice;

import org.apache.ibatis.annotations.Param;

import com.prcsteel.platform.order.model.model.Express;

import java.util.List;

/**
 * Created by lcw on 2015/8/1.
 */
public interface ExpressService {
    int deleteByPrimaryKey(Long id);

    int insert(Express record);

    int insertSelective(Express record);

    Express selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Express record);

    int updateByPrimaryKey(Express record);

    /**
     * 根据发票号查询发票
     *
     * @param expressName 快递单号
     * @return
     */
    Express selectByName(String expressName);
}
