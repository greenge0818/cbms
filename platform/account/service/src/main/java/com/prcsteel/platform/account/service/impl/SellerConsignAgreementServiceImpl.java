package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.AccountExtDtoForUpdate;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.SellerConsignAgreementService;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by dengxiyan on 2016/5/5.
 * 卖家代运营协议流程服务
 */
@Service("sellerConsignAgreementService")
public class SellerConsignAgreementServiceImpl implements SellerConsignAgreementService {

    @Resource
    AccountService accountService;

    @Resource
    protected TaskService taskService;

    @Resource
    AccountDao accountDao;

    /**
     * 审核
     * @param dto
     * @param taskActionInfo
     * @return
     */
    @Override
    @Transactional
    public void auditConsignAgreement(AccountExtDtoForUpdate dto, TaskActionInfo taskActionInfo) {
        //根据审核类型及状态检查当前任务是否已被执行
        checkTask(dto, taskActionInfo);

        //更新协议状态及理由
        updateAccountExt(dto);

        //审核通过需更新
        if (SellerConsignAgreementStatus.Approved.getCode().equals(dto.getNewSellerConsignAgreementStatus())){
            //更新客户性质为代运营卖家
            updateAccountTag(dto);
        }

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
        if (ext == null || taskActionInfo.getRequest() == null || !dto.getOldSellerConsignAgreementStatus().equals(ext.getSellerConsignAgreementStatus())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "代运营协议已被审核");
        }
    }

    private void updateAccountExt(AccountExtDtoForUpdate dto){
        //业务更新
        AccountExt ext = new AccountExt();
        ext.setCustAccountId(dto.getAccountId());
        ext.setSellerConsignAgreementStatus(dto.getNewSellerConsignAgreementStatus());
        ext.setSellerConsignAgreementDeclineReason(dto.getSellerConsignAgreementDeclineReason());
        if(accountService.updateByAccountIdSelective(ext,dto.getUser()) == 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核失败,客户代运营协议状态更新失败");
        }
    }


    private void updateAccountTag(AccountExtDtoForUpdate dto){
        Account accountDb = accountService.queryById(dto.getAccountId());
        Account account = new Account();
        account.setId(accountDb.getId());
        account.setAccountTag(accountService.reCaluteAccountTag(accountDb.getAccountTag(), AccountTag.temp.getCode(), AccountTag.consign.getCode()));
        account.setLastUpdatedBy(dto.getUser().getName());
        account.setLastUpdated(new Date());
        account.setModificationNumber(accountDb.getModificationNumber() + 1);
        if(accountDao.updateByPrimaryKeySelective(account) == 0){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"审核失败,客户代运营协议更新失败");
        }
    }




}
