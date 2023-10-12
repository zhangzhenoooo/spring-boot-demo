package com.zhangz.springbootdemosql.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String dbType = DbContextHolder.getDbType();
        log.debug("开始使用数据库:{}", dbType==null? DbContextHolder.OPERATION : dbType);
       DbContextHolder.clearDbType();
        return dbType;
    }
}