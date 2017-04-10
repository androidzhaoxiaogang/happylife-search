package com.happylifeplat.service.search.helper;


import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 属性文件帮助类，用来更新job 执行时间
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/29 17:42
 * @since JDK 1.8
 */
public class SysProps {
    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SysProps.class);
    private static String path = "sys.properties";
    private static Properties props = new Properties();


    static {
        InputStream input;
        try {
            input = SysProps.class.getClassLoader().getResourceAsStream(path);
            props.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static void update(String key, String value) {
        try {
            final String path = SysProps.class.getClassLoader().getResource(SysProps.path).getPath();
            FileOutputStream fos = new FileOutputStream(path);
            props.setProperty(key, value);
            props.store(fos, "last job finish at");
            fos.close();
        } catch (IOException e) {
            LOGGER.info("update " + key + " failed");
        }
    }

    public static void main(String[] args) {
        String time = SysProps.get("goods.lastTime");
        System.out.println(time);
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        SysProps.update("goods.lastTime", str);
        time = SysProps.get("goods.lastTime");
        System.out.println(time);
        SysProps.update("my.lastTime", str);

        SysProps.update("xiaoyu", "199195");


    }
}
