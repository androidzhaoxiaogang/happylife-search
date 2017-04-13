package com.happylifeplat.service.search.event;

import com.happylifeplat.service.search.entity.GoodsEs;
import com.happylifeplat.service.search.event.bean.ChangeEvent;
import com.happylifeplat.service.search.query.GoodsPage;
import org.springframework.stereotype.Component;

import java.util.List;

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




    @Override
    public List<GoodsEs> dataHandler(ChangeEvent changeEvent, GoodsPage goodsPage) {
        return null;
    }

    @Override
    public Class getEsHandler() {
        return null;
    }
}
