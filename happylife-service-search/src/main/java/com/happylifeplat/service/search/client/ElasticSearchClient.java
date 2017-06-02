package com.happylifeplat.service.search.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happylifeplat.facade.search.enums.OrderByEnum;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.query.SearchEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasChildQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasParentQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

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

    /**
     * jackson用于序列化操作的mapper
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int DEFAULT_PORT = 9300;//elasticsearch 集群的主节点的默认是9300
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);

    private ElasticSearchClient(String clusterName, String ip, Integer port) {
        if (StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("elasticsearch cluster master node ip cannot be null.");
        }
        if (null == port || port == 0) {
            port = DEFAULT_PORT;
        }
        //es集群的设置信息
        Settings settings = Settings.builder().put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
            LogUtil.error(LOGGER, "elasticsearch UnknownHostException 客户端连接失败：{}", e::getMessage);
        }
        LogUtil.info(LOGGER, () -> "elasticsearch 客户端初始化成功！连接的ip为："+ip);
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
        list.forEach(goodsEs -> {
            try {
                final String goodsJson = objectMapper.writeValueAsString(goodsEs);
                bulkRequest.add(
                        client.prepareIndex(index, type, goodsEs.getId()).setSource(goodsJson, XContentType.JSON));
            } catch (JsonProcessingException e) {
                LogUtil.error(LOGGER, "商品对象json格式化异常:{}", e::getMessage);
            }
        });
        return !bulkRequest.get().hasFailures();
    }


    /**
     * 更新商品文档中的region区域
     *
     * @param index            索引
     * @param type             索引类型
     * @param ids              文档id集合
     * @param providerRegionEs 更新的区域
     * @return !bulkRequest.get().hasFailures()
     * @throws Exception 异常信息
     */
    public static boolean updateGoodsRegion(String index, String type, List<String> ids, List<ProviderRegionEs> providerRegionEs) throws Exception {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        final String regions = objectMapper.writeValueAsString(providerRegionEs);
        String regionsUpdate = "{" + "\"regions\"" +":" + regions + "}";
        ids.forEach(id-> bulkRequest.add(client.prepareUpdate(index,type,id).setDoc(regionsUpdate, XContentType.JSON)));
        return !bulkRequest.get().hasFailures();
    }

    /**
     * 批量更新文档中的单个filed字段
     *
     * @param index      索引
     * @param type       类型
     * @param ids        文档id集合
     * @param fieldName  field字段名称
     * @param fieldValue field字段值
     * @return !bulkRequest.get().hasFailures()
     */
    public static boolean updateGoodsField(String index, String type, List<String> ids, String fieldName, String fieldValue) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        ids.forEach(id -> {
            try {
                final XContentBuilder xContentBuilder =
                        jsonBuilder().startObject().field(fieldName, fieldValue).endObject();
                bulkRequest.add(client.prepareUpdate(index, type, id).setDoc(xContentBuilder));
            } catch (IOException e) {
                LogUtil.error(LOGGER, "updateGoodsField json格式化异常:{}", e::getMessage);
            }
        });
        return !bulkRequest.get().hasFailures();
    }


    /**
     * 根据文档id删除
     *
     * @param index 索引
     * @param type  索引类型
     * @param ids   文档id集合
     * @return !bulkRequest.get().hasFailures()
     */
    public static boolean bulkDelete(String index, String type, List<String> ids) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        ids.forEach(id -> bulkRequest.add(client.prepareDelete(index, type, id)));
        return !bulkRequest.get().hasFailures();
    }


    public static void bulkDeleteRegion(String index, List<String> providerIds) {
        providerIds.forEach(providerId -> {
            final BulkByScrollResponse bulkByScrollResponse =
                    DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                            .filter(matchQuery("providerId", providerId))
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
            try {
                final String regionJson = objectMapper.writeValueAsString(providerRegionEs);
                bulkRequest.add(
                        client.prepareIndex(index, type)
                                .setParent("")
                                .setSource(regionJson, XContentType.JSON));
            } catch (JsonProcessingException e) {
                LogUtil.error(LOGGER, "商品对象json格式化异常:{}", e::getMessage);
            }

        });
        return !bulkRequest.get().hasFailures();
    }


    /**
     * es 父子关系文档联合查询
     *
     * @param index        索引
     * @param parentType   父文档
     * @param childType    子文档
     * @param searchEntity 查询条件
     * @return SearchResponse
     */
    public static SearchResponse multiMatchQueryRelation(String index, String parentType, String childType, SearchEntity searchEntity) {
        LogUtil.info(LOGGER, () -> "index = [" + index + "], " +
                "parentType = [" + parentType + "], childType = [" + childType + "]," +
                " searchEntity = [" + searchEntity + "]");
        SortOrder sortOrder = buildSort(searchEntity);

        QueryBuilder parentQb = QueryBuilders
                .multiMatchQuery(searchEntity.getKeywords(), searchEntity.getFields());
        final TermQueryBuilder childTermQuery =
                QueryBuilders.termQuery(searchEntity.getChildField(), searchEntity.getRegionId());
        // final HasParentQueryBuilder hasParentQueryBuilder = hasParentQuery(parentType, parentQb, Boolean.TRUE);


        //final HasChildQueryBuilder hasChildQueryBuilder = hasChildQuery(childType, childTermQuery, ScoreMode.None);
        final BoolQueryBuilder queryBuilder = boolQuery()
                .must(matchAllQuery())
                .filter(hasParentQuery(parentType,
                        boolQuery().should(parentQb)
                                .filter(hasChildQuery(childType, childTermQuery, ScoreMode.None)),
                        Boolean.TRUE));
        SearchResponse response = client.prepareSearch(index)
                .setTypes(parentType)
                .setQuery(queryBuilder)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addSort(searchEntity.getOrderField(), sortOrder)
                .setFrom(searchEntity.getPage())
                .setSize(searchEntity.getSize())
                .setExplain(true)
                .execute()
                .actionGet();
        LogUtil.info(LOGGER, () -> "elastic relation search index: " + index + " parentType:" + parentType + " childType:" + childType + " successful ");
        return response;
    }


    /**
     * 多字段查询
     *
     * @param index        索引
     * @param type         索引类型
     * @param searchEntity 查询实体
     * @return es查询结果
     */
    public static SearchResponse multiMatchQuery(String index, String type, SearchEntity searchEntity) {
        LogUtil.info(LOGGER, () -> " " + "index = [" + index + "], type = [" + type + "], searchEntity = [" + searchEntity + "]");
        if (searchEntity.getFields().length == 0 || searchEntity.getFields() == null) {
            return null;
        }
        SortOrder sortOrder = buildSort(searchEntity);
        QueryBuilder qb = QueryBuilders
                .multiMatchQuery(searchEntity.getKeywords(), searchEntity.getFields());

        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .addSort(searchEntity.getOrderField(), sortOrder)
                .setFrom(searchEntity.getPage())
                .setSize(searchEntity.getSize())
                .setExplain(true)
                .execute()
                .actionGet();
        LogUtil.info(LOGGER, () -> "elastic  multiMatchQuery search index: " + index + " type:" + type + " successful ");
        return response;

    }


    /**
     * 多字段查询
     *
     * @param index        索引
     * @param type         索引类型
     * @param searchEntity 查询实体
     * @return es查询结果
     */
    public static SearchResponse multiFieldQuery(String index, String type, SearchEntity searchEntity) {
        LogUtil.info(LOGGER, () -> " " + "index = [" + index + "], type = [" + type + "], searchEntity = [" + searchEntity + "]");

        Map<String, Object> fieldMap = searchEntity.getFieldMap();

        if (fieldMap == null || fieldMap.isEmpty()) {
            return null;
        }
        SortOrder sortOrder = buildSort(searchEntity);

        BoolQueryBuilder bqb = boolQuery();

        fieldMap.forEach((key, value) -> bqb.must(matchQuery(key, value)));


        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(bqb)
                .addSort(SortBuilders.scoreSort())
                .addSort(searchEntity.getOrderField(), sortOrder)
                .setFrom(searchEntity.getPage())
                .setSize(searchEntity.getSize())
                .execute()
                .actionGet();
        LogUtil.info(LOGGER, () ->
                "elastic search  by multiFieldQuery index: "
                        + index + " type:" + type + " successful ");
        return response;

    }


    /**
     * 关键词查询 单字段
     *
     * @param index        索引
     * @param type         索引类型
     * @param searchEntity 查询实体
     * @return es查询结果
     */
    public static SearchResponse termQuery1(String index, String type, SearchEntity searchEntity) {
        LogUtil.info(LOGGER, () -> " " + "index = [" + index + "], type = [" + type + "], searchEntity = [" + searchEntity + "]");

        if (StringUtils.isEmpty(searchEntity.getField())) {
            return null;
        }
        SortOrder sortOrder = buildSort(searchEntity);

        QueryBuilder queryBuilder = QueryBuilders
                .termQuery(searchEntity.getField(), searchEntity.getKeywords());
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addSort(searchEntity.getOrderField(), sortOrder)
                .setFrom(searchEntity.getPage())
                .setSize(searchEntity.getSize())
                .execute()
                .actionGet();
        LogUtil.info(LOGGER, () ->
                "elastic termQuery  by  index: "
                        + index + " type:" + type + " successful ");
        return response;

    }

    /**
     * 构建排序信息
     *
     * @param searchEntity 查询实体类
     * @return SortOrder
     */
    private static SortOrder buildSort(SearchEntity searchEntity) {
        SortOrder sortOrder;
        if (Objects.equals(searchEntity.getSortOrder(), OrderByEnum.ASC.toString())) {
            sortOrder = SortOrder.ASC;
        } else if (Objects.equals(OrderByEnum.DESC.toString(), searchEntity.getSortOrder())) {
            sortOrder = SortOrder.DESC;
        } else {
            sortOrder = SortOrder.DESC;
        }
        return sortOrder;
    }


    /**
     * 查询所有数据
     *
     * @param index 索引
     * @param type  类型
     * @return SearchResponse
     */
    public static SearchResponse searchByIndexAndType(String index, String type) {
        return client.prepareSearch(index).setTypes(type).get();
    }

    /**
     * 关闭es 客户端
     */
    public static void shutdown() {
        client.close();
    }
}
