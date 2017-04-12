package com.happylifeplat.service.search.event.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  更改事件实体对象
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 10:19
 * @since JDK 1.8
 */
public class ChangeEvent implements Serializable {

    private static final long serialVersionUID = 6027022256738947764L;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 数据对象
     */
    private Object data;

    public ChangeEvent(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);}




}
