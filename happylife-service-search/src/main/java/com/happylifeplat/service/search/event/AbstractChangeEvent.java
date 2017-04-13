package com.happylifeplat.service.search.event;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.plugin.mybatis.pager.PageParameter;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.executor.handler.ConcurrentHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.query.GoodsPage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 更改事件模板处理类，其他类请继承此类
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/13 10:56
 * @since JDK 1.8
 */
@Component
public abstract class AbstractChangeEvent implements ApplicationContextChangeEvent {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionChangeEvent.class);

    public abstract List<GoodsEs> dataHandler(ChangeEvent changeEvent, GoodsPage goodsPage);

    public abstract Class getEsHandler();

    @Autowired
    private ConcurrentHandler concurrentHandler;

    @ApolloConfig
    private Config config;

    @Value("${goods.pageSize}")
    private int goodsPageSize;

    @Override
    public void fireChangeEvent(ChangeEvent changeEvent) {
        LogUtil.info(LOGGER, " " + "changeEvent = [" + changeEvent + "]");
        PageParameter pageParameter = new PageParameter();
        pageParameter.setPageSize(goodsPageSize);
        GoodsPage goodsPage = new GoodsPage();
        int currentPage = 1;
        while (true) {
            pageParameter.setCurrentPage(currentPage);
            goodsPage.setPage(pageParameter);
            final List<GoodsEs> goodsEsList = dataHandler(changeEvent, goodsPage);
            if (CollectionUtils.isEmpty(goodsEsList)) {
                break;
            }
            CompletableFuture.supplyAsync(() -> {
                HandlerEntity<GoodsEs> handlerEntity = new HandlerEntity<>();
                handlerEntity.setType(EsConfigTypeEnum.GOODS.getCode());
                handlerEntity.setHandler(getEsHandler());
                handlerEntity.setIndex(ConstantSearch.INDEX);
                handlerEntity.setIndexType(ConstantSearch.GOODS_TYPE);
                handlerEntity.setData(goodsEsList);
                return handlerEntity;
            }).thenAccept(concurrentHandler::submit);
            //分页处理
            pageParameter = goodsPage.getPage();
            final int totalPage = pageParameter.getTotalPage();
            if (totalPage == currentPage) {
                break;
            }
            currentPage++;
        }
    }
}
