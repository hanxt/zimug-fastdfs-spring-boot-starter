package com.zimug.spring.fastdfs;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "zimug.fastdfs")
public class FastDFSProperties {

    private Integer connect_timeout = 5;
    private Integer network_timeout = 30;
    private String charset = "UTF-8";
    private List<String> tracker_server = new ArrayList<>();
    private Integer max_total;
    private Boolean http_anti_steal_token = false;
    private String http_secret_key = "";
    private Integer http_tracker_http_port = 8987;
    //下面这个实际上不是fastdfs的属性，为了方便实用自定义属性，表示访问nginx的http地址
    private String httpserver;

    public Integer getHttp_tracker_http_port() {
        return http_tracker_http_port;
    }

    public void setHttp_tracker_http_port(Integer http_tracker_http_port) {
        this.http_tracker_http_port = http_tracker_http_port;
    }

    public Boolean getHttp_anti_steal_token() {
        return http_anti_steal_token;
    }

    public void setHttp_anti_steal_token(Boolean http_anti_steal_token) {
        this.http_anti_steal_token = http_anti_steal_token;
    }

    public String getHttp_secret_key() {
        return http_secret_key;
    }

    public void setHttp_secret_key(String http_secret_key) {
        this.http_secret_key = http_secret_key;
    }

    public Integer getMax_total() {
        return max_total;
    }

    public void setMax_total(Integer max_total) {
        this.max_total = max_total;
    }

    public String getHttpserver() {
        return httpserver;
    }

    public void setHttpserver(String httpserver) {
        this.httpserver = httpserver;
    }

    public List<String> getTracker_server() {
        return tracker_server;
    }

    public void setTracker_server(List<String> tracker_server) {
        this.tracker_server = tracker_server;
    }

    public Integer getConnect_timeout() {
        return connect_timeout;
    }

    public void setConnect_timeout(Integer connect_timeout) {
        this.connect_timeout = connect_timeout;
    }

    public Integer getNetwork_timeout() {
        return network_timeout;
    }

    public void setNetwork_timeout(Integer network_timeout) {
        this.network_timeout = network_timeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}