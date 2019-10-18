package com.lyq3.elasticsearch.example.document.test.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lyq3.elasticsearch.example.common.entity.po.Product;
import com.lyq3.elasticsearch.example.document.mapper.ProductMapper;
import com.lyq3.elasticsearch.example.document.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 卡卢比
 * @createTime 2019年10月17日 16:14
 * @description
 */
public class ProductMapperTest extends BaseTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void test1(){
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>().last("limit 10"));
        for (Product product : products) {
            System.out.println(product.getName());
        }
    }
}
