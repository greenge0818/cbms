package com.prcsteel.platform.order.web.support;


import com.prcsteel.lts.api.config.Environments;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

/**
 * 应用环境变量
 * Created by Rolyer on 2016/1/12.
 */
public class AppEnvironment {

    public static Properties getEnvs(){
        BeanFactory context = new ClassPathXmlApplicationContext("cbms-config.xml");
        Environments envs = context.getBean(Environments.class);

        Properties prop = new Properties();
        if (envs!=null && envs.getProps()!=null && !envs.getProps().isEmpty()) {
            return envs.getProps();
        }

        return null;
    }
}
