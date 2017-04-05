package com.happylifeplat.facade.search.enums;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  按照某类型排序
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 14:55
 * @since JDK 1.8
 */
public enum SortTypeEnum {

    DISTANCE {//距离
        @Override
        public String toString() {
            return "1";
        }
    },
    ZONGHE {//综合
        @Override
        public String toString() {
            return "2";
        }
    },
    PRICE {//价格
        @Override
        public String toString() {
            return "3";
        }
    },
    SALESVOLUME {//销量
        @Override
        public String toString() {
            return "4";
        }
    },
    CREDIT {//店铺信用
        @Override
        public String toString() {
            return "5";
        }
    }
}
