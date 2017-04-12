package com.happylifeplat.service.search.event;

import com.happylifeplat.service.search.event.bean.ChangeEvent;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  事件触发接口
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/12 10:21
 * @since JDK 1.8
 */
public interface CommonChangeEvent {

    /**
     *  触发更改事件接口
     * @param changeEvent
     */
    void fireChangeEvent(ChangeEvent changeEvent);


}
