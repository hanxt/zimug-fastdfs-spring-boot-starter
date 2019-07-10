package com.zimug.spring.fastdfs;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class FastDFSClientFactory implements PooledObjectFactory<FastDFSClient> {

    private FastDFSProperties fastDfsConfiguration;

    FastDFSClientFactory(FastDFSProperties fastDfsConfiguration) {
        this.fastDfsConfiguration = fastDfsConfiguration;
    }


    public PooledObject<FastDFSClient> makeObject() throws Exception {

        ClientGlobal.setG_connect_timeout(fastDfsConfiguration.getConnect_timeout() * 1000);//连接超时的时限，单位为毫秒
        ClientGlobal.setG_network_timeout(fastDfsConfiguration.getNetwork_timeout() * 1000); //网络超时的时限，单位为毫秒
        ClientGlobal.setG_charset(fastDfsConfiguration.getCharset());//字符集

        ClientGlobal.setG_anti_steal_token(fastDfsConfiguration.getHttp_anti_steal_token());
        ClientGlobal.setG_secret_key(fastDfsConfiguration.getHttp_secret_key());
        ClientGlobal.setG_tracker_http_port(fastDfsConfiguration.getHttp_tracker_http_port());

        List<String> szTrackerServers = fastDfsConfiguration.getTracker_server();

        if(szTrackerServers == null) {
            throw new MyException("item \"tracker_server\" in fdfs_client.conf not found");
        } else {
            InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.size()];

            for(int i = 0; i < szTrackerServers.size(); ++i) {
                String[] parts = szTrackerServers.get(i).split(":", 2);
                if(parts.length != 2) {
                    throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                }

                tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
            ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
        }

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        FastDFSClient fastdfsClient = new FastDFSClient(trackerServer, storageServer);
        return new DefaultPooledObject<>(fastdfsClient);
    }

    public void destroyObject(PooledObject<FastDFSClient> pooledObject) throws Exception {
        if (pooledObject != null) {
            TrackerServer trackerServer = pooledObject.getObject().getTrackerServer();
            StorageServer storageServer = pooledObject.getObject().getStorageServer();
            storageServer.close();
            trackerServer.close();
        }
    }

    public boolean validateObject(PooledObject<FastDFSClient> pooledObject) {
        try {
            boolean isTrackerOK = ProtoCommon.activeTest(pooledObject.getObject().getTrackerServer().getSocket());
            boolean isStorageOK = ProtoCommon.activeTest(pooledObject.getObject().getStorageServer().getSocket());
            if(isTrackerOK && isStorageOK){
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    public void activateObject(PooledObject<FastDFSClient> pooledObject)  {
        System.out.println(pooledObject.getObject() + "资源被激活!");
    }

    public void passivateObject(PooledObject<FastDFSClient> pooledObject)  {
        System.out.println(pooledObject.getObject() + "资源被钝化!");
    }


}