package com.happylifeplat.service.search.mapper;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 商品图片dao接口
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 16:07
 * @since JDK 1.8
 */
public interface GoodsImageEsMapper {

    /**
     *  根据商品id获取商品主图
     * @param goodsId 商品id
     * @return 商品主图url
     */
    String findPrimaryImageUrlByGoodsId(String goodsId);
}
