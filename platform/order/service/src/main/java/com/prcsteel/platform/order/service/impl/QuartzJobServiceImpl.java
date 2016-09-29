package com.prcsteel.platform.order.service.impl;

import com.prcsteel.platform.order.model.model.QuartzJob;
import com.prcsteel.platform.order.persist.dao.QuartzJobDao;
import com.prcsteel.platform.order.service.QuartzJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Rolyer on 2016/7/6.
 */
@Service("quartzJobService")
public class QuartzJobServiceImpl implements QuartzJobService {

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobServiceImpl.class);

    private final static String JOB_STATUS_DOING    = "DOING";  //（任务）进行中
    private final static String JOB_STATUS_DONE     = "DONE";   //（任务）已完成

    @Resource
    private QuartzJobDao quartzJobDao;

    @Override
    public boolean starting(String name, String description) {
        Assert.hasText(name, "Job name must not be null or empty.");

        QuartzJob job = quartzJobDao.selectByJobName(name);
        int effect;
        if (job==null) { //不存在则新建，并设置状态值为DOING。
            job = new QuartzJob(name, JOB_STATUS_DOING, null, null, null, description);
            effect = quartzJobDao.insert(job);

        } else { //存在则检查状态，并更新状态值为DOING。

            if (JOB_STATUS_DOING.equalsIgnoreCase(job.getStatus())) { //Job已处于运行状态。
                logger.info("skip job {}, because the previous one still running.", name);
                return false;
            }

            effect = updateQuartzJob(name, JOB_STATUS_DONE, new Date(), new Date());
        }

        return effect > 0 ? true : false;
    }

    @Override
    public boolean finish(String name) {
        int effect = updateQuartzJob(name, JOB_STATUS_DONE, new Date(), null);
        return effect > 0 ? true : false;
    }

    /**
     * 更新Job
     * @param name 名称
     * @param status 状态
     * @param lastUpdated 更新时间
     * @param previousExecutedTime 执行时间
     * @return
     */
    private int updateQuartzJob(String name, String status, Date lastUpdated, Date previousExecutedTime){
        QuartzJob job = new QuartzJob(name, status, lastUpdated, previousExecutedTime, null, null);

        return quartzJobDao.updateByJobName(job);
    }
}
