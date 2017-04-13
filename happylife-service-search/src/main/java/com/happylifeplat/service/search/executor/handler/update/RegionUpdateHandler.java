package com.happylifeplat.service.search.executor.handler.update;

import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.executor.handler.ElasticSearchHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  文档区域更新处理,文档字段更新
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 17:15
 * @since JDK 1.8
 */
public class RegionUpdateHandler implements ElasticSearchHandler<GoodsEs> {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionUpdateHandler.class);
    /**
     * elastic 构建索引处理接口
     *
     * @param handlerEntity 实体类
     */
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
                try {
                    final List<String> ids = goodsEsList.parallelStream().filter(Objects::nonNull)
                            .map(GoodsEs::getId).collect(Collectors.toList());
                    final List<ProviderRegionEs> regions = goodsEsList.get(0).getRegions();
                    final boolean success = ElasticSearchClient.updateGoodsRegion(index, indexType, ids, regions);
                    if (success) {
                        LogUtil.info(LOGGER, () -> "批量更新商品区域");
                    }
                } catch (Exception e) {
                    LogUtil.error(LOGGER, "更新商品区域异常：{},", e::getMessage);
                }
            }
        }
    }
}
