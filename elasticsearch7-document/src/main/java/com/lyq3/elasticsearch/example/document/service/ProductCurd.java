package com.lyq3.elasticsearch.example.document.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.lyq3.elasticsearch.example.common.entity.po.Product;
import com.lyq3.elasticsearch.example.document.mapper.ProductMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateResponse;
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

    /**
     * 批量添加数据
     * @return
     */
    public void addBatch() throws IOException {
        Integer count = productMapper.selectCount(null);
        int page = count/10000;
        for (int i = 1 ; i <= page ; i++){

            this.addBatchEs(i);
        }
    }

    /**
     * 单个添加
     * @param page
     * @throws IOException
     */
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

    /**
     * 批量添加
     * @param page
     * @throws IOException
     */
    private void addBatchEs(int page) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>().last("limit "+(page-1)*10000+",10000"));
        for (Product product : products) {
            IndexRequest request = new IndexRequest("product");
            //设置ID
            request.id(product.getId().toString());
            //设置数据
            request.source(JSON.toJSONString(product),XContentType.JSON);
            //批量设置刷新策略不支持
//            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            //添加请求，支持增删改
            bulkRequest.add(request);
        }
        //调用
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //结果
        BulkItemResponse[] items = bulk.getItems();
        System.out.println(items.length);
        for (BulkItemResponse item : items) {
            DocWriteResponse response = item.getResponse();
            switch (item.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) response;
                    System.out.println("==新增：=="+indexResponse.getShardInfo());
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) response;
                    System.out.println("==修改：=="+updateResponse.getShardInfo());
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) response;
                    System.out.println("==删除：=="+deleteResponse.getShardInfo());
            }
        }
    }
}
