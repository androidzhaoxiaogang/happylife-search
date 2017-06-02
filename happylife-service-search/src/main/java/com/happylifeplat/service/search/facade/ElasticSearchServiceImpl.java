package com.happylifeplat.service.search.facade;


import com.google.common.collect.Lists;
import com.happylifeplat.commons.validator.BeanValidator;
import com.happylifeplat.commons.validator.ResponseError;
import com.happylifeplat.facade.search.entity.SearchRequest;
import com.happylifeplat.facade.search.enums.ResultCodeEnum;
import com.happylifeplat.facade.search.enums.SortTypeEnum;
import com.happylifeplat.facade.search.exception.SearchException;
import com.happylifeplat.facade.search.result.EntityResult;
import com.happylifeplat.facade.search.result.SearchResult;
import com.happylifeplat.facade.search.service.ElasticSearchService;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.event.GoodsTypeChangeEvent;
import com.happylifeplat.service.search.event.ProviderChangeEvent;
import com.happylifeplat.service.search.event.RegionChangeEvent;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.RegionIdUtils;
import com.happylifeplat.service.search.query.SearchEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired(required = false)
    private BeanValidator beanValidator;

    @Autowired(required = false)
    private RegionChangeEvent regionChangeEvent;

    @Autowired(required = false)
    private GoodsTypeChangeEvent goodsTypeChangeEvent;

    @Autowired(required = false)
    private ProviderChangeEvent providerChangeEvent;

    /**
     * es 查询接口
     *
     * @param searchRequest 查询参数
     * @return json格式字符串
     * @throws SearchException 异常信息
     */
    @Override
    public SearchResult search(SearchRequest searchRequest) throws SearchException {
        final ResponseError responseError = beanValidator.validator(searchRequest);
        if (!responseError.isSuccessful()) {
            SearchResult searchResult = new SearchResult();
            searchResult.setCode(responseError.getCode());
            searchResult.setMessage(ResultCodeEnum.getMessageByCode(responseError.getCode()));
            return searchResult;
        }
        LogUtil.info(LOGGER, () -> "查询参数：searchRequest = [" + searchRequest.toString() + "]");
        SearchEntity searchEntity = buildSearchEntity(searchRequest);
        final SearchResponse searchResponse = ElasticSearchClient
                .multiFieldQuery(ConstantSearch.INDEX, ConstantSearch.GOODS_TYPE, searchEntity);
        return buildResult(searchResponse);
    }

    /**
     * 供应商更改区域 触发事件，会rebuild es索引，请异步调用
     *
     * @param providerId 供应商id
     */
    @Override
    public void fireRegionChangeEvent(String providerId) {
        if (StringUtils.isNoneBlank(providerId)) {
            regionChangeEvent.fireChangeEvent(new ChangeEvent("RegionChange", providerId));
        }
    }

    /**
     * 商品类型更改触发事件接口
     * 会rebuild es索引中的商品类型名称信息，请异步调用
     *
     * @param goodsTypeId 商品类型id
     */
    @Override
    public void fireGoodsTypeChangeEvent(String goodsTypeId) {
        if (StringUtils.isNoneBlank(goodsTypeId)) {
            goodsTypeChangeEvent.fireChangeEvent(new ChangeEvent("GoodsTypeChange", goodsTypeId));
        }
    }

    /**
     * 供应商信息更改触发事件
     * 会rebuild es索引中的供应商名称（暂时）信息，请异步调用
     *
     * @param providerId 供应商id
     */
    @Override
    public void fireProviderChangeEvent(String providerId) {
        if (StringUtils.isNoneBlank(providerId)) {
            providerChangeEvent.fireChangeEvent(new ChangeEvent("ProviderChange", providerId));
        }
    }

    /**
     * 根据es返回的数据构造查询结果
     *
     * @param response es  response
     * @return SearchResult
     * @throws SearchException 异常信息
     */
    private SearchResult buildResult(SearchResponse response) throws SearchException {
        if (response == null || response.getHits() == null || response.getHits().getHits()==null) {
            throw new SearchException("未查询到到任何数据！");
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setCode(ResultCodeEnum.SUCCEED.getCode());
        searchResult.setMessage(ResultCodeEnum.SUCCEED.getMessage());
        try {
            final ArrayList<SearchHit> searchHits = Lists.newArrayList(response.getHits().getHits());
            if (CollectionUtils.isNotEmpty(searchHits)) {
                final List<EntityResult> entityResultList = searchHits.stream().filter(Objects::nonNull)
                        .map(this::buildEntityResultBySearchHit).collect(Collectors.toList());
                searchResult.setEntityResultList(entityResultList);
            }
        } catch (Exception e) {
            searchResult.setCode(ResultCodeEnum.FAIL.getCode());
            searchResult.setMessage(ResultCodeEnum.FAIL.getMessage());
            LogUtil.error(LOGGER, "数据转换异常！：{}", e::getMessage);
            throw new SearchException(String.join("查询发生异常信息", e.getMessage()));
        }
        LogUtil.info(LOGGER, () -> "返回数据：searchResult = [" + searchResult+ "]");
        return searchResult;
    }


    /**
     * 构建返回对象实体
     * @param searchHit es返回的实体
     * @return EntityResult
     */
    private EntityResult buildEntityResultBySearchHit(SearchHit searchHit){
        final Map<String, Object> source = searchHit.getSource();
        EntityResult entityResult = new EntityResult();
        if (source != null) {
            entityResult.setId(String.valueOf(source.get("id")));
            entityResult.setName(String.valueOf(source.get("name")));
            entityResult.setGoodsCategoryId(String.valueOf(source.get("goodsCategoryId")));
            entityResult.setGoodsTypeId(String.valueOf(source.get("goodsTypeId")));
            entityResult.setGoodsTypeName(String.valueOf(source.get("goodsTypeName")));
            entityResult.setProviderId(String.valueOf(source.get("providerId")));
            entityResult.setProviderName(String.valueOf(source.get("providerName")));
            entityResult.setThumbnail(String.valueOf(source.get("thumbnail")));
            if(Objects.nonNull(source.get("price"))){
                entityResult.setPrice(new BigDecimal(String.valueOf(source.get("price"))));
            }
            if(Objects.nonNull(source.get("costPrice"))){
                entityResult.setPrice(new BigDecimal(String.valueOf(source.get("costPrice"))));
            }
            if(Objects.nonNull(source.get("originalPrice"))){
                entityResult.setPrice(new BigDecimal(String.valueOf(source.get("originalPrice"))));
            }
            entityResult.setBarcode(String.valueOf(source.get("barcode")));
            entityResult.setCode(String.valueOf(source.get("code")));
        }
        return  entityResult;
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

        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", searchRequest.getKeywords());
        fieldMap.put("regions.regionId", RegionIdUtils.convert(searchRequest.getRegionId()));

        searchEntity.setFieldMap(fieldMap);

        searchEntity.setPage(searchRequest.getPage());
        searchEntity.setSize(searchRequest.getSize());

        /*
          排序
         */
        searchEntity.setSortOrder(searchRequest.getSort());
        if (Objects.equals(searchRequest.getSortType(), SortTypeEnum.PRICE.toString())) {
            searchEntity.setOrderField("price");
        } else {
            searchEntity.setOrderField("price");
        }

        return searchEntity;
    }
}
