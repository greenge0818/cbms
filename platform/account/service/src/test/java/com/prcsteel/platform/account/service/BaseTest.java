package com.prcsteel.platform.account.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 
 * @author zhoukun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:test-spring-mybatis.xml",
		"classpath:test-spring-persist.xml",
		"classpath:test-spring-service.xml"
		})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class BaseTest {

	@Test
	public void test(){
	}
	
}
