package com.happylifeplat.service.search.event;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.executor.handler.update.GoodsTypeUpdateHandler;
import com.happylifeplat.service.search.helper.LogUtil;
import com.happylifeplat.service.search.mapper.GoodsEsMapper;
import com.happylifeplat.service.search.mapper.GoodsTypeEsMapper;
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
 *  商品分类更改触发事件
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/13 11:44
 * @since JDK 1.8
 */
@Component
public class GoodsTypeChangeEvent extends  AbstractChangeEvent {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsTypeChangeEvent.class);


    @Autowired(required = false)
    private GoodsEsMapper goodsEsMapper;

    @Autowired(required = false)
    private GoodsTypeEsMapper goodsTypeEsMapper;

    @Override
    public List<GoodsEs> dataHandler(ChangeEvent changeEvent, GoodsPage goodsPage) {
        LogUtil.info(LOGGER, " " + "changeEvent = [" + changeEvent + "], goodsPage = [" + goodsPage + "]");
        final String goodsTypeId = (String) changeEvent.getData();
        goodsPage.setGoodsTypeId(goodsTypeId);
        final List<GoodsEs> goodsEsList = goodsEsMapper.listByGoodsTypeId(goodsPage);
        if (CollectionUtils.isNotEmpty(goodsEsList)) {
            //设置类型名称
            return goodsEsList.stream().filter(Objects::nonNull)
                    .map(goodsEs -> {
                        final String goodsTypeName = goodsTypeEsMapper.getNameById(goodsEs.getGoodsTypeId());
                        goodsEs.setGoodsTypeName(goodsTypeName);
                        return goodsEs;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Class getEsHandler() {
        return GoodsTypeUpdateHandler.class;
    }
}
