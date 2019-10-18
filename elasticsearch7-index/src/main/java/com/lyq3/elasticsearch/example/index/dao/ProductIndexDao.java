package com.lyq3.elasticsearch.example.index.dao;

import com.alibaba.fastjson.JSON;
import com.lyq3.elasticsearch.example.common.util.MappingUtils;
import com.lyq3.elasticsearch.example.common.entity.po.Product;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActiveShardCount;
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

    /**
     * 同步调用
     * @param product
     * @return
     * @throws IOException
     */
    public String createIndex(Product product) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("product");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
        request.mapping(MappingUtils.getMapping(Product.class),XContentType.JSON);
        //多少副本同步完成才返回成功
        request.waitForActiveShards(ActiveShardCount.from(1));
        System.out.println(MappingUtils.getMapping(Product.class));
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(createIndexResponse));
        return "成功";
    }

    /**
     * 异步调用
     * @param product
     * @return
     * @throws IOException
     */
    public String createIndexAsyn(Product product){
        CreateIndexRequest request = new CreateIndexRequest("product");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
        request.mapping(MappingUtils.getMapping(Product.class),XContentType.JSON);
        //多少副本同步完成才返回成功
        request.waitForActiveShards(ActiveShardCount.from(1));

        ActionListener<CreateIndexResponse> listener =
                new ActionListener<CreateIndexResponse>() {
                    /**
                     * 成功时调用
                     * @param createIndexResponse
                     */
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        System.out.println("成功");
                    }

                    /**
                     * 失败时调用
                     * @param e
                     */
                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("失败");
                    }
                };
        restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT,listener);
        return "===";
    }
}
