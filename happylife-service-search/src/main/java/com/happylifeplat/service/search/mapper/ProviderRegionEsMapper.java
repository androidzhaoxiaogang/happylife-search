package com.happylifeplat.service.search.mapper;

import com.happylifeplat.service.search.entity.ProviderRegionEs;

import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  供应商服务区域DAO
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 10:09
 * @since JDK 1.8
 */
public interface ProviderRegionEsMapper {

    /**
     * 根据创建时间获取供应商服务区域
     * @param createTime 创建时间
     * @return List<ProviderRegionEs>
     */
    List<ProviderRegionEs> listByCreateTime(String createTime);
}