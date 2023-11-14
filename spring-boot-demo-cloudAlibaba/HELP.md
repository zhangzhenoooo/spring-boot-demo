# spring boot 项目 集成 nacos 实现配置管理

> 参考链接：  
[1.spring 整合nacos](https://blog.csdn.net/weixin_44033066/article/details/129043571?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-129043571-blog-127871025.235^v38^pc_relevant_sort_base1&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

## 1 nacos 安装 (linux)

安装位置 192.168.1.225 /opt/bssoft/nacos/nacos
> 参考文档:  
[Nacos安装配置详细流程](https://blog.csdn.net/qq_52830988/article/details/128319218)

### 1.1 下载nacos

* 选择与springboot 和 cloud
  对应的nacos [版本对照](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)
  ;
* 下载选择 Assets 下的包
  ![img.png](img.png)

```shell
wget https://github.com/alibaba/nacos/releases/download/1.4.1/nacos-server-1.4.1.tar.gz
```

### 1.2 解压

进入压缩包所在的文件夹:

```shell
[root@localhost ~]# cd /opt/bssoft/nacos
```

把文件解压到/opt/bssoft/nacos 目录下:

```shell
 tar -zxvf nacos-server-1.2.1.zip
 unzip nacos-server-1.2.1.zip
```

### 1.3 启动

进入 bin目录

```shell
#非集群启动
./startup.sh -m standalone
```

### 1.4 关闭

```shell
./shutdown.sh
```

### 1.5 测试访问

默认用户名密码 nacos/nacos

```shell
http://192.168.1.225:8848/nacos
```

### 1.6 设置开机自启

#### 1.6.1 创建 nacos.service文件

```shell
vim /lib/systemd/system/nacos.service

#内容如下
[Unit]
Description=nacos
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/nacos/bin/startup.sh -m standalone
ExecReload=/usr/local/nacos/bin/shutdown.sh
ExecStop=/usr/local/nacos/bin/shutdown.sh
PrivateTmp=true

[Install]
WantedBy=multi-user.target

```

#### 1.6.2 修改nacos启动文件startup.sh

```shell
[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/local/jdk1.8.0_191 
#[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/usr/java
#[ ! -e "$JAVA_HOME/bin/java" ] && JAVA_HOME=/opt/taobao/java
#[ ! -e "$JAVA_HOME/bin/java" ] && unset JAVA_HOME

```

#### 1.6.3 重启 使配置生效

```shell
systemctl daemon-reload        #重新加载服务配置
systemctl enable nacos.service #设置为开机启动
systemctl start nacos.service  #启动nacos服务
systemctl stop nacos.service   #停止nacos服务
systemctl status nacos.service   #查看nacos服务的状态

```

## 2. nacos 持久化

Nacos默认有自带嵌入式数据库derby，但是如果做集群模式的话，就不能使用自己的数据库不然每个节点一个数据库，那么数据就不统一了，需要使用外部的mysql

### 2.1 配置nacos持久化

#### 2.1.1 修改 application.properties配置

增加支持mysql数据源配置（目前只支持mysql，版本要求：5.6.5+）; 文件位置： nacos/conf/application.properties

```yml
#*************** Config Module Related Configurations ***************#
### If user MySQL as datasource:
spring.datasource.platform=mysql

  ### Count of DB:

db.num=1
db.url.0=jdbc:mysql://192.168.1.188:3306/pc_coare0610ttt?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
  #db.url.1=jdbc:mysql://11.163.152.9:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user=root
db.password=root
```

#### 2.1.2 初始化nacos脚本

初始化nacos 脚本

```java
/nacos/conf 下的nacos-mysql.sql脚本
```

#### 2.1.2 重启nacos

我重启后之前配置的配置信息不见了？

```java

```

## 3. spring boot集成 nacos

### 3.1 引入依赖

* 注意 springboot 、springcloud Alibaba 、以及nacos 对应的版本。

```xml

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.12.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

```xml

<com.alibaba.cloud.alibaba.nacos>2.1.0.RELEASE</com.alibaba.cloud.alibaba.nacos>
<com.alibaba.cloud.alibaba.dependencies>2.2.9.RELEASE</com.alibaba.cloud.alibaba.dependencies>
```

```xml

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>${com.alibaba.cloud.alibaba.dependencies}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

        <!-- 注册中心 -->
<dependency>
<groupId>com.alibaba.cloud</groupId>
<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
<version>${com.alibaba.cloud.alibaba.nacos}</version>
</dependency>
        <!-- 配置中心 -->
<dependency>
<groupId>com.alibaba.cloud</groupId>
<artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
<version>${com.alibaba.cloud.alibaba.nacos}</version>
</dependency>
```

### 3.2 修改配置以及启动类

启动类增加注解：

```java
@EnableDiscoveryClient // 开启nacos
```

配置文件名称改为 bootstrap.yaml,配置如下

```yaml
server:
  port: 8888
  servlet:
    context-path: /configManager
  compression:
    enabled: true
    mime-types: application/json
    min-response-size: 2048

spring:
  application:
    name: configManager
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.1.225:8848
      config:
        server-addr: 192.168.1.225:8848
        #指定分组 根据项目分组
        group: DEFAULT_GROUP
        #指定文件名，没有则默认${spring.application.name}
        #指定读取配置中心文件后缀
        file-extension: yaml
  profiles:
    active: dev
```

### 3.3 在nacos 配置 配置文件

在配置列表，点击“新增”
> Data ID = spring.application.name + '-' + spring.profiles.active + '.' + spring.cloud.nacos.config.file-extension  
> Group = spring.cloud.nacos.config.group  
> 增加配置信息  
> 点击发布，则会同步配置到项目，可通过日志查看是否生效

### 3.4 共享配置

```text
    服务名.yaml 将 系统同一配置写在里面 
```

### 3.5 配置优先级

![img_1.png](img_1.png)

## 4. nacos 控制台的基本使用

[Nacos--详解以及使用（全网最全）](https://blog.csdn.net/maoheguxiang/article/details/129718265)

## 5. nacos 用法 example

[Nacos--详解以及使用（全网最全）](https://blog.csdn.net/maoheguxiang/article/details/129718265)

### 5.1 热配置 用例

#### 5.1。1 @value 注解

在使用@Value注入值的类上增加注解@RefreshScope

```java
@RefreshScope

@Value("${system.test.shardvalue}")
private String shardvalue;
```

#### 5.1.2 @ConfigurationProperties 注解

```java

@Component
@Data
@ConfigurationProperties(prefix = "system.test")
public class SystemTestProperties {
    private String shardvalue;
}
```

### 5.2 共享配置

    生成一个 applicant.yaml 的文件 ，在nacos 新增对应的配置

### 5.3 注册中心

#### 5.3.1 在配置文件中配置nacos为配置中心

```yaml
spring:
  application:
    name: spring-boot-demo-nacos-provider
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.1.225:8848
        # 集群
#        cluster-name: test
#        namespace: configManager
```

#### 5.3.2 dubbo 注册中心配置为 nacos

```yaml
dubbo:
  application:
    name: spring-boot-demo-nacos-provider
  #  scan:
  #    base-packages: com.zhangz.springbootdemonacosprovider.service.impl
  protocols:
    dubbo:
      name: dubbo
      port: 20881
  registry:
    address: nacos://${spring.cloud.nacos.discovery.server-addr}
    group: nacosDemo
 ```

#### 5.3.3 启动类

```java
@EnableDiscoveryClient // 开启nacos
@EnableDubbo 
```

#### 5.3.4 提供者

```java
// 在service上增加注解
@DubboService
```

#### 5.3.5 消费者

```java
// 使用 @DubboReference 注解
@DubboReference
private TestService testService;
```