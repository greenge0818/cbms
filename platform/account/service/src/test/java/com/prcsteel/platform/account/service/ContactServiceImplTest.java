package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.dto.ContactDto;
import com.prcsteel.platform.account.model.dto.DepartmentDto;
import com.prcsteel.platform.account.model.dto.SaveContactDto;
import com.prcsteel.platform.account.model.model.Account;
import com.prcsteel.platform.account.model.model.AccountContact;
import com.prcsteel.platform.account.model.model.Contact;
import com.prcsteel.platform.account.persist.dao.AccountContactDao;
import com.prcsteel.platform.account.persist.dao.AccountDao;
import com.prcsteel.platform.account.persist.dao.ContactDao;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;
import com.prcsteel.platform.core.persist.dao.CityDao;
import com.prcsteel.platform.core.persist.dao.DistrictDao;
import com.prcsteel.platform.core.persist.dao.ProvinceDao;
import com.prcsteel.platform.order.model.model.PoolOut;
import com.prcsteel.platform.order.persist.dao.PoolOutDao;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by dengxiyan on 2016/1/19.
 */
public class ContactServiceImplTest extends BaseTest {
    @Resource
    private ContactService contactService;

    private AccountDao accountDao;

    private ProvinceDao provinceDao;

    private CityDao cityDao;

    private DistrictDao districtDao;

    private ContactDao contactDao;

    private AccountContactDao accountContactDao;

    private PoolOutDao poolOutDao;

    @Before
    public void before() {
        accountDao = EasyMock.createMock(AccountDao.class);
        cityDao = EasyMock.createMock(CityDao.class);
        districtDao = EasyMock.createMock(DistrictDao.class);
        provinceDao = EasyMock.createMock(ProvinceDao.class);
        contactDao = EasyMock.createMock(ContactDao.class);
        accountContactDao = EasyMock.createMock(AccountContactDao.class);
        poolOutDao = EasyMock.createMock(PoolOutDao.class);

        ReflectionTestUtils.setField(contactService, "contactDao", contactDao);
        ReflectionTestUtils.setField(contactService, "accountContactDao", accountContactDao);
        ReflectionTestUtils.setField(contactService, "accountDao", accountDao);
        ReflectionTestUtils.setField(contactService, "cityDao", cityDao);
        ReflectionTestUtils.setField(contactService, "districtDao", districtDao);
        ReflectionTestUtils.setField(contactService, "provinceDao", provinceDao);
        ReflectionTestUtils.setField(contactService, "poolOutDao", poolOutDao);
    }

    @After
    public void after() {
        accountDao = null;
        cityDao = null;
        districtDao = null;
        provinceDao = null;
        contactDao = null;
        accountContactDao = null;

    }


//    @Test
//    public void testUpdateContactStatusThrowException() {
//        Contact contact = new Contact();
//        contact.setId(0);
//        //contact.setStatus(0); //锁定
//        contact.setLastUpdatedBy("admin");
//        EasyMock.expect(contactDao.updateStatusById(contact)).andReturn(0);
//        EasyMock.replay(contactDao);//执行测试
//        //调用需测试的方法
//        try {
//            contactService.updateContactStatus(contact);
//            Assert.fail("单元测试失败！");
//        } catch (BusinessException e) {
//            Assert.assertEquals("锁定/解锁用户失败", e.getMsg());
//        }
//        EasyMock.verify(contactDao);//验证结果
//    }

    @Test
    public void testSaveDept() {
        Account dept = createDept();
        dept.setId(1l);//1l
        User user = createUser();

        Account dbDept = new Account();
        dbDept.setId(1l);//null 1l
        dbDept.setModificationNumber(0);

        int deptCount = 0;//修改值 测试
        mockCheckDuplicateDept(deptCount);//不满足报异常

        if (deptCount == 0){
            BigDecimal companyCreditAmount = new BigDecimal("100");//设置公司额度
            Account company = new Account();
            company.setId(dept.getParentId());
            company.setCreditAmount(companyCreditAmount);

            //100 200当前所有部门额度和 = dbTotalCreditAmount + dept.getCreditAmount()
            BigDecimal dbTotalCreditAmount = new BigDecimal("200");// 数据库公司下的所有部门的额度和
            BigDecimal allDeptTotalCreditAmount = dbTotalCreditAmount.add(dept.getCreditAmount());
            mockIsEnoughCreditAmount(dept,company, dbTotalCreditAmount);//不满足 报异常了就不继续执行mock了

            if(dept.getCreditAmount() != null && allDeptTotalCreditAmount.compareTo(companyCreditAmount) != 1){
                if (dept.getId() != null && dept.getId() > 0){
                    EasyMock.expect(accountDao.selectByPrimaryKey(dept.getId())).andReturn(dbDept);
                    EasyMock.expect(accountDao.updateByPrimaryKeySelective(dept)).andReturn(0);
                }else {
                    EasyMock.expect(accountDao.insertSelective(dept)).andReturn(0);
                }
            }
        }
        EasyMock.replay(accountDao);
        try {
            //测试如果抛异常则 测试通过
            contactService.saveDept(dept, user);
            Assert.fail("单元测试失败！");
        } catch (BusinessException e) {
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS, e.getCode());
        }
        EasyMock.verify(accountDao);
    }

    private void mockCheckDuplicateDept(int returnVal){
        EasyMock.expect(accountDao.queryDeptCountByNameAndParentId(EasyMock.anyObject())).andReturn(returnVal);
    }

    private void mockIsEnoughCreditAmount(Account dept,Account company,BigDecimal totalCreditAmount){
        EasyMock.expect(accountDao.selectByPrimaryKey(dept.getParentId())).andReturn(company);
        EasyMock.expect(accountDao.queryCreditAmountTotalByParentId(dept.getParentId(), dept.getId())).andReturn(totalCreditAmount);
    }

    private Account createDept(){
        Account dept = new Account();
        dept.setName("测试部门");
        dept.setCreditAmount(BigDecimal.ZERO);
        dept.setParentId(1l);
       // dept.setId(1l);
        return dept;
    }

    private  User createUser(){
        User user = new User();
        user.setId(1l);
        user.setName("admin");
        return user;
    }

    /**
     * 测试新添加联系人
     */
    @Test
    public void testAddContact(){
        SaveContactDto dto =  createSaveContactDtoForAddContact();

        Contact contact = new Contact();
        Integer id = 1;
        contact.setId(id);
        contact = null;
        EasyMock.expect(contactDao.queryByTel(dto.getContact().getTel())).andReturn(contact);

        if (contact == null || (contact != null && contact.getId().equals(dto.getContact().getId()))){
            int insertReturanVal = 1;//0、1
            EasyMock.expect(contactDao.insertSelective(dto.getContact())).andReturn(insertReturanVal);
            if (insertReturanVal > 0){
                int batchInsertSize = dto.getManagerIdList().size() - 1; //dto.getManagerIdList().size()
                EasyMock.expect(accountContactDao.batchInsert(EasyMock.anyObject())).andReturn(batchInsertSize);
            }
        }

        EasyMock.replay(contactDao);
        EasyMock.replay(accountContactDao);

        try{
            contactService.saveContact(dto);
            Assert.fail("单元测试失败，接口方法未抛异常");
        }catch (BusinessException b1){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS, b1.getCode());
        }

        EasyMock.verify(contactDao);
        EasyMock.verify(accountContactDao);
    }

    /**
     * 测试选择已有联系人：添加联系人
     */
    @Test
    public void testChooseContact(){
        SaveContactDto dto = createSaveContactDtoForChooseContact();
        dto.setManagerIdList(null);//测试交易员参数为空
        if (dto.getManagerIdList() != null && dto.getManagerIdList().size() > 0){
            int batchInsertSize = dto.getManagerIdList().size() - 1; //dto.getManagerIdList().size()
            EasyMock.expect(accountContactDao.batchInsert(EasyMock.anyObject())).andReturn(batchInsertSize);
        }

        EasyMock.replay(accountContactDao);

        try{
            contactService.saveContact(dto);
            Assert.fail("单元测试失败，接口方法未抛异常");
        }catch (BusinessException b1){
            Assert.assertNotNull(b1);
        }

        EasyMock.verify(accountContactDao);
    }

    /**
     * 测试修改联系人
     */
    @Test
    public void testModifyContact(){
        SaveContactDto dto = createSaveContactDtoForModifyContact();
        Contact contact = new Contact();
        contact.setId(dto.getContact().getId());
        contact.setTel("13267777811");

        Contact dbContact = new Contact();
        dbContact.setId(dto.getContact().getId());
        dbContact.setTel("13267777811");
        dbContact.setModificationNumber(0);

        EasyMock.expect(contactDao.queryByTel(dto.getContact().getTel())).andReturn(contact);
        EasyMock.expect(contactDao.selectByPrimaryKey(dto.getContact().getId())).andReturn(dbContact);

        int updateReturanVal = 1;//0、1
        EasyMock.expect(contactDao.updateByPrimaryKeySelective(dto.getContact())).andReturn(updateReturanVal);
        if (updateReturanVal > 0){
            int count = 0;
            EasyMock.expect(accountContactDao.updateIsDeletedByAccountIdAndContactId(EasyMock.anyObject())).andReturn(count);
            if (count > 0){
                int batchInsertSize = dto.getManagerIdList().size();
                EasyMock.expect(accountContactDao.batchInsert(EasyMock.anyObject())).andReturn(batchInsertSize);
            }
        }

        EasyMock.replay(contactDao);
        EasyMock.replay(accountContactDao);

        try{
            contactService.saveContact(dto);
            Assert.fail("单元测试失败，接口方法未抛异常");
        }catch (BusinessException b1){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b1.getCode());
        }

        EasyMock.verify(contactDao);
        EasyMock.verify(accountContactDao);
    }


    private SaveContactDto createSaveContactDtoForAddContact(){
        SaveContactDto dto = new SaveContactDto();
        dto.setUser(createUser());
        dto.setDeptId(1l);
        dto.setIsInEditMode(false);

        List<Long> managerIdList = Arrays.asList(1l,2l,3l);
        dto.setManagerIdList(managerIdList);

        Contact contact = new Contact();
        contact.setName("测试添加");
        contact.setTel("13267777811");
        //contact.setStatus(1);

        dto.setContact(contact);
        return dto;
    }

    private SaveContactDto createSaveContactDtoForChooseContact(){
        SaveContactDto dto = new SaveContactDto();
        dto.setUser(createUser());
        dto.setDeptId(1l);
        dto.setIsInEditMode(false);

        List<Long> managerIdList = Arrays.asList(1l,2l,3l);
        dto.setManagerIdList(managerIdList);

        Contact contact = new Contact();
        contact.setId(1);
        contact.setName("测试添加");
        contact.setTel("13267777811");
        //contact.setStatus(1);

        dto.setContact(contact);
        return dto;
    }

    private SaveContactDto createSaveContactDtoForModifyContact(){
        SaveContactDto dto = new SaveContactDto();
        dto.setUser(createUser());
        dto.setDeptId(1l);
        dto.setIsInEditMode(true);

        List<Long> managerIdList = Arrays.asList(1l,2l,3l);
        dto.setManagerIdList(managerIdList);

        Contact contact = new Contact();
        contact.setId(1);
        contact.setName("测试添加");
        contact.setTel("13267777811");
        //contact.setStatus(1);

        dto.setContact(contact);
        return dto;
    }

    @Test
    public void testGetDeptsAndContactsByCompanyId()throws Exception{
        List<DepartmentDto> depts = new ArrayList<>();
        DepartmentDto departmentDto = new DepartmentDto();
        Long companyId = 2l;
        Long departId = 1l;
        departmentDto.setId(departId);
        departmentDto.setName("默认部门");

        DepartmentDto departmentDto2 = new DepartmentDto();
        Long depart2Id = 2l;
        departmentDto2.setId(depart2Id);
        departmentDto2.setName("测试部门");
        depts.add(departmentDto);
        depts.add(departmentDto2);
        EasyMock.expect(accountDao.queryDeptByCompanyId(companyId)).andReturn(depts);

        List<ContactDto> contactDtos = new ArrayList<>();
        ContactDto contactDto = new ContactDto();
        contactDto.setDeptId(departId);
        contactDto.setTel("1326666666");
        contactDto.setQq("123456789");
        contactDto.setEmail("58331111@qq.com");
        contactDto.setManagerIds("1");
        contactDto.setManagerName("张三 李四");
        contactDtos.add(contactDto);

        ContactDto contactDto2 = new ContactDto();
        contactDto2.setDeptId(depart2Id);
        contactDto2.setTel("12345678910");
        contactDto2.setQq("123456");
        contactDto2.setEmail("123456@qq.com");
        contactDto2.setManagerName("当前登录人 王五");
        contactDto2.setManagerIds("2");
        contactDtos.add(contactDto2);

        EasyMock.expect(contactDao.queryContactsByCompanyId(companyId)).andReturn(contactDtos);

        EasyMock.replay(accountDao, contactDao);

        List<Long> users = new ArrayList<>();
        users.add(4l);
        users.add(5l);

        List<DepartmentDto> actual = contactService.getDeptsAndContactsByCompanyId(companyId, users);

        EasyMock.verify(accountDao, contactDao);

        Assert.assertNotNull(actual);
        Assert.assertEquals(depts.size(), actual.size());

        //验证部门下的联系人信息
        Optional<DepartmentDto> op =  actual.stream().filter(b->b.getId().equals(depart2Id)).findFirst();
        Assert.assertTrue(op.isPresent());
        DepartmentDto department = op.get();
        Assert.assertNotNull(department);
        List<ContactDto> contacts = department.getContacts();
        Assert.assertNotNull(contacts);
        Assert.assertEquals(1, contacts.size());
        ContactDto actualContactDto =  contacts.get(0);
        Assert.assertNotNull(actualContactDto);
        Assert.assertEquals("***", actualContactDto.getTel());
        Assert.assertEquals("******",actualContactDto.getQq());
        Assert.assertEquals("******",actualContactDto.getEmail());
    }

    @Test
    public void testQueryUsersByOrgId(){
        List<User> users = new ArrayList<>();
        User user = new User();
        users.add(user);

        EasyMock.expect(contactDao.queryUsersByOrgId(1l)).andReturn(users);
        EasyMock.replay(contactDao);


        List<User> actualUsers = contactService.queryUserByOrgId(1l);
        EasyMock.verify(contactDao);

        Assert.assertNotNull(actualUsers);
        Assert.assertEquals(users.size(), actualUsers.size());
    }

    @Test
    public void testQueryBindUserIdsByDeptIdAndContactId(){
        List<User> users = new ArrayList<>();
        users.add(new User());
        AccountContact accountContact = new AccountContact();
        EasyMock.expect(accountContactDao.queryBindUserByDeptIdAndContactId(accountContact)).andReturn(users);
        EasyMock.replay(accountContactDao);

        List<User> actual = contactService.queryBindUserByDeptIdAndContactId(accountContact);
        EasyMock.verify(accountContactDao);
        Assert.assertNotNull(actual);
        Assert.assertEquals(users.size(),actual.size());
    }

    @Test
    public void testGetContactsOfSiblingsDepart(){
        Integer existContactId = 4;
        List<ContactDto> contactsOfCompany = initContactsOfCompany(existContactId);
        EasyMock.expect(contactDao.queryContactsByCompanyId(1l)).andReturn(contactsOfCompany);

        List<Contact> contactsOfDepart = new ArrayList<>();
        Contact contact = new Contact();
        contact.setId(existContactId);
        contactsOfDepart.add(contact);
        EasyMock.expect(contactDao.queryContactsByDepartId(3l)).andReturn(contactsOfDepart);

        EasyMock.replay(contactDao);

        List<Long> userIds = new ArrayList<>();
        userIds.add(0,1L);
        userIds.add(1,2L);
        List<ContactDto> actual = contactService.getContactsOfSiblingsDepart(1l, 3l, userIds);
        EasyMock.verify(contactDao);
        Assert.assertNotNull(actual);
        Assert.assertEquals(2,actual.size());
    }

    private List<ContactDto> initContactsOfCompany(Integer existContactId){
        List<ContactDto> contactsOfCompany = new ArrayList<>();
        Integer sameContactId = 1;
        ContactDto dto1 = new ContactDto();
        dto1.setDeptId(1l);
        dto1.setId(sameContactId);
        contactsOfCompany.add(dto1);

        ContactDto dto2 = new ContactDto();
        dto2.setDeptId(2l);
        dto2.setId(sameContactId);
        contactsOfCompany.add(dto2);

        ContactDto dto3 = new ContactDto();
        dto3.setDeptId(2l);
        dto3.setId(2);
        contactsOfCompany.add(dto3);

        ContactDto dto4 = new ContactDto();
        dto4.setDeptId(3l);
        dto4.setId(existContactId);
        contactsOfCompany.add(dto4);
        return contactsOfCompany;
    }

   /* @Test
    public void testDeleteDeptHasContactOfDepart(){
        //删除部门测试有联系人不能删除
        Long departId = 1l;
        User user = new User();
        List<Contact> list = new ArrayList<>();
        list.add(new Contact());
        EasyMock.expect(contactDao.queryContactsByDepartId(departId)).andReturn(list);
        EasyMock.replay(contactDao);

        try{
            contactService.deleteDept(departId,user);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals("部门联系人不为空，无法删除部门",e.getMsg());
        }
        EasyMock.verify(contactDao);
    }*/


    /*@Test
    public void testDeleteDeptHasSecondSettlement(){
        //删除部门有二结为结款
        Long departId = 1l;
        User user = new User();
        EasyMock.expect(contactDao.queryContactsByDepartId(departId)).andReturn(null);
        EasyMock.replay(contactDao);

        Account account = new Account();
        account.setBalanceSecondSettlement(new BigDecimal(0));
        account.setBalanceSecondSettlementFreeze(new BigDecimal(1.00));

        EasyMock.expect(accountDao.selectByPrimaryKey(departId)).andReturn(account);
        EasyMock.replay(accountDao);
        try{
            contactService.deleteDept(departId,user);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals("该部门有账务关系未处理完毕，无法删除",e.getMsg());
        }
        EasyMock.verify(contactDao,accountDao);
    }*/

   /* @Test
    public void testDeleteDeptHasBalance(){
        //删除部门有账户余额
        Long departId = 1l;
        User user = new User();
        EasyMock.expect(contactDao.queryContactsByDepartId(departId)).andReturn(null);

        Account account = new Account();
        account.setBalanceSecondSettlement(new BigDecimal(0));
        account.setBalanceSecondSettlementFreeze(new BigDecimal(0));

        //账户余额
        account.setBalance(new BigDecimal(0));
        account.setBalanceFreeze(new BigDecimal((1.00)));

        EasyMock.expect(accountDao.selectByPrimaryKey(departId)).andReturn(account);
        EasyMock.replay(contactDao,accountDao);
        try{
            contactService.deleteDept(departId,user);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals("该部门有账务关系未处理完毕，无法删除",e.getMsg());
        }
        EasyMock.verify(contactDao,accountDao);
    }*/

 /*   @Test
    public void testDeleteDeptIsFinishInvoiceOut(){
        //删除部门有未处理完的销项票
        Long departId = 1l;
        User user = new User();
        EasyMock.expect(contactDao.queryContactsByDepartId(departId)).andReturn(null);

        Account account = new Account();
        account.setBalanceSecondSettlement(new BigDecimal(0));
        account.setBalanceSecondSettlementFreeze(new BigDecimal(0));
        account.setBalance(new BigDecimal(0));
        account.setBalanceFreeze(new BigDecimal((0.00)));

        EasyMock.expect(accountDao.selectByPrimaryKey(departId)).andReturn(account);

        List<PoolOut> poolOuts = new ArrayList<>();
        poolOuts.add(new PoolOut());
        EasyMock.expect(poolOutDao.queryByBuyer(EasyMock.anyObject())).andReturn(poolOuts);

        EasyMock.replay(contactDao,accountDao,poolOutDao);

        try{
            contactService.deleteDept(departId,user);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals("该部门有销项票未处理完毕，无法删除成功",e.getMsg());
        }
        EasyMock.verify(contactDao,accountDao,poolOutDao);
    }*/

/*    @Test
    public void testDeleteDeptUpdateIsDeletedById(){
        //删除部门有未处理完的销项票
        Long departId = 1l;
        User user = new User();
        EasyMock.expect(contactDao.queryContactsByDepartId(departId)).andReturn(null);

        Account account = new Account();
        account.setBalanceSecondSettlement(new BigDecimal(0));
        account.setBalanceSecondSettlementFreeze(new BigDecimal(0));
        account.setBalance(new BigDecimal(0));
        account.setBalanceFreeze(new BigDecimal((0.00)));

        EasyMock.expect(accountDao.selectByPrimaryKey(departId)).andReturn(account);
        EasyMock.expect(poolOutDao.queryByBuyer(EasyMock.anyObject())).andReturn(null);
        EasyMock.expect(accountDao.updateIsDeletedById(departId, user.getName())).andReturn(0);

        EasyMock.replay(contactDao,accountDao,poolOutDao);

        try{
            contactService.deleteDept(departId,user);
            Assert.fail("单元测试失败");
        }catch (BusinessException e){
            Assert.assertEquals("部门删除失败",e.getMsg());
        }
        EasyMock.verify(contactDao,accountDao,poolOutDao);
    }*/
}
