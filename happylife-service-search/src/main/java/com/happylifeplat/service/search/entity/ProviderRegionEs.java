package com.happylifeplat.service.search.entity;

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


    private String id;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 供应商id
     */
    private String providerId;

    /**
     * 服务区域id
     */
    private String administrativeRegionId;


    private Integer status;

    private Integer leafCheckedNumber;

    private Integer leafNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getAdministrativeRegionId() {
        return administrativeRegionId;
    }

    public void setAdministrativeRegionId(String administrativeRegionId) {
        this.administrativeRegionId = administrativeRegionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLeafCheckedNumber() {
        return leafCheckedNumber;
    }

    public void setLeafCheckedNumber(Integer leafCheckedNumber) {
        this.leafCheckedNumber = leafCheckedNumber;
    }

    public Integer getLeafNumber() {
        return leafNumber;
    }

    public void setLeafNumber(Integer leafNumber) {
        this.leafNumber = leafNumber;
    }
}
