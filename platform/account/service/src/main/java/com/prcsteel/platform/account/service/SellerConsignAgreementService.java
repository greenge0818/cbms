package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountExtDtoForUpdate;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;

/**
 * Created by dengxiyan on 2016/5/5.
 */
public interface SellerConsignAgreementService {
    /**
     * 审核
     * @param dto
     * @param taskActionInfo
     * @return
     */
    void auditConsignAgreement(AccountExtDtoForUpdate dto,TaskActionInfo taskActionInfo);


}
