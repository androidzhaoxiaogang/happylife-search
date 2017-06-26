package com.happylifeplat.service.search.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  供应商服务范围实体类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 9:57
 * @since JDK 1.8
 */
public class ProviderRegionEs implements Serializable {

    private static final long serialVersionUID = -6407693686404714807L;

    /**
     * 供应商id
     */
    private String providerId;

    /**
     * 服务区域id
     */
    private String regionId;

    private Integer status;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);}
}
