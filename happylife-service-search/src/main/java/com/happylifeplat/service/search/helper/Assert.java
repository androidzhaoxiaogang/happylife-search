package com.happylifeplat.service.search.helper;


import com.happylifeplat.facade.search.exception.SearchException;
/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  Assert 判断工具类
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/5 9:05
 * @since JDK 1.8
 */
public class Assert {

    private Assert() {

    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new SearchException(message);
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new SearchException("argument invalid,Please check");
        }
    }

    public static void checkConditionArgument(boolean condition, String message) {
        if (!condition) {
            throw new SearchException(message);
        }
    }

}
