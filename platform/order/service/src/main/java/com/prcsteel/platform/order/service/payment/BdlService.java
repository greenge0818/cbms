package com.prcsteel.platform.order.service.payment;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.order.model.dto.IcbcBdDto;
import com.prcsteel.platform.order.model.model.IcbcBdlDetail;

/**
 * Created by kongbinheng on 2015/10/28.
 */
public interface BdlService {

    /**
     * 保存查询返回数据
     * @param list
     * @param nextTag
     * @return
     */
    public boolean saveQuery(List<Object> list, String nextTag);

    /**
     * 查询起始笔数
     * @param beginDate
     * @param endDate
     * @return
     */
    public int queryBeginNumber(String beginDate, String endDate);

    public String queryNextTag(String beginDate);
    
    /**
     * 查询付款回执单信息
     * @param creditAmount 金额
     * @param recipName 客户名
     * @param recipAccNo 账号
     * @return
     */
    IcbcBdDto queryBankReceipts(BigDecimal creditAmount, String recipName, String recipAccNo);
}
