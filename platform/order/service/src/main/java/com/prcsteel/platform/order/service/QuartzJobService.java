package com.prcsteel.platform.order.service;

/**
 * Created by Rolyer on 2016/7/6.
 */
public interface QuartzJobService {

    /**
     * （标记）Job开始
     * @param name 名称
     * @param description 描述
     * @return
     */
    boolean starting(String name, String description);

    /**
     * （标记）Job完成
     * @param name 名称
     * @return
     */
    boolean finish(String name);
}
