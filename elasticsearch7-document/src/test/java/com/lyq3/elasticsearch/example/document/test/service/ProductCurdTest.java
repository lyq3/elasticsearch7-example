package com.lyq3.elasticsearch.example.document.test.service;

import com.lyq3.elasticsearch.example.document.service.ProductCurd;
import com.lyq3.elasticsearch.example.document.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author 卡卢比
 * @createTime 2019年10月17日 17:26
 * @description
 */
public class ProductCurdTest extends BaseTest {
    @Autowired
    private ProductCurd productCurd;

    @Test
    public void testAdd() throws IOException {
        productCurd.add();
    }

    @Test
    public void testBatchAdd() throws IOException {
        productCurd.addBatch();
    }
}
