package com.happylifeplat.service.search.mapper;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 供应商DAO
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 16:09
 * @since JDK 1.8
 */
public interface ProviderEsMapper {

    /**
     * 根据id 获取名称
     *
     * @return providerName
     */
    String getNameById(String id);
}
