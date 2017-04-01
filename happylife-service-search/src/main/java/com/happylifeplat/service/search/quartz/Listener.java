package com.happylifeplat.service.search.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 任务监听
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:59
 * @since JDK 1.8
 */
public class Listener implements JobListener {


    @Override
    public String getName() {
        return "Job_Listener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobKey = context.getJobDetail().getKey().getName();
        boolean isCancel = JobScheduler.getInstance().isRunning(jobKey);
        context.getJobDetail().getJobDataMap().put("cancel", isCancel);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

    }
}
