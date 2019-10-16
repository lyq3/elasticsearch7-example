package com.lyq3.elasticsearch.example.springboot.config;

import org.apache.http.HttpHost;
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
}
