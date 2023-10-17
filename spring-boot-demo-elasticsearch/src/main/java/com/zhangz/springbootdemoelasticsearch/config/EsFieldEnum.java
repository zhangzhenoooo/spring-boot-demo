package com.zhangz.springbootdemoelasticsearch.config;

import lombok.Getter;

@Getter
@SuppressWarnings("ALL")
public enum EsFieldEnum {

    TEXT("text"),

    KEYWORD("keyword"),

    INTEGER("integer"),

    LONG("long"),

    DOUBLE("double"),

    DATE("date"),

    /**
     * 单条数据
     */
    OBJECT("object"),

    /**
     * 嵌套数组
     */
    NESTED("nested"),
    ;

    EsFieldEnum (String type) {
        this.type = type;
    }
    private final String type;
}
