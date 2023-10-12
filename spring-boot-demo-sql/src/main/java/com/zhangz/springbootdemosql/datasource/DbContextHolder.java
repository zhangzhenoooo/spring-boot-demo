package com.zhangz.springbootdemosql.datasource;

public class DbContextHolder {
    /**
     * default DataSource
     */
    public static final String ORALCE_OPERATION = "orcale_operation";
    public static final String ORALCE_CORE = "orcale_core";
 

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