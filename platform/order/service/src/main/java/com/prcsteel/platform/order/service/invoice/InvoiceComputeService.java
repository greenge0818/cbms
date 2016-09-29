package com.prcsteel.platform.order.service.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.prcsteel.platform.order.model.model.InvoiceOutApply;
import com.prcsteel.platform.acl.model.model.User;

/**
 * Created by kongbinheng on 2015/8/2.
 */
public interface InvoiceComputeService {

    /**
     * 根据服务中心开票计算
     * @param orgId
     * @return
     */
    public List<InvoiceOutApply> compute(Long orgId, String applyJson);

    /**
     * 提交所有的通过的开票申请给财务
     * @param orgId
     * @param user
     * @param totalActualAmount
     * @param applyJson
     * @return
     */
    public boolean billingByOrgId(Long orgId, User user, BigDecimal totalActualAmount, String applyJson);


}
