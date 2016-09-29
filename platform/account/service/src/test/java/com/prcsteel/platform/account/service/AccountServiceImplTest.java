package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.AccountDtoForUpdate;
import com.prcsteel.platform.account.model.dto.CompanyDto;
import com.prcsteel.platform.account.model.dto.SaveAccountDto;
import com.prcsteel.platform.account.model.enums.AccountStructureType;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.model.model.AccountExt;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.model.query.CompanyQuery;
import com.prcsteel.platform.account.persist.dao.*;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.acl.persist.dao.UserDao;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.common.service.FileService;
import com.prcsteel.platform.common.utils.StringToReplace;
import com.prcsteel.platform.core.model.model.City;
import com.prcsteel.platform.core.model.model.Province;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.DistrictDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.core.service.CommonService;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by lichaowei on 2016/2/15.
 */
public class AccountServiceImplTest extends BaseTest {

    @Resource
    AccountService accountService;

    AccountDao accountDao;

    AccountAttachmentDao aamDao;

    ProvinceDao provinceDao;

    CityDao cityDao;

    DistrictDao districtDao;

    UserDao userDao;

    //AccountContactDao accountContactDao;

    AccountBankDao accountBankDao;

    ContactDao contactDao;

    ProxyFactoryDao proxyFactoryDao;

    AccountAttachmentDao accountAttachmentDao;

    AccountExtDao accountExtDao;

    @Before
    public void before() {
        accountDao = EasyMock.createMock(AccountDao.class);
        cityDao = EasyMock.createMock(CityDao.class);
        districtDao = EasyMock.createMock(DistrictDao.class);
        provinceDao = EasyMock.createMock(ProvinceDao.class);
        contactDao = EasyMock.createMock(ContactDao.class);

        aamDao = EasyMock.createMock(AccountAttachmentDao.class);
        userDao = EasyMock.createMock(UserDao.class);
        //accountContactDao = EasyMock.createMock(AccountContactDao.class);
        accountBankDao = EasyMock.createMock(AccountBankDao.class);
        proxyFactoryDao = EasyMock.createMock(ProxyFactoryDao.class);

        accountAttachmentDao = EasyMock.createMock(AccountAttachmentDao.class);
        accountExtDao = EasyMock.createMock(AccountExtDao.class);

        ReflectionTestUtils.setField(accountService, "accountDao", accountDao);
        ReflectionTestUtils.setField(accountService, "cityDao", cityDao);
        ReflectionTestUtils.setField(accountService, "districtDao", districtDao);
        ReflectionTestUtils.setField(accountService, "provinceDao", provinceDao);
        ReflectionTestUtils.setField(accountService, "contactDao", contactDao);

        ReflectionTestUtils.setField(accountService, "aamDao", aamDao);
        ReflectionTestUtils.setField(accountService, "userDao", userDao);
        //ReflectionTestUtils.setField(accountService, "accountContactDao", accountContactDao);
        ReflectionTestUtils.setField(accountService, "accountBankDao", accountBankDao);
        ReflectionTestUtils.setField(accountService, "proxyFactoryDao", proxyFactoryDao);

        ReflectionTestUtils.setField(accountService, "accountAttachmentDao", accountAttachmentDao);
        ReflectionTestUtils.setField(accountService, "accountExtDao", accountExtDao);
    }

    @After
    public void after() {
        accountDao = null;
        cityDao = null;
        districtDao = null;
        provinceDao = null;
        contactDao = null;

        aamDao = null;
        userDao = null;
        //accountContactDao = null;
        accountBankDao = null;
        proxyFactoryDao = null;

        accountAttachmentDao = null;
        accountExtDao = null;

    }

    @Test
    public void testSaveCheckAccount() {
        SaveAccountDto saveInfo = new SaveAccountDto();
        //saveInfo.setAccount(new Account());
        //saveInfo.setContact(new Contact());

        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("账号信息为空", e.getMsg());
        }
    }

    @Test
    public void testSaveCheckAccountAccountName() {
        SaveAccountDto saveInfo = new SaveAccountDto();
        Account account = new Account();
        account.setName("长沙钢为网络科技有限公司");
        account.setId(0L);
        account.setStatus(0); //锁定
        account.setLastUpdatedBy("admin");
        saveInfo.setAccount(account);
        saveInfo.setContact(new Contact());

        Account resultAccount = new Account();
        resultAccount.setName("长沙钢为网络科技有限公司");
        resultAccount.setId(1L);
        EasyMock.expect(accountDao.selectAccountByName("长沙钢为网络科技有限公司")).andReturn(resultAccount);

        EasyMock.replay(accountDao);//执行测试
        //调用需测试的方法
        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("该公司名称已经存在", e.getMsg());
        }
        EasyMock.verify(accountDao);//验证结果
    }
//
//    @Test
//    public void testSaveCheckAccountContactTel() {
//        SaveAccountDto saveInfo = new SaveAccountDto();
//        Contact contact = new Contact();
//        contact.setTel("18670033656");
//        saveInfo.setContact(contact);
//        saveInfo.setAccount(new Account());
//
//        Contact resultContact = new Contact();
//        resultContact.setTel("18670033656");
//        resultContact.setId(1);
//        EasyMock.expect(contactDao.selectByTel(contact.getTel())).andReturn(resultContact);
//
//        EasyMock.replay(contactDao);//执行测试
//        //调用需测试的方法
//        try {
//            accountService.save(saveInfo);
//            Assert.fail("单元测试失败");
//        } catch (BusinessException e) {
//            Assert.assertEquals("该联系人手机号码已经存在", e.getMsg());
//        }
//        EasyMock.verify(contactDao);//验证结果
//    }

    @Test
    public void testSaveCheckAccountAccountCode() {
        SaveAccountDto saveInfo = new SaveAccountDto();
        AccountExt accountExt = new AccountExt();
        accountExt.setAccountCode("62222225263987");
        saveInfo.setAccountExt(accountExt);
        saveInfo.setAccount(new Account());
        saveInfo.setContact(new Contact());
        saveInfo.setUser(createUser());

        AccountBank resultAccountBank = new AccountBank();
        resultAccountBank.setBankAccountCode("62222225263987");
        resultAccountBank.setAccountId(1L);
        EasyMock.expect(accountBankDao.selectByAccountCode(accountExt.getAccountCode())).andReturn(resultAccountBank);
        EasyMock.replay(accountBankDao);//执行测试

        Account resultAccount = new Account();
        resultAccount.setName("长沙钢为网络科技有限公司");
        resultAccount.setId(1L);
        EasyMock.expect(accountDao.selectByPrimaryKey(resultAccountBank.getAccountId())).andReturn(resultAccount);
        EasyMock.replay(accountDao);//执行测试

        User resultUser = new User();
        resultUser.setLoginId("chenhu");
        resultUser.setTel("18670033666");
        EasyMock.expect(userDao.queryByLoginId("chenhu")).andReturn(resultUser);

        EasyMock.replay(userDao);//执行测试
        //调用需测试的方法
        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("尾号为【3987】银行账号已经与【长沙钢为网络科技有限公司】绑定，请联系陈虎【18670033666】处理", e.getMsg());
        }
        EasyMock.verify(accountBankDao);//验证结果
        EasyMock.verify(accountDao);
        EasyMock.verify(userDao);
    }

    @Test
    public void testSaveAddAccount() {
        SaveAccountDto saveInfo = passedSaveCheckTest();

        EasyMock.expect(accountDao.insertSelective(saveInfo.getAccount())).andReturn(0);

        EasyMock.replay(accountDao);//执行测试
        //调用需测试的方法
        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("添加Company账号失败", e.getMsg());
        }
        EasyMock.verify(accountDao);//验证结果
    }

    @Test
    public void testSaveAddAccountExt() {
        SaveAccountDto saveInfo = passedSaveCheckTest();

        passedAddAccountTest(saveInfo);

        EasyMock.expect(accountExtDao.insertSelective(saveInfo.getAccountExt())).andReturn(0);
        EasyMock.replay(accountExtDao);
        //调用需测试的方法
        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("添加客户子表失败", e.getMsg());
        }
        EasyMock.verify(accountDao, accountExtDao);
    }

    @Test
    public void testSaveAddAccountBank() {
        SaveAccountDto saveInfo = passedSaveCheckTest();

        passedAddAccountTest(saveInfo);

        EasyMock.expect(accountExtDao.insertSelective(saveInfo.getAccountExt())).andReturn(1);
        EasyMock.replay(accountExtDao);

        Province resultProvince = new Province();
        resultProvince.setName("湖南省");
        EasyMock.expect(provinceDao.selectByPrimaryKey(saveInfo.getAccountExt().getProvinceId())).andReturn(resultProvince);
        EasyMock.replay(provinceDao);

        City resultCity = new City();
        resultCity.setName("长沙市");
        EasyMock.expect(cityDao.selectByPrimaryKey(saveInfo.getAccountExt().getCityId())).andReturn(resultCity);
        EasyMock.replay(cityDao);

        String accountCode = saveInfo.getAccountExt().getAccountCode();

        AccountBank resultAccountBank=new AccountBank();
        resultAccountBank.setAccountId(1L);
        EasyMock.expect(accountBankDao.selectByAccountCode(StringToReplace.toReplaceAll(accountCode))).andReturn(resultAccountBank);

//        EasyMock.expect(accountBankDao.updateByPrimaryKeySelective(EasyMock.anyObject())).andReturn(0);
        EasyMock.replay(accountBankDao);

        //调用需测试的方法
        try {
            accountService.save(saveInfo);
            Assert.fail("单元测试失败");
        } catch (BusinessException e) {
            Assert.assertEquals("该银行账号已经存在", e.getMsg());
        }
        EasyMock.verify(accountDao, accountExtDao, accountBankDao);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setLoginId("testAdmin");
        user.setName("测试管理员");
        return user;
    }

    public Account createAccount() {
        Account account = new Account();
        account.setName("长沙钢为网络科技有限公司");

        account.setAccountTag(1L);
        account.setOrgId(1L);
        account.setOrgName("邯郸服务中心");
        account.setBusinessType("merchant");
        account.setId(1L);
        account.setStatus(0); //锁定
        account.setLastUpdatedBy("admin");
        return account;
    }

    public Contact createContact() {
        Contact contact = new Contact();
        contact.setId(1);
        contact.setTel("18670033656");
        contact.setName("张三");
        contact.setEmail("123@1.com");
        contact.setNote("并没有");
        contact.setQq("555555555");
        return contact;
    }

    public AccountExt createAccountExt() {
        AccountExt accountExt = new AccountExt();
        accountExt.setCustAccountId(1L);
        accountExt.setName("长沙钢为网络科技有限公司");
        accountExt.setZip("0731-85627369");
        accountExt.setMailAddr("");
        accountExt.setMobil("18670033667");
        accountExt.setFax("0731-85263655");
        accountExt.setLegalPersonName("法人");
        accountExt.setWebSiteUrl("http://www.prcsteel.com");
        accountExt.setProvinceId(19L);
        accountExt.setCityId(202L);
        accountExt.setDistrictId(1747L);

        // 三证
        accountExt.setLicenseCode("");
        accountExt.setRegAddress("");
        accountExt.setOrgCode("");

        // 五证合一
        accountExt.setCreditCode("");

        // 代运营资料
        accountExt.setInvoiceSpeed("fast");

        // 开票资料
        accountExt.setTaxCode("");
        accountExt.setAddr("湖南长沙约会去");
        accountExt.setTaxTel("0731-88552552");
        accountExt.setTaxBankNameMain("");
        accountExt.setTaxBankNameBranch("");
        accountExt.setBankNumber("");
        accountExt.setInvoiceType("NORMAL");

        // 打款资料
        accountExt.setBankProvinceId(19L);
        accountExt.setBankCityId(202L);
        accountExt.setBankNameMain("");
        accountExt.setBankNameBranch("");
        accountExt.setBankCode("");
        accountExt.setAccountCode("62222225263987");
        return accountExt;
    }

    private AccountBank createAccountBank(AccountExt accountExt) {
        String accountCode = accountExt.getAccountCode();
        AccountBank accountBank = new AccountBank();
        accountBank.setAccountId(accountExt.getCustAccountId());
        accountBank.setBankCode(StringToReplace.toReplaceAll(accountExt.getBankCode())); // 开户银行行号
        accountBank.setBankName(StringToReplace.toReplaceAll(accountExt.getBankNameMain())); // 开户银行主行
        accountBank.setBankNameBranch(StringToReplace.toReplaceAll(accountExt.getBankNameBranch())); // 开户银行支行
        accountBank.setBankProvinceId(accountExt.getBankProvinceId());
        accountBank.setBankCityId(accountExt.getBankCityId());

        accountBank.setBankAccountCode(StringToReplace.toReplaceAll(accountCode)); // 银行账号
        accountBank.setCreated(new Date());
        accountBank.setCreatedBy("admin");
        accountBank.setLastUpdated(new Date());
        accountBank.setLastUpdatedBy("admin");
        accountBank.setModificationNumber(0);
        return accountBank;
    }

    private SaveAccountDto passedSaveCheckTest() {
        SaveAccountDto saveInfo = new SaveAccountDto();
        Account account = createAccount();
        saveInfo.setAccount(account);
        saveInfo.setContact(createContact());
        saveInfo.setAccountExt(createAccountExt());
        saveInfo.setUser(createUser());

        EasyMock.expect(accountDao.selectAccountByName(saveInfo.getAccount().getName())).andReturn(null);
        EasyMock.expect(contactDao.selectByTel(saveInfo.getContact().getTel())).andReturn(null);
        EasyMock.expect(accountBankDao.selectByAccountCode(StringToReplace.toReplaceAll(saveInfo.getAccountExt().getAccountCode()))).andReturn(null);
        return saveInfo;
    }

    private void passedAddAccountTest(SaveAccountDto saveInfo) {
        saveInfo.getAccount().setId(1L);

        EasyMock.expect(accountDao.insertSelective(saveInfo.getAccount())).andReturn(1);

        EasyMock.expect(accountDao.insertSelective(saveInfo.getAccount())).andReturn(2);

        EasyMock.expect(accountDao.getAccountCode()).andReturn("00291999");
        EasyMock.replay(accountDao);
    }

    @Test
    public void testBatchUpateStatusByPrimaryKeys(){
        AccountDtoForUpdate dto = new AccountDtoForUpdate();
        List<Long> ids = Arrays.asList(1l,2l);
        dto.setIds(ids);
        EasyMock.expect(accountDao.batchUpateStatusByPrimaryKeys(dto)).andReturn(1);
        EasyMock.replay(accountDao);

        try{
            accountService.batchUpateStatusByPrimaryKeys(dto);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals(e.getMsg(),"批量修改状态失败");
        }

    }

    @Test
    public void testQueryCompanyByCompanyQuery(){
        List<CompanyDto> list = new ArrayList<>();
        CompanyQuery query = new CompanyQuery();
        list.add(new CompanyDto());
        EasyMock.expect(accountDao.queryCompanyByCompanyQuery(query)).andReturn(list);
        EasyMock.replay(accountDao);

        List<CompanyDto> resultList = accountService.queryCompanyByCompanyQuery(query);
        Assert.assertNotNull(resultList);
        Assert.assertEquals(list.size(),resultList.size());
    }

    @Test
    public void testQueryCompanyTotalByCompanyQuery(){
        CompanyQuery query = new CompanyQuery();
        int total = 1;
        EasyMock.expect(accountDao.queryCompanyTotalByCompanyQuery(query)).andReturn(total);
        EasyMock.replay(accountDao);
        Assert.assertEquals(1, total);
    }
}

