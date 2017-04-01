package com.happylifeplat.service.search.quartz;

import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.task.Task;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.EverythingMatcher.allJobs;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * JobScheduler 提交任务等
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:59
 * @since JDK 1.8
 */
public class JobScheduler {
    private Logger logger = Logger.getLogger(getClass());
    private Scheduler scheduler;
    private static JobScheduler instance = new JobScheduler();

    private JobScheduler() {
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
            scheduler.getListenerManager().addJobListener(new Listener(), allJobs());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public static JobScheduler getInstance() {
        return instance;
    }

    public JobScheduler pushJobs(List<JobInfo> jobInfoList) {
        jobInfoList.forEach(info -> {
            JobDetail job = newJob(Task.class).withIdentity(info.getName(), "jobs").build();
            job.getJobDataMap().put("jobInfo", info);
            CronTrigger trigger = newTrigger().withIdentity(info.getName(), "triggers")
                    .withSchedule(cronSchedule(info.getCron())).build();
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public boolean isRunning(String jobKey) {
        try {
            for (JobExecutionContext context : scheduler.getCurrentlyExecutingJobs()) {
                // logger.info(context.getJobDetail().getKey().getName() + " is running");
                if (context.getJobDetail().getKey().getName().equals(jobKey)) {
                    return true;
                }
            }
        } catch (SchedulerException e) {
            logger.info("get jobs status failed");
            e.printStackTrace();
        }
        return false;
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public void shutdown() {
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
