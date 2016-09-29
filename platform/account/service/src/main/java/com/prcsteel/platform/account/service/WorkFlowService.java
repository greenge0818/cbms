package com.prcsteel.platform.account.service;

import java.util.List;

import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;

import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.acl.model.model.User;

/**
 * 工作流辅助服务类
 * @author tangwei
 *
 */
public interface WorkFlowService {

	  /**
	   * 客户资料审核工作流启动参数设置
	   * @param accountId
	   * @param accountExt
	   * @param loginUser
	   * @return
	   */
	public List<StartProcessRequest> startWorkFlow(Long accountId,AccountExt accountExt);
	
  /**
   * 审核task列表查询
   * @param candidateUse 节点权限value
   * @return
   */
    public ListTaskQuery createAuditTaskQuery(Integer candidateUse,String processInstanceBusinessKey);

    
    /**
     * 编辑Task查询列表
     * @param taskDefinitionKey
     * @return
     */
    public ListTaskQuery createEditTaskQuery(String  taskDefinitionKey,String processInstanceBusinessKey);

    
    /**
     * 执行下一步客户审核任务Task参数
     * @param list
     * @param variable
     * @param accountId
     * @return
     */
    public TaskActionInfo getNextTaskInfo(List<TaskInfo> list,List<ActivitiVariable> variables,String accountId);
    
    /**
     * 启动流程参数
     * @param auditFlag
     * @param accountId
     * @param flowType
     * @param processDefKey 
     * @return
     */
    public StartProcessRequest createStartRequest (String auditFlag,Long accountId,WorkFlowEnum flowType,String processDefKey);


	/**
	 * 客户协议启动流程参数
	 * @param managerAuditFlag 总经理审核权限标记
	 * @param financeAuditFlag 财务审核人员权限标记
	 * @param serviceAuditFlag 综服部审核人员权限标记
	 * @param loginUser   用户
	 * @param accountId 客户id
	 * @param flowType 流程类型
	 * @param processDefKey 流程定义id
	 * @return
	 */
	public StartProcessRequest createAccountAgreementStartRequest (String managerAuditFlag,String financeAuditFlag,String serviceAuditFlag,String isUploadFlag,User loginUser,Long accountId,WorkFlowEnum flowType,String processDefKey);

	/**
	 * 卖家代运营协议启动流程参数
	 * @param accountId 客户id
	 * @param isUpload 是否上传图片
	 * @param loginUser 登录人
	 * @return
	 */
	public StartProcessRequest createSellerConsignAgreementWorkFlow(Long accountId,String isUpload);


	/**
	 * 买家年度采购协议启动流程参数
	 * @param accountId 客户id
	 * @param isUpload 是否上传图片
	 * @param loginUser 登录人
	 * @return
	 */
	public StartProcessRequest createAnnualPurchaseAgreementWorkFlow(Long accountId,String isUpload);


    /**
     * 获取所有task任务
     * @param taskList
     * @param query
     */
	public List<TaskInfo> getAllTaskList(ListTaskQuery query);
	
}
