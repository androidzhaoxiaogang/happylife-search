package com.happylifeplat.service.search.event;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.executor.handler.update.RegionUpdateHandler;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.happylifeplat.service.search.mapper.ProviderRegionEsMapper;
import com.happylifeplat.service.search.query.GoodsPage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 供应商区域更改处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 10:15
 * @since JDK 1.8
 */
@Component
public class RegionChangeEvent extends AbstractChangeEvent {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegionChangeEvent.class);

    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;

    @Autowired(required = false)
    private ProviderRegionEsMapper providerRegionEsMapper;


    @Override
    public List<GoodsEs> dataHandler(ChangeEvent changeEvent, GoodsPage goodsPage) {
        final String providerId = (String) changeEvent.getData();
        goodsPage.setProviderId(providerId);
        final List<GoodsEs> goodsEsList = goodsEsMapper.listByProviderIdPage(goodsPage);
        if (CollectionUtils.isNotEmpty(goodsEsList)) {
            //设置商品对应的服务区域
            return goodsEsList.stream().filter(Objects::nonNull)
                    .map(goodsEs -> {
                        final List<ProviderRegionEs> providerRegionEsList =
                                providerRegionEsMapper.listByProviderId(providerId);
                        goodsEs.setRegions(providerRegionEsList);
                        return goodsEs;
                    }).collect(Collectors.toList());

        }
        return null;
    }

    @Override
    public Class getEsHandler() {
        return RegionUpdateHandler.class;
    }

}
