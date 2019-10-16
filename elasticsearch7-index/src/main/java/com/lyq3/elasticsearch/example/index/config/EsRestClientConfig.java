package com.lyq3.elasticsearch.example.index.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author 卡卢比
 * @createTime 2019年10月10日 14:55
 * @description
 */
@Configuration
public class EsRestClientConfig {
    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";
    @Value("${es.hosts}")
    private String[] hosts;
    @Value("${es.client.connect_timeout_millis}")
    private int CONNECT_TIMEOUT_MILLIS = 1000;
    @Value("${es.client.socket_timeout_millis}")
    private int SOCKET_TIMEOUT_MILLIS = 30000;
    @Value("${es.client.connection_request_timeout_millis}")
    private int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    @Value("${es.client.max_conn_per_route}")
    private int MAX_CONN_PER_ROUTE = 10;
    @Value("${es.client.max_conn_total}")
    private int MAX_CONN_TOTAL = 30;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hostList = Arrays.stream(hosts)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        return RestClient.builder(hostList);
    }

    /**
     * 构建RestHighLevelClient客户端
     * @param restClientBuilder
     * @return
     */
    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(RestClientBuilder restClientBuilder) {
        this.setConnectTimeOutConfig(restClientBuilder);
        this.setMutiConnectConfig(restClientBuilder);
        return new RestHighLevelClient(restClientBuilder);
    }

    /**
     * 将字符串地址转换为HttpHost对象
     * @param s
     * @return
     */
    private HttpHost makeHttpHost(String s) {
        assert !StringUtils.isEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            System.err.println(ip+"+"+port);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }


    /**
     * 配置连接时间延时
     * */
    public void setConnectTimeOutConfig(RestClientBuilder restClientBuilder){
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }
    /**
     * 使用异步httpclient时设置并发连接数
     * */
    public void setMutiConnectConfig(RestClientBuilder restClientBuilder){
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            return httpClientBuilder;
        });
    }

}
