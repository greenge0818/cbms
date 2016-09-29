package com.prcsteel.platform.account.persist.dao;

import com.prcsteel.platform.account.model.dto.ContractTemplateDto;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountContractTemplateDao {
    //获取模板列表
    List<AccountContractTemplate> selectByModel(AccountContractTemplate act);

    int deleteByPrimaryKey(Long id);

    int insertSelective(AccountContractTemplate record);

    AccountContractTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountContractTemplate record);

    int updateByPrimaryKey(AccountContractTemplate record);
    //将指定模板设为非默认
    void disableOtherByAccountIdAndType(Long accountId,String type);

    /**
     * 根据accountId获取所有的合同模板
     * @param accountId
     * @return
     */
    List<AccountContractTemplate> selectContractTemplateById(Long accountId);

    /**
     * 设置合同模板为默认模板
     * @param id
     */
    int setDefaultById(Long contractTemplateId);

    /**
     * 发布系统默认合同模板
     * @param id
     */
    int releaseTemplate(Long id);

    /**
     * 不发布修改的系统默认合同模板
     * @param id
     */
    int doNotReleaseTemplate(Long id);

    /**
     * 统计公司待审核合同模板列表
     * @param query
     * @return
     */
    List<ContractTemplateDto> queryContractTemplateByStatus(CompanyQuery query);

    /**
     * 统计公司待审核合同模板列表
     * @param query
     * @return
     */
    int queryTotalContractTemplateByStatus(CompanyQuery query);

    /**
     * 更新合同模板状态
     * @param id
     * @param status
     * @param lastUpdateBy
     * @param statusDeclineReason
     * @return
     */
    int updateContractTemplateStatus(@Param("id")Long id, @Param("status")String status,
                                     @Param("lastUpdateBy")String lastUpdateBy, @Param("statusDeclineReason")String statusDeclineReason);

}