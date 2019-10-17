package com.lyq3.elasticsearch.example.index.dao;

import com.alibaba.fastjson.JSON;
import com.lyq3.elasticsearch.example.common.util.MappingUtils;
import com.lyq3.elasticsearch.example.index.entity.Product;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * @author 卡卢比
 * @createTime 2019年10月16日 16:41
 * @description
 */
@Repository
public class ProductIndexDao {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public String createIndex(Product product) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("product");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
        request.mapping(MappingUtils.getMapping(Product.class),XContentType.JSON);
        System.out.println(MappingUtils.getMapping(Product.class));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(createIndexResponse));
        return "成功";
    }
}
