package com.happylifeplat.service.search.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/4/7 15:27
 * @since JDK 1.8
 */
public class ProTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("classpath:sys.properties");
            properties.setProperty("url", "jdbc:mysql://localhost:3306/");
            properties.setProperty("username", "root");
            properties.setProperty("password", "root");
            properties.setProperty("database", "bbs");//保存键值对到内存
            properties.store(output, "andieguo modify" );// 保存键值对到文件中
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
