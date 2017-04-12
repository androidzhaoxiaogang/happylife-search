package com.happylifeplat.facade.search.service;

import com.happylifeplat.BaseTest;
import com.happylifeplat.facade.search.entity.SearchRequest;
import com.happylifeplat.facade.search.result.SearchResult;
import com.happylifeplat.service.search.executor.handler.ConcurrentHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.SpringBeanUtils;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  elasticSearch dubbo 接口测试
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/6 15:01
 * @since JDK 1.8
 */

public class ElasticSearchServiceTest extends  BaseTest {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceTest.class);

    @Autowired
    private ElasticSearchService elasticSearchService;


    @Test
    public void search() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeywords("发哥");
        searchRequest.setRegionId("440304001");
        final SearchResult searchResult = elasticSearchService.search(searchRequest);
        LogUtil.info(LOGGER,"查询的结果为：{}",searchResult);
    }

    @Test
    public void fireRegionChangeEvent(){
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("classpath:spring/applicationTestContext.xml");
        final ConcurrentHandler concurrentHandler = context.getBean(ConcurrentHandler.class);
        concurrentHandler.init();
        String providerId="ca285b69-7192-4996-9c41-ce4686a1b63c";
        elasticSearchService=context.getBean(ElasticSearchService.class);
        elasticSearchService.fireRegionChangeEvent(providerId);
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}