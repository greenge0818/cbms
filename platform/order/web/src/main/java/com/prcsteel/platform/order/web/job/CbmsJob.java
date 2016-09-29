package com.prcsteel.platform.order.web.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.prcsteel.lts.api.config.Environments;

/**
 * Created by Rolyer on 2015/10/15.
 */
public abstract class CbmsJob {
    private static final Logger logger = Logger.getLogger(CbmsJob.class);

    /**
     * 执行内容
     */
    public abstract void execute();

    /**
     * 是否启用当前Job判断（满足针对特定Job需要单独启用/禁用）。
     * @return
     */
    public abstract boolean isEnabled();

    /**
     * 配置读取
     * @return
     */
    protected Environments getEnvs(){
        BeanFactory context = new ClassPathXmlApplicationContext("cbms-config.xml");
        Environments envs = context.getBean(Environments.class);

        return envs;
    }
}
