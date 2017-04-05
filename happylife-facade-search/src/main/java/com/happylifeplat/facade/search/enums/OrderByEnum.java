package com.happylifeplat.facade.search.enums;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 排序枚举
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 14:54
 * @since JDK 1.8
 */
public enum OrderByEnum {

    /**
     * 升序
     */
    ASC {

        @Override
        public String toString() {
            return "asc";
        }
    },
    /**
     * 降序
     */
    DESC {
        @Override
        public String toString() {
            return "desc";
        }
    }
}
