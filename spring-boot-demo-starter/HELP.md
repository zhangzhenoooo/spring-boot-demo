
##
### 自定义banner
[参考链接](https://blog.csdn.net/weixin_43553153/article/details/130013697)

[生成banner地址](https://www.bootschool.net/ascii-art/cartoons)

1. 默认只需在resources下新建banner.txt即可
2. 自定义banner位置
```yml
spring:
  banner:
  	# 或者 static/banner.txt
    location: classpath:static/banner.txt
```