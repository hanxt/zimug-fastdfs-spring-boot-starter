package com.zimug.spring.fastdfs;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zimug on 17-2-3.
 */
public class FastDFSClientUtil {

    private FastDFSProperties fastDFSProperties;

    FastDFSClientUtil(FastDFSProperties fastDFSProperties) {
        this.fastDFSProperties = fastDFSProperties;
    }

    /**
     * 上传base64编码文件(通常是图片)
     * @param picFileBase64 照片文件等
     * @param file_ext_name 文件名后缀
     * @return fastdfs保存路径
     */
    public String uploadBase64File(String picFileBase64,String file_ext_name) throws Exception {
        //判断图片是否为空
        if (picFileBase64.length() == 0) {
            return "";
        }
        return uploadFile(Base64.decode(picFileBase64), file_ext_name);
    }



    /**
     * 下载服务器文件到本地
     * @param filepath 如:group1/M00/00/00/wKgBPFekWyiAMx1VAACgzAM4CW0916.jpg
     * @return 图片对应的二进制数组
     */
    public  byte[] downloadFile(String filepath) throws Exception{
        int firstIndex = filepath.indexOf("/");
        String groupName = filepath.substring(0,firstIndex);
        filepath = filepath.substring(firstIndex+1);
        return  downloadFile(groupName, filepath);
    }

    /**
     * 上传二进制文件到服务器
     * @param file_buff  二进制文件
     * @param file_ext_name  文件名后缀
     * @return  文件id,如：group1/M00/00/00/wKgBPFekWyiAMx1VAACgzAM4CW0916.jpg
     */
    public String uploadFile(byte[] file_buff, String file_ext_name) throws Exception {
        GenericObjectPool<FastDFSClient> pool =  FastDFSClientPool.getInstance();
        FastDFSClient client = pool.borrowObject();
        String result = client.getStorageClient1().upload_file1(file_buff, file_ext_name, null);
        pool.returnObject(client);

        return result;
    }

    /**
     * 上传本地文件到服务器
     * @param local_filename 本地文件名
     * @param file_ext_name 文件扩展名
     * @return 文件id,如：group1/M00/00/00/wKgBPFekWyiAMx1VAACgzAM4CW0916.jpg
     */
    public String uploadFile(String local_filename, String file_ext_name) throws Exception {
        GenericObjectPool<FastDFSClient> pool =  FastDFSClientPool.getInstance();
        FastDFSClient client = pool.borrowObject();
        String result = client.getStorageClient1().upload_file1(local_filename, file_ext_name, null);
        pool.returnObject(client);

        return result;
    }

    /**
     * 下载服务器文件到本地
     * @param groupName 文件组
     * @param filepath 文件服务器路径
     * @return 二进制文件
     */
    public byte[] downloadFile(String groupName,String filepath) throws Exception {
        GenericObjectPool<FastDFSClient> pool =  FastDFSClientPool.getInstance();
        FastDFSClient client = pool.borrowObject();
        byte[] result =   client.getStorageClient1().download_file(groupName, filepath);
        pool.returnObject(client);

        return  result;
    }


    /**
     * 反盗链：获取资源的url
     * @param remoteFilename 文件地址：例如：group1/M00/00/00/wKgB2ViEMZOAeE4rAAF1DzcVmmk051.jpg(包含groupname)
     * @param group             如：group1
     */
    public String getSourceUrl(String remoteFilename, String group) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
        String httpserver = fastDFSProperties.getHttpserver();
        httpserver  = httpserver.endsWith("/") ? httpserver:httpserver+"/";

        int lts = (int)(System.currentTimeMillis() / 1000);
        remoteFilename = remoteFilename.replace(group+"/","");//替换掉group

        String token = ProtoCommon.getToken(remoteFilename, lts, ClientGlobal.getG_secret_key());
        return  httpserver + group + "/" + remoteFilename + "?token=" + token + "&ts=" + lts;
    }



    /**
     * 反盗链：获取资源的url
     * @param remoteFilename 文件地址：例如：group1/M00/00/00/wKgB2ViEMZOAeE4rAAF1DzcVmmk051.jpg(包含groupname)
     */
    public String getSourceUrl(String remoteFilename) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {

        if(remoteFilename != null && !remoteFilename.equals("")){
            String httpserver = fastDFSProperties.getHttpserver();
            httpserver  = httpserver.endsWith("/") ? httpserver:httpserver+"/";

            String group = remoteFilename.substring(0,remoteFilename.indexOf("/"));

            int lts = (int)(System.currentTimeMillis() / 1000);
            remoteFilename = remoteFilename.replace(group+"/","");//替换掉group/

            ClientGlobal.setG_charset(fastDFSProperties.getCharset());//字符集
            ClientGlobal.setG_secret_key(fastDFSProperties.getHttp_secret_key());
            String token = ProtoCommon.getToken(remoteFilename, lts, ClientGlobal.getG_secret_key());
            return  httpserver + group + "/" + remoteFilename + "?token=" + token + "&ts=" + lts;
        }else{
            return "";
        }
    }



    /*public String uploadFile(MultipartFile picFile) throws Exception {
        //判断图片是否为空
        if (picFile.isEmpty()) {
            return "";
        }
        //获取图片扩展名
        String originalFilename = picFile.getOriginalFilename();
        //取扩展名，不要"."
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return uploadFile(picFile.getBytes(), extName);
    }*/
}
