package com.happylifeplat.facade.search.enums;

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
    FAIL(-1,"查询失败，发生了异常")



    ;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
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
