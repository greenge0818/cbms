package com.prcsteel.platform.order.service.withdraw.impl;

import com.prcsteel.platform.order.model.model.WithDraw;
import com.prcsteel.platform.order.persist.dao.WithDrawDao;
import com.prcsteel.platform.order.service.FinanceService;
import com.prcsteel.platform.order.service.withdraw.WithdrawService;
import com.prcsteel.donet.iv.finance.model.response.DealDetailList;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by caochao on 2015/9/9.
 */
@Service("withdrawService")
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithDrawDao withDrawDao;
    @Value("${ivFinanceService}")
    private String ivFinanceServiceAddress;  // 接口服务地址
    @Value("${ivFinanceServiceKEY}")
    private String ivFinanceServiceKEY; //接口key

    public int deleteByPrimaryKey(Integer id) {
        return withDrawDao.deleteByPrimaryKey(id);
    }

    public int insert(WithDraw record) {
        return withDrawDao.insert(record);
    }

    public int insertSelective(WithDraw record) {
        return withDrawDao.insertSelective(record);
    }

    public WithDraw selectByPrimaryKey(Integer id) {
        return withDrawDao.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(WithDraw record) {
        return withDrawDao.updateByPrimaryKeySelective(record);
    }

    public int queryMaxSyncId() {
        Integer id = withDrawDao.queryMaxSyncId();
        return id == null ? -1 : id;
    }

    public DealDetailList getLatestWithdrawRecord(int id, int num) {
        //创建客户端代理工厂
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(FinanceService.class);
        //设置地址
        factory.setAddress(ivFinanceServiceAddress);
        FinanceService financeService = (FinanceService) factory.create();
        DealDetailList result = financeService.getAcountList(ivFinanceServiceKEY, id, num);
        return result;
    }
}
