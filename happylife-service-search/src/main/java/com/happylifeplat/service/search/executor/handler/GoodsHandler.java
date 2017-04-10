package com.happylifeplat.service.search.executor.handler;

import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.helper.LogUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 商品构建索引处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/10 10:14
 * @since JDK 1.8
 */
public class GoodsHandler implements ElasticSearchHandler<GoodsEs> {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHandler.class);

    @Override
    public void action(HandlerEntity<GoodsEs> handlerEntity) {
        if (Objects.nonNull(handlerEntity)) {
            if (Objects.equals(handlerEntity.getType(), EsConfigTypeEnum.GOODS.getCode())) {
                final List<GoodsEs> goodsEsList = handlerEntity.getData();
                final String index = handlerEntity.getIndex();
                final String indexType = handlerEntity.getIndexType();
                if (CollectionUtils.isEmpty(goodsEsList)) {
                    return;
                }
                //商品是disable 或者是失效的，需要删除索引
                try {
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
                                    ElasticSearchClient.bulkDelete(index, indexType, ids);
                            if (bulkDelete) {
                                LogUtil.info(LOGGER, "成功删除索引，索引id为：{}", () -> ids);
                            }
                        } catch (Exception e) {
                            LogUtil.error(LOGGER, "删除索引失败：{}", e::getMessage);
                        }

                    }
                    final List<GoodsEs> createList = booleanListMap.get(Boolean.FALSE);
                    final boolean success =
                            ElasticSearchClient.bulkGoodsIndex(index, indexType, createList);
                    if (success) {
                        LogUtil.info(LOGGER, () -> "商品建立索引成功！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.error(LOGGER, "商品建立索引异常：{},", e::getMessage);
                }
            }
        }
    }

}
