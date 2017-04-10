package com.happylifeplat.service.search.executor;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.plugin.mybatis.pager.PageParameter;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.constant.ConstantSearch;
import com.happylifeplat.service.search.entity.EsConfig;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.executor.handler.ConcurrentHandler;
import com.happylifeplat.service.search.executor.handler.GoodsHandler;
import com.happylifeplat.service.search.executor.handler.RegionHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.SpringBeanUtils;
import com.happylifeplat.service.search.helper.SysProps;
import com.happylifeplat.service.search.mapper.EsConfigMapper;
import com.happylifeplat.service.search.mapper.ProviderRegionEsMapper;
import com.happylifeplat.service.search.query.RegionPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 供应商区域处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 9:40
 * @since JDK 1.8
 */
@Component
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

    @ApolloConfig
    private Config config;

    @Value("${region.pageSize:1000}")
    private int regionPageSize;

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
            int currentPage = 1;
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

                /**
                 * 封装成handlerEntity 异步提交
                 */
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
            }
            updateLastTime();
        } catch (Exception e) {
            LogUtil.error(LOGGER, "服务区域建立索引失败：{},", e::getMessage);
        }

    }


    private void updateLastTime() {
        final EsConfig byType = getByType(EsConfigTypeEnum.REGION.getCode());
        byType.setLastTime(new Date());
        esConfigMapper.update(byType);

    }

    private String getLastTime() {
        final EsConfig byType = getByType(EsConfigTypeEnum.REGION.getCode());
        if (Objects.nonNull(byType)) {
            return DATE_FORMAT.format(byType.getLastTime());
        }
        return ConstantSearch.DEFULT_LAST_TIME;
    }

    private EsConfig getByType(int type) {
        return esConfigMapper.getByType(type);
    }

    @ApolloConfigChangeListener("application")
    private void pageSizeChangeHandler(ConfigChangeEvent changeEvent) {
        LogUtil.info(LOGGER, () -> " " + "changeEvent = [" + changeEvent + "]");
        if (changeEvent.isChanged("region.pageSize")) {
            regionPageSize = config.getIntProperty("region.pageSize", regionPageSize);
            LogUtil.info(LOGGER, "region.pageSize {}", () -> regionPageSize);
        }
    }
}
