package com.happylifeplat.facade.search.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 查询结果
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/24 15:15
 * @since JDK 1.8
 */
public class SearchResult {
    private int code; //返回码,0表示success
    private String message;//消息
    private Object result;//搜索结果集

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
