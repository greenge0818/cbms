package com.prcsteel.platform.account.service;

import com.prcsteel.platform.account.model.model.AccountContractTemplate;
import com.prcsteel.platform.account.persist.dao.AccountContractTemplateDao;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rolyer on 2016/1/29.
 */
public class AccountContractTemplateServiceImplTest extends BaseTest {

    @Resource
    private AccountContractTemplateService accountContractTemplateService;

    private IMocksControl mockControl = EasyMock.createControl();
    private AccountContractTemplateDao accountContractTemplateDao;

    @Before
    public void before() throws Exception {
        accountContractTemplateDao = mockControl.createMock(AccountContractTemplateDao.class);

        ReflectionTestUtils.setField(accountContractTemplateService, "accountContractTemplateDao", accountContractTemplateDao);
    }

    @After
    public void after() throws Exception {
        accountContractTemplateDao = null;
    }

    @Test
    public void test_query_sys_template() throws Exception {
        List<AccountContractTemplate> list = new ArrayList<>();
        EasyMock.expect(accountContractTemplateDao.selectByModel(EasyMock.anyObject(AccountContractTemplate.class))).andReturn(list);
        mockControl.replay();
        List<AccountContractTemplate> result = accountContractTemplateService.querySysTemplate();
        mockControl.verify();

        Assert.assertNotNull(result);
        Assert.assertEquals(0,result.size());
    }

    @Test
    public void test_add_sys_contract_template() throws Exception {
        AccountContractTemplate act = new AccountContractTemplate();

        EasyMock.expect(accountContractTemplateDao.insertSelective(act)).andReturn(1);
        mockControl.replay();

        Boolean result = accountContractTemplateService.saveContractTemplate(act);

        mockControl.verify();

        Assert.assertTrue(result);
    }

    private static Long id = 1L;

    private void mockpdateByPrimaryKeySelective(AccountContractTemplate act){
        EasyMock.expect(accountContractTemplateDao.updateByPrimaryKeySelective(act)).andReturn(1);
        mockControl.replay();
    }

    @Test
    public void test_update_sys_contract_template() throws Exception {
        AccountContractTemplate act = new AccountContractTemplate();
        act.setId(id);

        mockpdateByPrimaryKeySelective(act);

        Boolean result = accountContractTemplateService.saveContractTemplate(act);

        mockControl.verify();

        Assert.assertTrue(result);
    }

    /**
     * 伪造模板数据
     * @param totalItems List这个模板实例个数
     * @return
     */
    private List<AccountContractTemplate> fakeTemplateList(int totalItems){
        List<AccountContractTemplate> templates = new ArrayList<>();
        for (int i = 0; i< totalItems; i++) {
            AccountContractTemplate act = new AccountContractTemplate();
            templates.add(act);
        }

        return  templates;
    }

    @Test
    public void test_query_one_account_contract_template() throws Exception {
        List<AccountContractTemplate> template = fakeTemplateList(1);

        EasyMock.expect(accountContractTemplateDao.selectByModel(EasyMock.anyObject(AccountContractTemplate.class))).andReturn(template);
        mockControl.replay();

        AccountContractTemplate result = accountContractTemplateService.queryAccountContractTemplate(id);

        mockControl.verify();

        Assert.assertNotNull(result);
    }

    @Test
    public void test_query_one_account_contract_template_but_fetch_null() throws Exception {
        List<AccountContractTemplate> template = fakeTemplateList(0);

        EasyMock.expect(accountContractTemplateDao.selectByModel(EasyMock.anyObject(AccountContractTemplate.class))).andReturn(template);
        mockControl.replay();

        AccountContractTemplate result = accountContractTemplateService.queryAccountContractTemplate(id);

        mockControl.verify();

        Assert.assertNull(result);
    }

    @Test
    public void test_approved_contract_template() throws Exception {
        mockpdateByPrimaryKeySelective(EasyMock.anyObject(AccountContractTemplate.class));

        Boolean res = accountContractTemplateService.approvedContractTemplate(id, true);

        mockControl.verify();

        Assert.assertTrue(res);
    }

    @Test
    public void test_enabled_contract_template() throws Exception {
        mockpdateByPrimaryKeySelective(EasyMock.anyObject(AccountContractTemplate.class));

        Boolean res = accountContractTemplateService.enabledContractTemplate(id, true);

        mockControl.verify();

        Assert.assertTrue(res);
    }

    @Test
    public void testFetchTemplateContent() throws Exception {
        String content = "这是合同条款。";
        List<AccountContractTemplate> list = fakeTemplateList(1);
        list.get(0).setContent(content);

        EasyMock.expect(accountContractTemplateDao.selectByModel(EasyMock.anyObject(AccountContractTemplate.class))).andReturn(list);
        mockControl.replay();

        String res = accountContractTemplateService.fetchTemplateContent(new AccountContractTemplate());

        mockControl.verify();

        Assert.assertEquals(content, res);
    }

    @Test
    public void testResolveTemplate() throws Exception {
        List<AccountContractTemplate> list = fakeTemplateList(1);

        EasyMock.expect(accountContractTemplateDao.selectByModel(EasyMock.anyObject(AccountContractTemplate.class))).andReturn(list);
        mockControl.replay();

        String res = accountContractTemplateService.resolveTemplate(id, "seller", 0L);

        mockControl.verify();

        Assert.assertNotNull(res);
    }


    @Test
    public void testDeleteTemplateById() {
        EasyMock.expect(accountContractTemplateDao.deleteByPrimaryKey(id)).andReturn(1);
        mockControl.replay();

        Boolean res = accountContractTemplateService.deleteTemplateById(id);

        mockControl.verify();

        Assert.assertTrue(res);
    }


}