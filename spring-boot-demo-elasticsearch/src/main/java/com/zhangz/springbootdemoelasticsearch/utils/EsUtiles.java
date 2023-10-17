package com.zhangz.springbootdemoelasticsearch.utils;

import com.zhangz.springbootdemoelasticsearch.config.EsField;
import com.zhangz.springbootdemoelasticsearch.config.EsFieldEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
@Slf4j
public class EsUtiles {
    public static XContentBuilder generateBuilder(Class clazz) {
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.startObject("properties");
            java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field f : declaredFields) {
                if (f.isAnnotationPresent(EsField.class)) {
                    // 获取注解
                    EsField declaredAnnotation = f.getDeclaredAnnotation(EsField.class);
                    if (declaredAnnotation.type() == EsFieldEnum.OBJECT) {
                        // 获取当前类的对象-- Action
                        Class<?> type = f.getType();
                        java.lang.reflect.Field[] df2 = type.getDeclaredFields();
                        builder.startObject(f.getName());
                        builder.startObject("properties");
                        // 遍历该对象中的所有属性
                        for (java.lang.reflect.Field f2 : df2) {
                            if (f2.isAnnotationPresent(EsField.class)) {
                                // 获取注解
                                EsField declaredAnnotation2 = f2.getDeclaredAnnotation(EsField.class);
                                builder.startObject(f2.getName());
                                builder.field("type", declaredAnnotation2.type().getType());
                                // keyword不需要分词
                                if (declaredAnnotation2.type() == EsFieldEnum.TEXT) {
                                    builder.field("analyzer", declaredAnnotation2.analyzer().getType());
                                }
                                builder.endObject();
                            }
                        }
                        builder.endObject();

                    } else {
                        builder.startObject(f.getName());
                        builder.field("type", declaredAnnotation.type().getType());
                        // keyword不需要分词
                        if (declaredAnnotation.type() == EsFieldEnum.TEXT) {
                            builder.field("analyzer", declaredAnnotation.analyzer().getType());
                        }
                    }
                    builder.endObject();
                }
            }
            // 对应property
            builder.endObject();
            builder.endObject();
        } catch (Exception e) {
            log.error("【ES操作】 组装映射字段语句异常");
        }

        return builder;
    }

}
