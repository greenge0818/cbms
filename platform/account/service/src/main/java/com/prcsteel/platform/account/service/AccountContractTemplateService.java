package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AgreementTemplateDto;
import com.prcsteel.platform.account.model.dto.ContractInfoDto;
import com.prcsteel.platform.account.model.dto.ContractTemplateDto;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountContractTemplateBarcodeHistory;
import com.prcsteel.platform.account.model.model.AccountContractTemplateOprt;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.acl.model.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Rolyer on 2016/1/21.
 */
public interface AccountContractTemplateService {
    /**
     * 读取所以系统模板
     * @return
     */
    List<AccountContractTemplate> querySysTemplate();

    /**
     * 保存模板
     * @param act
     * @return
     */
    Boolean saveContractTemplate(AccountContractTemplate act);

    /**
     * 获取指定ID模板
     * @param id
     * @return
     */
    AccountContractTemplate queryAccountContractTemplate(Long id);

    /**
     * 审核模板（更新状态）
     * @param id
     * @param approved true 通过; false 不通过。
     * @return
     */
    Boolean approvedContractTemplate(Long id, Boolean approved);

    /**
     * 启用/禁用模板
     * @param id ID
     * @param enabled true 启用; false 禁用。
     * @return
     */
    Boolean enabledContractTemplate(Long id, Boolean enabled);

    /**
     * 获取指定模板内容
     * @param act
     * @return
     */
    String fetchTemplateContent(AccountContractTemplate act);

    /**
     * 恢复模板
     * @param id
     * @param type
     * @param accountId
     * @return
     */
    String resolveTemplate(Long id, String type, Long accountId);

    /**
     * 删除模板
     * @param id
     * @return
     */
    Boolean deleteTemplateById(Long id);

    /**
     * 根据公司Id获取公司部门下的合同
     * @param accountId
     * @return
     */
    Map<String,List<AccountContractTemplate>> selectAllContractTemplateById(Long accountId);

    /**
     * 设置默认合同模板
     * @param contractTemplateId
     * @param contractTemplateType
     * @param accountId
     */
    void setDefaultContractTemplate(Long contractTemplateId, String contractTemplateType,Long accountId);

    /** 模版处理操作
     * @param actOprt  对账户合同模板操作时所需要的参数对象
     * @return
     */
    String doTemplateOprt(AccountContractTemplateOprt actOprt);

    /**
     * 发布系统默认合同模板
     * @param id
     * @param type
     */
    Boolean releaseTemplate(Long id, String type);

    /**
     * 不发布修改的系统默认合同模板
     * @param id
     */
    Boolean doNotReleaseTemplate(Long id);


    /**
     * 读取模板
     * @param type
     * @param isChangeContract
     * @return
     */
    public String readTemplate(String type, boolean isChangeContract);

    /**
     * 获取年度采购协议信息
     * @return
     */
    AgreementTemplateDto getYearPurchaseAgreementTemplateInfo(AccountContractTemplate param, Long currentCompanyId);

    /**
     * 获取年度采购协议
     * @param  param 查询模板的条件参数
     * @return
     */
    AccountContractTemplate getYearPurchaseTemplate(AccountContractTemplate param);

    /**
     * 获取当前客户信息
     * @param  currentCompanyId 当前公司id
     * @return
     */
    ContractInfoDto getYearPurchaseContractInfoDto(Long currentCompanyId);

    /**
     * 保存年度采购协议
     * @param act
     * @param user
     * @return
     */
    void saveYearPurchaseTemplate(AccountContractTemplate act, User user);

    /**
     * 恢复 年度采购协议 到系统默认模板
     * @param act
     * @param user
     * @return
     */
    void recoverYearPurchaseTemplate(AccountContractTemplate act, User user);

    /**
     * 获取代运营协议信息
     * @return
     */
    AgreementTemplateDto getConsignAgreementTemplateInfo(AccountContractTemplate param,Long currentCompanyId);

    /**
     * 保存公司自定义卖家代运营协议模板（保存失败抛出异常）
     * 1、添加/编辑模板 2、更新公司代运营协议状态为“待审核”
     * @param saveInfo
     * @param user
     */
    void saveConsignTemplate(AccountContractTemplate saveInfo,User user);

    /**
     * 恢复 代运营协议 到系统默认模板
     * @param act
     * @param user
     * @return
     */
    void recoverConsignTemplate(AccountContractTemplate act, User user);

    /**
     * 更新条码信息
     * @param barCode
     * @param user
     */
    void updateTemplateBarCodeHisitoryByBarcode(String barCode, User user);

    /**
     * 更新代运营协议条码并保存条码历史记录
     * 更新成功返回条码
     * @param currentCompanyId
     * @param user
     * @return
     */
    String updateConsignBarCode(Long currentCompanyId,User user);

    /**
     * 根据条码查询条码历史记录
     * @param barCode
     * @return
     */
    AccountContractTemplateBarcodeHistory queryBarCodeHistoryByBarcode(String barCode);

    /**
     * 更新年度采购协议条码并保存条码历史记录
     *
     * @param currentCompanyId
     * @param user
     * @return
     */
    String updateYearPurchaseBarCode(Long currentCompanyId, User user);

    AccountContractTemplate queryOneAccountContractTemplate(AccountContractTemplate defaultQuery);

    /**
     * 查询公司待审核合同模板列表
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
     * @param disagreeDesc
     * @return
     */
    int updateContractTemplateStatus(Long id, String status,String lastUpdateBy, String disagreeDesc);
}
