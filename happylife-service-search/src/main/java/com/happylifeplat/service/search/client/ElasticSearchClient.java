package com.happylifeplat.service.search.client;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * es客户端 spring来初始化，作为单列存在
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:30
 * @since JDK 1.8
 */
public class ElasticSearchClient {

    private static TransportClient client;

    private static final int DEFAULT_PORT = 9300;//elasticsearch 集群的主节点的默认是9300
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);

    private ElasticSearchClient(String clusterName, String ip, Integer port) {
        if (StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("elasticsearch cluster master node ip cannot be null.");
        }
        if (null == port || port.intValue() == 0) {
            port = DEFAULT_PORT;
        }
        //es集群的设置信息
        Settings settings = Settings.builder().put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
           /* client = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));*/
        LOGGER.debug("elasticsearch 客户端初始化成功！");
    }


    /**
     * 批量构建商品索引  和服务区域是父子关联关系
     *
     * @param index 索引
     * @param type  类型
     * @param list  商品集合
     * @return true false
     */
    public static boolean bulkGoodsIndex(String index, String type, List<GoodsEs> list) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        list.forEach(goodsEs -> bulkRequest.add(
                client.prepareIndex(index, type, goodsEs.getId()).setSource(goodsEs)));
        return !bulkRequest.get().hasFailures();
    }

    public static boolean bulkDelete(String index, String type, List<String> ids){
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        ids.forEach(id-> bulkRequest.add(client.prepareDelete(index, type,id)));
       return  !bulkRequest.get().hasFailures();
    }

    public static void bulkDeleteRegion(String index,List<String> providerIds ){
        providerIds.forEach(providerId->{
            final BulkByScrollResponse bulkByScrollResponse =
                    DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                    .filter(QueryBuilders.matchQuery("providerId", providerId))
                    .source(index)
                    .get();
             bulkByScrollResponse.getDeleted();
        });

    }

    /**
     * 创建服务区域索引  和商品type是父子关联关系
     *
     * @param index 索引
     * @param type  类型
     * @param list  服务区域集合
     * @return true false
     */
    public static boolean bulkRegionIndex(String index, String type, List<ProviderRegionEs> list) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        list.forEach(providerRegionEs -> {
            bulkRequest.add(
                    client.prepareIndex(index, type)
                            .setParent(providerRegionEs.getGoodsId())
                            .setSource(providerRegionEs));
        });
        return !bulkRequest.get().hasFailures();
    }

    public static void searchAll() {
        SearchResponse response = client.prepareSearch().get();

    }

    public static void searchByIndexType(String index, String type) {
        SearchResponse response = client.prepareSearch(index).setTypes(type).get();
    }

    public static void shutdown() {
        client.close();
    }
}
