package com.happylifeplat.facade.search.enums;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  ES 配置枚举
 * @author yu.xiao @happylifeplat.com
 * @version 1.0
 * @date 2017 /4/7 17:11
 * @since JDK 1.8
 */
public enum EsConfigTypeEnum {

    /**
     * Goods es config enum.
     */
    GOODS(0, "商品"),


    /**
     * Region es config enum.
     */
    REGION(1, "区域");

    private Integer code;
    private String desc;

    EsConfigTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
