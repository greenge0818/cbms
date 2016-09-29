package com.prcsteel.platform.account.service;

import org.prcsteel.rest.sdk.activiti.pojo.ProcessInstance;

/**
 * Created by dengxiyan on 2016/5/6.
 * 工作流公用服务接口
 */
public interface WorkFlowCommonService {
    /**
     * 根据流程定义的key及业务id查询
     * @param processDefinitionKey 流程定义的key
     * @param businessKey 业务id
     * @return
     */
    ProcessInstance queryOneProcessInstance(String processDefinitionKey,String businessKey);


    /**
     * 判断流程是否结束
     * 不存在流程实例或结束
     * @param processDefinitionKey 流程定义的key
     * @param businessKey          流程定义的key
     * @return true 结束 false 未结束
     */
    boolean isProcessEnded(String processDefinitionKey,String businessKey);

}
