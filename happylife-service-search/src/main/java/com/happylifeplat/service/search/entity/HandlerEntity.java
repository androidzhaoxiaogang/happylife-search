package com.happylifeplat.service.search.entity;

import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * es 处理的实体类
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/10 9:50
 * @since JDK 1.8
 */
public class HandlerEntity<T> {

    /**
     * es处理类
     */
    private Class handler;

    /**
     * 数据类型
     */
    private Integer type;


    /**
     * 索引
     */
    private String index;

    /**
     * 索引类型
     */
    private String indexType;

    /**
     * 处理的数据
     */
    private List<T> data;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }


    public Class getHandler() {
        return handler;
    }

    public void setHandler(Class handler) {
        this.handler = handler;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
