package com.prcsteel.platform.account.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.prcsteel.platform.account.model.enums.GlobalIdModule;
import com.prcsteel.platform.account.model.model.GlobalId;
import com.prcsteel.platform.account.persist.dao.GlobalIdDao;

import junit.framework.Assert;

/**
 * 
 * @author zhoukun
 */
public class GlobalIdServiceImplTest extends BaseTest {

	@Resource
	private GlobalIdService globalIdService;
	
	private GlobalIdDao globalIdDao;
	
	@Before
    public void before() {
		globalIdDao = EasyMock.createMock(GlobalIdDao.class);
        ReflectionTestUtils.setField(globalIdService, "globalIdDao", globalIdDao);
        
        List<GlobalId> allModules = new ArrayList<>();
        GlobalId g = new GlobalId();
        g.setCurrentId(0L);
        g.setModuleName(GlobalIdModule.Account.toString());
        g.setRemark("Test");
        allModules.add(g);
        EasyMock.expect(globalIdDao.selectAll()).andReturn(allModules);
        EasyMock.expect(globalIdDao.selectByModuleName(EasyMock.anyObject())).andReturn(g);
        EasyMock.expect(globalIdDao.setCurrentId(EasyMock.anyObject())).andReturn(1);
        EasyMock.replay(globalIdDao);
    }
	
	@After
    public void after() {
		globalIdDao = null;
    }
	
	@Test
	public void testGetId(){
		long id = globalIdService.getId(GlobalIdModule.Account);
		Assert.assertEquals(2, id);
	}
	
}
