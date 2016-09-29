package com.prcsteel.platform.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.prcsteel.platform.account.model.enums.AccountBankDataStatus;
import com.prcsteel.platform.account.model.model.AccountBank;
import com.prcsteel.platform.account.persist.dao.AccountBankDao;
import com.prcsteel.platform.acl.model.model.User;
import com.prcsteel.platform.common.constants.Constant;
import com.prcsteel.platform.common.exception.BusinessException;

/**
 * Created by dengxiyan on 2016/3/11.
 */
public class AccountBankServiceTest extends BaseTest{
    @Resource
    private AccountBankService accountBankService;

    private AccountBankDao accountBankDao;

    private IMocksControl mocksControl = EasyMock.createControl();

    @Before
    public void before() throws Exception {
        accountBankDao = mocksControl.createMock(AccountBankDao.class);
        ReflectionTestUtils.setField(accountBankService, "accountBankDao", accountBankDao);
    }

    @After
    public void after() throws Exception {
        accountBankDao = null;
    }

    @Test
    public void  testQuery(){
        Map<String,Object> paramMap = new HashMap<>();
        List<AccountBank> list = new ArrayList<>();
        EasyMock.expect(accountBankDao.query(paramMap)).andReturn(list);
        mocksControl.replay();
        List<AccountBank> result = accountBankService.query(paramMap);

        mocksControl.verify();

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testSetDefaultBank(){
        AccountBank bank = new AccountBank();
        bank.setId(1l);
        bank.setModificationNumber(0);
        bank.setBankDataStatus(AccountBankDataStatus.Approved.getCode());

        Long bankId = 1l;
        Long accountId = 2l;

        EasyMock.expect(accountBankDao.selectByPrimaryKey(bankId)).andReturn(bank);
        EasyMock.expect(accountBankDao.updateByAccountId(EasyMock.anyObject())).andReturn(2);


        EasyMock.expect(accountBankDao.updateByPrimaryKeySelective(bank)).andReturn(0);

        mocksControl.replay();

        try{
            accountBankService.setDefaultBank(accountId, bankId, new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }

    @Test
    public void testDeleteLogicallyBankInfo(){
        AccountBank bank = new AccountBank();
        bank.setModificationNumber(0);
        Long bankId = 1l;

        EasyMock.expect(accountBankDao.selectByPrimaryKey(bankId)).andReturn(bank);
        EasyMock.expect(accountBankDao.updateByPrimaryKeySelective(EasyMock.anyObject())).andReturn(0);
        mocksControl.replay();

        try{
            accountBankService.updateDeletedById(bankId,null,new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }


    @Test
    public void testSaveBankInfoCheckAccountCode(){
        AccountBank bank = createAccountBank();
        bank.setId(null);
        AccountBank dbBank = createAccountBank();
        EasyMock.expect(accountBankDao.selectByAccountCode(EasyMock.anyObject())).andReturn(dbBank);
        mocksControl.replay();
        try{
            accountBankService.saveBankInfo(null, bank, new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }

    @Test
    public void testSaveBankInfoCheckImag(){
        AccountBank bank = createAccountBank();
        bank.setUrl(null);
        EasyMock.expect(accountBankDao.selectByAccountCode(EasyMock.anyObject())).andReturn(null);
        mocksControl.replay();
        try{
            accountBankService.saveBankInfo(null,bank,new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }

    @Test
    public void testAddBankInfo(){
        AccountBank bank = createAccountBank();
        bank.setId(null);
        EasyMock.expect(accountBankDao.selectByAccountCode(EasyMock.anyObject())).andReturn(null);
        //EasyMock.expect(accountBankDao.query(EasyMock.anyObject())).andReturn(null);
        EasyMock.expect(accountBankDao.insertSelective(bank)).andReturn(0);
        mocksControl.replay();
        try{
            accountBankService.saveBankInfo(null,bank,new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }

    @Test
    public void testModifyBankInfo(){
        AccountBank bank = createAccountBank();
        AccountBank dbBank = createAccountBank();
        EasyMock.expect(accountBankDao.selectByAccountCode(EasyMock.anyObject())).andReturn(null);
        EasyMock.expect(accountBankDao.selectByPrimaryKey(bank.getId())).andReturn(dbBank);
        EasyMock.expect(accountBankDao.updateByPrimaryKeySelective(bank)).andReturn(0);

        mocksControl.replay();
        try{
            accountBankService.saveBankInfo(null,bank,new User());
            Assert.fail("测试失败，应抛出异常");
        }catch (BusinessException b){
            Assert.assertEquals(Constant.EXCEPTIONCODE_BUSINESS,b.getCode());
        }
        mocksControl.verify();
    }

    private AccountBank createAccountBank(){
        AccountBank bank = new AccountBank();
        bank.setBankAccountCode("55 000 ");
        bank.setId(1l);
        bank.setUrl("11111111");
        bank.setModificationNumber(0);
        return bank;
    }


}
