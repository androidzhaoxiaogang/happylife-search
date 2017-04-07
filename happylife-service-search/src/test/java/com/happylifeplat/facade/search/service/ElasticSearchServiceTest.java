package com.happylifeplat.facade.search.service;

import com.happylifeplat.BaseTest;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/6 15:01
 * @since JDK 1.8
 */

public class ElasticSearchServiceTest extends BaseTest {

    @Autowired(required = false)
    private GoodsEsMapper  goodsEsMapper;

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceTest.class);

    @Test
    public void search() throws Exception {

        LOGGER.info("============");
        goodsEsMapper.listByUpdateTime("2017-04-05");
        LOGGER.info("============");

    }




}