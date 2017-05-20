package com.happylifeplat.service.search.client;

import com.happylifeplat.facade.search.service.ElasticSearchService;
import com.happylifeplat.service.search.helper.LogUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/7 12:58
 * @since JDK 1.8
 */
public class ClientTest {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTest.class);

    public static void main(String[] args) {
        String clusterName = "elasticsearch53";
        String ip = "120.76.52.162";
        Integer port = 9300;
        //es集群的设置信息
        Settings settings = Settings.builder().put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
        try {
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
            final SearchResponse searchResponse = client.prepareSearch("goods").setTypes("goods").get();
            LogUtil.info(LOGGER, "数据:{}", searchResponse);
        } catch (UnknownHostException e) {
            LogUtil.error(LOGGER, "elasticsearch UnknownHostException 客户端连接失败：{}", e::getMessage);
        }

    }
}
