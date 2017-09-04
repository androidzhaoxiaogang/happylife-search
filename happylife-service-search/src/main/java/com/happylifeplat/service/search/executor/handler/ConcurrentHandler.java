package com.happylifeplat.service.search.executor.handler;

import com.google.common.reflect.Reflection;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.helper.Assert;
import com.happylifeplat.service.search.helper.FixedThreadPoolHelper;
import com.happylifeplat.service.search.helper.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 多线程构建索引处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/5 9:05
 * @since JDK 1.8
 */
@Component
public class ConcurrentHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrentHandler.class);

    /**
     * 需要回滚的事务队列
     */
    private static BlockingQueue<HandlerEntity> QUEUE;

    /**
     * 初始化
     */
    public void init() {
        synchronized (LOGGER) {
            QUEUE = new LinkedBlockingQueue<>(ConstantSearch.MAX_QUEUE);
            final ExecutorService executorService =
                    FixedThreadPoolHelper.getInstance().getExecutorService();
            final int MAX_THREAD = FixedThreadPoolHelper.getInstance().getDefaultThreadMax();
            LogUtil.info(LOGGER, "启动构建elasticSearch索引,线程数:{}", () -> MAX_THREAD);
            for (int i = 0; i < MAX_THREAD; i++) {
                executorService.execute(new Worker());
            }
        }
    }


    /**
     * 提交到队列
     *
     * @param handlerEntity 处理实体类
     */
    public void submit(HandlerEntity handlerEntity) {
        Assert.notNull(handlerEntity);
        try {
            QUEUE.put(handlerEntity);
        } catch (InterruptedException e) {
            LogUtil.error(LOGGER, e::getMessage);
        }

    }

    /**
     * 线程执行器
     */
    class Worker implements Runnable {

        @Override
        public void run() {
            execute();
        }

        /**
         * 执行..
         */
        @SuppressWarnings("unchecked")
        private void execute() {
            while (true) {
                try {
                    HandlerEntity handlerEntity = QUEUE.take();//得到需要处理的es实体对象进行动态代理
                    if (handlerEntity != null) {
                        final Object clazz =handlerEntity.getHandler().newInstance();
                        final ElasticSearchHandler elasticSearchHandler =
                                Reflection.newProxy(ElasticSearchHandler.class, new EsHandler(clazz));
                        elasticSearchHandler.action(handlerEntity);
                    }
                } catch (Exception e) {
                    LOGGER.error(" failure ," + e.getMessage());
                }
            }

        }
    }

    /**
     * es处理 代理类
     */
    public class EsHandler implements InvocationHandler {

        private Object handler;

        EsHandler(Object handler) {
            this.handler = handler;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(handler, args);
        }
    }
}
