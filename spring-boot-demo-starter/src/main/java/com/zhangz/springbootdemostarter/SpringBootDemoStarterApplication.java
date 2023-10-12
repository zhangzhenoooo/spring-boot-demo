package com.zhangz.springbootdemostarter;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** 启动类
 * @author 100451
 */
@ComponentScan(basePackages = {"com.zhangz", "com.bosssoft" // TODO 后续剔除
})

@ServletComponentScan("com.zhangz.springbootdemoapi")
@SpringBootApplication
/**
 * 开启dubbo配置
 */
@EnableDubboConfiguration
/**
 * 指定mapper扫描包
 */
@MapperScan("com.zhangz.springbootdemosql.mapper.*")
public class SpringBootDemoStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoStarterApplication.class, args);
    }

}
