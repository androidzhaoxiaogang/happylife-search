package com.happylifeplat.service.search.event;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.plugin.mybatis.pager.PageParameter;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.executor.handler.ConcurrentHandler;
import com.happylifeplat.service.search.executor.handler.GoodsHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.happylifeplat.service.search.mapper.ProviderRegionEsMapper;
import com.happylifeplat.service.search.query.GoodsPage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  供应商区域更改处理
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 10:15
 * @since JDK 1.8
 */
@Component
public class RegionChangeEvent implements  CommonChangeEvent{


    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionChangeEvent.class);

    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;


    @Autowired
    private ConcurrentHandler concurrentHandler;

    @Autowired(required = false)
    private ProviderRegionEsMapper providerRegionEsMapper;

    @ApolloConfig
    private Config config;

    @Value("${goods.pageSize}")
    private int goodsPageSize;

    /**
     * 触发更改事件接口
     *
     * @param changeEvent 事件实体类
     */
    @Override
    public void fireChangeEvent(ChangeEvent changeEvent) {
        LogUtil.info(LOGGER, " " + "changeEvent = [" + changeEvent + "]");
        final String providerId = (String) changeEvent.getData();
        int currentPage = 1;
        PageParameter pageParameter = new PageParameter();
        pageParameter.setPageSize(goodsPageSize);
        GoodsPage goodsPage = new GoodsPage();
        goodsPage.setProviderId(providerId);
        while (true) {
            pageParameter.setCurrentPage(currentPage);
            goodsPage.setPage(pageParameter);
            final List<GoodsEs> goodsEsList = goodsEsMapper.listByProviderIdPage(goodsPage);
            if (CollectionUtils.isEmpty(goodsEsList)) {
                break;
            } else {
                //设置商品对应的服务区域
                final List<GoodsEs> esList = goodsEsList.parallelStream().filter(Objects::nonNull)
                        .map(goodsEs -> {
                            final List<ProviderRegionEs> providerRegionEsList =
                                    providerRegionEsMapper.listByProviderId(providerId);
                            goodsEs.setRegions(providerRegionEsList);
                            return goodsEs;
                        }).collect(Collectors.toList());
                CompletableFuture.supplyAsync(() -> {
                    HandlerEntity<GoodsEs> handlerEntity = new HandlerEntity<>();
                    handlerEntity.setType(EsConfigTypeEnum.GOODS.getCode());
                    handlerEntity.setHandler(GoodsHandler.class);
                    handlerEntity.setIndex(ConstantSearch.INDEX);
                    handlerEntity.setIndexType(ConstantSearch.GOODS_TYPE);
                    handlerEntity.setData(esList);
                    return handlerEntity;
                }).thenAccept(concurrentHandler::submit);
            }
            LogUtil.info(LOGGER, changeEvent.getEventName()+",当前处理页数为：{}", currentPage);
            //分页处理
            pageParameter = goodsPage.getPage();
            final int totalPage = pageParameter.getTotalPage();
            if (totalPage == currentPage) {
                break;
            }
            currentPage++;
        }
    }


    @ApolloConfigChangeListener("application")
    private void pageSizeChangeHandler(ConfigChangeEvent changeEvent) {
        LogUtil.info(LOGGER, () -> " " + "changeEvent = [" + changeEvent + "]");
        if (changeEvent.isChanged("goods.pageSize")) {
            goodsPageSize = config.getIntProperty("goods.pageSize", goodsPageSize);
            LogUtil.info(LOGGER, "goods.pageSize {}", () -> goodsPageSize);
        }
    }
}
