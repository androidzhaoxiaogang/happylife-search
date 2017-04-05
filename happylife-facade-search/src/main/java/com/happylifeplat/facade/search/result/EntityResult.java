package com.happylifeplat.facade.search.result;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  返回实体类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/1 10:57
 * @since JDK 1.8
 */
public class EntityResult implements Serializable {

    private static final long serialVersionUID = 5368316239844232306L;

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
     * 商品类别id
     */
    private String goodsCategoryId;


    /**
     * 供应商id
     */
    private String providerId;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
