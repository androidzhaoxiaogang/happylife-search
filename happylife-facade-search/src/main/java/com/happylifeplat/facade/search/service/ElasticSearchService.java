package com.happylifeplat.facade.search.service;

import com.happylifeplat.facade.search.entity.SearchRequest;
import com.happylifeplat.facade.search.exception.SearchException;
import com.happylifeplat.facade.search.result.SearchResult;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  ElasticSearch  dubbo接口
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/24 15:16
 * @since JDK 1.8
 */
public interface ElasticSearchService {

    /**
     * es 查询接口
     *
     * @param searchRequest 查询参数
     * @return SearchResult 返回对象
     * @throws SearchException 异常信息
     */
    SearchResult search(SearchRequest searchRequest) throws SearchException;


    /**
     * 供应商更改区域 触发事件，
     * 会rebuild es索引中的区域信息，请异步调用
     * @param providerId 供应商id
     */
    void fireRegionChangeEvent(String providerId);


    /**
     *  商品类型更改触发事件接口
     *  会rebuild es索引中的商品类型名称信息，请异步调用
     * @param goodsTypeId 商品类型id
     */
    void fireGoodsTypeChangeEvent(String goodsTypeId);

    /**
     *  供应商信息更改触发事件
     *   会rebuild es索引中的供应商名称（暂时）信息，请异步调用
     * @param providerId 供应商id
     */
    void fireProviderChangeEvent(String providerId);

}
