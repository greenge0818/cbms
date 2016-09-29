package com.prcsteel.platform.account.service.impl;


import com.prcsteel.platform.account.model.dto.AccountDto;
import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.ContactWithPotentialCustomerDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.enums.AccountStructureType;
import com.prcsteel.platform.account.model.enums.AccountTag;
import com.prcsteel.platform.account.model.enums.AttachmentType;
import com.prcsteel.platform.account.model.model.*;
import com.prcsteel.platform.account.model.query.ContactQuery;
import com.prcsteel.platform.account.model.query.ContactWithPotentialCustomerQuery;
import com.prcsteel.platform.account.persist.dao.*;
import com.prcsteel.platform.account.service.AccountContactService;
import com.prcsteel.platform.account.service.ContactService;
import com.prcsteel.platform.account.service.GlobalIdService;
import com.prcsteel.platform.acl.model.dto.UserOrgDto;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.utils.CbmsNumberUtil;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.DistrictDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by dengxiyan on 2016/1/15.
 */
@Service("contactService")
public class ContactServiceImpl implements ContactService {

    @Resource
    private AccountDao accountDao;
    @Resource
    private ProvinceDao provinceDao;
    @Resource
    private CityDao cityDao;
    @Resource
    private DistrictDao districtDao;
    @Resource
    private ContactDao contactDao;
    @Resource
    private AccountContactDao accountContactDao;

    @Resource
    GlobalIdService globalIdService;

    @Resource
    PoolOutDao poolOutDao;

    @Resource
    UserDao userDao;
    @Resource
    AccountAttachmentDao aamDao;
    @Resource
    AccountExtDao accountExtDao;

    @Resource
    private AccountContactService accountContactService;


    @Override
    public AccountDto getCompanyById(Long id) {
        Account account = accountDao.selectByPrimaryKey(id);
        if (account == null) {
            return null;
        }
        //客户性质名称转换
        account.setTagName(getAccountTagName(account.getAccountTag()));
        AccountDto dto = new AccountDto();
        dto.setCustomerProperty(getCustomerProperty(account.getAccountTag()));
        dto.setAccount(account);
        if (account.getBusinessType() != null) {
            dto.setBusinessType(AccountBusinessType.valueOf(account
                    .getBusinessType()));
        }
        if (account.getProvinceId() != null) {
            dto.setProvince(provinceDao.selectByPrimaryKey(account.getProvinceId()));
        }
        if (account.getCityId() != null) {
            dto.setCity(cityDao.selectByPrimaryKey(account.getCityId()));
        }
        if (account.getDistrictId() != null) {
            dto.setDistrict(districtDao.selectByPrimaryKey(account.getDistrictId()));
        }
        if (account.getBankCityId() != null) {
            dto.setBankCity(cityDao.selectByPrimaryKey(account.getBankCityId()));
        }
        List<AccountAttachment> attachmentList = aamDao.listByAccountId(account.getId());
        // 买家年度采购协议，卖家代运营协议可以一对多，分开处理
        List<AccountAttachment> purchaseAgreements = new ArrayList<AccountAttachment>();
        List<AccountAttachment> consignContracts = new ArrayList<AccountAttachment>();
        Map<String, Object> attachments = new HashMap<String, Object>();
        for (AccountAttachment aa : attachmentList) {
            if (aa.getType().equals(
                    AttachmentType.pic_purchase_agreement.getCode())) {
                purchaseAgreements.add(aa);
            } else if (aa.getType().equals(
                    AttachmentType.pic_consign_contract.getCode())) {
                consignContracts.add(aa);
            } else {
                attachments.put(aa.getType(), aa);
            }
        }
        attachments.put(AttachmentType.pic_purchase_agreement.getCode(), purchaseAgreements);
        attachments.put(AttachmentType.pic_consign_contract.getCode(), consignContracts);
        dto.setAttachments(attachments);

        AccountExt accountExt = accountExtDao.selectByAccountId(id);
        dto.setAccountExt(accountExt);

        //取得公司部门信息  add by peanut on 2016-03-10 15:10
        dto.setDepartment(accountDao.queryDeptByCompanyId(id));

        return dto;
    }

    @Override
    public void saveDept(Account dept, User user) {

        checkDuplicateDept(dept);

        //检查当部门分配额度总和大于公司总信用额度时 去掉检查
      /*  if (dept.getCreditAmount() != null && !isEnoughCreditAmount(dept)) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "分配额度不能大于公司总信用额度");
        }*/

        dept.setLastUpdated(new Date());
        dept.setLastUpdatedBy(user.getName());
        if (dept.getId() != null && dept.getId() > 0) {
            Account dbDept = accountDao.selectByPrimaryKey(dept.getId());
            dept.setModificationNumber(dbDept.getModificationNumber() + 1);
            //||updateAccountExt(dept, user)==0部门不需要保存到扩展表
            if (accountDao.updateByPrimaryKeySelective(dept) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门保存失败");
            }
        } else {
            //dept.setId(globalIdService.getId(GlobalIdModule.Account));
            dept.setCreated(new Date());
            dept.setCreatedBy(user.getName());
            dept.setModificationNumber(0);
            dept.setStructureType(AccountStructureType.Department.toString());
            dept.setOrgId(0l);
            dept.setOrgName("");
            //||addAccountExt(dept, user)==0 新增部门不需要保存到扩展表
            if (accountDao.insertSelective(dept) == 0) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门保存失败");
            }
        }

    }

    /**
     * 判断金额是否足够
     */
    private boolean isEnoughCreditAmount(Account dept) {
        Account company = accountDao.selectByPrimaryKey(dept.getParentId());
        //查询所有部门总额(排除当前部门)
        BigDecimal total = CbmsNumberUtil.buildMoney(accountDao.queryCreditAmountTotalByParentId(dept.getParentId(), dept.getId()));
        BigDecimal allDeptCreditAmount = total.add(CbmsNumberUtil.buildMoney(dept.getCreditAmount()));
        return allDeptCreditAmount.compareTo(CbmsNumberUtil.buildMoney(company.getCreditAmount())) != 1;
    }

    @Override
    public void deleteDept(Long deptId, User user) {

        /*
          1、部门下如果有联系人，无法删除部门，并提示 “部门联系人不为空，无法删除部门”；
          2、部门如果使用了信用额度，
             或 部门如果有二结未结款；
             或 部门如果有账户余额，均无法操作删除部门，提示 “该部门有账务关系未处理完毕，无法删除 ”；
          3、部门如果有未处理完的销项票，则提示 “该部门有销项票未处理完毕，无法删除成功”；
        */

        if (hasContactOfDepart(deptId)) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门联系人不为空，无法删除部门");
        }

        //TODO 信用额度的使用的字段待定
        Account account = accountDao.selectByPrimaryKey(deptId);
        if (isUsedCreditAmount(account) || hasSecondSettlement(account) || hasBalance(account)) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该部门有账务关系未处理完毕，无法删除");
        }

        //TODO 和后面销项票开票规则保持一致
        if (!isFinishInvoiceOut(deptId)) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该部门有销项票未处理完毕，无法删除成功");
        }

        if (accountDao.updateIsDeletedById(deptId, user.getName()) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门删除失败");
        }
    }

    @Override
    public Account getAccountById(Long id) {
        return accountDao.selectByPrimaryKey(id);
    }

    @Override
    public List<DepartmentDto> getDeptsAndContactsByCompanyId(Long companyId, List<Long> userIds) {
        List<DepartmentDto> depts = accountDao.queryDeptByCompanyId(companyId);
        if (depts != null && depts.size() > 0) {
            //查询所有联系人
            List<ContactDto> contactDtos = contactDao.queryContactsByCompanyId(companyId);
            if (contactDtos != null && contactDtos.size() > 0) {
                //控制字段显示:如果当前登录人不包含在交易员中，则不显示联系人的信息和不允许操作联系人
                contactDtos.forEach(a -> {
                    if (!isContain(a.getManagerIds(), userIds)) {
                        //联系信息不为空，展示为加密*号
                        if(StringUtils.isNotEmpty(a.getTel())){
                            a.setTel("***");
                        }
                        if(StringUtils.isNotEmpty(a.getQq())){
                            a.setQq("******");
                        }
                        if(StringUtils.isNotEmpty(a.getEmail())){
                            a.setEmail("******");
                        }
                        a.setHiddenBtn(true);
                    }
                });

                depts.forEach(a -> {
                    //过滤出来部门的所有联系人
                    List<ContactDto> contacts = contactDtos.stream().filter(b -> a.getId().equals(b.getDeptId())).collect(Collectors.toList());
                    if (contacts != null) {
                        a.setContacts(contacts);
                    }
                });
            }

        }
        return depts;
    }

    private void checkDuplicateDept(Account dept) {
        ContactQuery query = new ContactQuery();
        query.setDeptId(dept.getId());
        query.setDeptName(dept.getName());
        query.setParentId(dept.getParentId());
        int count = accountDao.queryDeptCountByNameAndParentId(query);
        if (count > 0) throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "部门已存在");
    }

    @Override
    public ContactDto getContactById(Integer contactId) {
        ContactDto dto = new ContactDto();
        Contact contact = contactDao.selectByPrimaryKey(contactId);
        try {
            PropertyUtils.copyProperties(dto, contact);
        } catch (Exception e) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "用户参数问题");
        }
        return dto;
    }

    private String getAccountTagName(Long code) {
        if (code != null && code > 0) {
            List<AccountTag> tags = AccountTag.getNameByCode(code);
            return tags.stream().map(a -> a.getName()).collect(Collectors.joining(","));
        }
        return "";
    }

    private String getCustomerProperty(Long code) {
        if (code != null && code > 0) {
            List<AccountTag> tags = AccountTag.getNameByCode(code);
            tags.remove(AccountTag.temp);
            tags.remove(AccountTag.consign);
            return tags.stream().map(a -> a.getName()).collect(Collectors.joining(","));
        }
        return "";
    }

    @Override
    @Transactional
    public void saveContact(SaveContactDto dto) {
        if (dto.getContact() == null) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "联系人参数问题");
        }
        if (dto.isInEditMode()) {
            modifyContact(dto);
        } else {
            addContact(dto);
            //CBMS新增客户联系人时，通知微信端 add by wangxianjun 20150526
            Contact contact = contactDao.selectByPrimaryKey(dto.getContact().getId());
            accountContactService.noticeWechat(contact.getTel().trim(), dto.getUser());
        }
    }

    /**
     * 添加新联系人或添加已有联系人
     *
     * @param dto
     */
    private void addContact(SaveContactDto dto) {
        Contact contact = dto.getContact();

        //选择已有联系人
        if (contact.getId() != null && contact.getId() > 0) {
            //添加批量部门-联系人-交易员关系
            bindAccountContact(dto);
        } else { //添加新联系人

            //检查是否存在联系人 电话号码
            Contact dbContact = contactDao.queryByTel(contact.getTel());
            if (dbContact != null) {
                if (!dbContact.getName().equals(contact.getName())) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "该手机号已经存在，请输入该手机号正确的联系人姓名！");
                }
                dto.setContact(dbContact);
            } else {
                contact.setCreatedBy(dto.getUser().getName());
                contact.setLastUpdatedBy(dto.getUser().getName());
                contact.setModificationNumber(0);
                if (contactDao.insertSelective(contact) == 0) {
                    throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "新增用户失败");
                }
            }
            //添加批量部门-联系人-交易员关系
            bindAccountContact(dto);
        }
    }

    private void modifyContact(SaveContactDto dto) {
        Contact contact = dto.getContact();
        //检查是否存在联系人 电话号码
        Contact dbContact = contactDao.queryByTel(contact.getTel());
        if (dbContact != null && !dbContact.getId().equals(contact.getId())) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "用户的号码不能重复");
        }

        Contact bunContact = contactDao.selectByPrimaryKey(contact.getId());
        contact.setModificationNumber(bunContact.getModificationNumber() + 1);
        contact.setLastUpdatedBy(dto.getUser().getName());
        if (contactDao.updateByPrimaryKeySelective(contact) > 0) {
            //解绑
            unbindAccountContact(new AccountContact(dto.getDeptId(), contact.getId(), dto.getUser().getName()));
            //添加关系
            bindAccountContact(dto);
        } else {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "编辑用户失败");
        }
    }

    /**
     * 绑定客户-联系人-交易员的关系
     *
     * @param dto
     */
    private void bindAccountContact(SaveContactDto dto) {
        List<Long> managerIdList = dto.getManagerIdList();
        if (managerIdList == null || managerIdList.size() == 0) {
            throw new BusinessException(Constant.ERROR_MISSING_REQUIRED_PARAM, "交易员参数为空问题");
        }
        List<AccountContact> list = new ArrayList<>();
        managerIdList.forEach(a -> {
            // 关系是否存在
            AccountContact accountContact = accountContactService.queryByIds(dto.getDeptId(), dto.getContact().getId(), a);
            if (accountContact != null) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "联系人" + dto.getContact().getName() + "信息重复！");
            }

            list.add(createAccountContact(dto, a));
        });

        // 批量保存
        if (!list.isEmpty()) {
            if (accountContactDao.batchInsert(list) != list.size()) {
                throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "客户联系人交易员关系添加失败");
            }
        }
    }

    private AccountContact createAccountContact(SaveContactDto dto, Long managerId) {
        AccountContact accountContact = new AccountContact();
        // TODO globalIdService.getId(GlobalIdModule.AccountContact) 取不到值 getId报空指针异常
        //accountContact.setId(globalIdService.getId(GlobalIdModule.AccountContact));
        accountContact.setAccountId(dto.getDeptId());
        accountContact.setContactId(dto.getContact().getId());
        accountContact.setStatus(Integer.valueOf(Constant.YES));
        accountContact.setManager(managerId);
        accountContact.setModificationNumber(0);
        accountContact.setCreatedBy(dto.getUser().getName());
        accountContact.setLastUpdatedBy(dto.getUser().getName());
        return accountContact;
    }


    /**
     * 解绑客户-联系人-交易员的关系
     *
     * @param accountContact
     */
    private void unbindAccountContact(AccountContact accountContact) {
        if (accountContactDao.updateIsDeletedByAccountIdAndContactId(accountContact) == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_BUSINESS, "删除联系人交易员关系失败");
        }
    }


    /**
     * 查询服务中心的所有交易员
     *
     * @param orgId
     * @return
     */
    @Override
    public List<User> queryUserByOrgId(Long orgId) {
        return contactDao.queryUsersByOrgId(orgId);
    }

    /**
     * 查询部门中特定联系人所属的交易员
     *
     * @return
     */
    @Override
    public List<User> queryBindUserByDeptIdAndContactId(AccountContact accountContact) {
        return accountContactDao.queryBindUserByDeptIdAndContactId(accountContact);
    }

    /**
     * 获得其他兄弟部门的所有联系人（该公司的所有联系人排除本部门已添加的联系人）
     * 用于添加已有联系人
     *
     * @param companyId
     * @param departId
     * @return
     */
    public List<ContactDto> getContactsOfSiblingsDepart(Long companyId, Long departId, List<Long> userIds) {
        //查询公司下所有部门的所有联系人
        List<ContactDto> contactsOfCompany = contactDao.queryContactsByCompanyId(companyId);
        if (contactsOfCompany != null && contactsOfCompany.size() > 0) {

            //一个联系人可能在多个部门，去重
            contactsOfCompany = contactsOfCompany.stream().filter(distinctByKey(p -> p.getId())).collect(Collectors.toList());
            List<Contact> contactsOfDepart = contactDao.queryContactsByDepartId(departId);
            contactsOfCompany.removeIf(a -> contactsOfDepart.stream().anyMatch(b -> a.getId().equals(b.getId())));

            // 排除与该用户无权限的数据，交易员只能查看自己有关系的联系人
            if (userIds != null) {
                contactsOfCompany.removeIf(a -> userIds.stream().anyMatch(b -> !a.getManagerIds().contains(b.toString())));
            }

            return contactsOfCompany;
        }
        return new ArrayList<>();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 判断部门下是否有联系人
     *
     * @param departId
     * @return true 有 false 没有
     */
    private boolean hasContactOfDepart(Long departId) {
        List<Contact> list = contactDao.queryContactsByDepartId(departId);
        return list != null && list.size() > 0;
    }

    /**
     * 判断部门是否使用了信用额度
     *
     * @param account
     * @return true 是 false 否
     */
    private boolean isUsedCreditAmount(Account account) {
        //TODO 判断部门是否使用了信用额度  客户信息表后面会增加相关字段吧：已使用信用额度
        //TODO 账户信息
        return false;
    }

    /**
     * 判断部门是否有二结未结款
     *
     * @param account
     * @return true 是 false 否
     */
    private boolean hasSecondSettlement(Account account) {
        return account.getBalanceSecondSettlement().doubleValue() != 0
                || account.getBalanceSecondSettlementFreeze().doubleValue() != 0;
    }

    /**
     * 判断部门是否有账户余额
     *
     * @param account
     * @return true 是 false 否
     */
    private boolean hasBalance(Account account) {
        return account.getBalance().doubleValue() != 0
                || account.getBalanceFreeze().doubleValue() != 0;
    }

    /**
     * 判断部门是否有未处理完的销项票（销项票池中未开重量和未开金额是否都开完）
     * 按部门开票
     *
     * @return true 已完成开票 false 未完成开票
     */
    private boolean isFinishInvoiceOut(Long deptId) {
//        //TODO 先判断原客户id字段当成部门id
//        //查询未开票记录
//        List<PoolOut> poolOuts = poolOutDao.queryInvoicingByDeptId(deptId);
//        return  poolOuts == null || poolOuts.size() == 0;
        return true;
    }

    @Override
    public Contact queryByTel(String tel) {
        return contactDao.queryByTel(tel);
    }

    /**
     * 查找业务服务中心及服务中心的所有交易员
     *
     * @param orgId 指定的业务服务中心id
     * @return
     */
    @Override
    public List<UserOrgDto> queryTradersBusinessOrgByOrgIdSelective(Long orgId) {
        return userDao.queryTradersBusinessOrgByOrgIdSelective(orgId);
    }

    private boolean isContain(String managerIds, String userId) {
        if (StringUtils.isEmpty(managerIds)) {
            return false;
        }
        String[] array = managerIds.split(",");
        return Arrays.asList(array).contains(userId);
    }

    private boolean isContain(String managerIds, List<Long> userIds) {

        //管理员看全部
        if (userIds == null) {
            return true;
        }

        if (StringUtils.isEmpty(managerIds)) {
            return false;
        }

        //包含返回true
        String[] array = managerIds.split(",");
        for (String s : array) {
            if (userIds.contains(Long.parseLong(s))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 根据部门ID查询联系人
     *
     * @param departId 部门ID
     * @return 联系人集合
     */
    @Override
    public List<Contact> queryContactsByDepartId(Long departId) {
        return contactDao.queryContactsByDepartId(departId);
    }

    /**
     * 查询联系人
     *
     * @param query 查询dto
     * @return 联系人集合
     */
    @Override
    public List<ContactDto> queryContactsByContactDto(ContactDto query) {
        return contactDao.queryContactsByContactDto(query);

    }

    public synchronized String getCode() {
        return accountDao.getAccountCode();
    }

    /**
     * 添加客户扩展表
     * @param account
     * @param user
     * @return
     */
    private int addAccountExt(Account account, User user) {
        AccountExt accountExt=new AccountExt();
        Date now = new Date();
        accountExt.setCustAccountId(account.getId());
        accountExt.setName(account.getName());
        accountExt.setFax(account.getFax());
        accountExt.setRegTime(now);
        accountExt.setCode(getCode());

        accountExt.setCreated(now);
        accountExt.setCreatedBy(user.getName());
        accountExt.setLastUpdated(now);
        accountExt.setLastUpdatedBy(user.getName());
        accountExt.setModificationNumber(0);
        Integer flag = accountExtDao.insertSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "添加客户子表失败");
        }
        return flag;

    }

    /**
     *  修改客户扩展表
     * @param account
     * @param user
     * @return
     */
    private int updateAccountExt(Account account,User user ) {
        AccountExt accountExt=new AccountExt();
        if (account.getId() == null) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "不存在该账号");
        }
        AccountExt  acountExtOne=accountExtDao.selectByAccountId(account.getId());
        if (acountExtOne == null) {
            addAccountExt(account,user);
        }
        accountExt.setCustAccountId(account.getId());
        accountExt.setName(account.getName());
        accountExt.setFax(account.getFax());
        accountExt.setModificationNumber(acountExtOne==null?0:acountExtOne.getModificationNumber()+1);
        accountExt.setLastUpdated(new Date());
        accountExt.setLastUpdatedBy(user.getName());
        Integer flag = accountExtDao.updateByAccountIdSelective(accountExt);
        if (flag == 0) {
            throw new BusinessException(Constant.EXCEPTIONCODE_UNKONWN, "修改账号子表失败");
        }
        return  flag;
    }

    @Override
    public int updateTelByEcUserId(Contact accountContact){
        return contactDao.updateTelByEcUserId(accountContact);
    }

    @Override
    public int updateEcUserIdByTel(Contact accountContact){
        return contactDao.updateEcUserIdByTel(accountContact);
    }

    @Override
    public Contact queryByEcUserId(Integer ecUserId){
        return contactDao.queryByEcUserId(ecUserId);
    }

    @Override
    public int saveContact(Contact accountContact){
        return contactDao.insertSelective(accountContact);
    }
	@Override
	public void updateById(Contact contact) {
		contactDao.updateByPrimaryKeySelective(contact);
	}
	
	@Override
	public List<ContactWithPotentialCustomerDto> queryContactWithPotentialCustomer(
			ContactWithPotentialCustomerQuery query) {
		return contactDao.queryContactWithPotentialCustomer(query);
	}

	@Override
	public int totalContactWithPotentialCustomer(ContactWithPotentialCustomerQuery query) {
		return contactDao.totalContactWithPotentialCustomer(query);
	}

}
