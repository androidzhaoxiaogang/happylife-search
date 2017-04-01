package com.happylifeplat.service.search.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.quartz.JobScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 18:19
 * @since JDK 1.8
 */
public class JobInit implements ApplicationContextAware {

    //Spring应用上下文环境
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        start();
    }

    /**
     * 开始执行
     */
    private void start() {
        JobScheduler jobScheduler = JobScheduler.getInstance();
        List<JobInfo> jobInfoList=  (List<JobInfo>) applicationContext.getBean("jobs");
        try {
            jobScheduler.pushJobs(jobInfoList).start();
        } catch (SchedulerException e) {
            ElasticSearchClient.shutdown();
            JobScheduler.getInstance().shutdown();
            System.exit(1);
        }
    }
}
