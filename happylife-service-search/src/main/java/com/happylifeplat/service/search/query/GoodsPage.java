package com.happylifeplat.service.search.query;

import com.happylifeplat.plugin.mybatis.pager.PageParameter;

import java.io.Serializable;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  商品分页查询参数
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/5 9:05
 * @since JDK 1.8
 */
public class GoodsPage implements Serializable {
    private static final long serialVersionUID = -7365348882764297841L;


    /**
     * 分页信息
     */
    private PageParameter page;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 商品更新时间
     */
    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public PageParameter getPage() {
        return page;
    }

    public void setPage(PageParameter page) {
        this.page = page;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
