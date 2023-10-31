# spring boot 项目 集成 nacos  实现配置管理
>参考链接：  
[1.spring 整合nacos](https://blog.csdn.net/weixin_44033066/article/details/129043571?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-129043571-blog-127871025.235^v38^pc_relevant_sort_base1&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

## 1 nacos 安装 (linux)
>参考文档:  
[Nacos安装配置详细流程](https://blog.csdn.net/qq_52830988/article/details/128319218)

### 1.1 下载nacos 
ps:选择与springboot 和 cloud 对应的nacos版本  
版本对照：(https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)  
```shell
wget https://github.com/alibaba/nacos/archive/refs/tags/2.0.0.tar.gz
```
### 1.2 解压
 进入压缩包所在的文件夹:
```shell
[root@localhost ~]# cd /opt/bssoft/nacos
```
 把文件解压到/opt/bssoft/nacos 目录下:  
```shell
[root@localhost upload]# tar -zxvf 2.0.0.tar.gz
```
