package com.happylifeplat.service.search.mapper;

import com.happylifeplat.service.search.entity.GoodsEs;

import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  获取商品dao
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:16
 * @since JDK 1.8
 */
public interface GoodsEsMapper {

    /**
     * 根据更新时间获取商品
     * @param updateTime 更新时间
     * @return List<GoodsEs>
     */
    List<GoodsEs> listByUpdateTime(String updateTime);
}
