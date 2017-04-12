package com.happylifeplat.service.search.mapper;

import com.happylifeplat.service.search.entity.ProviderRegionEs;
import com.happylifeplat.service.search.query.RegionPage;

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
    @Deprecated
    List<ProviderRegionEs> listByCreateTime(String createTime);

    /**
     * 分页查询供应商服务范围
     * @param regionPage 分页参数
     * @return List<ProviderRegionEs>
     */
    @Deprecated
    List<ProviderRegionEs> listPage(RegionPage regionPage);

    /**
     * 获取供应商所对应的服务区域
     * @param providerId 供应商id
     * @return List<ProviderRegionEs>
     */
    List<ProviderRegionEs> listByProviderId(String providerId);

}
