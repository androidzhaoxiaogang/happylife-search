package com.happylifeplat.facade.search.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao @happylifeplat.com
 * @version 1.0
 * @date 2017 /4/1 11:04
 * @since JDK 1.8
 */
public enum ResultCodeEnum {


    /**
     * Succeed result code enum.
     */
    SUCCEED(1000000, "查询成功"),

    /**
     * Fail result code enum.
     */
    FAIL(-1,"查询失败，发生了异常"),


    REGION_IS_NOT_NULL(1188501,"行政区id不能为空"),

    KEY_WORD_IS_NOT_NULL(1188502,"查询关键字不能为空"),

    ;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * 根据value 获取枚举对象
     *  add by xiaoyu
     * @param value 枚举值
     * @return BackgroundCode enum
     */
    public static ResultCodeEnum getEnum(final Integer value) {
        Optional<ResultCodeEnum> backgroundCode =
                Arrays.stream(ResultCodeEnum.values())
                        .filter(v -> Objects.equals(v.getCode(), value))
                        .findFirst();
        return backgroundCode.orElse(ResultCodeEnum.SUCCEED);
    }


    /**
     * 根据value 获取 desc
     *
     * @param value value
     * @return desc string
     */
    public static String getMessageByCode(final Integer value) {
        return getEnum(value).getMessage();
    }

    /**
     * 消息码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

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
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }




}
