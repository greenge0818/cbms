package com.prcsteel.platform.order.service.withdraw;

import com.prcsteel.platform.order.model.model.WithDraw;
import com.prcsteel.donet.iv.finance.model.response.DealDetailList;

/**
 * Created by caochao on 2015/9/9.
 */
public interface WithdrawService {

    int deleteByPrimaryKey(Integer id);

    int insert(WithDraw record);

    int insertSelective(WithDraw record);

    WithDraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithDraw record);

    int queryMaxSyncId();

    DealDetailList getLatestWithdrawRecord(int id,int num);
}
