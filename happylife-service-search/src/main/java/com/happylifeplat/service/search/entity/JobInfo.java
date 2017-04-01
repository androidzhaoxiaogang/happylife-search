package com.happylifeplat.service.search.entity;

/**
 * <p>Description: .</p>
 * <p>Company: 深圳市旺生活互联网科技有限公司</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 *  job配置实体
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @date 2017/3/24 15:22
 * @since JDK 1.8
 */
public class JobInfo {
    /**
     * job名称
     */
    private String name;

    /**
     * es 索引
     */
    private String index;

    /**
     * es type
     */
    private String type;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 时间参数
     */
    private String param;


    /**
     * job 处理对象
     */
    private String handler;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }


    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "JobInfo{" +
                "name='" + name + '\'' +
                ", index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", cron='" + cron + '\'' +
                ", param='" + param + '\'' +
                ", handler=" + handler +
                '}';
    }
}
