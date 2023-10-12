package com.zhangz.springbootdemosql.datasource;

public class DbContextHolder {
    /**
     * default DataSource
     */
    public static final String OPERATION = "operation";
    public static final String CORE = "core";
 

    private static final ThreadLocal contextHolder = new ThreadLocal();

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return (String) contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}