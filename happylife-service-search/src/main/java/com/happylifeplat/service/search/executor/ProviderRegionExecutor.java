package com.happylifeplat.service.search.executor;

import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.entity.EsConfig;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.executor.handler.ConcurrentHandler;
import com.happylifeplat.service.search.executor.handler.RegionHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.mapper.EsConfigMapper;
import com.happylifeplat.service.search.mapper.ProviderRegionEsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 供应商区域处理 废弃
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 9:40
 * @since JDK 1.8
 */
@Component
@Deprecated
public class ProviderRegionExecutor implements ElasticSearchExecutor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsExecutor.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired(required = false)
    private ProviderRegionEsMapper providerRegionEsMapper;

    @Autowired(required = false)
    private EsConfigMapper esConfigMapper;

    @Autowired
    private ConcurrentHandler concurrentHandler;

    @Override
    public void execute(JobInfo jobInfo) {
        LogUtil.debug(LOGGER, () -> " 开始执行创建服务区域索引："
                + "jobInfo = [" + jobInfo.toString() + "]");
        final String index = jobInfo.getIndex();
        final String type = jobInfo.getType();
        final String createTime = getLastTime();
        try {
            //区域不能用分页查询
            final List<ProviderRegionEs> providerRegionEsList =
                    providerRegionEsMapper.listByCreateTime(createTime);
            if (CollectionUtils.isEmpty(providerRegionEsList)) {
                updateLastTime();
                return;
            }
            LogUtil.info(LOGGER,"当前区域数据数量为：{}条",providerRegionEsList.size());
            /**
             * 封装成handlerEntity 异步提交
             */
            CompletableFuture.supplyAsync(() -> {
                HandlerEntity<ProviderRegionEs> handlerEntity = new HandlerEntity<>();
                handlerEntity.setType(EsConfigTypeEnum.REGION.getCode());
                handlerEntity.setHandler(RegionHandler.class);
                handlerEntity.setIndex(index);
                handlerEntity.setIndexType(type);
                handlerEntity.setData(providerRegionEsList);
                return handlerEntity;
            }).thenAccept(concurrentHandler::submit);
            updateLastTime();
          /*  int currentPage = 1;
            PageParameter pageParameter = new PageParameter();
            pageParameter.setPageSize(regionPageSize);
            RegionPage regionPage = new RegionPage();
            regionPage.setCreateTime(createTime);
            while (true) {
                pageParameter.setCurrentPage(currentPage);
                regionPage.setPage(pageParameter);
                List<ProviderRegionEs> providerRegionEs =
                        providerRegionEsMapper.listPage(regionPage);
                if (CollectionUtils.isEmpty(providerRegionEs)) {
                    return;
                }

                *//**
                 * 封装成handlerEntity 异步提交
                 *//*
                CompletableFuture.supplyAsync(() -> {
                    HandlerEntity<ProviderRegionEs> handlerEntity = new HandlerEntity<>();
                    handlerEntity.setType(EsConfigTypeEnum.REGION.getCode());
                    handlerEntity.setHandler(RegionHandler.class);
                    handlerEntity.setIndex(index);
                    handlerEntity.setIndexType(type);
                    handlerEntity.setData(providerRegionEs);
                    return handlerEntity;
                }).thenAccept(concurrentHandler::submit);
                pageParameter = regionPage.getPage();
                final int totalPage = pageParameter.getTotalPage();
                if (totalPage == currentPage) {
                    break;
                }
                currentPage++;
            }*/

        } catch (Exception e) {
            LogUtil.error(LOGGER, "服务区域建立索引失败：{},", e::getMessage);
        }

    }


    /**
     * 更新最后操作时间
     */
    private void updateLastTime() {
        final EsConfig byType = getByType(EsConfigTypeEnum.REGION.getCode());
        byType.setLastTime(new Date());
        esConfigMapper.update(byType);

    }

    /**
     * 获取上一次操作时间
     * @return byType.getLastTime()
     */
    private String getLastTime() {
        final EsConfig byType = getByType(EsConfigTypeEnum.REGION.getCode());
        if (Objects.nonNull(byType)) {
            return DATE_FORMAT.format(byType.getLastTime());
        }
        return ConstantSearch.DEFAULT_LAST_TIME;
    }

    private EsConfig getByType(int type) {
        return esConfigMapper.getByType(type);
    }

}
