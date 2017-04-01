package com.happylifeplat.service.search.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 处理regionId的工具类
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/30 18:12
 * @since JDK 1.8
 */
public class RegionIdUtils {

    public static String convert(final String regionId) {
        String regionIdEs = "";
        Joiner joiner = Joiner.on("-");
        for (int i = 2; i <= regionId.length(); i += 2) {
            if (i > 6 || i == regionId.length()) {
                regionIdEs += regionId;
                break;
            } else {
                String substring = regionId.substring(0, i);
                regionIdEs = joiner.join(substring, regionIdEs);
            }
        }
        return regionIdEs;
    }


    public static void main(String[] args) {
        final String convert = convert("440305001");
        System.out.println(convert);

    }
}
