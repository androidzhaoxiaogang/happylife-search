package com.happylifeplat.service.search.event;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.executor.handler.update.ProviderUpdateHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.happylifeplat.service.search.mapper.ProviderEsMapper;
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
 * 供应商更改事件处理
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/13 11:45
 * @since JDK 1.8
 */
@Component
public class ProviderChangeEvent extends AbstractChangeEvent {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderChangeEvent.class);

    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;

    @Autowired(required = false)
    private ProviderEsMapper providerEsMapper;

    @Override
    public List<GoodsEs> dataHandler(ChangeEvent changeEvent, GoodsPage goodsPage) {
        LogUtil.info(LOGGER, " " + "changeEvent = [" + changeEvent + "], goodsPage = [" + goodsPage + "]");
        final String providerId = (String) changeEvent.getData();
        goodsPage.setProviderId(providerId);
        final List<GoodsEs> goodsEsList = goodsEsMapper.listByProviderIdPage(goodsPage);
        if (CollectionUtils.isNotEmpty(goodsEsList)) {
            //设置供应商名称
            return goodsEsList.stream().filter(Objects::nonNull)
                    .map(goodsEs -> {
                        final String providerName = providerEsMapper.getNameById(goodsEs.getProviderId());
                        goodsEs.setProviderName(providerName);
                        return goodsEs;
                    }).collect(Collectors.toList());
        }
        return null;

    }

    @Override
    public Class getEsHandler() {
        return ProviderUpdateHandler.class;
    }
}
