package com.lyq3.elasticsearch.example.document.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.lyq3.elasticsearch.example.common.entity.po.Product;
import com.lyq3.elasticsearch.example.document.mapper.ProductMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author 卡卢比
 * @createTime 2019年10月17日 16:39
 * @description
 */
@Component
public class ProductCurd {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RestHighLevelClient client;

    /**
     * 添加数据
     * @return
     */
    public void add() throws IOException {
        Integer count = productMapper.selectCount(null);
        int page = count/10000;
        for (int i = 2 ; i <= page ; i++){

            addEs(i);
        }
    }

    private void addEs(int page) throws IOException {
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>().last("limit "+(page-1)*10000+",10000"));
        for (Product product : products) {
            IndexRequest request = new IndexRequest("product");
            //设置ID
            request.id(product.getId().toString());
            //设置数据
            request.source(JSON.toJSONString(product),XContentType.JSON);
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//            request.opType()
//            request.setIfSeqNo()
            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
            //结果：新增？更新？
            System.out.println(index.getResult());
            //分片信息
            System.out.println(index.getShardInfo());
        }
    }
}
