package com.happylifeplat;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/7 10:14
 * @since JDK 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationTestContext.xml"})
public class BaseTest {
    
    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
}
