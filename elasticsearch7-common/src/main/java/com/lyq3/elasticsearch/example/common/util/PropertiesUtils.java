package com.lyq3.elasticsearch.example.common.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;

/**
 * @author 卡卢比
 * @createTime 2019年10月17日 11:16
 * @description 封装Properties的工具类
 */
public class PropertiesUtils {
    /**
     * 根据实体类构建Properties
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> JSONObject getProperties( Class<T> clazz){
        if (clazz == null){
            throw new NullPointerException("传入参数不能为空");
        }
        JSONObject jsonObject = new JSONObject();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType() == String.class){
                JSONObject properties = new JSONObject();
                properties.put("type","text");
                properties.put("analyzer","ik_max_word");
                properties.put("search_analyzer","ik_smart");
                jsonObject.put(field.getName(),properties);
            }else {
                JSONObject properties = new JSONObject();
                jsonObject.put(field.getName(),properties);
                properties.put("type","text");
            }
        }
        return jsonObject;
    }
}
