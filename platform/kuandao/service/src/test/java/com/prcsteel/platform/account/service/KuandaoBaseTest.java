package com.prcsteel.platform.account.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
/**
 * 
 * @author zhoukun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:test1-spring-mybatis.xml",
		"classpath:test1-spring-persist.xml",
		"classpath:test1-spring-service.xml",
		"classpath:test1-spring-jms.xml"
		})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
	})
public class KuandaoBaseTest {

	@Test
	public void test(){
		
	}
	
}
