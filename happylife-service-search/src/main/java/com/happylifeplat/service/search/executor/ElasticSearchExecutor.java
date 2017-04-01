package com.happylifeplat.service.search.executor;

import com.happylifeplat.service.search.entity.JobInfo;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 18:12
 * @since JDK 1.8
 */
public interface ElasticSearchExecutor {

    /**
     * jobInfo 执行接口
     * @param jobInfo job信息
     */
    void execute(JobInfo jobInfo);
}
