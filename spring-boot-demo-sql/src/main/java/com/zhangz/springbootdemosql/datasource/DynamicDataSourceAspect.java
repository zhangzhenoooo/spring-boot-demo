package com.zhangz.springbootdemosql.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @功能描述 多数据源切换切面
 * @author gcj
 * @date 2020-08-04
 */
@Aspect
@Component
@Order(value = 1)
@Slf4j
public class DynamicDataSourceAspect {

  
    @Pointcut("execution(* com.zhangz.springbootdemosql.mapper.operation.*Mapper.*(..))")
    public void operationJoinPointExpression(){}

    @Pointcut("execution(* com.zhangz.springbootdemosql.service.operation.*Service.*(..))")
    public void operationServiceJoinPointExpression(){}
    
    @Pointcut("execution(* com.zhangz.springbootdemosql.mapper.core.*Mapper.*(..))")
    public void coreJoinPointExpression(){}
    @Pointcut("execution(* com.zhangz.springbootdemosql.service.core.*Service.*(..))")
    public void coreServiceJoinPointExpression(){}

    @Pointcut("execution(* com.zhangz.springbootdemosql.mapper.demo.*Mapper.*(..))")
    public void h2JoinPointExpression(){}
    @Pointcut("execution(* com.zhangz.springbootdemosql.service.demo.*Service.*(..))")
    public void h2ServiceJoinPointExpression(){}
    
    
    @Before("operationServiceJoinPointExpression()")
    public void changeDataSource1() {
        DbContextHolder.setDbType(DbContextHolder.OPERATION);
    }

    @Before("operationJoinPointExpression()")
    public void changeDataSource2() {
        DbContextHolder.setDbType(DbContextHolder.OPERATION);
    }

    @Before("coreJoinPointExpression()")
    public void changeDataSource3() {
        DbContextHolder.setDbType(DbContextHolder.CORE);
    }

    @Before("coreServiceJoinPointExpression()")
    public void changeDataSource4() {
        DbContextHolder.setDbType(DbContextHolder.CORE);
    }

    @Before("h2JoinPointExpression()")
    public void changeDataSource5() {
        DbContextHolder.setDbType(DbContextHolder.DEMO);
    }

    @Before("h2ServiceJoinPointExpression()")
    public void changeDataSource6() {
        DbContextHolder.setDbType(DbContextHolder.DEMO);
    }


}
