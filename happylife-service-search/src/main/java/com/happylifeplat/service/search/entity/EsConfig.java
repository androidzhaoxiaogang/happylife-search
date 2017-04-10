package com.happylifeplat.service.search.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  es 更新时间表
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/7 17:04
 * @since JDK 1.8
 */
public class EsConfig implements Serializable {


    private static final long serialVersionUID = -7010952634323956695L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 类型 {@linkplain com.happylifeplat.facade.search.enums.EsConfigTypeEnum}
     */
    private Integer type;

    /**
     * 上次更新时间
     */
    private Date lastTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
