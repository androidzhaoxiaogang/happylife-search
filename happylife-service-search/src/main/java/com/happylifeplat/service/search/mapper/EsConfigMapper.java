package com.happylifeplat.service.search.mapper;

import com.happylifeplat.service.search.entity.EsConfig;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  es_config 表 mapper
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/10 14:24
 * @since JDK 1.8
 */
public interface EsConfigMapper {

    /**
     * 新增
     * @param esConfig 实体类
     */
    void save(EsConfig esConfig);


    /**
     * 根据类型获取对象
     * @param type 类型
     * @return EsConfig
     */
    EsConfig getByType(int type);


    /**
     * 更新
     * @param esConfig 实体类
     */
    void update(EsConfig esConfig);



}
