package com.happylifeplat.facade.search.result;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;


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
public class SearchResult implements Serializable {

    private static final long serialVersionUID = -5917876107104499384L;
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息提示
     */
    private String message;

    /**
     * 实体结果集合
     */
    private List<EntityResult> entityResultList;


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

    public List<EntityResult> getEntityResultList() {
        return entityResultList;
    }

    public void setEntityResultList(List<EntityResult> entityResultList) {
        this.entityResultList = entityResultList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
