package com.happylifeplat.service.search.query;

import com.happylifeplat.facade.search.entity.SearchRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  搜索实体类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:42
 * @since JDK 1.8
 */
public class SearchEntity extends SearchRequest {

    private static final long serialVersionUID = -6262526116715030938L;
    /**
     * 搜索的字段
     */
    private String field;

    /**
     * 搜索的多字段集合
     */
    private String[] fields;

    /**
     * 排序的字段
     */
    private String orderField;

    /**
     * asc-升序,desc-降序
     */
    private String sortOrder;

    /**
     * key:搜索的字段，value:搜索关键词或内容
     */
    private Map<String, Object> fieldMap;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Map<String, Object> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Object> fieldMap) {
        this.fieldMap = fieldMap;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
