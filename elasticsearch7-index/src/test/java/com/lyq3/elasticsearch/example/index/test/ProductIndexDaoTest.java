package com.lyq3.elasticsearch.example.index.test;

import com.lyq3.elasticsearch.example.index.dao.ProductIndexDao;
import com.lyq3.elasticsearch.example.index.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author 卡卢比
 * @createTime 2019年10月16日 16:48
 * @description
 */
public class ProductIndexDaoTest extends BaseTest {
    @Autowired
    private ProductIndexDao productIndexDao;

    @Test
    public void testCreateIndex() throws IOException {
        String index = productIndexDao.createIndex(new Product());
        System.out.println(index);
    }
}
