package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountExtDtoForUpdate;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;

/**
 * Created by dengxiyan on 2016/5/6.
 */
public interface AnnualPurchaseAgreementService {

    /**
     * 审核买家年度采购协议
     * @param dto
     * @param taskActionInfo
     */
    void auditAnnualPurchaseAgreement(AccountExtDtoForUpdate dto,TaskActionInfo taskActionInfo);
}
