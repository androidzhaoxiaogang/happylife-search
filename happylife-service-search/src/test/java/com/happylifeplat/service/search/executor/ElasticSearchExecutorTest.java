package com.happylifeplat.service.search.executor;

import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.JobInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/7 10:18
 * @since JDK 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationTestContext.xml"})
public class ElasticSearchExecutorTest {

    @Autowired(required = false)
    GoodsHandlerExecutor goodsHandlerExecutor;


    @Test
    public void executeGoods() throws Exception {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setIndex("goods");
        jobInfo.setType("goods");
        jobInfo.setParam("goods.lastTime");
        goodsHandlerExecutor.execute(jobInfo);
    }


    @Test
    public void testEsClient(){
        GoodsEs goodsEs = new GoodsEs();
        goodsEs.setId("899999999999");
        goodsEs.setName("xiaoyu888");
        List<GoodsEs> goodsEsList = new ArrayList<>();
        goodsEsList.add(goodsEs);
        goodsEs=new GoodsEs();
        goodsEs.setId("77777");
        goodsEs.setName("test2");
        goodsEsList.add(goodsEs);
        ElasticSearchClient.bulkGoodsIndex("goods","goods", goodsEsList);
    }


    @Test
    public void testBulkDelete(){
        List<String> ids = new ArrayList<>();
        ids.add("899999999999");
        ids.add("77777");
        ids.add("ssasasasa");
        ElasticSearchClient.bulkDelete("goods","goods",ids);
    }

}