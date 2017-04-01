
package com.happylifeplat.facade.search.exception;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  异常
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/24 15:15
 * @since JDK 1.8
 */
public class SearchException extends Exception {

    public SearchException() {
        super();
    }

    public SearchException(String message) {
        super(message);
    }


    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }


    public SearchException(Throwable cause) {
        super(cause);
    }


    protected SearchException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

