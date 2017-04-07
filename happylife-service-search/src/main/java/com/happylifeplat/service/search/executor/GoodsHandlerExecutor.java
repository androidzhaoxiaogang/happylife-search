package com.happylifeplat.service.search.executor;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.happylifeplat.plugin.mybatis.pager.PageParameter;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.SysProps;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.happylifeplat.service.search.query.GoodsPage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 商品处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 18:15
 * @since JDK 1.8
 */
@Component
public class GoodsHandlerExecutor implements ElasticSearchExecutor {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHandlerExecutor.class);

    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;

    @ApolloConfig
    private Config config;

    @Value("${goods.pageSize:1000}")
    private int goodsPageSize;


    @Override
    public void execute(JobInfo jobInfo) {
        LogUtil.info(LOGGER, () -> "开始建立商品索引,jobInfo = [" + jobInfo.toString() + "]");
        final String index = jobInfo.getIndex();
        final String type = jobInfo.getType();
        final String param = jobInfo.getParam();
        final String updateTime = SysProps.get(param);
        try {
            int currentPage = 1;
            PageParameter pageParameter = new PageParameter();
            pageParameter.setPageSize(goodsPageSize);
            GoodsPage goodsPage = new GoodsPage();
            goodsPage.setUpdateTime(updateTime);
            while (true) {
                pageParameter.setCurrentPage(currentPage);
                goodsPage.setPage(pageParameter);
                final List<GoodsEs> goodsEsList = goodsEsMapper.listPage(goodsPage);
                if (CollectionUtils.isEmpty(goodsEsList)) {
                    return;
                }
                //商品是disable 或者是失效的，需要删除索引
                final Map<Boolean, List<GoodsEs>> booleanListMap = goodsEsList.parallelStream()
                        .collect(Collectors.partitioningBy(goodsEs ->
                                goodsEs.getDisable() || goodsEs.getInvalid()));
                final List<GoodsEs> deleteList = booleanListMap.get(Boolean.TRUE);
                //如果有删除的，那么就删除索引
                if (CollectionUtils.isNotEmpty(deleteList)) {
                    final List<String> ids = deleteList.stream()
                            .map(GoodsEs::getId)
                            .collect(Collectors.toList());
                    try {
                        final boolean bulkDelete;
                        bulkDelete =
                                ElasticSearchClient.bulkDelete(index, type, ids);
                        if (bulkDelete) {
                            LogUtil.info(LOGGER, "成功删除索引，索引id为：{}", () -> ids);
                        }
                    } catch (Exception e) {
                        LogUtil.error(LOGGER, "删除索引失败：{}", e::getMessage);
                    }

                }
                final List<GoodsEs> createList = booleanListMap.get(Boolean.FALSE);
                final boolean success =
                        ElasticSearchClient.bulkGoodsIndex(index, type, createList);
                if (success) {
                    LogUtil.info(LOGGER, () -> "商品建立索引成功！");
                }
                pageParameter = goodsPage.getPage();
                final int totalPage = pageParameter.getTotalPage();
                if (totalPage == currentPage) {
                    break;
                }
                currentPage++;
            }
            SysProps.update(param, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(LOGGER, "商品建立索引异常：{},", e::getMessage);
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
