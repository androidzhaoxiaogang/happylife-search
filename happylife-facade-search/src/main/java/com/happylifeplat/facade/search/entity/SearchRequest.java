package com.happylifeplat.facade.search.entity;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 查询请求实体类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/24 15:15
 * @since JDK 1.8
 */
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = -7988816313585619349L;
    /**
     * 查询关键字
     */
    private String keywords= StringUtils.EMPTY;

    /**
     * 行政区id
     */
    private String regionId;

    /**
     * 页数，ES默认0为第一页
     */
    private Integer page = 0;

    /**
     *  默认每页显示20条记录
     */
    private Integer size = 20;

    /**
     * 排序类型
     */
    private String sortType;

    /**
     * asc-升序，desc-降序
     */
    private String sort;

    /**
     * 过滤条件
     */
    private FilterCondition filterCondition;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public FilterCondition getFilterCondition() {
        return filterCondition;
    }

    public void setFilterCondition(FilterCondition filterCondition) {
        this.filterCondition = filterCondition;
    }
}
