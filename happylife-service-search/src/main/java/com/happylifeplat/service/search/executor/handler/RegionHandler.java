package com.happylifeplat.service.search.executor.handler;

import com.happylifeplat.facade.search.enums.EsConfigTypeEnum;
import com.happylifeplat.service.search.client.ElasticSearchClient;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
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
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/10 10:15
 * @since JDK 1.8
 */
@Deprecated
public class RegionHandler implements  ElasticSearchHandler<ProviderRegionEs> {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHandler.class);

    @Override
    public void action(HandlerEntity<ProviderRegionEs> handlerEntity) {

        if (Objects.nonNull(handlerEntity)) {
            if (Objects.equals(handlerEntity.getType(), EsConfigTypeEnum.REGION.getCode())) {
                final List<ProviderRegionEs> providerRegionEsList = handlerEntity.getData();
                final String index = handlerEntity.getIndex();
                final String indexType = handlerEntity.getIndexType();
                if(CollectionUtils.isEmpty(providerRegionEsList)){
                    return;
                }
                try {
                    final List<String> providerIds = providerRegionEsList.parallelStream()
                            .map(ProviderRegionEs::getProviderId)
                            .distinct().collect(Collectors.toList());
                    //先根据供应商id 删除索引
                    ElasticSearchClient.bulkDeleteRegion(index, providerIds);
                    final boolean success = ElasticSearchClient
                            .bulkRegionIndex(index, indexType, providerRegionEsList);
                    if (success) {
                        LogUtil.debug(LOGGER, () -> "服务区域建立索引成功！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.error(LOGGER, "服务区域建立索引失败：{},", e::getMessage);
                }
            }
        }
    }
}
