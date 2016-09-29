package com.prcsteel.platform.order.persist.dao;

import com.prcsteel.platform.order.model.model.QuartzJob;

/**
 * Created by Rolyer on 2016/7/6.
 */
public interface QuartzJobDao {
    /**
     * 新建Quartz Job
     * @param job Job信息
     * @return
     */
    int insert(QuartzJob job);

    /**
     * 查询Quartz Job
     * @param jobNmae Job名称
     * @return
     */
    QuartzJob selectByJobName(String jobNmae);

    /**
     * 根据主键（id）更新Quartz Job
     * @param job Job信息
     * @return
     */
    int updateByJobName(QuartzJob job);
}
