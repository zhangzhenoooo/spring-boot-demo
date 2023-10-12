package com.zhangz.springbootdemosql.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: agency-operation
 * @description: datasource 多数据源
 * @author: gcj
 * @create: 2020-07-17 10:48
 **/
@Configuration
public class RoutingDataSourceConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean(name = DbContextHolder.OPERATION)
    @ConfigurationProperties(prefix = "spring.datasource.druid." + DbContextHolder.OPERATION)
    public DataSource operationDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = DbContextHolder.CORE)
    @ConfigurationProperties(prefix = "spring.datasource.druid." + DbContextHolder.CORE)
    public DataSource coreDataSource() {
        return new DruidDataSource();
    }
    
    @Bean
    @Primary
    public DynamicDataSource dataSource(@Qualifier("operation") DataSource operationDataSource, @Qualifier("core") DataSource coreDataSource) {
        
        DynamicDataSource dynamic = new DynamicDataSource();
        dynamic.setTargetDataSources(new HashMap<Object, Object>() {
            {
                put(DbContextHolder.OPERATION, operationDataSource);
                put(DbContextHolder.CORE, coreDataSource);

            }
        });
        // 设置默认数据源
        dynamic.setDefaultTargetDataSource(operationDataSource);
        return dynamic;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(MybatisPlusProperties properties) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource(operationDataSource(), coreDataSource()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        // 添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[] {paginationInterceptor()});

        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            sqlSessionFactory.setMapperLocations(properties.resolveMapperLocations());
        }
        return sqlSessionFactory.getObject();
    }

    // 配置Druid的监控
    // 1、配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "bosssoft");
        initParams.put("loginPassword", "123456");
        initParams.put("allow", "");// 默认就是允许所有访问
        // initParams.put("allow", "localhost")：表示只有本机可以访问
        initParams.put("deny", "");
        // 设置初始化参数
        bean.setInitParameters(initParams);
        return bean;
    }

    // 配置 Druid 监控 之 web 监控的 filter
    // WebStatFilter：用于配置Web和Druid数据源之间的管理关联监控统计
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        // exclusions：设置哪些请求进行过滤排除掉，从而不进行统计
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid,/druid/*,/v2/*,/swagger-resources,/swagger-resources/*");
        bean.setInitParameters(initParams);
        // "/*" 表示过滤所有请求
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

}
