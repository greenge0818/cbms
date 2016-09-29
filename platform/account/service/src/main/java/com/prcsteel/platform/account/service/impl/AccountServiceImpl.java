package com.prcsteel.platform.account.service.impl;

import com.prcsteel.platform.account.model.dto.*;
import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.enums.AccountStructureType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AccountTransApplyType;
import com.prcsteel.platform.account.model.enums.AccountType;
import com.prcsteel.platform.account.model.enums.AnnualPurchaseAgreementStatus;
import com.prcsteel.platform.account.model.enums.AttachmentType;
import com.prcsteel.platform.account.model.enums.CardInfoStatus;
import com.prcsteel.platform.account.model.enums.ContractTemplateStatus;
import com.prcsteel.platform.account.model.enums.InvoiceDataStatus;
import com.prcsteel.platform.account.model.enums.SellerConsignAgreementStatus;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountAcceptDraft;
import com.prcsteel.platform.account.model.model.AccountAttachment;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountBusinessType;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.model.ProxyFactory;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.persist.dao.AccountAttachmentDao;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.persist.dao.AccountContractTemplateDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.AccountExtDao;
import com.prcsteel.platform.account.persist.dao.AccountTransLogDao;
import com.prcsteel.platform.account.persist.dao.ContactDao;
import com.prcsteel.platform.account.persist.dao.ProxyFactoryDao;
import com.prcsteel.platform.account.service.AccountAcceptDraftService;
import com.prcsteel.platform.account.service.AccountFundService;
import com.prcsteel.platform.account.service.AccountService;
import com.prcsteel.platform.account.service.CustGroupingInforService;
import com.prcsteel.platform.account.service.WorkFlowCommonService;
import com.prcsteel.platform.account.service.WorkFlowService;
import com.prcsteel.platform.acl.model.enums.RoleType;
import com.prcsteel.platform.acl.model.model.Role;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.RoleDao;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.FileUtil;
import com.prcsteel.platform.common.utils.StringToReplace;
import com.prcsteel.platform.common.utils.Tools;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.DistrictDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.order.model.enums.AssociationType;
import com.prcsteel.platform.order.model.enums.PayType;
import com.prcsteel.platform.order.model.query.WXContractQuery;
import constant.WorkFlowConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.prcsteel.rest.sdk.activiti.enums.WorkFlowEnum;
import org.prcsteel.rest.sdk.activiti.pojo.ActivitiVariable;
import org.prcsteel.rest.sdk.activiti.pojo.TaskActionInfo;
import org.prcsteel.rest.sdk.activiti.pojo.TaskInfo;
import org.prcsteel.rest.sdk.activiti.query.ListTaskQuery;
import org.prcsteel.rest.sdk.activiti.request.StartProcessRequest;
import org.prcsteel.rest.sdk.activiti.request.TaskActionRequest;
import org.prcsteel.rest.sdk.activiti.service.ProcessService;
import org.prcsteel.rest.sdk.activiti.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Green.Ge
 * @version V1.0
 * @Title: AccountServiceImpl.java
 * @Package com.prcsteel.platform.order.service.impl
 * @Description: 客户服务实现
 * @date 2015年7月14日 下午1:28:08
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    public static final String ATTACHMENTSAVEPATH = "img" + File.separator
            + "account" + File.separator;
    private static final String FAILCODE = "201";
    private static final String BANDEDCODE = "202";
    @Resource
    AccountDao accountDao;
    @Resource
    AccountAttachmentDao aamDao;
    @Resource
    ProvinceDao provinceDao;
    @Resource
    CityDao cityDao;
    @Resource
    DistrictDao districtDao;
    @Resource
    UserDao userDao;
    @Resource
    AccountContractTemplateDao actDao;
    @Resource
    AccountContactDao accountcontactDao;
    @Resource
    private AccountBankDao accountBankDao;
    @Resource
    FileService fileService;
    @Resource
    ContactDao contactDao;
    @Resource
    ProxyFactoryDao proxyFactoryDao;
    @Resource
    AccountAttachmentDao accountAttachmentDao;
    @Resource
    AccountExtDao accountExtDao;
	@Resource
    AccountFundService accountFundService;
    @Resource
    RoleDao roleDao;
    @Resource
    AccountTransLogDao accountTransLogDao;
    @Resource
    CustGroupingInforService custGroupingInforService;
    @Resource
    AccountAcceptDraftService accountAcceptDraftService;

    @Resource
    WorkFlowService workFlowService;
    
	@Resource
    private TaskService taskService;
	
	@Resource
	ProcessService processService;
	
    @Resource
    WorkFlowCommonService workFlowCommonService;
    
    @Override
    public List<Account> list() {

        return null;
    }

    @Override
    public Account selectByTel(String tel) {
        return accountDao.selectByTel(tel);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {

        return accountDao.deleteByPrimaryKey(id);
    }

    public synchronized String getCode() {
        return accountDao.getAccountCode();
    }

    @Override
    @Transactional
    public HashMap<String, Object> insert(AccountDto record,
                                          List<MultipartFile> attachments, AccountContact contact)
            throws BusinessException {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Account account = record.getAccount();
        String accountCode = account.getAccountCode();
        if (accountCode != null && !"".equals(accountCode.replaceAll(" ", ""))) {
            // 找到银行卡信息
            AccountBank accountBank = accountBankDao
                    .selectByAccountCode(StringToReplace
                            .toReplaceAll(accountCode));
            if (accountBank != null) {
                throw new BusinessException(
                        Constant.EXCEPTIONCODE_BUSINESS,
                        "尾号为【"
                                + accountCode.substring(accountCode.length() - 4)
                                + "】银行账号已经与【"
                                + accountDao.selectByPrimaryKey(
                                accountBank.getAccountId()).getName()
                                + "】绑定，请联系陈虎【"
                                + userDao.queryByLoginId("chenhu").getTel()
                                + "】处理");
            }
        }
        User user = record.getManager();
        account.setManagerId(user.getId());
        account.setCreatedBy(user.getLoginId());
        account.setLastUpdatedBy(user.getLoginId());
        account.setRegTime(new Date());
        account.setCode(getCode());
        if (StringUtils.isEmpty(account.getBusinessType())) {
            account.setBusinessType(null);
        }
        accountDao.insertSelective(account);
        Long accountId = account.getId();
        // 插入银行信息，若银行行号不为空且在银行信息表不存在则新增银行信息 modify by kongbinheng 20150803
        if (accountCode != null && !"".equals(accountCode.replaceAll(" ", ""))) {
            AccountBank accountBank = accountBankDao
                    .selectByAccountCode(StringToReplace
                            .toReplaceAll(accountCode));
            if (accountBank == null) {
                accountBank = new AccountBank();
                accountBank.setAccountId(accountId); // 公司ID
                accountBank.setBankCode(StringToReplace.toReplaceAll(account
                        .getBankCode())); // 开户银行行号
                accountBank.setBankName(StringToReplace.toReplaceAll(account
                        .getBankNameMain())); // 开户银行主行
                accountBank.setBankNameBranch(StringToReplace
                        .toReplaceAll(account.getBankNameBranch())); // 开户银行支行
                accountBank.setBankProvinceId(account.getBankProvinceId());
                accountBank.setBankCityId(account.getBankCityId());
                if (account.getBankProvinceId() != null)
                    accountBank.setBankProvinceName(provinceDao
                            .selectByPrimaryKey(account.getBankProvinceId())
                            .getName());
                if (account.getBankCityId() != null)
                    accountBank.setBankCityName(cityDao.selectByPrimaryKey(
                            account.getBankCityId()).getName());
                accountBank.setBankAccountCode(StringToReplace
                        .toReplaceAll(accountCode)); // 银行账号
                accountBank.setCreated(new Date());
                accountBank.setCreatedBy(user.getLoginId());
                accountBank.setLastUpdated(new Date());
                accountBank.setLastUpdatedBy(user.getLoginId());
                accountBank.setModificationNumber(0);
                accountBankDao.insertSelective(accountBank);
            } else {
                if (accountId.equals(accountBank.getAccountId())) {
                    accountBank.setBankCode(StringToReplace
                            .toReplaceAll(account.getBankCode())); // 开户银行行号
                    accountBank.setBankName(StringToReplace
                            .toReplaceAll(account.getBankNameMain())); // 开户银行主行
                    accountBank.setBankNameBranch(StringToReplace
                            .toReplaceAll(account.getBankNameBranch())); // 开户银行支行
                    accountBankDao.updateByPrimaryKeySelective(accountBank);
                } else {

                }
            }
        }

        // String basePath = rootPath + ATTACHMENTSAVEPATH + account.getCode()
        // + File.separator;
        boolean hasTaxReg = false;
        boolean hasInvoiceData = false;
        boolean hasPaymentData = false;//打款资料
        boolean hasOpenAccountLicense = false;//银行开户许可证
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if ("pic_tax_reg".equals(file.getName())) {
                    hasTaxReg = true;
                } else if ("pic_invoice_data".equals(file.getName())) {
                    hasInvoiceData = true;
                } else if ("pic_payment_data".equals(file.getName())) {
                    hasPaymentData = true;
                } else if ("pic_open_account_license".equals(file.getName())) {
                    hasOpenAccountLicense = true;
                }
                AccountAttachment accountAttachment = new AccountAttachment();
                accountAttachment.setCreatedBy(user.getLoginId());
                accountAttachment.setLastUpdatedBy(user.getLoginId());
                accountAttachment.setAccountId(accountId);
                AttachmentType type = AttachmentType.valueOf(file.getName());
                accountAttachment.setType(type.getCode());
                // String savePath = basePath + type.getCode() + "."
                // + FileUtil.getFileSuffix(file.getOriginalFilename());
                // FileUtil.saveFile(file, savePath);
                String saveKey = "";
                try {
                    saveKey = fileService.saveFile(
                            file.getInputStream(),
                            ATTACHMENTSAVEPATH
                                    + account.getCode()
                                    + File.separator
                                    + type.getCode()
                                    + "."
                                    + FileUtil.getFileSuffix(file
                                    .getOriginalFilename()));
                } catch (IOException e) {
                    BusinessException be = new BusinessException(
                            Constant.EXCEPTIONCODE_SYSTEM, "保存"
                            + AttachmentType.valueOf(file.getName())
                            + "出错");
                    throw be;
                }
                accountAttachment.setUrl(saveKey);
                aamDao.insertSelective(accountAttachment);

            }
        }
        if (hasTaxReg || hasInvoiceData) {
            accountDao.updateInvoiceDataStatusByPrimaryKey(account.getId(),
                    InvoiceDataStatus.Requested.getCode(),null, user.getLoginId());// 买家开票资料待审核
        } else {
            accountDao
                    .updateInvoiceDataStatusByPrimaryKey(account.getId(),
                            InvoiceDataStatus.Insufficient.getCode(),null,
                            user.getLoginId());// 买家开票资料不足
        }
        // 打款资料或银行开户许可证选其一
        if (hasPaymentData || hasOpenAccountLicense) {
            accountDao.updateBankDataStatusByPrimaryKey(account.getId(), AccountBankDataStatus.Requested.getCode(), null, null, user.getLoginId());
        } else {
            accountDao.updateBankDataStatusByPrimaryKey(account.getId(), AccountBankDataStatus.Insufficient.getCode(), null, null, user.getLoginId());
        }

        accountDao.updateByPrimaryKeySelective(account);

        insertAccountContact(contact, accountId, user);

        result.put("success", true);
        result.put("data", accountId);

        return result;
    }

    private void insertAccountContact(AccountContact contact, Long accountId,
                                      User user) {
        /** 增加联系人 **/
        if (contact != null) {
            if (accountDao.selectByTel(contact.getTel()) != null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
                        "公司电话不允许重复");
            } else if (accountcontactDao.queryByTel(contact.getTel()) != null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
                        "联系人号码不允许重复");
            }
            contact.setAccountId(accountId);
            contact.setModificationNumber(0);
            contact.setCreatedBy(user.getLoginId());
            contact.setCreated(new Date());
            contact.setLastUpdatedBy(user.getLoginId());
            contact.setLastUpdated(new Date());
            accountcontactDao.insert(contact);
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,
                    "联系人为必填");
        }
    }

    @Override
    public AccountDto selectByPrimaryKey(Long id) {
        return modelToDTO(accountDao.selectByPrimaryKey(id));
    }

    @Override
    public AccountDto selectByCode(String code) {
        return modelToDTO(accountDao.selectByCode(code));
    }

    @Override
    @Transactional
    public HashMap<String, Object> updateByPrimaryKeySelective(
            AccountDto record, List<MultipartFile> attachments,
            AccountContact contact) throws BusinessException {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Account account = record.getAccount();
        User user = record.getManager();
        String accountCode = account.getAccountCode();
        if (accountCode != null && !"".equals(accountCode.replaceAll(" ", ""))) {
            // 找到银行卡信息
            AccountBank accountBank = accountBankDao
                    .selectByAccountCode(StringToReplace
                            .toReplaceAll(accountCode));
            // 却发现不是本客户的
            if (accountBank != null
                    && !accountBank.getAccountId().equals(account.getId())) {
                result.put("success", false);
                result.put(
                        "data",
                        "银行卡号与客户【"
                                + accountDao.selectByPrimaryKey(
                                accountBank.getAccountId()).getName()
                                + "】重复，请检查");
                return result;
            }
        }
        Long accountId = account.getId();
        String bankCode = account.getBankCode(); // 开户银行行号
        // 插入银行信息，若银行行号不为空且在银行信息表不存在则新增银行信息 modify by kongbinheng 20150803
        if (accountCode != null && !"".equals(accountCode.replaceAll(" ", ""))) {
            AccountBank accountBank = accountBankDao
                    .selectByAccountCode(accountCode.replaceAll(" ", ""));
            if (accountBank == null) {
                accountBank = new AccountBank();
                accountBank.setAccountId(accountId); // 公司ID
                accountBank.setBankCode(StringToReplace.toReplaceAll(bankCode)); // 开户银行行号
                accountBank.setBankName(StringToReplace.toReplaceAll(account
                        .getBankNameMain())); // 开户银行主行
                accountBank.setBankNameBranch(StringToReplace
                        .toReplaceAll(account.getBankNameBranch())); // 开户银行支行
                accountBank.setBankProvinceId(account.getBankProvinceId());
                accountBank.setBankCityId(account.getBankCityId());
                if (account.getBankProvinceId() != null)
                    accountBank.setBankProvinceName(provinceDao
                            .selectByPrimaryKey(account.getBankProvinceId())
                            .getName());
                if (account.getBankCityId() != null)
                    accountBank.setBankCityName(cityDao.selectByPrimaryKey(
                            account.getBankCityId()).getName());
                accountBank.setBankAccountCode(StringToReplace
                        .toReplaceAll(accountCode)); // 银行账号
                accountBank.setCreated(new Date());
                accountBank.setCreatedBy(user.getLoginId());
                accountBank.setLastUpdated(new Date());
                accountBank.setLastUpdatedBy(user.getLoginId());
                accountBank.setModificationNumber(0);
                accountBankDao.insertSelective(accountBank);
            } else {
                // if(accountId.equals(accountBank.getAccountId())){
                accountBank.setBankCode(StringToReplace.toReplaceAll(bankCode)); // 开户银行行号
                accountBank.setBankName(StringToReplace.toReplaceAll(account
                        .getBankNameMain())); // 开户银行主行
                accountBank.setBankNameBranch(StringToReplace
                        .toReplaceAll(account.getBankNameBranch())); // 开户银行支行
                accountBankDao.updateByPrimaryKeySelective(accountBank);
                // }else{
                // //客户ID对不上
                // }
            }
        }

        account.setCode(null);// 不允许更改 避免强行提交
        account.setName(null);// 不允许更改 避免强行提交
        if (StringUtils.isEmpty(account.getBusinessType())) {
            account.setBusinessType(null);
        }

        Account tempAccount = accountDao.selectByPrimaryKey(account.getId());

        if (!StringUtils.isBlank(tempAccount.getAccountCode())
                && Constant.FROMBANKTRANSACTIONJOB.equals(account.getRowId())) { // 如果原来的客户银行卡号不为空，那么就不更新主表银行信息
            account.setBankCode(null);
            account.setBankNameMain(null);
            account.setAccountCode(null);
        }
        // 回归本来的类型，不因操作页面不同而变
        if (!AccountType.both.toString().equals(account.getType())) {
            account.setType(tempAccount.getType());
        }

        account.setLastUpdated(new Date());
        account.setLastUpdatedBy(user.getLoginId());
        account.setModificationNumber(tempAccount.getModificationNumber() + 1);

        accountDao.updateByPrimaryKeySelectiveExcludeBankInfo(account);
        // String basePath = rootPath + ATTACHMENTSAVEPATH + old.getCode()
        // + File.separator;

        boolean taxRegChanged = false;
        boolean invoiceDataChange = false;
        boolean paymentDataChanged = false;//打款资料
        boolean openAccountLicenseChanged = false;//银行开户许可证
        // 代运营合同一对多，先删再插
        if (attachmentsContainConsignContract(attachments)) {
            aamDao.deleteByAccountIdAndType(accountId,
                    AttachmentType.pic_consign_contract.getCode());
        }
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if ("pic_tax_reg".equals(file.getName())) {
                    taxRegChanged = true;
                } else if ("pic_invoice_data".equals(file.getName())) {
                    invoiceDataChange = true;
                } else if ("pic_payment_data".equals(file.getName())) {
                    paymentDataChanged = true;
                } else if ("pic_open_account_license".equals(file.getName())) {
                    openAccountLicenseChanged = true;
                }
                AttachmentType type = AttachmentType.valueOf(file.getName());

                AccountAttachment accountAttachment = null;
                if (!AttachmentType.pic_consign_contract.equals(type)) {
                    List<AccountAttachment> list = accountAttachmentDao.selectByAccountIdAndType(accountId, type.getCode());
                    if (list != null && list.size() > 0) {
                        accountAttachment = list.get(0);
                    }
                }
                // String savePath = basePath + type.getCode() + "."
                // + FileUtil.getFileSuffix(file.getOriginalFilename());
                if (accountAttachment == null) {
                    accountAttachment = new AccountAttachment();
                    accountAttachment.setAccountId(accountId);
                    accountAttachment.setType(type.getCode());
                    accountAttachment.setCreated(new Date());
                    accountAttachment.setCreatedBy(user.getLoginId());
                    // accountAttachment.setLastUpdatedBy(user.getLoginId());
                }
                accountAttachment.setLastUpdated(new Date());
                accountAttachment.setLastUpdatedBy(user.getLoginId());
                String saveKey = "";
                try {
                    saveKey = fileService.saveFile(
                            file.getInputStream(),
                            ATTACHMENTSAVEPATH
                                    + tempAccount.getCode()
                                    + File.separator
                                    + type.getCode()
                                    + "."
                                    + FileUtil.getFileSuffix(file
                                    .getOriginalFilename()));
                } catch (IOException e) {
                    BusinessException be = new BusinessException(
                            Constant.EXCEPTIONCODE_BUSINESS, "保存"
                            + AttachmentType.valueOf(file.getName())
                            + "出错");
                    throw be;
                }
                accountAttachment.setUrl(saveKey);
                if (accountAttachment.getId() == null
                        || type.equals(AttachmentType.pic_consign_contract)) {
                    aamDao.insertSelective(accountAttachment);
                } else {
                    aamDao.updateByPrimaryKeySelective(accountAttachment);
                }
            }
        }

        // 是否改了其中之一 买家开票资料审核
        if (!account.getType().equals(AccountType.seller)
                && !(((StringUtils.isEmpty(account.getTaxCode()) && StringUtils
                .isEmpty(tempAccount.getTaxCode())) || (account
                .getTaxCode() != null && account.getTaxCode().equals(
                tempAccount.getTaxCode())))
                && ((StringUtils.isEmpty(account.getAddr()) && StringUtils
                .isEmpty(tempAccount.getAddr())) || (account
                .getAddr() != null && account.getAddr().equals(
                tempAccount.getAddr())))
                && ((StringUtils.isEmpty(account.getTel()) && StringUtils
                .isEmpty(tempAccount.getTel())) || (account
                .getTel() != null && account.getTel().equals(
                tempAccount.getTel())))
                && ((StringUtils.isEmpty(account.getBankNameMain()) && StringUtils
                .isEmpty(tempAccount.getBankNameMain())) || (account
                .getBankNameMain() != null && account
                .getBankNameMain().equals(
                        tempAccount.getBankNameMain())))
                && ((StringUtils.isEmpty(account.getBankNameBranch()) && StringUtils
                .isEmpty(tempAccount.getBankNameBranch())) || (account
                .getBankNameBranch() != null && account
                .getBankNameBranch().equals(
                        tempAccount.getBankNameBranch())))
                && ((StringUtils.isEmpty(account.getAccountCode()) && StringUtils
                .isEmpty(tempAccount.getAccountCode())) || (account
                .getAccountCode() != null && account
                .getAccountCode().equals(
                        tempAccount.getAccountCode())))
                && ((StringUtils.isEmpty(account.getInvoiceType()) && StringUtils
                .isEmpty(tempAccount.getInvoiceType())) || (account
                .getInvoiceType() != null && account
                .getInvoiceType().equals(
                        tempAccount.getInvoiceType())))
                && !taxRegChanged && !invoiceDataChange)) {
            // 原来的状态就是资料不足，本次修改税务登记证和开票资料仍然未发生改变，既本次仍然未上传，则不做状态更改
            if (!(InvoiceDataStatus.Insufficient.getCode().equals(
                    tempAccount.getInvoiceDataStatus())
                    && !taxRegChanged && !invoiceDataChange)) {
                accountDao.updateInvoiceDataStatusByPrimaryKey(account.getId(),
                        InvoiceDataStatus.Requested.getCode(),null,
                        user.getLoginId());
            }

        }

        // modify kongbinheng 2015-11-20
        // 开户银行、开户银行所在城市、银行账号、行号、打款资料、银行开户许可证是否改了其中之一，需要进行审核
        if (!(((StringUtils.isEmpty(account.getBankNameMain()) && StringUtils.isEmpty(tempAccount.getBankNameMain()))
                || (account.getBankNameMain() != null && account.getBankNameMain().equals(tempAccount.getBankNameMain())))
                && ((StringUtils.isEmpty(account.getBankNameBranch()) && StringUtils.isEmpty(tempAccount.getBankNameBranch()))
                || (account.getBankNameBranch() != null && account.getBankNameBranch().equals(tempAccount.getBankNameBranch())))
                && ((StringUtils.isEmpty(String.valueOf(account.getBankProvinceId())) && StringUtils.isEmpty(String.valueOf(tempAccount.getBankProvinceId())))
                || (account.getBankProvinceId() != null && account.getBankProvinceId().equals(tempAccount.getBankProvinceId())))
                && ((StringUtils.isEmpty(String.valueOf(account.getBankCityId())) && StringUtils.isEmpty(String.valueOf(tempAccount.getBankCityId())))
                || (account.getBankCityId() != null && account.getBankCityId().equals(tempAccount.getBankCityId())))
                && ((StringUtils.isEmpty(account.getAccountCode()) && StringUtils.isEmpty(tempAccount.getAccountCode()))
                || (account.getAccountCode() != null && account.getAccountCode().equals(tempAccount.getAccountCode())))
                && ((StringUtils.isEmpty(account.getBankCode()) && StringUtils.isEmpty(tempAccount.getBankCode()))
                || (account.getBankCode() != null && account.getBankCode().equals(tempAccount.getBankCode())))
                && !paymentDataChanged && !openAccountLicenseChanged)) {
            // 原来的状态就是资料不足，本次修改打款资料和银行开户许可证仍然未发生改变，既本次仍然未上传，则不做状态更改
            if (!(AccountBankDataStatus.Insufficient.getCode().equals(tempAccount.getBankDataStatus())
                    && !paymentDataChanged && !openAccountLicenseChanged)) {
                accountDao.updateBankDataStatusByPrimaryKey(account.getId(), AccountBankDataStatus.Requested.getCode(), null, null, user.getLoginId());
            }
        }

        if (contact != null) {
            AccountContact oldContact = accountcontactDao.queryByTel(contact
                    .getTel());
            if (oldContact != null) {
                if (!oldContact.getName().equals(contact.getName())) {
                    throw new BusinessException(
                            Constant.EXCEPTIONCODE_BUSINESS, "新增联系人的号码已被其他人占用");
                } else if (!oldContact.getAccountId().equals(account.getId())) {
                    throw new BusinessException(
                            Constant.EXCEPTIONCODE_BUSINESS, "新增的联系人已被其他公司占用");
                } else {
                    if (oldContact.getIsMain() != Integer.parseInt(Constant.YES
                            .toString())) { // 判断该联系人原来是否是主联系人
                        Map<String, Object> map = new HashMap<>();
                        map.put("accountId", account.getId());
                        map.put("isMain", Constant.YES.toString());
                        map.put("type", AccountType.buyer.toString());
                        List<AccountContactDto> list = accountcontactDao
                                .queryByAccountId(map); // 把原来的主联系人干掉
                        for (AccountContact temp : list) {
                            temp.setIsMain(Integer.parseInt(Constant.NO
                                    .toString()));
                            temp.setLastUpdated(new Date());
                            temp.setLastUpdatedBy(user.getLoginId());
                            temp.setModificationNumber(temp
                                    .getModificationNumber() + 1);
                            if (accountcontactDao
                                    .updateAccountContactById(temp) != 1) {
                                throw new BusinessException(
                                        Constant.EXCEPTIONCODE_BUSINESS,
                                        "更新原联系人类型失败");
                            }
                        }
                    }
                    contact.setId(oldContact.getId());
                    contact.setIsMain(Integer.parseInt(Constant.YES.toString()));
                    contact.setType(AccountType.both.toString());
                    contact.setLastUpdated(new Date());
                    contact.setLastUpdatedBy(user.getLoginId());
                    contact.setModificationNumber(tempAccount
                            .getModificationNumber() + 1);
                    if (accountcontactDao.updateAccountContactById(contact) != 1) {
                        throw new BusinessException(
                                Constant.EXCEPTIONCODE_BUSINESS, "更新联系人类型失败");
                    }
                }
            } else {
                insertAccountContact(contact, accountId, user);
            }
        }

        result.put("success", true);
        result.put("data", account.getId());
        return result;

    }

    private boolean attachmentsContainConsignContract(List<MultipartFile> list) {
        if (list == null) {
            return false;
        }
        for (MultipartFile file : list) {
            if (file.getName().equals(
                    AttachmentType.pic_consign_contract.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int updateByPrimaryKey(Account record) {
        return accountDao.updateByPrimaryKey(record);
    }

    private AccountDto modelToDTO(Account model) {
        if (model == null)
            return null;
        AccountDto dto = new AccountDto();
        dto.setAccount(model);
        if (model.getBusinessType() != null) {
            dto.setBusinessType(AccountBusinessType.valueOf(model
                    .getBusinessType()));
        }
        dto.setProvince(provinceDao.selectByPrimaryKey(model.getProvinceId()));
        dto.setCity(cityDao.selectByPrimaryKey(model.getCityId()));
        dto.setDistrict(districtDao.selectByPrimaryKey(model.getDistrictId()));
        dto.setManager(userDao.queryById(model.getManagerId()));
        dto.setBankProvince(provinceDao.selectByPrimaryKey(model
                .getBankProvinceId()));
        dto.setBankCity(cityDao.selectByPrimaryKey(model.getBankCityId()));
        List<AccountAttachment> attachmentList = aamDao.listByAccountId(model
                .getId());
        // 代运营合同可以一对多，分开处理
        List<AccountAttachment> consignContracts = new ArrayList<AccountAttachment>();

        Map<String, Object> attachments = new HashMap<String, Object>();
        for (AccountAttachment aa : attachmentList) {
            if (aa.getType().equals(
                    AttachmentType.pic_consign_contract.getCode())) {
                consignContracts.add(aa);
            } else {
                attachments.put(aa.getType(), aa);
            }
        }
        attachments.put(AttachmentType.pic_consign_contract.getCode(),
                consignContracts);
        dto.setAttachments(attachments);
        return dto;
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long id, Integer status,
                                            String loginId) {
        return accountDao.updateStatusByPrimaryKey(id, status, loginId) > 0;
    }

    @Override
    public boolean updateInvoiceDataStatusByPrimaryKey(Long id, String status,String reason,
                                                       String loginId) {
        return accountDao.updateInvoiceDataStatusByPrimaryKey(id, status, reason,
                loginId) > 0;
    }

    @Override
    public List<SellerAccountDto> listSellerSearch(
            Map<String, Object> paramMap, List<Long> userIds) {
        Object companyName = paramMap.get("companyName");
        if (companyName != null
                && StringUtils.isNotEmpty(companyName.toString().replaceAll(
                " ", ""))) {
            paramMap.put("companyName", "%" + companyName.toString() + "%");
        }

        Object business = paramMap.get("business");
        if (business != null
                && StringUtils.isNotEmpty(business.toString().replaceAll(" ",
                ""))) {
            paramMap.put("business", "%" + business.toString() + "%");
        }

        List<SellerAccountDto> list = accountDao.listSellerSearch(paramMap);

        if (userIds != null) {
            // 非当前用户的客户不可查看手机号码
            for (SellerAccountDto item : list) {
                if (!userIds.contains(item.getManagerId())) {
                    item.setMobil(Tools.hidePhoneNumber(item.getMobil()));
                }
            }
        }
        return list;
    }

    @Override
    public int totalSellerSearch(Map<String, Object> paramMap) {
        return accountDao.totalSellerSearch(paramMap);
    }

    @Override
    public List<Map<String, Object>> selectByAccountNameAndContactName(
            Map<String, Object> paramMap) {
        String accountName = (String) paramMap.get("accountName");
        if (StringUtils.isNotEmpty(accountName)) {
            paramMap.put("accountName", "%" + accountName.replaceAll(" ", "")
                    + "%");
        }

        String contactName = (String) paramMap.get("contactName");
        if (StringUtils.isNotEmpty(contactName)) {
            paramMap.put("contactName", "%" + contactName.replaceAll(" ", "")
                    + "%");
        }
        String contactTel = (String) paramMap.get("contactTel");
        if (StringUtils.isNotEmpty(contactTel)) {
            paramMap.put("contactTel", "%" + contactTel.replaceAll(" ", "")
                    + "%");
        }

        return accountDao.selectByAccountNameAndContactName(paramMap);
    }

    @Override
    public int totalAccount(Map<String, Object> paramMap) {
        return accountDao.totalAccount(paramMap);
    }

    @Override
    public List<Account> listAccountByName(Map<String, Object> paramMap) {
        Object name = paramMap.get("name");
        if (name != null
                && StringUtils.isNotEmpty(name.toString().replaceAll(" ", ""))) {
            paramMap.put("name", "%" + name.toString() + "%");
        }
        return accountDao.listAccountByName(paramMap);
    }

    @Override
    public int totalListAccountByName(Map<String, Object> paramMap) {
        return accountDao.totalListAccountByName(paramMap);
    }

    @Override
    public Account selectAccountByName(String name) {
        Account account = null;
        if (name != null && StringUtils.isNotEmpty(name.replaceAll(" ", ""))) {
            account = accountDao.selectAccountByName(Tools.toDBC(name));//客户名称全角转换到半角 modify by wangxianjun
        }
        return account;
    }

    @Override
    public List<AccountContractTemplate> selectCTByModel(
            AccountContractTemplate act) {
        return actDao.selectByModel(act);
    }

    @Override
    @Transactional
    public int saveContractTemplate(AccountContractTemplate act,
                                    MultipartFile thumbnailFile) {
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            Account account = accountDao.selectByPrimaryKey(act.getAccountId());
            String savePath = ATTACHMENTSAVEPATH
                    + account.getCode()
                    + File.separator
                    + "contracttemplate"
                    + File.separator
                    + act.getType()
                    + File.separator
                    + act.getId()
                    + "."
                    + FileUtil.getFileSuffix(thumbnailFile
                    .getOriginalFilename());
            // FileUtil.saveFile(thumbnailFile, basePath);
            String key = "";
            try {
                key = fileService.saveFile(thumbnailFile.getInputStream(),
                        savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            act.setThumbnailUrl(key);
        }
        if (act.getEnabled() == 1) {
            actDao.disableOtherByAccountIdAndType(act.getAccountId(),
                    act.getType());
        }
        if (act.getId() == null) {
            actDao.insertSelective(act);
        } else {
            act.setStatus(ContractTemplateStatus.PENDING.getValue()); //更新状态为待审核
            actDao.updateByPrimaryKeySelective(act);
        }

        return 0;
    }

    public Account queryById(Long id) {
        return accountDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public boolean registerCompany(String companyName, String contact,
                                   String mobile, User operator) {
        Account account = new Account();
        account.setName(Tools.toDBC(companyName));//客户名称全角转换到半角 modify by wangxianjun
        account.setCreated(new Date());
        account.setCreatedBy(operator.getName());
        account.setModificationNumber(0);
        if (accountDao.insertSelective(account) > 0) {
            AccountContact accountContact = new AccountContact();
            accountContact.setAccountId(account.getId());
            accountContact.setTel(mobile);
            accountContact.setName(contact);
            accountContact.setCreated(new Date());
            accountContact.setCreatedBy(operator.getName());
            accountContact.setModificationNumber(0);
            return accountcontactDao.insertSelective(accountContact) > 0;
        } else
            return false;
    }

    @Override
    public List<Account> selectUncheckedBuyerList(Long orgId, String name) {
        return accountDao.selectUncheckedBuyerList(orgId, name);
    }

    @Override
    public int updateType(Long id, String type) {
        Account account = new Account();
        account.setId(id);
        account.setType(type);
        return accountDao.updateByPrimaryKeySelective(account);
    }

    @Override
    public List<AccountDto> queryForBalance(List<Long> accountIds) {
        return accountDao.queryForBalance(accountIds);
    }

    @Override
    public BigDecimal selectSecondBalance(Long accountId) {
        return accountDao.selectSecondBalance(accountId);
    }

    /**
     * 客户银行账号审核列表数据
     * @param name
     * @param beginTime
     * @param endTime
     * @param orderBy
     * @param order
     * @param start
     * @param length
     * @return
     */
    public List<Account> selectBankCodeVerifyList(String name, String beginTime, String endTime, String orderBy, String order, Integer start, Integer length){
        if (StringUtils.isBlank(name) || "null".equals(name.toLowerCase())) {
            name = "";
        }
        return accountDao.selectBankCodeVerifyList(name, beginTime, endTime, orderBy, order, start, length);
    }

    /**
     * 客户银行账号审核列表总数
     * @param name
     * @param beginTime
     * @param endTime
     * @return
     */
    public int selectBankCodeVerifyTotal(String name, String beginTime, String endTime){
        return accountDao.selectBankCodeVerifyTotal(name, beginTime, endTime);
    }

    /**
     * 客户银行账号审核更新状态和理由
     * @param id
     * @param bankDataStatus
     * @param bankDataReason
     * @param lastUpdatedBy
     * @return
     */
    public int updateBankDataStatusByPrimaryKey(Long id, String bankDataStatus, String bankDataReason, String bankDataReminded, String lastUpdatedBy){
        return accountDao.updateBankDataStatusByPrimaryKey(id, bankDataStatus, bankDataReason, bankDataReminded, lastUpdatedBy);
    }

    @Override
    public List<AccountAllDto> selectAllSellerAccount() {
        return accountDao.selectAllSellerAccount();
    }

    @Override
    @Transactional
    public SaveAccountDto save(SaveAccountDto saveInfo) {
        // check
        checkAccount(saveInfo);

        // set status
        setAccountStatus(saveInfo);

        User user = saveInfo.getUser();
        Long accountId;

        // add account
        accountId = addAccount(saveInfo.getAccount(), AccountStructureType.Company, saveInfo.getUser());
        saveInfo.getAccount().setId(accountId);

        // add default department
        Long departmentId = addAccount(saveInfo.getAccount(), AccountStructureType.Department, saveInfo.getUser());
        saveInfo.getAccount().setParentId(accountId);
        saveInfo.setDepartmentId(departmentId);
        saveInfo.getAccount().setId(accountId); // 添加部门以后会修改saveInfo.getAccount().id的值

        // add accountExt
        addAccountExt(accountId, saveInfo.getAccountExt(), user);

        // add proxyFactory
        if (saveInfo.getProxyFactoryList() != null && saveInfo.getProxyFactoryList().size() > 0) {
            addProxyFactory(saveInfo.getProxyFactoryList(), accountId, user);
        }

        // add contact
        Contact contact = saveInfo.getContact();
        Integer contactId = contact.getId();
        if (contactId == null || contactId == 0) {
            contactId = addContact(saveInfo.getContact(), user);
            saveInfo.getContact().setId(contactId);//把联系人ID返回
        }

        // add accountContact
        addAccountContact(departmentId, contactId, user);

        String bankUrl = "";
        // add accountAttachment
        if (saveInfo.getAttachmentList() != null && saveInfo.getAttachmentList().size() > 0) {
            saveInfo.getAttachmentList().forEach(item -> {
                item.setAccountId(accountId);
            });
            fossilizedImage(user, saveInfo.getAttachmentList());
            Optional<AccountAttachment> attachment = saveInfo.getAttachmentList().stream()
                    .filter(a -> a.getType().equals(AttachmentType.pic_payment_data.getCode())).findFirst();
            if (attachment.isPresent()) {
                bankUrl = attachment.get().getUrl();
            }
        }

        // add AccountBank
        addAccountBank(accountId, saveInfo, user, bankUrl);
       
        //启用工作流
        List<StartProcessRequest> processList = workFlowService.startWorkFlow(accountId, saveInfo.getAccountExt());
        if(processList != null){
        	for(StartProcessRequest process:processList){
        		try {
        			processService.startProcess(process);
				} catch (Exception e) {
					throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "启动审核流程失败");
				}
        	}
        }
        return saveInfo;
    }

    /**
     * 验证账号信息
     *
     * @param saveInfo 验证信息
     */
    private void checkAccount(SaveAccountDto saveInfo) {
        if (saveInfo == null || saveInfo.getAccount() == null || saveInfo.getContact() == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "账号信息为空");
        }
        // 验证该账号是否被注册
        if (saveInfo.getAccount().getName() != null) {
            Account account = accountDao.selectAccountByName(saveInfo.getAccount().getName());
            if (account != null && account.getId() > 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该公司名称已经存在");
            }
        }
        // 验证该账号联系人手机号码是否被使用，如果存在，名称必须一致 （可以与其他账号共用联系人）
        if (saveInfo.getContact().getTel() != null) {
            Contact contact = contactDao.queryByTel(saveInfo.getContact().getTel());
            if (contact != null) {
                if (!contact.getName().equals(saveInfo.getContact().getName())) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该联系人已经存在,请输入正确的联系人姓名");
                }
                saveInfo.setContact(contact);
            }
        }
        String accountCode = saveInfo.getAccountExt().getAccountCode();
        if (accountCode != null && !"".equals(accountCode.replaceAll(" ", ""))) {
            // 找到银行卡信息
            AccountBank accountBank = accountBankDao.selectByAccountCode(StringToReplace.toReplaceAll(accountCode));
            if (accountBank != null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS,"尾号为【"
                                + accountCode.substring(accountCode.length() - 4)
                                + "】银行账号已经与【"
                                + accountDao.selectByPrimaryKey(
                                accountBank.getAccountId()).getName()
                                + "】绑定，请联系陈虎【"
                                + userDao.queryByLoginId("chenhu").getTel()
                                + "】处理");
            }
        }

        // set default status
        AccountExt accountExt=saveInfo.getAccountExt();
        // 买家年度采购协议：买家客户时，初始状态是“待打印”；非买家客户时，初始状态是“-”
        if ((saveInfo.getAccount().getAccountTag() & AccountTag.buyer.getCode()) == AccountTag.buyer.getCode()) {
            accountExt.setAnnualPurchaseAgreementStatus(AnnualPurchaseAgreementStatus.ToPrint.getCode());
        }
        // 卖家代运营协议：不限定买卖家，初始状态都是“待打印”,如果上传了图片，状态为 已上传待审核
        List<AccountAttachment> attachmentList = saveInfo.getAttachmentList();
        if (attachmentList != null && attachmentList.size() > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_consign_contract.getCode())).count() > 0) {
            accountExt.setSellerConsignAgreementStatus(SellerConsignAgreementStatus.Uploaded.getCode());
        }
        else {
            accountExt.setSellerConsignAgreementStatus(SellerConsignAgreementStatus.ToPrint.getCode());
        }
    }

    private void setAccountStatus(SaveAccountDto saveInfo) {
        List<AccountAttachment> attachmentList = saveInfo.getAttachmentList();
        AccountExt accountExt = saveInfo.getAccountExt();
        Long accountId = saveInfo.getAccount().getId();
        AccountExt dbAccountExt = null;
        if (accountId != null && accountId > 0) {
            dbAccountExt = accountExtDao.selectByAccountId(accountId);
        }
        Integer attachmentCount = (attachmentList != null && attachmentList.size() > 0) ? attachmentList.size() : 0;
        // 证件资料，打款资料，开票资料没有填写任何一项，状态为 未上传，填写任何一项为 待审核
        if (dbAccountExt == null || dbAccountExt.getCardInfoStatus() == null) {
            accountExt.setCardInfoStatus(CardInfoStatus.Insufficient.getCode());
        }
        if (StringUtils.isNotEmpty(accountExt.getLicenseCode()) || StringUtils.isNotEmpty(accountExt.getOrgCode())
                || StringUtils.isNotEmpty(accountExt.getRegAddress()) || StringUtils.isNotEmpty(accountExt.getCreditCode())) {
            // 添加账号或者修改任何一项，或者上传了图片，状态改变成 待审核
            if ((accountId == null || accountId == 0) || (dbAccountExt != null &&
                    (!accountExt.getLicenseCode().equals(dbAccountExt.getLicenseCode()) || !accountExt.getOrgCode().equals(dbAccountExt.getOrgCode())
                            || !accountExt.getRegAddress().equals(dbAccountExt.getRegAddress()) || !accountExt.getCreditCode().equals(dbAccountExt.getCreditCode())))
                    || (attachmentCount > 0 &&
                    (attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_license.getCode())).count() > 0
                            || attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_org_code.getCode())).count() > 0
                            || attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_tax_reg.getCode())).count() > 0
                            || attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_credit_code.getCode())).count() > 0))
                    ) {
                accountExt.setCardInfoStatus(CardInfoStatus.Requested.getCode());
            }
        }
        // 打款资料
        if (dbAccountExt == null || dbAccountExt.getBankDataStatus() == null) {
            accountExt.setBankDataStatus(AccountBankDataStatus.Insufficient.getCode());
        }
        if (accountExt.getBankProvinceId() != null || StringUtils.isNotEmpty(accountExt.getBankNameMain())
                || StringUtils.isNotEmpty(accountExt.getBankCode()) || StringUtils.isNotEmpty(accountExt.getAccountCode())
                || (attachmentCount > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_payment_data.getCode())).count() > 0)) {
            // 添加账号或者修改任何一项，或者上传了图片，状态改变成 待审核
            if ((accountId == null || accountId == 0) || (dbAccountExt != null &&
                    (!accountExt.getBankProvinceId().equals(dbAccountExt.getBankProvinceId()) || !accountExt.getBankNameMain().equals(dbAccountExt.getBankNameMain())
                            || !accountExt.getBankCode().equals(dbAccountExt.getBankCode()) || !accountExt.getAccountCode().equals(dbAccountExt.getAccountCode())))
                    || (attachmentCount > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_payment_data.getCode())).count() > 0)
                    ) {
                accountExt.setBankDataStatus(AccountBankDataStatus.Requested.getCode());
            }
        }
        // 开票资料
        if (dbAccountExt == null || dbAccountExt.getInvoiceDataStatus() == null) {
            accountExt.setInvoiceDataStatus(InvoiceDataStatus.Insufficient.getCode());
        }
        if (StringUtils.isNotEmpty(accountExt.getTaxCode()) || StringUtils.isNotEmpty(accountExt.getAddr())
                || StringUtils.isNotEmpty(accountExt.getTaxTel()) || StringUtils.isNotEmpty(accountExt.getTaxBankNameMain())
                || StringUtils.isNotEmpty(accountExt.getBankNumber())
                || (attachmentCount > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_invoice_data.getCode())).count() > 0)) {
            // 添加账号或者修改任何一项，或者上传了图片，状态改变成 待审核
            if ((accountId == null || accountId == 0) || (dbAccountExt != null &&
                    (!accountExt.getTaxCode().equals(dbAccountExt.getTaxCode()) || !accountExt.getAddr().equals(dbAccountExt.getAddr())
                    || !accountExt.getTaxTel().equals(dbAccountExt.getTaxTel()) || !accountExt.getTaxBankNameMain().equals(dbAccountExt.getTaxBankNameMain())
                    || !accountExt.getBankNumber().equals(dbAccountExt.getBankNumber())
                    ))
                    || (attachmentCount > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_invoice_data.getCode())).count() > 0)
                    ) {
                accountExt.setInvoiceDataStatus(InvoiceDataStatus.Requested.getCode());
            }
        }
    }

    private Long addAccount(Account account, AccountStructureType structureType, User user) {
        Integer defaultValue = 0;
        if (structureType == AccountStructureType.Company) {
            account.setParentId(0L);
            account.setName(Tools.toDBC(account.getName()));//客户名称全角转换到半角 modify by wangxianjun
        } else if (structureType == AccountStructureType.Department) {
            account.setName("钢材部");
            account.setParentId(account.getId());
            account.setId(null);
        }
        account.setBalance(BigDecimal.ZERO);
        account.setBalanceFreeze(BigDecimal.ZERO);
        account.setBalanceSecondSettlement(BigDecimal.ZERO);
        account.setBalanceSecondSettlementFreeze(BigDecimal.ZERO);
        account.setBuyerDealTotal(defaultValue);
        account.setSellerDealTotal(defaultValue);
        account.setCreditAmount(BigDecimal.ZERO);
        account.setStructureType(structureType.toString());
        if (StringUtils.isEmpty(account.getBusinessType())) {
            account.setBusinessType(null);
        }
        account.setManagerId(user.getId());
        if ((account.getAccountTag() & AccountTag.seller.getCode()) == AccountTag.seller.getCode()) {
            account.setAccountTag(account.getAccountTag() | AccountTag.temp.getCode());
        }

        //account.setLastBillTime(new Date());
        account.setCreated(new Date());
        account.setCreatedBy(user.getName());
        account.setLastUpdated(new Date());
        account.setLastUpdatedBy(user.getName());
        account.setModificationNumber(defaultValue);
        Integer flag = accountDao.insertSelective(account);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加" + structureType.toString() + "账号失败");
        }
        return account.getId();
    }

    private void addProxyFactory(List<ProxyFactory> proxyFactoryList, Long accountId, User user) {
        proxyFactoryList.forEach(item -> {
            item.setAccountId(accountId);
            item.setCreated(new Date());
            item.setCreatedBy(user.getName());
            item.setLastUpdated(new Date());
            item.setLastUpdatedBy(user.getName());
            item.setModificationNumber(0);
            Integer factoryFlag = proxyFactoryDao.insertSelective(item);
            if (factoryFlag == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加代理钢厂失败");
            }
        });
    }

    private void addAccountExt(Long accountId, AccountExt accountExt, User user) {
        Date now = new Date();
        accountExt.setCustAccountId(accountId);
        accountExt.setRegTime(now);
        accountExt.setCode(getCode());
        accountExt.setName(Tools.toDBC(accountExt.getName()));//客户名称全角转换到半角 modify by wangxianjun
        accountExt.setCreated(now);
        accountExt.setCreatedBy(user.getName());
        accountExt.setLastUpdated(now);
        accountExt.setLastUpdatedBy(user.getName());
        accountExt.setModificationNumber(0);
        Integer flag = accountExtDao.insertSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加客户子表失败");
        }
    }

    private Integer addContact(Contact contact, User user) {
        contact.setCreated(new Date());
        contact.setCreatedBy(user.getName());
        contact.setLastUpdated(new Date());
        contact.setLastUpdatedBy(user.getName());
        contact.setModificationNumber(0);
        Integer flag = contactDao.insertSelective(contact);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加联系人失败");
        }
        return contact.getId();
    }

    private void addAccountContact(Long accountId, Integer contactId, User user) {
        AccountContact accountContact = new AccountContact();
        Role role = roleDao.queryById(user.getRoleId());
        // 只有交易员才能建立交易员关系
        if (role.getRoleType() != null && role.getRoleType().equals(RoleType.Trader.toString())) {
            accountContact.setManager(user.getId());
        } else {
            accountContact.setManager(0L);
        }
        accountContact.setAccountId(accountId);
        accountContact.setContactId(contactId);
        accountContact.setStatus(Integer.valueOf(Constant.YES));
        accountContact.setCreated(new Date());
        accountContact.setCreatedBy(user.getName());
        accountContact.setLastUpdated(new Date());
        accountContact.setLastUpdatedBy(user.getName());
        accountContact.setModificationNumber(0);
        Integer flag = accountcontactDao.insertSelective(accountContact);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加联系人关系失败");
        }
    }

    private void addAccountBank(Long accountId, SaveAccountDto saveInfo, User user,String url) {
        AccountExt accountExt=saveInfo.getAccountExt();
        String accountCode = accountExt.getAccountCode();
        List<AccountAttachment> attachmentList = saveInfo.getAttachmentList();
        Integer attachmentCount = (attachmentList != null && attachmentList.size() > 0) ? attachmentList.size() : 0;
        if (accountExt.getBankProvinceId() != null || StringUtils.isNotEmpty(accountExt.getBankNameMain())
                || StringUtils.isNotEmpty(accountExt.getBankCode()) || (accountCode != null && !"".equals(accountCode.replaceAll(" ", "")))
                || (attachmentCount > 0 && attachmentList.stream().filter(a -> a.getType().equals(AttachmentType.pic_payment_data.getCode())).count() > 0)) {
            AccountBank accountBank = accountBankDao.selectByAccountCode(StringToReplace.toReplaceAll(accountCode));
            if (accountBank == null) {
                accountBank = new AccountBank();
                accountBank.setIsDefault(0);
                accountBank.setBankDataStatus(AccountBankDataStatus.Requested.getCode());//待审核
                accountBank.setAccountId(accountId); // 公司ID
                accountBank.setBankCode(StringToReplace.toReplaceAll(accountExt.getBankCode())); // 开户银行行号
                accountBank.setBankName(StringToReplace.toReplaceAll(accountExt.getBankNameMain())); // 开户银行主行
                accountBank.setBankNameBranch(StringToReplace.toReplaceAll(accountExt.getBankNameBranch())); // 开户银行支行
                accountBank.setBankProvinceId(accountExt.getBankProvinceId());
                accountBank.setBankCityId(accountExt.getBankCityId());
                accountBank.setUrl(url);
                if (accountExt.getBankProvinceId() != null) {
                    accountBank.setBankProvinceName(provinceDao.selectByPrimaryKey(accountExt.getBankProvinceId()).getName());
                }
                if (accountExt.getBankCityId() != null) {
                    accountBank.setBankCityName(cityDao.selectByPrimaryKey(accountExt.getBankCityId()).getName());
                }
                accountBank.setBankAccountCode(StringToReplace.toReplaceAll(accountCode)); // 银行账号
                accountBank.setCreated(new Date());
                accountBank.setCreatedBy(user.getLoginId());
                accountBank.setLastUpdated(new Date());
                accountBank.setLastUpdatedBy(user.getLoginId());
                accountBank.setModificationNumber(0);
                Integer flag = accountBankDao.insertSelective(accountBank);
                if (flag == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加银行信息失败");
                }
            } else {
                throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "该银行账号已经存在");
            }
        }
    }
    /**
     * 上传用户附件公用方法（保存到临时目录）
     *
     * @param file 文件
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        checkFile(file);

        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String basePath = Constant.FILESAVETEMPPATH + format.format(cal.getTime()) + File.separator; // 按日期归类存放
        String tempPath = new Date().getTime() + "." + FileUtil.getFileSuffix(file.getOriginalFilename());
        String savePath = basePath + tempPath;
        String url = "";
        try {
            url = fileService.saveFile(file.getInputStream(), savePath);
            if (StringUtils.isEmpty(url)) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "文件保存失败");
            }
        } catch (Exception ex) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "文件保存失败，" + ex.getMessage());
        }
        return url;
    }

    private void checkFile(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "没有找到上传的文件");
        }

        String suffix = FileUtil.getFileSuffix(file.getOriginalFilename());

        if (suffix == null || !Constant.IMAGE_SUFFIX.contains(suffix.toLowerCase())) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,
                    AttachmentType.valueOf(file.getName()).getName() + "文件格式不正确");
        }
        if (file.getSize() / Constant.M_SIZE > Constant.MAX_IMG_SIZE) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM,
                    AttachmentType.valueOf(file.getName()).getName() + "超过" + Constant.MAX_IMG_SIZE + "M");
        }
    }

    @Override
    public void fossilizedImage(User user ,List<AccountAttachment> accountAttachmentList) {
        if(accountAttachmentList==null || accountAttachmentList.size()==0){
            return;
        }
        Date date = new Date();
        String userName = user.getName();
        for(int i=0 ; i<accountAttachmentList.size() ; i++) {
            AccountAttachment newAccountAttachment = accountAttachmentList.get(i);
            Long accountId = newAccountAttachment.getAccountId();
            String type = newAccountAttachment.getType();
            String tempUrl = newAccountAttachment.getUrl();
            String fileName = newAccountAttachment.getUrl();
            fileName = fileName.substring(fileName.lastIndexOf(File.separator), fileName.length());
            String url = ATTACHMENTSAVEPATH + accountId + File.separator + fileName;
            InputStream stream = fileService.getFileData(tempUrl);
            String key = fileService.saveFile(stream, url);

            fileService.removeFile(tempUrl);
            AccountAttachment oldAccountAttachment = null;
            // 买家年度采购协议 和 卖家代运营协议 可以多张
            if (!AttachmentType.pic_consign_contract.getCode().equals(type) && !AttachmentType.pic_purchase_agreement.getCode().equals(type)) {
                List<AccountAttachment> list = accountAttachmentDao.selectByAccountIdAndType(accountId, type);
                if (list != null && list.size() > 0) {
                    oldAccountAttachment = list.get(0);
                }
            }
            if (null != oldAccountAttachment) {
                oldAccountAttachment.setLastUpdated(date);
                oldAccountAttachment.setLastUpdatedBy(userName);
                oldAccountAttachment.setModificationNumber(oldAccountAttachment.getModificationNumber() + 1);

                oldAccountAttachment.setUrl(key);
                accountAttachmentDao.updateByPrimaryKeySelective(oldAccountAttachment);
            } else {
                newAccountAttachment.setLastUpdated(date);
                newAccountAttachment.setLastUpdatedBy(userName);
                newAccountAttachment.setCreated(date);
                newAccountAttachment.setCreatedBy(userName);
                newAccountAttachment.setModificationNumber(0);

                newAccountAttachment.setUrl(key);
                accountAttachmentDao.insertSelective(newAccountAttachment);
            }
        }
    }
    
    /**
     * 保存修改
     * @param saveInfo
     */
    @Transactional
    public void saveEdit(SaveAccountDto saveInfo) {
        User user = saveInfo.getUser();
        Account saveAccount = saveInfo.getAccount();
        // add afeng 保存客户前对客户名称进行校验
        Account selectAccount = null;
        if (saveAccount.getName() != null && StringUtils.isNotEmpty(saveAccount.getName().replaceAll(" ", ""))) {
        	selectAccount = accountDao.selectAccountByName(saveAccount.getName());
        }
        if (selectAccount != null && !selectAccount.getId().equals(saveAccount.getId())) {
        	 throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "该账号已存在！");
        }
        AccountExt saveAccountExt = saveInfo.getAccountExt();
        
        AccountExt accountExt = accountExtDao.selectByAccountId(saveAccount.getId());
        // 如果是卖家，寄售合同已经审核通过，需要修改AccountTag
        if ((saveAccount.getAccountTag() & AccountTag.seller.getCode()) == AccountTag.seller.getCode()) {
            if (SellerConsignAgreementStatus.Approved.getCode().equals(accountExt.getSellerConsignAgreementStatus())) {
                saveAccount.setAccountTag(saveAccount.getAccountTag() | AccountTag.consign.getCode());
            } else {
                saveAccount.setAccountTag(saveAccount.getAccountTag() | AccountTag.temp.getCode());
            }
        }
        else{
            // 纯买家没有服务中心
            saveAccount.setOrgId(0L);
            saveAccount.setOrgName("");
        }
        // 如果本次修改添加了买家角色，需要添加买家年度采购协议状态：买家客户时，初始状态是“待打印”；
        if ((saveInfo.getAccount().getAccountTag() & AccountTag.buyer.getCode()) == AccountTag.buyer.getCode()
                && accountExt.getAnnualPurchaseAgreementStatus() == null) {
            accountExt.setAnnualPurchaseAgreementStatus(AnnualPurchaseAgreementStatus.ToPrint.getCode());
        }

        updateAccount(user, saveAccount);
        updateAccountExt(user, accountExt, saveAccountExt);
    }

    private void updateAccount(User user, Account saveAccount) {
        Account account = accountDao.selectByPrimaryKey(saveAccount.getId());
        if (account == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "不存在该账号");
        }
        account.setName(Tools.toDBC(saveAccount.getName()));//客户名称全角转换到半角 modify by wangxianjun
        account.setAccountTag(saveAccount.getAccountTag());
        account.setOrgId(saveAccount.getOrgId());
        account.setOrgName(saveAccount.getOrgName());
        account.setBusinessType(saveAccount.getBusinessType());
        //迁移风控代码，增加5个字段 add by zhoucai@prcsteel.com 2016-5-6
        // 因可能存在客户将注册资金清空保存，此字段单独写方法保存 afeng
        if (saveAccount.getRegisterCapital() == null) {
        	account.setEspecially(true);
        } else {
        	account.setRegisterCapital(saveAccount.getRegisterCapital()); 
        }
        account.setRegisterDate(saveAccount.getRegisterDate());
        account.setBuyerLabel(saveAccount.getBuyerLabel());
        account.setPaymentLabel(saveAccount.getPaymentLabel());
        account.setSupplierLabel(saveAccount.getSupplierLabel());
        account.setSellerSingleTradeWeight(saveAccount.getSellerSingleTradeWeight());
        account.setSellerAllTradeWeight(saveAccount.getSellerAllTradeWeight());
        account.setSellerAllTradeQuality(saveAccount.getSellerAllTradeQuality());
        account.setIsSellerPercent(saveAccount.getIsSellerPercent());



        account.setLastBillTime(new Date());
        account.setLastUpdated(new Date());
        account.setLastUpdatedBy(user.getName());
        account.setModificationNumber(account.getModificationNumber() + 1);
        Integer flag = accountDao.updateByPrimaryKeySelective(account);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改账号失败");
        }
    }

    private void updateAccountExt(User user, AccountExt accountExt, AccountExt saveAccountExt) {
        if (accountExt == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "不存在该账号");
        }
        accountExt.setName(Tools.toDBC(saveAccountExt.getName()));//客户名称全角转换到半角 modify by wangxianjun
        accountExt.setLegalPersonName(saveAccountExt.getLegalPersonName());
        accountExt.setWebSiteUrl(saveAccountExt.getWebSiteUrl());
        accountExt.setMobil(saveAccountExt.getMobil());
        accountExt.setTel(saveAccountExt.getTel());
        accountExt.setFax(saveAccountExt.getFax());
        accountExt.setZip(saveAccountExt.getZip());
        accountExt.setMailAddr(saveAccountExt.getMailAddr());
        accountExt.setProvinceId(saveAccountExt.getProvinceId());
        accountExt.setCityId(saveAccountExt.getCityId());
        accountExt.setDistrictId(saveAccountExt.getDistrictId());

        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy(user.getName());
        accountExt.setModificationNumber(accountExt.getModificationNumber() + 1);
        Integer flag = accountExtDao.updateByAccountIdSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改账号子表失败");
        }
    }

    /**
     * 批量修改状态
     *
     * @param param
     */
    @Override
    public void batchUpateStatusByPrimaryKeys(AccountDtoForUpdate param) {
        int count = accountDao.batchUpateStatusByPrimaryKeys(param);
        if (count != param.getIds().size()) throw  new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "批量修改状态失败");
    }

    /**
     * 保存证件资料
     * @param saveInfo
     */
    @Override
    @Transactional
    public void saveCredentials(SaveAccountDto saveInfo) {
        User user = saveInfo.getUser();
        // set status
        AccountExt oldExt = accountExtDao.selectByAccountId(saveInfo.getAccount().getId());
        String oldCardInfoStatus = oldExt == null ? "":oldExt.getCardInfoStatus();
        String oldInvoiceInfoStatus = oldExt == null ? "":oldExt.getInvoiceDataStatus();
        setAccountStatus(saveInfo);
        Account saveAccount = saveInfo.getAccount();
        AccountExt saveAccountExt = saveInfo.getAccountExt();
        updateAccountExtByCredentials(user, saveAccount.getId(), saveAccountExt);
        if (saveInfo.getAttachmentList() != null && saveInfo.getAttachmentList().size() > 0) {
            saveInfo.getAttachmentList().forEach(item -> {
                item.setAccountId(saveAccount.getId());
            });
        }
        fossilizedImage(user,saveInfo.getAttachmentList());
        try {
            //工作流启动判断
            AccountExt saveExt = saveInfo.getAccountExt();
            List<StartProcessRequest> processList = new ArrayList<StartProcessRequest>();
            StartProcessRequest startRequest;
            ListTaskQuery taskQuery;
            List<TaskInfo> taskList;
            //证件资料变成带审核
            if(CardInfoStatus.Requested.getCode().equals(saveExt.getCardInfoStatus())){
            	//未上,已通过-带审核,启用新的任务
            	if(StringUtils.isEmpty(oldCardInfoStatus)|| CardInfoStatus.Insufficient.getCode().equals(oldCardInfoStatus) 
            			|| CardInfoStatus.Approved.getCode().equals(oldCardInfoStatus)){
            		startRequest = workFlowService.createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_CARDINFO.getValue().toString(),
            				saveInfo.getAccount().getId(),
        					WorkFlowEnum.CARD_INFO,WorkFlowConstant.DOC_PROCESS_DEFINITION_KEY);
            		processList.add(startRequest);
            		//审核未通过-带审核,继续任务
            	}else if(CardInfoStatus.Declined.getCode().equals(oldCardInfoStatus)){
                	taskQuery =  workFlowService.createEditTaskQuery(WorkFlowConstant.DOC_EDIT_DOC,saveInfo.getAccount().getId().toString());
                	taskList = workFlowService.getAllTaskList(taskQuery);
                	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, null, saveInfo.getAccount().getId().toString());
                	if(actionInfo.getRequest() != null){
                		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
                	}
            	}
            }
            //开票资料
            if(InvoiceDataStatus.Requested.getCode().equals(saveExt.getInvoiceDataStatus())){
            	//未上传,已通过,-带审核,启用新的任务
            	if(StringUtils.isEmpty(oldInvoiceInfoStatus) || InvoiceDataStatus.Insufficient.getCode().equals(oldInvoiceInfoStatus) 
            			|| InvoiceDataStatus.Approved.getCode().equals(oldInvoiceInfoStatus)){
            		startRequest = workFlowService.createStartRequest(WorkFlowEnum.ACCOUNT_AUDIT_INVOICE.getValue().toString(),
            				saveInfo.getAccount().getId(),
        					WorkFlowEnum.INVOICE_DATA,WorkFlowConstant.INV_PROCESS_DEFINITION_KEY);
            		processList.add(startRequest);
            		//审核未通过-带审核,继续任务
            	}else if(InvoiceDataStatus.Declined.getCode().equals(oldInvoiceInfoStatus)){
                	taskQuery =  workFlowService.createEditTaskQuery(WorkFlowConstant.INV_EDIT_INVOICE,saveInfo.getAccount().getId().toString());
                	taskList = workFlowService.getAllTaskList(taskQuery);
                	TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, null, saveInfo.getAccount().getId().toString());
                	if(actionInfo.getRequest() != null){
                		taskService.invokeTaskAction(actionInfo.getTaskId(),actionInfo.getRequest());
                	}
            	}
            }
         	//启动所有新任务
        	for(StartProcessRequest process:processList){
        		processService.startProcess(process);
        	}
		} catch (Exception e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "启动审核流程失败");
		}
    }

    private void updateAccountExtByCredentials(User user, Long accountId, AccountExt saveAccountExt) {
        AccountExt accountExt = accountExtDao.selectByAccountId(accountId);
        if (accountExt == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "不存在该账号");
        }
        accountExt.setLicenseCode(saveAccountExt.getLicenseCode());
        accountExt.setLegalPersonName(saveAccountExt.getLegalPersonName());
        accountExt.setRegAddress(saveAccountExt.getRegAddress());
        accountExt.setOrgCode(saveAccountExt.getOrgCode());
        accountExt.setCreditCode(saveAccountExt.getCreditCode());
        accountExt.setInvoiceSpeed(saveAccountExt.getInvoiceSpeed());
        accountExt.setTaxCode(saveAccountExt.getTaxCode());
        accountExt.setAddr(saveAccountExt.getAddr());
        accountExt.setTaxTel(saveAccountExt.getTaxTel());
        accountExt.setTaxBankNameMain(saveAccountExt.getTaxBankNameMain());
        accountExt.setTaxBankNameBranch(saveAccountExt.getTaxBankNameBranch());
        accountExt.setBankNumber(saveAccountExt.getBankNumber());
        accountExt.setInvoiceType(saveAccountExt.getInvoiceType());
        accountExt.setBankProvinceId(saveAccountExt.getBankProvinceId());
        accountExt.setBankCityId(saveAccountExt.getBankCityId());
        accountExt.setBankNameMain(saveAccountExt.getBankNameMain());
        accountExt.setBankNameBranch(saveAccountExt.getBankNameBranch());
        accountExt.setBankCode(saveAccountExt.getBankCode());
        accountExt.setAccountCode(saveAccountExt.getAccountCode());
        accountExt.setCardInfoStatus(saveAccountExt.getCardInfoStatus());
        accountExt.setBankDataStatus(saveAccountExt.getBankDataStatus());
        accountExt.setInvoiceDataStatus(saveAccountExt.getInvoiceDataStatus());

        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy(user.getName());
        accountExt.setModificationNumber(accountExt.getModificationNumber() + 1);
        Integer flag = accountExtDao.updateByAccountIdSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改账号子表失败");
        }
    }

    /**
     * 根据条件查询公司列表
     *
     * @param query
     * @return
     */
    @Override
    public List<CompanyDto> queryCompanyByCompanyQuery(CompanyQuery query) {
        return accountDao.queryCompanyByCompanyQuery(query);
    }

    /**
     * 根据条件查询公司列表总数
     *
     * @param query
     * @return
     */
    @Override
    public int queryCompanyTotalByCompanyQuery(CompanyQuery query) {
        return accountDao.queryCompanyTotalByCompanyQuery(query);
    }

    /**
     * 保存资金分配或撤回
     * @param accountList
     * @param user
     */
	@Override
	@Transactional
	public List<Account> saveFundAllocations(String accountList, User user,Long accountId) {
        List<AccountFundAllocationsDto> accountFundAllocationsDtos = jsonToAccountFundAllocationsDto(accountList);
		String code = null;
		String existCode = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String strData = sdf.format(date);
		for(int i = 0; i < accountFundAllocationsDtos.size(); i++){
            AccountFundAllocationsDto allocationsDto = accountFundAllocationsDtos.get(i).getAllocationsDtoList().get(0);
            String associationType = allocationsDto.getAssociationType();
            List<AccountFundAllocationsDto> allocationsDtoList = accountFundAllocationsDtos.get(i).getAllocationsDtoList();
			//产生关联单号
			if("MONEYALLOCATION".equals(associationType)){ //关联类型为资金分配
				existCode = accountTransLogDao.selectAccountTransLogByType("DB");
				code = "DB" + strData;
			}else if("MONEYBACK".equals(associationType)){ //关联类型为资金撤回
				existCode = accountTransLogDao.selectAccountTransLogByType("CH");	
				code = "CH" + strData;
			}
			if(existCode != "" && existCode != null){
				existCode = existCode.substring(existCode.length()-4, existCode.length());
			}else{
				existCode="0000";
			}
			int num=Integer.valueOf(existCode)+1;
		    StringBuffer sb=new StringBuffer();
		    String str=String.valueOf(num);
		    for (int q = 0; q < 4-str.length(); q++) {
		        sb.append("0");
		    }if(str.length()<=4){
		    	str=sb.toString()+str;
		    } 
		    code = code + str;
		    for(int j = 0; j < allocationsDtoList.size(); j++){
                AccountFundAllocationsDto allocationsDto1=allocationsDtoList.get(j);
		    	accountFundService.updateAccountFund(allocationsDto1.getAccountId(), AssociationType.valueOf(allocationsDto1.getAssociationType()),
						code, AccountTransApplyType.valueOf(allocationsDto1.getAccountTransApplyType()),
                        allocationsDto1.getBalance(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), new Date());

                //更新银票资金分配信息（资金分配记录有两条，第一条为公司分配给部门，第二条为部门充值）
                if(1 == j && "MONEYALLOCATION".equals(associationType)){
                    Long departmentId = allocationsDto1.getAccountId();
                    if(new Boolean(allocationsDto1.isDraftAllocation())){
                        accountAcceptDraftService.save(new AccountAcceptDraft(departmentId, allocationsDto1.getAcceptDraftId(),
                                allocationsDto1.getBalance()), user);
                        accountFundService.payForCredit(departmentId,  AssociationType.ACCEPT_DRAFT, code, 0l, Constant.SYSTEMNAME, new Date());
                    }else{
                        accountFundService.payForCredit(departmentId,  AssociationType.MONEYALLOCATION, code, 0l, Constant.SYSTEMNAME, new Date());
                    }
                }
            }
		}
		return accountDao.selectAccountInfoById(accountId);
	}

    private List<AccountFundAllocationsDto> jsonToAccountFundAllocationsDto(String accountList){
        List<AccountFundAllocationsDto> accounts = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        AccountFundAllocationsDto allocationsDto;
        try {
            JsonNode allNode = mapper.readTree(accountList);
            for (JsonNode node : allNode) {
                AccountFundAllocationsDto twoAllocationsDtos = new AccountFundAllocationsDto();
                List<AccountFundAllocationsDto> allocationsDtos = new ArrayList<>();
                for (JsonNode nodeItem : node){
                    allocationsDto = new AccountFundAllocationsDto();
                    allocationsDto.setAccountId(new Long(nodeItem.path("accountId").asText()));
                    allocationsDto.setBalance(new BigDecimal(nodeItem.path("balance").asText()));
                    allocationsDto.setAssociationType(nodeItem.path("associationType").asText());
                    allocationsDto.setAccountTransApplyType(nodeItem.path("accountTransApplyType").asText());
                    if(!nodeItem.path("consignType").isNull() && !StringUtils.isEmpty(nodeItem.path("isDraftAllocation").toString())){
                        allocationsDto.setIsDraftAllocation(Boolean.valueOf(nodeItem.path("isDraftAllocation").asInt() > 0));
                    }
                    if(!nodeItem.path("acceptDraftId").isNull() && !StringUtils.isEmpty(nodeItem.path("acceptDraftId").toString())){
                        allocationsDto.setAcceptDraftId(new Long(nodeItem.path("acceptDraftId").asText()));
                    }
                    if(!nodeItem.path("acceptDraftAmount").isNull() && !StringUtils.isEmpty(nodeItem.path("acceptDraftAmount").toString())){
                        allocationsDto.setAcceptDraftAmount(new BigDecimal(nodeItem.path("acceptDraftAmount").asText()));
                    }
                    allocationsDtos.add(allocationsDto);
                }
                twoAllocationsDtos.setAllocationsDtoList(allocationsDtos);
                accounts.add(twoAllocationsDtos);
            }
        }
        catch (IOException var16) {
            throw new BusinessException(BANDEDCODE, "转换失败！");
        }
        return accounts;
    }

    /**
     * 查询公司的所有部门
     * @param companyId
     * @return
     */
    @Override
    public List<DepartmentDto> queryDeptByCompanyId(Long companyId) {
            return accountDao.queryDeptByCompanyId(companyId);
    }

    /**
     * 查询操作员有权限的 归属服务中心的部门
     * @param companyId
     * @return
     */
    @Override
    public List<DepartmentDto> queryDeptByCompanyIdAndBelongOrg(Long companyId, Long operatorOrgId){
        List<DepartmentDto> list = accountDao.queryDeptByCompanyId(companyId);
        List<DepartmentDto> result = new ArrayList<DepartmentDto>();
        String operatorOrgIdStr = operatorOrgId.toString();

        for(DepartmentDto dto : list){
            if(StringUtils.isNotEmpty(dto.getBelongOrg())){
                List<String> belongOrgList = java.util.Arrays.asList(dto.getBelongOrg().split(","));
                if(belongOrgList.contains(operatorOrgIdStr)){
                    result.add(dto);
                }
            }

        }
        return result;
    }

    /**
     * 根据交易员ID查询相关联的公司列表
     * @param query
     * @return
     */
    @Override
    public List<Account> queryAccountByManager(Account query){
        return accountDao.queryAccountByManager(query);
    }

    /**
     *
     *根据客户名称查询 有几个部门
     * @param accountId  客户名称
     * @return
     */
    public int queryDepartmentByName(Long accountId){
        return accountDao.queryDepartmentByName(accountId);
    }
    public  Long queryDepartment(Long id){
        return accountDao.queryDepartment(id);
    }

    /**
     * 根据账号Id修改
     *
     * @param accountExt 修改对象
     * @param user       操作用户
     */
    @Override
    public Integer updateByAccountIdSelective(AccountExt accountExt,User user) {
        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy(user.getName());
        return accountExtDao.updateByAccountIdSelective(accountExt);
    }

    /**
     * 绑定客户，将微信与本系统客户相互绑定
     * 也就是通过手机查询本系统，将open_id存入到与之对应的记录中。
     *
     * @param query：
     * 		token:
     * 		openid: 要绑定的号码
     * 		phone： 用来在本系系统查找联系人是不是存在
     * @throws BusinessException 如果抛出异常刚说明绑定失败，e.getMessage()可得到保存失败原因
     * @author tuxianming
     * @date 2016-03-04
     */
    @Override
    public void wxBindContract(WXContractQuery query) throws BusinessException {
        //从缓存里面去查找token，如果找不到则token超时
		/*if(StringUtils.isEmpty(query.getToken()) || cacheService.get(query.getToken())==null){
			throw new BusinessException(FAILCODE, "token失效");
		}*/
        if(contactDao.queryByTel(query.getOpenId()) != null){
            throw new BusinessException(BANDEDCODE, "该手机号码已绑定！");
        }

        //查询cust_account_contact, 不存在就绑定失败，存在就是设置openid
        Contact contact = contactDao.queryByTel(query.getPhone());
        if(contact==null)
            throw new BusinessException(FAILCODE, "您还不是本公司的联系人！");
        if(contact.getOpenId() != null){
            throw new BusinessException(BANDEDCODE, "该手机号码已绑定！");
        }

        Contact updateObj = new Contact();
        updateObj.setId(contact.getId());
        updateObj.setOpenId(query.getOpenId());

        try{
            contactDao.updateOpenId(updateObj);
        }catch(Exception e){
            throw new BusinessException(FAILCODE, e.getMessage());
        }
    }

    /**
     * 解除绑定的客户，也就是绑定客户的逆向操作
     * @param query
     * 		token:
     * 		openid: 要绑定的号码
     * 		phone： 用来在本系系统查找联系人是不是存在
     * @throws BusinessException 如果抛出异常刚说明解绑失败，e.getMessage()可得到保存失败原因
     * @author tuxianming
     * @date 2016-03-04
     */
    @Override
    public void wxUnbindContract(WXContractQuery query) throws BusinessException {
        //从缓存里面去查找token，如果找不到则token超时
		/*if(StringUtils.isEmpty(query.getToken()) || cacheService.get(query.getToken())==null){
			throw new BusinessException(FAILCODE, "token失效");
		}*/
        if(contactDao.queryByOpenId(query.getOpenId()) == null){
            throw new BusinessException(BANDEDCODE, "该手机号码已解绑！");
        }
        //查询cust_account_contact, 不存在就绑定失败，存在就是设置openid
        Contact contact = contactDao.queryByTel(query.getPhone());
        if(contact==null)
            throw new BusinessException(FAILCODE, "您还不是本公司的联系人！");

        Contact updateObj = new Contact();
        updateObj.setId(contact.getId());

        try{
            contactDao.updateOpenId(updateObj);
        }catch(Exception e){
            throw new BusinessException(FAILCODE, e.getMessage());
        }
    }
    
	@Override
	public CustAccountDto queryForAccountId(Long id) {
		return accountDao.queryForAccountId(id);
	}

    @Override
    public AccountExt queryAccountExtByAccountId(Long accountId) {
        return accountExtDao.selectByAccountId(accountId);
    }

    @Override
    public AccountCreditDto payForCredit(Long accountId, BigDecimal amount, String option, User user) {
        Date date = new Date();
        String code = custGroupingInforService.createCode();
        if(Constant.OPERATION_DEDUCTION.equals(option)){   //抵扣
            accountFundService.updateAccountFund(accountId, AssociationType.CREDIT_SERIAL, code, AccountTransApplyType.CREDITLIMIT_TRANSTO_ACCOUNT,
                    amount, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, amount,
                    BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), date);   //信用额度转入资金账户
            accountFundService.updateAccountFund(accountId, AssociationType.CREDIT_SERIAL, code, AccountTransApplyType.SECONDARY_SETTLEMENT_ACCOUNT_BALANCES,
                    amount.negate(), BigDecimal.ZERO, amount, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), date);   //资金账户抵扣二次结算账户
        }else if(Constant.OPERATION_RESTITUTION.equals(option)){   //归还
            accountFundService.updateAccountFund(accountId, AssociationType.CREDIT_SERIAL, code, AccountTransApplyType.INTO_THE_CAPITAL_ACCOUNT,
                    amount, BigDecimal.ZERO, amount.negate(), BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), date);   //二结转入资金账户
            accountFundService.updateAccountFund(accountId, AssociationType.CREDIT_SERIAL, code, AccountTransApplyType.CREDITLIMI_TREPAYMENT,
                    amount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, amount.negate(),
                    BigDecimal.ZERO, PayType.BALANCE, user.getId(), user.getName(), date);   //资金账户抵扣
        }
        return accountDao.selectActualCreditBalanceById(accountId);
    }

    /**
     * 保存协议上传的图片，更新协议状态为“已上传待审核” chengui
     * @param user
     * @param saveAccount
     * @return
     */
    @Override
    @Transactional
    public void saveAgreementUploadFiles(User user, SaveAccountDto saveAccount) {
        AccountExt oldAccountExt = queryAccountExtByAccountId(saveAccount.getAccountExt().getCustAccountId());
        String oldAnnualStatus = oldAccountExt == null ?"":oldAccountExt.getAnnualPurchaseAgreementStatus();
        String oldSellerStatus = oldAccountExt == null ?"":oldAccountExt.getSellerConsignAgreementStatus();
        fossilizedImage(user, saveAccount.getAttachmentList());
        updateAccountExtByUploadFiles(user, saveAccount.getAccountExt(), saveAccount.getAttachmentList().get(0).getType());
        try {
        	 AccountExt newAccountExt = queryAccountExtByAccountId(saveAccount.getAccountExt().getCustAccountId());
             //工作流启动判断
             StartProcessRequest startRequest;
             String isUpload = "1";//已上传
             String type = saveAccount.getAttachmentList().get(0).getType();//代运营协议或采购协议

             //年度采购协议图片上传
             if(AttachmentType.pic_purchase_agreement.getCode().equals(type)){
                 //已上传待审核
                 if (AnnualPurchaseAgreementStatus.Uploaded.getCode().equals(newAccountExt.getAnnualPurchaseAgreementStatus())){
                     //待打印->已上传待审核 启动流程  判断流程是否结束 ，未结束执行下一个任务 结束，启动新的
                     if (AnnualPurchaseAgreementStatus.ToPrint.getCode().equals(oldAnnualStatus)){
                         if (workFlowCommonService.isProcessEnded(WorkFlowConstant.BUYER_AGREEMENT_PROCESS_DEFINITION_KEY,newAccountExt.getCustAccountId().toString())){
                             startRequest = workFlowService.createAnnualPurchaseAgreementWorkFlow(newAccountExt.getCustAccountId(), isUpload);
                             processService.startProcess(startRequest);
                         }else{
                             List<ActivitiVariable> variables = new ArrayList<>();
                             variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                             //推动到下一个任务
                             invokeNextTaskSellerConsignAgreement(WorkFlowConstant.BUYER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());
                         }

                         //一审未通过、二审未通过、审核未通过->已上传待审核，继续执行任务 当前节点打印编辑或上传图片节点
                     }else if(AnnualPurchaseAgreementStatus.FirstDeclined.getCode().equals(oldAnnualStatus)
                             || AnnualPurchaseAgreementStatus.SecondDeclined.getCode().equals(oldAnnualStatus)
                             || AnnualPurchaseAgreementStatus.Declined.getCode().equals(oldAnnualStatus)){

                         List<ActivitiVariable> variables = new ArrayList<>();
                         variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                         //推动到下一个任务
                          invokeNextTaskSellerConsignAgreement(WorkFlowConstant.BUYER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());

                         //二审通过->已上传待审核，继续执行任务 当前节点打印上传图片节点
                     }else if(AnnualPurchaseAgreementStatus.SecondApproved.getCode().equals(oldAnnualStatus)){
                         //推动到下一个任务
                         invokeNextTaskSellerConsignAgreement(WorkFlowConstant.BUYER_AGREEMENT_UPLOAD_IMG, null, newAccountExt.getCustAccountId());
                     }
                 }

                 //代运营协议图片上传
             }else if(AttachmentType.pic_consign_contract.getCode().equals(type)) {
                 //已上传待审核
                 if (SellerConsignAgreementStatus.Uploaded.getCode().equals(newAccountExt.getSellerConsignAgreementStatus())){
                     //待打印->已上传待审核 启动流程 判断流程是否结束 未结束执行下一个任务 结束启动新的，
                     if (SellerConsignAgreementStatus.ToPrint.getCode().equals(oldSellerStatus)){
                         if (workFlowCommonService.isProcessEnded(WorkFlowConstant.SELLER_AGREEMENT_PROCESS_DEFINITION_KEY,newAccountExt.getCustAccountId().toString())){
                             startRequest = workFlowService.createSellerConsignAgreementWorkFlow(newAccountExt.getCustAccountId(), isUpload);
                             processService.startProcess(startRequest);
                         }else{
                             List<ActivitiVariable> variables = new ArrayList<>();
                             variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                             //推动到下一个任务
                             invokeNextTaskSellerConsignAgreement(WorkFlowConstant.SELLER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());
                         }
                         //一审未通过、二审未通过、审核未通过->已上传待审核，继续执行任务 当前节点打印编辑或上传图片节点
                     }else if(SellerConsignAgreementStatus.FirstDeclined.getCode().equals(oldSellerStatus)
                             || SellerConsignAgreementStatus.SecondDeclined.getCode().equals(oldSellerStatus)
                             || SellerConsignAgreementStatus.Declined.getCode().equals(oldSellerStatus)){

                         List<ActivitiVariable> variables = new ArrayList<>();
                         variables.add(new ActivitiVariable(WorkFlowConstant.VAR_AGREEMENT_ISUPLOAD,isUpload));
                         //推动到下一个任务
                         invokeNextTaskSellerConsignAgreement(WorkFlowConstant.SELLER_AGREEMENT_EDIT_AGREEMENT, variables, newAccountExt.getCustAccountId());

                      //二审通过->已上传待审核，继续执行任务 当前节点打印上传图片节点
                     }else if(SellerConsignAgreementStatus.SecondApproved.getCode().equals(oldSellerStatus)){
                         //推动到下一个任务
                         invokeNextTaskSellerConsignAgreement(WorkFlowConstant.SELLER_AGREEMENT_UPLOAD_IMG, null, newAccountExt.getCustAccountId());
                     }
                 }
             }
		} catch (Exception e) {
			throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "启动审核流程失败");
		}
    }

    /**
     * 协议上传图片后更新协议状态 chengui
     * @param user
     * @param saveAccountExt
     * @param type
     * @return
     */
    private void updateAccountExtByUploadFiles(User user, AccountExt saveAccountExt, String type) {
        AccountExt accountExt = accountExtDao.selectByAccountId(saveAccountExt.getCustAccountId());
        if (accountExt == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "不存在该账号");
        }

        if(AttachmentType.pic_purchase_agreement.getCode().equals(type)){
            accountExt.setAnnualPurchaseAgreementStatus(AnnualPurchaseAgreementStatus.Uploaded.getCode());
        }else{
            accountExt.setSellerConsignAgreementStatus(SellerConsignAgreementStatus.Uploaded.getCode());
        }
        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy(user.getName());
        accountExt.setModificationNumber(accountExt.getModificationNumber() + 1);
        Integer flag = accountExtDao.updateByAccountIdSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改账号子表失败");
        }
    }

    /**
     * 重新计算AccountTag
     *
     * @param souceAccountTag 原accountTag
     * @param subAccountTag   需要减掉的accountTag
     * @param addAccountTag   需要添加accountTag
     * @return
     */
    @Override
    public Long reCaluteAccountTag(Long souceAccountTag, Long subAccountTag, Long addAccountTag) {
        List<AccountTag> tagList = null;
        if (souceAccountTag != null && souceAccountTag != 0) {
            tagList = AccountTag.getNameByCode(souceAccountTag);
        }
        if (tagList == null) {
            tagList = new ArrayList<>();
        }
        //删除
        if (subAccountTag != null && subAccountTag != 0) {
            AccountTag subTag = AccountTag.getSingleTagByCode(subAccountTag);
            if (subTag != null && tagList.contains(subTag)) {
                tagList.remove(subTag);
            }
        }
        //添加
        if (addAccountTag != null && addAccountTag != 0) {
            AccountTag addTag = AccountTag.getSingleTagByCode(addAccountTag);
            if (addTag != null && !tagList.contains(addTag))
                tagList.add(addTag);
        }
        //计算并返回
        if (tagList.size() == 0) {
            return 0l;
        } else {
            Long resultTag = 0l;
            for (AccountTag tag : tagList) {
                resultTag = resultTag | tag.getCode();
            }
            return resultTag;
        }
    }

    @Override
    public List<Long> queryOrgIdsByAccountId(Long accountId){
        Account account = accountDao.selectByPrimaryKey(accountId);
        if (account != null && account.getOrgId() != 0) {
            List<Long> orgIdList = new ArrayList<>();
            orgIdList.add(account.getOrgId());
            return orgIdList;
        }
        return accountDao.queryOrgIdsByAccountId(accountId);
    }
    
    /**
     * 执行下一步代运营协议审核任务
     * @param taskDefinitionKey 任务id
     * @param variables 变量
     * @param accountId 客户id
     * @return
     */
    private void invokeNextTaskSellerConsignAgreement(String taskDefinitionKey,List<ActivitiVariable> variables,Long accountId) {
        try{
            //根据任务节点id查询当前节点的所有任务
            ListTaskQuery taskQuery =  workFlowService.createEditTaskQuery(taskDefinitionKey,accountId.toString());
            List<TaskInfo> taskList = workFlowService.getAllTaskList(taskQuery);
            //查找并设置下一个任务的参数
            TaskActionInfo actionInfo =workFlowService.getNextTaskInfo(taskList, variables, accountId.toString());
            TaskActionRequest taskActionRequest = actionInfo.getRequest();
            if(taskActionRequest != null){
                //流转到下个任务
                taskService.invokeTaskAction(actionInfo.getTaskId(), taskActionRequest);
            }
        }catch (Exception e){
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, e.getMessage());
        }
    }

    /**
     * 获取客户信息（联系人任取其一）
     * @param query
     *  若要查询多种身份客户，accountTag应传递或操作结果
     * @return
     */
    @Override
    public List<AccountForAppDto> queryAccountWithSingleContactByManager(CompanyQuery query){
        return accountDao.queryAccountWithSingleContactByManager(query);
    }

    /**
     * 按部门Id查询客户信息（若Id本身为客户Id，则返回当前客户信息）
     * @param id
     * @return Account
     * @author chengui
     */
    @Override
    public Account selectParentById(Long id) {
        Account account = accountDao.queryParentByDeptId(id);
        if(null == account){
            account = accountDao.selectByPrimaryKey(id);
        }
        return account;
    }

    /**
     *  款道2.0会员体系会修改客户的公司全称和组织机构代码；
     *  支付体系会修改会员的账户余额。
     * @param account
     * @return
     */
    @Override
    public int updateAccount(Account account) {
        account.setLastUpdated(new Date());
        account.setLastUpdatedBy("款道");
        AccountExt  accountExt=new AccountExt();
        accountExt.setCustAccountId(account.getId());
        accountExt.setOrgCode(account.getOrgCode());
        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy("款道");
        return accountDao.updateByPrimaryKeySelective(account)+accountExtDao.updateByAccountIdSelective(accountExt);
    }
    //超市根据超市用户ID查询相关联的客户名称
    @Override
    public List<String> selectAccountNameByEcId(Integer ecId){
        return contactDao.selectAccountNameByEcId(ecId);
    }

	@Override
	public List<Account> listDeparmentAccount(Map<String, Object> paramMap) {
		Object name = paramMap.get("name");
        if (name != null
                && StringUtils.isNotEmpty(name.toString().replaceAll(" ", ""))) {
            paramMap.put("name", "%" + name.toString() + "%");
        }
        return accountDao.listDeparmentAccount(paramMap);
	}


}
