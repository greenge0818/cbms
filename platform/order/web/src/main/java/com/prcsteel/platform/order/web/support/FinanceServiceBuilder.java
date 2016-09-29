package com.prcsteel.platform.order.web.support;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.prcsteel.platform.order.service.FinanceService;
import com.prcsteel.framework.nido.core.ServiceBuilder;

/**
 * 
 * @author zhoukun
 */
@Component("financeServiceBuilder")
public class FinanceServiceBuilder implements ServiceBuilder {

	@Value("${ivFinanceService}")
    private String ivFinanceServiceAddress;  // 接口服务地址
	
	@Override
	public Object build(String clsName, Class<?> cls) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        //注册接口
        factory.setServiceClass(FinanceService.class);
        //设置地址
        factory.setAddress(ivFinanceServiceAddress);
        return factory.create();
	}

}
