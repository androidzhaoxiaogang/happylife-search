package com.happylifeplat.service.search.task;

import com.happylifeplat.service.search.bootstrap.JobBootstrap;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.executor.ElasticSearchExecutor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * task 任务
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:59
 * @since JDK 1.8
 */
public class Task implements Job {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        JobInfo info = (JobInfo) data.get("jobInfo");
        if (data.getBoolean("cancel")) {
            LOGGER.info(info.getName() + " new task just be canceled, this job is in running");
            return;
        }
        //获取配置的job处理对象，进行代理调用
        String handler = info.getHandler();
        final ElasticSearchExecutor elasticSearchExecutor = JobBootstrap.getExecutor(handler);
        elasticSearchExecutor.execute(info);
       /* try {
            Object bean = Class.forName(handler).newInstance();
            ElasticSearchExecutor elasticSearchExecutor =
                    Reflection.newProxy(ElasticSearchExecutor.class, new worker(bean));
            elasticSearchExecutor.execute(info);
        } catch (Exception e) {
            LOGGER.error("callback execute failure:classname  " +handler + "," + e.getMessage());
        }*/
    }


    public class worker implements InvocationHandler {

        private Object handler;

        public worker(Object handler) {
            this.handler = handler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(handler, args);
        }
    }


}
