package com.zimug.spring.fastdfs;


import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by zimug on 17-2-6.
 */
class FastDFSClientPool {


    private static FastDFSProperties fastDFSProperties;

    FastDFSClientPool(FastDFSProperties fastDFSProperties) {
        FastDFSClientPool.fastDFSProperties = fastDFSProperties;
    }

    private static class Nested {
        //使用StorageClient1进行上传
        private  static GenericObjectPool<FastDFSClient> pool =  null;

        static {
            try{//按照当前路径,用户目录，类路径进行搜索查找文件

                GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                config.setMaxTotal(fastDFSProperties.getMax_total()); //整个池最大值
                config.setBlockWhenExhausted(false);//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
                config.setMaxWaitMillis(-1); //获取不到永远等待
                config.setNumTestsPerEvictionRun(10); // always test all idle objects
                config.setTestOnBorrow(true);
                config.setTestOnReturn(false);
                config.setTestWhileIdle(false);
                config.setTimeBetweenEvictionRunsMillis(60000L); //-1不启动。默认1min一次
                config.setMinEvictableIdleTimeMillis(10 * 60000L); //可发呆的时间,10mins

                pool = new GenericObjectPool<>(new FastDFSClientFactory(fastDFSProperties), config);
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }


    //获取连接池单例
    static GenericObjectPool<FastDFSClient> getInstance(){
        return Nested.pool;
    }


}
