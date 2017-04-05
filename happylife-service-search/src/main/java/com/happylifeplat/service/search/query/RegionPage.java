package com.happylifeplat.service.search.query;

import com.happylifeplat.plugin.mybatis.pager.PageParameter;

import java.io.Serializable;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/5 9:14
 * @since JDK 1.8
 */
public class RegionPage  implements Serializable{


    private static final long serialVersionUID = 4791187692071622102L;
    /**
     * 分页信息
     */
    private PageParameter page;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 创建时间
     */
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
