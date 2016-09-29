package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountExtDtoForUpdate;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by dengxiyan on 2016/5/6.
 */
@Service("annualPurchaseAgreementService")
public class AnnualPurchaseAgreementServiceImpl implements AnnualPurchaseAgreementService{
    @Resource
    AccountService accountService;

    @Resource
    protected TaskService taskService;

    @Resource
    AccountDao accountDao;

    /**
     * 审核买家年度采购协议
     * @param dto
     * @param taskActionInfo
     */
    @Override
    @Transactional
    public void auditAnnualPurchaseAgreement(AccountExtDtoForUpdate dto, TaskActionInfo taskActionInfo) {
        //根据审核类型及状态检查当前任务是否已被执行
        checkTask(dto, taskActionInfo);

        //更新协议状态及理由
        updateAccountExt(dto);

        //执行当前任务
        taskService.invokeTaskAction(taskActionInfo.getTaskId(), taskActionInfo.getRequest());
    }

    /**
     * 检查任务执行情况
     *
     * @param dto
     * @param taskActionInfo
     * @return
     */
    private void checkTask(AccountExtDtoForUpdate dto,TaskActionInfo taskActionInfo) {
        AccountExt ext = accountService.queryAccountExtByAccountId(dto.getAccountId());
        if (ext == null || taskActionInfo.getRequest() == null || !dto.getOldAnnualPurchaseAgreementStatus().equals(ext.getAnnualPurchaseAgreementStatus())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "年度采购协议已被审核");
        }
    }

    private void updateAccountExt(AccountExtDtoForUpdate dto){
        //业务更新
        AccountExt ext = new AccountExt();
        ext.setCustAccountId(dto.getAccountId());
        ext.setAnnualPurchaseAgreementStatus(dto.getNewAnnualPurchaseAgreementStatus());
        ext.setAnnualPurchaseAgreementDeclineReason(dto.getAnnualPurchaseAgreementDeclineReason());
        if(accountService.updateByAccountIdSelective(ext, dto.getUser()) == 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核失败,年度采购协议状态更新失败");
        }
    }
}
