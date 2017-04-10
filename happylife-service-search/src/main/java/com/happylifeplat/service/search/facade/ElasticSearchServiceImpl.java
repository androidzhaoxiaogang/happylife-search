package com.happylifeplat.service.search.facade;


import com.google.common.collect.Lists;
import com.happylifeplat.facade.search.entity.SearchRequest;
import com.happylifeplat.facade.search.enums.ResultCodeEnum;
import com.happylifeplat.facade.search.result.EntityResult;
import com.happylifeplat.facade.search.result.SearchResult;
import com.happylifeplat.facade.search.enums.SortTypeEnum;
import com.happylifeplat.facade.search.exception.SearchException;
import com.happylifeplat.facade.search.service.ElasticSearchService;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.RegionIdUtils;
import com.happylifeplat.service.search.query.SearchEntity;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * es搜索服务
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:42
 * @since JDK 1.8
 */
@Service("elasticSearchService")
public class ElasticSearchServiceImpl implements ElasticSearchService {


    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);


    /**
     * es 查询接口
     *
     * @param searchRequest 查询参数
     * @return json格式字符串
     * @throws SearchException 异常信息
     */
    @Override
    public SearchResult search(SearchRequest searchRequest) throws SearchException {
        LogUtil.info(LOGGER, () -> "查询参数：searchRequest = [" + searchRequest.toString() + "]");
        SearchEntity searchEntity = buildSearchEntity(searchRequest);
        final SearchResponse searchResponse = ElasticSearchClient
                .multiMatchQueryRelation(ConstantSearch.INDEX, ConstantSearch.GOODS_TYPE, ConstantSearch.REGION_TYPE, searchEntity);
        return buildResult(searchResponse);
    }

    /**
     * 根据es返回的数据构造查询结果
     *
     * @param response es  response
     * @return SearchResult
     * @throws SearchException 异常信息
     */
    private SearchResult buildResult(SearchResponse response) throws SearchException {
        if (response == null || response.getHits() == null) {
            throw new SearchException("未查询到到任何数据！");
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setCode(ResultCodeEnum.SUCCEED.getCode());
        searchResult.setMessage(ResultCodeEnum.SUCCEED.getMessage());
        try {
            final ArrayList<SearchHit> searchHits = Lists.newArrayList(response.getHits().getHits());
            if (CollectionUtils.isNotEmpty(searchHits)) {
                final List<EntityResult> entityResultList = searchHits.stream().filter(Objects::nonNull)
                        .map(searchHit -> {
                            final Map<String, Object> source = searchHit.getSource();
                            EntityResult entityResult = new EntityResult();
                            if (source != null) {
                                entityResult.setId(String.valueOf(source.get("id")));
                                entityResult.setBarcode(String.valueOf(source.get("barcode")));
                                entityResult.setCode(String.valueOf(source.get("code")));
                                entityResult.setCostPrice((BigDecimal) source.get("cost_price"));
                                entityResult.setName(String.valueOf(source.get("name")));
                            }
                            return entityResult;
                        }).collect(Collectors.toList());
                searchResult.setEntityResultList(entityResultList);
            }
        } catch (Exception e) {
            searchResult.setCode(ResultCodeEnum.FAIL.getCode());
            searchResult.setMessage(ResultCodeEnum.FAIL.getMessage());
            LogUtil.error(LOGGER, "数据转换异常！：{}", e::getMessage);
            throw new SearchException(String.join("查询发生异常信息", e.getMessage()));
        }
        return searchResult;
    }

    /**
     * 根据前台搜索信息 ，构建查询条件
     *
     * @param searchRequest 前台搜索信息
     * @return SearchEntity es查询条件
     */
    private SearchEntity buildSearchEntity(SearchRequest searchRequest) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setKeywords(searchRequest.getKeywords());
        //设置多个字段查询 商品文档 父文档字段
        String[] fields = new String[]{"name"};
        searchEntity.setFields(fields);

        /**
         * 子文档字段  termQuery
         */
        searchEntity.setChildField("administrative_region_id");
        searchEntity.setRegionId(RegionIdUtils.convert(searchRequest.getRegionId()));

        /**
         * 分页信息
         */
        searchEntity.setPage(searchRequest.getPage());
        searchEntity.setSize(searchRequest.getSize());

        /**
         * 排序
         */
        searchEntity.setSortOrder(searchRequest.getSort());
        if (searchRequest.getSortType().equals(SortTypeEnum.PRICE.toString())) {
            searchEntity.setOrderField("price");
        } else {
            searchEntity.setOrderField("index");
        }

        return searchEntity;
    }
}
