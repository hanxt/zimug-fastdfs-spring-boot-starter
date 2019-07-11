# zimug-fastdfs-spring-boot-starter
实现fastdfs与spring boot的快速集成，并加入连接池机制


## 使用方法
git clone 本项目之后，本地install，引入
```
        <dependency>
            <groupId>com.zimug.spring</groupId>
            <artifactId>zimug-fastdfs-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>

```


### 配置方法
···
zimug:
  fastdfs:
    httpserver: http://192.168.1.91:80/      #这个不是fastdfs属性，但是填上之后，在使用FastDFSClientUtil会得到完整的http文件访问路径
    connect_timeout: 5
    network_timeout: 30
    charset: UTF-8
    tracker_server:                         # tracker_server 可以配置成数组
      - 192.168.1.91:22122
    max_total: 50
    http_anti_steal_token: false            # 如果有防盗链的话，这里true
    http_secret_key:                        # 有防盗链，这里填secret_key

···


### 使用

···

    @Resource
    FastDFSClientUtil fastDFSClientUtil;


    使用fastDFSClientUtil提供的方法上传、下载、删除
···
