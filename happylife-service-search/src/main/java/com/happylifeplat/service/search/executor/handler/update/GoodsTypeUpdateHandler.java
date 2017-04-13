package com.happylifeplat.service.search.executor.handler.update;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.entity.HandlerEntity;
import com.happylifeplat.service.search.executor.handler.ElasticSearchHandler;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  商品分类更新处理 ,文档字段更新
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/13 11:47
 * @since JDK 1.8
 */
public class GoodsTypeUpdateHandler implements ElasticSearchHandler<GoodsEs> {


    /**
     * elastic 构建索引处理接口
     *
     * @param handlerEntity 实体类
     */
    @Override
    public void action(HandlerEntity<GoodsEs> handlerEntity) {

    }
}
