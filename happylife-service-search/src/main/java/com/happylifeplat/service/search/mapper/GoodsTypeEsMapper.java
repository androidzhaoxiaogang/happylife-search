package com.happylifeplat.service.search.mapper;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  商品类别DAO
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 16:08
 * @since JDK 1.8
 */
public interface GoodsTypeEsMapper {

    /**
     * 根据id获取类别名称
     * @param id 分类id
     * @return typeName
     */
    String getNameById(String id);
}
