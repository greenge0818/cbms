package com.prcsteel.platform.order.web.support;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.prcsteel.framework.nido.engine.Nido;

/**
 * 
 * @author zhoukun
 */
@Component("cbmsApplicationListener")
public class CbmsApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Resource
	private SpringServiceBuilder springServiceBuilder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Nido.init();
		Nido.getNidoContext().setServiceBuilder(springServiceBuilder);// 设置bean构造器
	}

}
