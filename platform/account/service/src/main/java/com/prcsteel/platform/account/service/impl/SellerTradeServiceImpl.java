package com.prcsteel.platform.account.service.impl;

import java.util.List;
import java.util.Map;
import com.prcsteel.platform.account.model.dto.SellerTradeDto;

import com.prcsteel.platform.account.model.query.SellerTradeQuery;
import org.springframework.stereotype.Service;

import com.prcsteel.platform.account.persist.dao.SellerTradeDao;
import com.prcsteel.platform.account.service.SellerTradeService;

import javax.annotation.Resource;
/**
 * @author zhoucai@prcsteel.com
 * @version V1.0
 * @Description: query sellers records
 * @date 2016/3/27
 */
@Service("SellerTradeService")
public class SellerTradeServiceImpl implements SellerTradeService {

    @Resource
    SellerTradeDao sellerTradeDao;
    @Override
    public List<SellerTradeDto> querySellerTradeListByDto(SellerTradeQuery query){

        return sellerTradeDao.querySellerTradeListByDto(query);

    }


    @Override
    public SellerTradeDto countSellerTradeFlow( Map<String, Object> paramMap){

        return sellerTradeDao.countSellerTradeFlow(paramMap);
    }
    /**
     * 统计寄售总条数
     *
     * @param query
     * @return
     */
    @Override
    public Integer countSellerFlow(SellerTradeQuery query){
        return sellerTradeDao.countSellerFlow(query);
    }


    /**
     * 为订单增加项目依赖
     *
     * @param query
     * @return
     */
    @Override
    public Integer changeOrderProject( SellerTradeQuery query){
        return sellerTradeDao.changeOrderProject(query);
    }
	
}
