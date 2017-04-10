package com.happylifeplat.service.search.executor.handler;

import com.happylifeplat.service.search.entity.HandlerEntity;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p> Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * es 构建索引handler 接口
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/10 10:05
 * @since JDK 1.8
 */
public interface ElasticSearchHandler<T> {

    /**
     * elastic 构建索引处理接口
     * @param handlerEntity 实体类
     */
    void action(HandlerEntity<T> handlerEntity);
}
