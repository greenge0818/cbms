package com.prcsteel.platform.common.aspect;

import java.lang.annotation.*;

/**
 * 操作信息注解
 *
 * Created by Rolyer on 2016/6/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpAction {

    /**
     * 键值，指定从参数中提取值的参数名
     * @return
     */
    String key();

    /**
     * 缓存时间,默认5分钟
     * @return
     */
    int exp() default 5*60;

    /**
     * 缓存内容
     * @return
     */
    String content() default "PROCESSING";

    /**
     * 是否为Ajax请求，是 true；否 false。默认true。
     * @return
     */
    boolean isAjax() default true;
}
