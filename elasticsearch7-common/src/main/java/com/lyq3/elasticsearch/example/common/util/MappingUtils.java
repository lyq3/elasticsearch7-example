package com.lyq3.elasticsearch.example.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 卡卢比
 * @createTime 2019年10月17日 11:37
 * @description
 */
public class MappingUtils {

    public static <T> String getMapping(Class<T> clazz){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("properties",PropertiesUtils.getProperties(clazz));
        return jsonObject.toJSONString();
    }
}
