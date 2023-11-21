# 关系型数据库实例

## mybatis plus
[参考文档](https://blog.csdn.net/qq_43548590/article/details/130149066)
1. 引入依赖
```xml
          <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        <dependency>
        <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
                <!-- 连接池  -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc8</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-extension</artifactId>
        </dependency>
```
2. 创建mapper以及mapper.xml
2.1 mapper
```java
    public interface AgencyInfoMapper extends BaseMapper<AgencyInfo> {
    AgencyInfo querybyAgencyName(String agencyName);
    }
 ```
2.2 创建实体类(对应表)
```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("C_OC_B_AGENCY_INFO")
public class AgencyInfo implements Serializable {
```
2.3 创建mapper对应xml (一般存储在resources目录下)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.zhangz.springbootdemosql.mapper.operation.AgencyInfoMapper">

    <sql id="tableName">C_OC_B_AGENCY_INFO</sql>
    <select id="querybyAgencyName" resultType="com.zhangz.springbootdemosql.entity.operation.AgencyInfo">
        select * from
        <include refid="tableName"/>
        <where>
            AGENCY_NAME = #{agencyName,jdbcType=VARCHAR}
        </where>
    </select>
</mapper>
```
3. 启动类配置扫描mapper包路径
```java
    @MapperScan("com.zhangz.springbootdemosql.mapper")
```
4. 配置文件
```yml
spring:
  # 数据库
  datasource:
    name: oracle
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: oracle.jdbc.OracleDriver
    url: jdbc:oracle:thin:@192.168.2.20:1521:orcl
    username: OPERATION_CENTER
    password: OPERATION_CENTER

```
## mybatis plus 代码自动生成器

## mybatis plus 多数据库实例连接
[参考文档](https://www.cnblogs.com/kobe-lin/p/11893455.html)

## mybatis plus 多数据库适配以及切换
1.修改数据库连接配置
```yml
spring:
  datasource:
    name: mysql
```
2.修改mapper扫描路径
```yml
# mybatis-plus的配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*/${spring.datasource.name}/*Mapper.xml
```
## flayway 

## 4.连接H2数据库
### 4.1 连接异常:org.h2.jdbc.JdbcSQLSyntaxErrorException:Table “CATALOGS“ not found；SQL statement
```shell

#原因
idea 使用 SELECT CATALOG_NAME FROM INFORMATION_SCHEMA.CATALOGS 连接 h2数据库，新版本的h2不支持该语句导致
#解决方案
url 后面拼接 ;OLD_INFORMATION_SCHEMA=TRUE
```
### 4.2 
