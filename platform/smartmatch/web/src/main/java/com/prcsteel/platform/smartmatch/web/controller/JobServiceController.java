package com.prcsteel.platform.smartmatch.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prcsteel.platform.common.vo.Result;
import com.prcsteel.platform.smartmatch.web.job.ResourceInventoryReportJob;

/**
 * Created by Rolyer on 2016/1/12.
 */
@RestController
@RequestMapping("/jobService")
public class JobServiceController {
    private static final Logger logger =  LoggerFactory.getLogger(JobServiceController.class);

    @Resource
    private ResourceInventoryReportJob resourceInventoryReportJob;
   

    @RequestMapping(value = "/resourceInventoryReportJob", method = RequestMethod.GET)
    public Result resourceInventoryReportJob() {
        resourceInventoryReportJob.execute();

        return new Result();

    }

}
