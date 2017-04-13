package com.happylifeplat.service.search.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  存放在es里的商品实体类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 9:46
 * @since JDK 1.8
 */
public class GoodsEs implements Serializable{


    private static final long serialVersionUID = -7936118358265107227L;

    /**
     * 商品id
     */
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 助记码（商品名称首字母）
     */
    private String mnemonicCode;

    /**
     * 商城价（实际销售价格）
     */
    private BigDecimal price;

    /**
     * 原价（参考价格）
     */
    private BigDecimal originalPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;


    /**
     * 商品类型id
     */
    private String goodsTypeId;

    /**
     * 商品类型名称
     */
    private String goodsTypeName;

    /**
     * 商品类别id
     */
    private String goodsCategoryId;


    /**
     * 供应商id
     */

    private String providerId;

    /**
     * 供应商名称
     */
    private String providerName;


    /**
     * 商品状态
     */
    private Integer status;


    /**
     * 是否删除
     */
    private Boolean disable;


    /**
     * 商品缩略图url
     */
    private String thumbnail;


    /**
     * 是否有定金
     */
    private Boolean hasDeposit;

    /**
     * 审核状态
     */
    private Integer verifyStatus;

    /**
     * 是否失效
     */
    private Boolean invalid;

    /**
     * 支付类型
     */
    private  Integer payType;


    /**
     * 反规范化你的数据  将区域信息存放在商品中
     */
    private List<ProviderRegionEs> regions;

    public List<ProviderRegionEs> getRegions() {
        return regions;
    }

    public void setRegions(List<ProviderRegionEs> regions) {
        this.regions = regions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getHasDeposit() {
        return hasDeposit;
    }

    public void setHasDeposit(Boolean hasDeposit) {
        this.hasDeposit = hasDeposit;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(String goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);}
}
