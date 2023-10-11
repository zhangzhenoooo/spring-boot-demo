package com.zhangz.springbootdemostarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

 @ComponentScan(basePackages = {
        "com.zhangz",
         "com.bosssoft" //TODO 后续剔除
})
@SpringBootApplication
@ServletComponentScan("com.zhangz.springbootdemoapi")
public class SpringBootDemoStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoStarterApplication.class, args);
    }

}
