package com.happylifeplat.service.search.executor;

import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.JobInfo;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.helper.SysProps;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHandlerExecutor.class);

    @Override
    public void execute(JobInfo jobInfo) {
        LogUtil.debug(LOGGER, () -> "开始建立商品索引,jobInfo = [" + jobInfo.toString() + "]");
        final String index = jobInfo.getIndex();
        final String type = jobInfo.getType();
        final String param = jobInfo.getParam();
        final String updateTime = SysProps.get(param);
        try {
            final List<GoodsEs> goodsEsList = goodsEsMapper.listByUpdateTime(updateTime);
            if(CollectionUtils.isNotEmpty(goodsEsList)){
                //商品是disable 或者是失效的，需要删除索引
                final Map<Boolean, List<GoodsEs>> booleanListMap = goodsEsList.parallelStream()
                        .collect(Collectors.partitioningBy(goodsEs ->
                                goodsEs.getDisable() || goodsEs.getInvalid()));
                final List<GoodsEs> deleteList = booleanListMap.get(Boolean.TRUE);
                //如果有删除的，那么就删除索引
                if(CollectionUtils.isNotEmpty(deleteList)){
                    final List<String> ids = deleteList.stream().map(GoodsEs::getId).collect(Collectors.toList());
                    final boolean bulkDelete = ElasticSearchClient.bulkDelete(index, type, ids);
                    if(bulkDelete){
                        LogUtil.info(LOGGER,"成功删除索引，索引id为：{}",() -> ids);
                    }
                }
                final List<GoodsEs> createList = booleanListMap.get(Boolean.FALSE);
                final boolean success =
                        ElasticSearchClient.bulkGoodsIndex(index, type, createList);
                if (success) {
                    SysProps.update(param, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                    LogUtil.info(LOGGER, () -> "商品建立索引成功！");
                }

            }
        } catch (Exception e) {
            LogUtil.error(LOGGER,"商品建立索引异常：{},", e::getMessage);
        }
    }
}
